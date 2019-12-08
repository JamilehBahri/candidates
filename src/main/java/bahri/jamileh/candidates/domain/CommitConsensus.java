package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.Set;

public class CommitConsensus extends AbstractConsensus {

     private Set<String> sameVoteIds;

     private Set<String> tipSet;

     private boolean confirm;

    public  CommitConsensus(){

    }
    public CommitConsensus(int consensusId, int electionId, String candidateId,
                           LocalDateTime startTime, LocalDateTime persistTime,
                           Set<String> sameVoteIds, Set<String> tipSet) {
        super(consensusId, electionId, candidateId, startTime, persistTime);
        this.sameVoteIds = sameVoteIds;
        this.tipSet = tipSet;
    }

    public CommitConsensus(int consensusId, int electionId, String candidateId,
                           LocalDateTime startTime, LocalDateTime persistTime,
                           Set<String> sameVoteIds, boolean confirm) {
        super(consensusId, electionId, candidateId, startTime, persistTime);
        this.sameVoteIds = sameVoteIds;
        this.confirm = confirm;
    }

    public Set<String> getSameVoteIds() {
        return sameVoteIds;
    }

    public Set<String> getTipSet() {
        return tipSet;
    }

    public boolean isConfirm() {
        return confirm;
    }

    @Override
    public String toString() {
        return "CommitConsensus{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", consensusId=" + getConsensusId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", sameVoteIds=" + sameVoteIds +
                ", tipSet=" + tipSet +
                '}';
    }
}
