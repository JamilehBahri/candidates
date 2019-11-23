package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;

public class ReplyTally extends AbstractTally {

    private boolean electionResult;

    public ReplyTally(int electionId, String candidateId, LocalDateTime startTime,
                      LocalDateTime persistTime, boolean electionResult) {
        super(electionId, candidateId, startTime, persistTime);
        this.electionResult = electionResult;
    }


    public ReplyTally(long id, int electionId, String candidateId, LocalDateTime startTime,
                      LocalDateTime persistTime, boolean electionResult) {
        super(id, electionId, candidateId, startTime, persistTime);
        this.electionResult = electionResult;
    }
    public ReplyTally(){

    }

    public boolean isElectionResult() {
        return electionResult;
    }

    @Override
    public String toString() {
        return "ReplyTally{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", electionResult=" + electionResult +
                '}';
    }
}
