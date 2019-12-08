package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.Set;

public class PreparedConsensus extends AbstractConsensus {

    private Set<String> commonVoteIds;

    public PreparedConsensus(){

    }
    public PreparedConsensus(int consensusId, int electionId, String candidateId, LocalDateTime startTime,
                             LocalDateTime persistTime, Set<String> commonVoteIds) {
        super(consensusId, electionId, candidateId, startTime, persistTime);
        this.commonVoteIds = commonVoteIds;
    }

    public Set<String> getCommonVoteIds() {
        return commonVoteIds;
    }

    @Override
    public String toString() {
        return "PreparedConsensus{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", consensusId=" + getConsensusId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", commonVoteIds=" + commonVoteIds +
                '}';
    }
}
