package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.Map;

public class PreparedTally extends AbstractTally {

    private Map<String, Integer> candidateTally;

    private String hashTipSet;

    public PreparedTally(long id, int electionId, String candidateId, LocalDateTime startTime,
                         LocalDateTime persistTime, Map<String, Integer> candidateTally, String hashTipSet) {
        super(id, electionId, candidateId, startTime, persistTime);
        this.candidateTally = candidateTally;
        this.hashTipSet = hashTipSet;
    }

    public PreparedTally(int electionId, String candidateId, LocalDateTime startTime, LocalDateTime persistTime,
                         Map<String, Integer> candidateTally, String hashTipSet) {
        super(electionId, candidateId, startTime, persistTime);
        this.candidateTally = candidateTally;
        this.hashTipSet = hashTipSet;
    }

    public PreparedTally() {
        super();
    }

    public Map<String, Integer> getCandidateTally() {
        return candidateTally;
    }

    public String getHashTipSet() {
        return hashTipSet;
    }

    @Override
    public String toString() {
        return "PreparedTallyRepository{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", candidateTally=" + candidateTally +
                ", hashTipSet='" + hashTipSet + '\'' +
                "} ";
    }
}
