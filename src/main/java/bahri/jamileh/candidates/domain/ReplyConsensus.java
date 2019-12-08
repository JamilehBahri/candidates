package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;

public class ReplyConsensus extends AbstractConsensus {

    private boolean result;

    public ReplyConsensus(int consensusId, int electionId, String candidateId,
                          LocalDateTime startTime, LocalDateTime persistTime, boolean result) {
        super(consensusId, electionId, candidateId, startTime, persistTime);
        this.result = result;
    }

    public ReplyConsensus(){

    }

    public boolean isResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ReplyConsensus{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", consensusId=" + getConsensusId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", result=" + result +
                "} ";
    }
}
