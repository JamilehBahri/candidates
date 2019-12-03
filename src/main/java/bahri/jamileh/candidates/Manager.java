package bahri.jamileh.candidates;

import bahri.jamileh.candidates.domain.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Component("candidatemanager")
public class Manager {

    private final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    @Value("${candidateId}")
    private String candidateId;

//    @Value("${candaidate.threshold.startconsensus.votecount}")
//    private int baseThreshold;

    @Value("${Vote.topicname}")
    private String ballotBoxGenesisTopic;

    @Value("${Candidate.genesis.topicname}")
    private String candidateGenesisTopic;

    @Value("${Consensus.preprepare.vote.topicname}")
    private String consensusPreprepareTopic;

    @Value("${Consensus.prepare.vote.topicname}")
    private String consensusPrepareTopic;

    @Value("${Consensus.commit.vote.topicname}")
    private String consensusCommitTopic;

    @Value("${Consensus.reply.vote.topicname}")
    private String consensusReplyTopic;

    @Value("${Tally.prepare.vote.topicname}")
    private String tallyPrepareTopic;

    @Value("${Tally.commit.vote.topicname}")
    private String tallyCommitTopic;

    @Value("${Tally.reply.vote.topicname}")
    private String tallyReplyTopic;

    @Value("${Consensus.statistics}")
    private String consensusStatisticsTopic;

    private volatile boolean isRunning = true;

    private int consensusId = 0;

    private ConsensusStatistics consensusStatistics;

    private volatile boolean startconsensus = true;

    @Autowired
    private JmsTemplate jmsTemplate;

    private CandidateGenesis candidateGenesis = new CandidateGenesis();

    private Map<String, Set<Integer>> tip = new HashMap<>();

    private List<Vote> whiteList = new ArrayList<>();

    private List<Vote> grayList = new ArrayList<>();

    private List<Vote> greenList = new ArrayList<>();

    private PrePreparedConsensus prePreparedConsensus;

    private PreparedConsensus preparedConsensus;

    private CommitConsensus commitConsensus;

    private ReplyConsensus replyConsensus;

    private PreparedTally preparedTally;

    private CommitTally commitTally;

    private ReplyTally replyTally;

    private PrePreparedConsensus prePreparedMessage = new PrePreparedConsensus();

    private PreparedConsensus preParedMessage = new PreparedConsensus();

    private CommitConsensus commitMessage = new CommitConsensus();

    private ReplyConsensus replyMessage = new ReplyConsensus();

    private PreparedTally preparedTallyMessage = new PreparedTally();

    private CommitTally commitTallyMessage = new CommitTally();

    private ReplyTally replyTallyMessage = new ReplyTally();

    private TransferQueue<String> transferQueueGenesis = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueueVotes = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueuePreprepareMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueuePrepareMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueueCommitMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueueReplyMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueuePrepareTallyMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueueCommitTallyMessage = new LinkedTransferQueue<>();

    private TransferQueue<String> transferQueueReplyTallyMessage = new LinkedTransferQueue<>();


    //Threads
    private Thread generateCandidateTipsetThread = new Thread(this::getCandidateGenesisAndGenerateCandidateTipset);
    private Thread getVotesThread = new Thread(this::getVotesAndAddVotesToGrayList);

    //Consensus Threads
    private Thread prePrepareStepThread = new Thread(this::prePrepareStep);
    private Thread preparedStepThread = new Thread(this::prepareStep);
    private Thread commitStepThread = new Thread(this::commitStep);
    private Thread replyStepThread = new Thread(this::replyStep);

    //Tally Threads
    private Thread prepareTallyThread = new Thread(this::prePareTallyStep);
    private Thread commitTallyThread = new Thread(this::commitTallyStep);
    private Thread replyTallyThread = new Thread(this::replyTallyStep);

    //critical section handler consensus
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private final Object lock3 = new Object();
    private final Object lock4 = new Object();

    //critical section handler tally
    private final Object lock5 = new Object();
    private final Object lock6 = new Object();
    private final Object lock7 = new Object();

    // S.T
    private Set<String> preparedCommonVoteId;
    private Set<String> commitSameVoteId = new HashSet<>();
    private Set<String> replyFinalVoteId;

    @PostConstruct
    public void init() {

        generateCandidateTipsetThread.start();
        getVotesThread.start();
        prePrepareStepThread.start();
        preparedStepThread.start();
        commitStepThread.start();
        replyStepThread.start();

        prepareTallyThread.start();
        commitTallyThread.start();
        replyTallyThread.start();
    }

    @PreDestroy
    public void finishing() {
        isRunning = false;
        generateCandidateTipsetThread.interrupt();
        getVotesThread.interrupt();
        prePrepareStepThread.interrupt();
        preparedStepThread.interrupt();
        commitStepThread.interrupt();
        replyStepThread.interrupt();
        prepareTallyThread.interrupt();
        commitTallyThread.interrupt();
        replyTallyThread.interrupt();
        try {
            generateCandidateTipsetThread.join(100000);
            getVotesThread.join(100000);
            prePrepareStepThread.join(100000);
            preparedStepThread.join(100000);
            commitStepThread.join(100000);
            replyStepThread.join(100000);

            prepareTallyThread.join(100000);
            commitTallyThread.join(100000);
            replyTallyThread.join(100000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Candidate.genesis.topicname}")
    public void receiveGenesis(String message) {
        try {
            LOGGER.info("'Candidate Manager' received receiveGenesis message='{}'", message);
            transferQueueGenesis.transfer(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Vote.topicname}")
    public void receiveVotes(String vote) {
        try {
            LOGGER.info("'Candidate Manager' received vote='{}'", vote);
            transferQueueVotes.transfer(vote);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Consensus.preprepare.vote.topicname}")
    public void receivePreprepareMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receivePreprepareMessage='{}'", msg);
            transferQueuePreprepareMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Consensus.prepare.vote.topicname}")
    public void receivePrepareMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receivePrepareMessage='{}'", msg);
            transferQueuePrepareMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Consensus.commit.vote.topicname}")
    public void receiveCommitMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receiveCommitMessage='{}'", msg);
            transferQueueCommitMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Consensus.reply.vote.topicname}")
    public void receiveReplyMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receiveReplyMessage='{}'", msg);
            transferQueueReplyMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Tally.prepare.vote.topicname}")
    public void receivePrepareTallyMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receivePrepareTallyMessage='{}'", msg);
            transferQueuePrepareTallyMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Tally.commit.vote.topicname}")
    public void receiveCommitTallyMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receiveCommitTallyMessage='{}'", msg);
            transferQueueCommitTallyMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${Tally.reply.vote.topicname}")
    public void receiveReplyTallyMessage(String msg) {
        try {
            LOGGER.info("'Candidate Manager' received receiveReplyTallyMessage='{}'", msg);
            transferQueueReplyTallyMessage.transfer(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getCandidateGenesisAndGenerateCandidateTipset() {

        try {
            candidateGenesis = convertJsonToCandidateGenesis(transferQueueGenesis.take());
            Set<Integer> lst = new HashSet<>();
            for (Integer value : candidateGenesis.getCandidates()) {
                tip.put(value + "", lst);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getVotesAndAddVotesToGrayList() {

        while (isRunning) {
            try {
                whiteList.add(convertJsonToVote(transferQueueVotes.take()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (whiteList.size() >= candidateGenesis.getStartconsensusvotecount()) {
                for (Vote v : whiteList) {
                    grayList.add(v);
                }
                if (startconsensus) {
                    startconsensus = false;
                    whiteList.clear();
                    consensusId++;
                    LOGGER.info("Moved Votes From WhiteList To GrayList And Start Consensus Id :'{}'", consensusId);
                    synchronized (lock1) {
                        lock1.notify();
                    }
                }

            }
        }
    }

    public void prePrepareStep() {

        while (isRunning) {
            synchronized (lock1) {
                try {
                    lock1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            consensusStatistics = new ConsensusStatistics(candidateGenesis.getElectionId(), candidateId,
                    consensusId, LocalDateTime.now(), null, grayList.size(), false);

            Set<String> voteId = new HashSet<>();
            for (Vote v : grayList) {
                voteId.add(v.getVoteId());
            }
            prePreparedConsensus = new PrePreparedConsensus(consensusId, candidateGenesis.getElectionId(),
                    candidateId, LocalDateTime.now(), LocalDateTime.now(), voteId);

            //check for get all candidate or not
            preparedCommonVoteId = new HashSet<>(prePreparedConsensus.getGrayVoteIds());
            sendMessage(consensusPreprepareTopic, convertObjectToJson(prePreparedConsensus));

            synchronized (lock2) {
                lock2.notify();
            }
        }
    }

    //TODO: wait to get all candidate gray list from preprepared topic
    //TODO: common all gray lists ,then generate prepard message
    public void prepareStep() {

        int receivedCount = 0;
        while (isRunning) {
            synchronized (lock2) {
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (candidateGenesis.getCandidates().size() - 1 != receivedCount) {
                try {
                    prePreparedMessage = convertJsonToPreprepareMessage(transferQueuePreprepareMessage.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                receivedCount++;
                if (!prePreparedMessage.getCandidateId().equals(candidateId)) {
                    //done  // TODO‌:diff--> unity of each prepreparedmessage and base is preparedCommonVoteId -->unity is send and ununity move to WL
                    for (String s : prePreparedMessage.getGrayVoteIds()) {
                        if (preparedCommonVoteId.contains(s))
//                            commitSameVoteId = new HashSet<>();
                            commitSameVoteId.add(s);
                        //TODO: ununity move to WL
//                    else
//                        whiteList.add(s);

                    }
                }
            }
            //  preparedCommonVoteId.retainAll(prePreparedMessage.getGrayVoteIds());

            if (candidateGenesis.getCandidates().size() - 1 == receivedCount) {
                receivedCount = 0;
                preparedConsensus = new PreparedConsensus(consensusId, candidateGenesis.getElectionId(),
                        candidateId, LocalDateTime.now(), LocalDateTime.now(), preparedCommonVoteId);

                LOGGER.info("***consensus id in prepared step is '{}'", consensusId);

//                        commitSameVoteId = new HashSet<>(preparedConsensus.getCommonVoteIds());
                sendMessage(consensusPrepareTopic, convertObjectToJson(preparedConsensus));

                synchronized (lock3) {
                    lock3.notify();
                }
            }

        }
    }

    //TODO: wait to get all candidate common list from prepared topic
    //TODO: all common lists ,then generate commit message
    public void commitStep() {
        int receivedCount = 0;
        while (isRunning) {
            synchronized (lock3) {
                try {
                    lock3.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            replyFinalVoteId = new HashSet<>();
            while (candidateGenesis.getCandidates().size() - 1 != receivedCount) {
                try {
                    preParedMessage = convertJsonToPrepareMessage(transferQueuePrepareMessage.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                receivedCount++;
                if (!preParedMessage.getCandidateId().equals(candidateId)) {
//                    receivedCount++;
                    // TODO‌:diff--> unity of each prepreparedmessage and base is preparedCommonVoteId -->unity is send and ununity move to WL
                    // TODO: in diff determin 2/3 by counter
                    for (String s : preParedMessage.getCommonVoteIds()) {
                        if (commitSameVoteId.contains(s))
                            replyFinalVoteId.add(s);
                        //TODO: ununity move to WL
//                    else
//                        whiteList.add(s);
                    }
                }
            }
//                    commitSameVoteId.retainAll(preParedMessage.getCommonVoteIds());
            if (candidateGenesis.getCandidates().size() - 1 == receivedCount) {
                receivedCount = 0;
                commitConsensus = new CommitConsensus(consensusId, candidateGenesis.getElectionId(),
                        candidateId, LocalDateTime.now(), LocalDateTime.now(), commitSameVoteId, true);

               commitSameVoteId.clear();
                sendMessage(consensusCommitTopic, convertObjectToJson(commitConsensus));

                synchronized (lock4) {
                    lock4.notify();
                }
            }
        }
    }

    //TODO: wait to get all candidate commit messge from commit topic
    //TODO: apply or reject consensus
    // apply -->‌move votes to green list and add to tip set
    // reject --> move to white list
    public void replyStep() {
        int countConfirmConsensus = 0;
        int receivedCount = 0;
        while (isRunning) {
            synchronized (lock4) {
                try {
                    lock4.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (candidateGenesis.getCandidates().size() - 1 != receivedCount) {
                try {
                    commitMessage = convertJsonToCommitMessage(transferQueueCommitMessage.take());
                    receivedCount++;
                    if (!commitMessage.getCandidateId().equals(candidateId)) {
                        if (commitMessage.isConfirm())
                            countConfirmConsensus++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (candidateGenesis.getCandidates().size() - 1 == receivedCount) {
                receivedCount = 0;
                //consensus is correct
                //TODO: reach 2/3 --> apply consensusu --> dont need to send msg to topic
                if (countConfirmConsensus >= (receivedCount / 3)) {
                    replyConsensus = new ReplyConsensus(consensusId, candidateGenesis.getElectionId(),
                            candidateId, LocalDateTime.now(), LocalDateTime.now(), true);
                    sendMessage(consensusReplyTopic, convertObjectToJson(replyConsensus));

                    //add vote to tip and count each candidate vote
                    for (Vote v : grayList) {
                        greenList.add(v);
                    }
                    for (Vote v : grayList) {
                        for (Integer i : v.getCandidates()) {
                            tip.put(i + "", v.getCandidates());
                        }
                    }

                    consensusStatistics.setEndTime(LocalDateTime.now());
                    consensusStatistics.setConsensusResult(true);
                    sendMessage(consensusStatisticsTopic, convertObjectToJson(consensusStatistics));

                    grayList.clear();

                    //Check this
                    startconsensus = true;

                    if (greenList.size() == candidateGenesis.getMaxGenerateVotes()) {
                        synchronized (lock5) {
                            lock5.notify();
                        }
                    }
                }
            }
        }
    }

    public void prePareTallyStep() {

        while (isRunning) {
            synchronized (lock5) {
                try {
                    lock5.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Map<String, Integer> candidateTally = new HashMap<>();
            for (Integer value : candidateGenesis.getCandidates()) {
                candidateTally.put(value + "", 0);
            }
            //count each candidates vote
            for (int i = 1; i <= candidateGenesis.getCandidates().size(); i++) {
                for (Vote v : greenList) {
                    if (v.getCandidates().contains(i)) {
                        int count = candidateTally.get(i + "");
                        candidateTally.put(i + "", count++);
                    }
                }
            }
            preparedTally = new PreparedTally(candidateGenesis.getElectionId(), candidateId,
                    LocalDateTime.now(), LocalDateTime.now(), candidateTally, Objects.hash(tip) + "");
            sendMessage(tallyPrepareTopic, convertObjectToJson(preparedTally));

            synchronized (lock6) {
                lock6.notify();
            }
        }
    }

    public void commitTallyStep() {

        Set<String> adherents = new HashSet<>();
        Set<String> opponents = new HashSet<>();
        int receivedCount = 0;

        while (isRunning) {
            synchronized (lock6) {
                try {
                    lock6.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //determinate adherents and opponents
            while (candidateGenesis.getCandidates().size() - 1 != receivedCount) {
                try {
                    preparedTallyMessage = convertJsonToPrepareTallyMessage(transferQueuePrepareTallyMessage.take());
                    receivedCount++;

                    if (preparedTallyMessage.getCandidateId().equals(candidateId)) {
                        if (preparedTallyMessage.getHashTipSet() == Objects.hash(tip) + "")
                            adherents.add(preparedTallyMessage.getCandidateId());
                        else
                            opponents.add(preparedTallyMessage.getCandidateId());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (candidateGenesis.getCandidates().size() - 1 == receivedCount) {
                receivedCount =0;
                //more than 2/3 aggree on result
                if (adherents.size() >= (adherents.size() + opponents.size()) / 3) {
                    commitTally = new CommitTally(candidateGenesis.getElectionId(), candidateId,
                            LocalDateTime.now(), LocalDateTime.now(), adherents, opponents, true);
                    sendMessage(tallyCommitTopic, convertObjectToJson(commitTally));
                } else {
                    commitTally = new CommitTally(candidateGenesis.getElectionId(), candidateId,
                            LocalDateTime.now(), LocalDateTime.now(), adherents, opponents, false);
                    sendMessage(tallyCommitTopic, convertObjectToJson(commitTally));
                }
                LOGGER.info("***consensus id in commit tally step is '{}'", consensusId);

                synchronized (lock7) {
                    lock7.notify();
                }

            }



        }

    }

    public void replyTallyStep() {

        int countAgreeOnElectionResult = 0;
        int countDisAgreeOnElectionResult = 0;
        int receivedCount = 0;

        while (isRunning) {
            synchronized (lock7) {
                try {
                    lock7.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //determinate election result
            while (candidateGenesis.getCandidates().size() - 1 != receivedCount) {
                receivedCount++;
                try {
                    commitTallyMessage = convertJsonToCommitTallyMessage(transferQueueCommitTallyMessage.take());
                    if (commitTallyMessage.getCandidateId().equals(candidateId)) {
                        if (commitTallyMessage.isTallyResult())
                            countAgreeOnElectionResult++;
                        else
                            countDisAgreeOnElectionResult++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (candidateGenesis.getCandidates().size() - 1 == receivedCount) {

                receivedCount =0;
                //more than 2/3 aggree on result
                if (countAgreeOnElectionResult >= (countAgreeOnElectionResult + countDisAgreeOnElectionResult) / 3) {
                    replyTally = new ReplyTally(candidateGenesis.getElectionId(), candidateId,
                            LocalDateTime.now(), LocalDateTime.now(), true);
                    sendMessage(tallyReplyTopic, convertObjectToJson(replyTally));
                } else {
                    replyTally = new ReplyTally(candidateGenesis.getElectionId(), candidateId,
                            LocalDateTime.now(), LocalDateTime.now(), false);
                    sendMessage(tallyReplyTopic, convertObjectToJson(replyTally));
                }
            }


        }
    }

    public void sendMessage(String topicname, String message) {
        jmsTemplate.convertAndSend(topicname, message);
        LOGGER.info("sending consensus message='{}' to topic='{}'", message, topicname);
    }

    public String convertObjectToJson(Object obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(obj);
        System.out.println(jsonString);
        return jsonString;
    }

    public CandidateGenesis convertJsonToCandidateGenesis(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, CandidateGenesis.class);

        } else
            return null;
    }

    public Vote convertJsonToVote(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, Vote.class);

        } else
            return null;
    }

    public PrePreparedConsensus convertJsonToPreprepareMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, PrePreparedConsensus.class);

        } else
            return null;
    }

    public PreparedConsensus convertJsonToPrepareMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, PreparedConsensus.class);

        } else
            return null;
    }

    public CommitConsensus convertJsonToCommitMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, CommitConsensus.class);

        } else
            return null;
    }

    public ReplyConsensus convertJsonToReplyMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, ReplyConsensus.class);

        } else
            return null;
    }

    public PreparedTally convertJsonToPrepareTallyMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, PreparedTally.class);

        } else
            return null;
    }

    public CommitTally convertJsonToCommitTallyMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, CommitTally.class);

        } else
            return null;
    }

    public ReplyTally convertJsonToReplyTallyMessage(String jsonString) {

        if (jsonString != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(jsonString, ReplyTally.class);

        } else
            return null;
    }

}
