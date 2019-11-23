package bahri.jamileh.candidates.domain;

import java.time.LocalDateTime;
import java.util.Set;

public class PrePreparedConsensus extends AbstractConsensus {

    private Set<String> grayVoteIds;

//    public PrePreparedConsensus(long id, int electionId, int consensusId, String candidateId,
//                                LocalDateTime startTime, LocalDateTime persistTime, Set<Integer> grayVoteIds) {
//        super(id, electionId, consensusId, candidateId, startTime, persistTime);
//        this.grayVoteIds = grayVoteIds;
//    }

    public PrePreparedConsensus(int consensusId, int electionId, String candidateId, LocalDateTime startTime,
                                LocalDateTime persistTime, Set<String> grayVoteIds) {
        super(consensusId, electionId, candidateId, startTime, persistTime);
        this.grayVoteIds = grayVoteIds;
    }

    public PrePreparedConsensus(){
        super();
    }

    public Set<String> getGrayVoteIds() {
        return grayVoteIds;
    }

    @Override
    public String toString() {
        return "PrePreparedConsensus{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", consensusId=" + getConsensusId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", grayVoteIds=" + grayVoteIds +
                "} ";
    }
}
