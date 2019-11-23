package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

//public class CandidateGenesis extends AbstractDomain {
    public class CandidateGenesis {

    private int electionId;

    private int candidateChoose;

    private LocalDateTime electionStartTime;

    private LocalDateTime electionEndTime;

    private Set<Integer> candidates;

    private String masterCandidateId;

    private int minParticipants;

    private int maxParticipants;

    private LocalDateTime issuedTime;

    private LocalDateTime persistTime;

    private int startconsensusvotecount;

    private int maxGenerateVotes;

    public CandidateGenesis() {
    }


    public CandidateGenesis(int electionId, int candidateChoose, LocalDateTime electionStartTime,
                            LocalDateTime electionEndTime, Set<Integer> candidates, String masterCandidateId,
                            int minParticipants, int maxParticipants, LocalDateTime issuedTime,
                            LocalDateTime persistTime, int startconsensusvotecount, int maxGenerateVotes) {
        this.electionId = electionId;
        this.candidateChoose = candidateChoose;
        this.electionStartTime = electionStartTime;
        this.electionEndTime = electionEndTime;
        this.candidates = candidates;
        this.masterCandidateId = masterCandidateId;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.issuedTime = issuedTime;
        this.persistTime = persistTime;
        this.startconsensusvotecount = startconsensusvotecount;
        this.maxGenerateVotes = maxGenerateVotes;
    }

    public int getElectionId() {
        return electionId;
    }

    public int getCandidateChoose() {
        return candidateChoose;
    }

    public LocalDateTime getElectionStartTime() {
        return electionStartTime;
    }

    public LocalDateTime getElectionEndTime() {
        return electionEndTime;
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

    public String getMasterCandidateId() {
        return masterCandidateId;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public LocalDateTime getIssuedTime() {
        return issuedTime;
    }

    public LocalDateTime getPersistTime() {
        return persistTime;
    }

    public int getStartconsensusvotecount() {
        return startconsensusvotecount;
    }

    public int getMaxGenerateVotes() {
        return maxGenerateVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateGenesis candidateGenesis = (CandidateGenesis) o;
        return getElectionId() == candidateGenesis.getElectionId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getElectionId());
    }

    @Override
    public String toString() {
        return "Genesis{" +
//                "id=" + getId() +
                ", electionId=" + electionId +
                ", candidateChoose=" + candidateChoose +
                ", electionStartTime=" + electionStartTime +
                ", electionEndTime=" + electionEndTime +
                ", candidates=" + candidates +
                ", masterCandidateId='" + masterCandidateId + '\'' +
                ", minParticipants=" + minParticipants +
                ", maxParticipants=" + maxParticipants +
                ", issuedTime=" + issuedTime +
                ", persistTime=" + persistTime +
                '}';
    }
}
