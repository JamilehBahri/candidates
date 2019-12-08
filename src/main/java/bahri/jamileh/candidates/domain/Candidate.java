package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.Objects;

//@Entity
//public class Candidate extends AbstractDomain {
public class Candidate {

//   @Column
    private int electionId;

    private String candidateId;

    private LocalDateTime persistTime;

    private long votes;

    public Candidate() {
    }


    public Candidate(int electionId, String candidateId, LocalDateTime persistTime, long votes) {
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.persistTime = persistTime;
        this.votes = votes;
    }

    public int getElectionId() {
        return electionId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public LocalDateTime getPersistTime() {
        return persistTime;
    }

    public long getVotes() {
        return votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return getElectionId() == candidate.getElectionId() &&
                Objects.equals(getCandidateId(), candidate.getCandidateId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getElectionId(), getCandidateId());
    }

    @Override
    public String toString() {
        return "Candidate{" +
//                "id=" + getId() +
                ", electionId=" + electionId +
                ", candidateId='" + candidateId + '\'' +
                ", persistTime=" + persistTime +
                ", votes=" + votes +
                '}';
    }
}
