package bahri.jamileh.candidates.domain;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//public class Vote extends AbstractDomain {
    public class Vote {

    private int electionId;

    private String voteId;

    private String ballotBoxId;

    private LocalDateTime issuedTime;

    private LocalDateTime persistTime;

    private LocalDateTime lastChangeTime;

//    private List<Candidate> candidates;
    private Set<Integer> candidates;

    private VoteState voteState;

    private int grayStateCount;


    public Vote(int electionId, String voteId, String ballotBoxId, LocalDateTime issuedTime, LocalDateTime persistTime,
                LocalDateTime lastChangeTime, Set<Integer> candidates, VoteState voteState, int grayStateCount) {
        this.electionId = electionId;
        this.voteId = voteId;
        this.ballotBoxId = ballotBoxId;
        this.issuedTime = issuedTime;
        this.persistTime = persistTime;
        this.lastChangeTime = lastChangeTime;
        this.candidates = candidates;
        this.voteState = voteState;
        this.grayStateCount = grayStateCount;
    }

    public int getElectionId() {
        return electionId;
    }

    public String getVoteId() {
        return voteId;
    }

    public String getBallotBoxId() {
        return ballotBoxId;
    }

    public LocalDateTime getIssuedTime() {
        return issuedTime;
    }

    public LocalDateTime getPersistTime() {
        return persistTime;
    }

    public LocalDateTime getLastChangeTime() {
        return lastChangeTime;
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

    public VoteState getVoteState() {
        return voteState;
    }

    public int getGrayStateCount() {
        return grayStateCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote votes = (Vote) o;
        return Objects.equals(getVoteId(), votes.getVoteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVoteId());
    }

    @Override
    public String toString() {
        return "Vote{" +
//                "id=" + getId() +
                ", electionId=" + electionId +
                ", voteId='" + voteId + '\'' +
                ", ballotBoxId='" + ballotBoxId + '\'' +
                ", issuedTime=" + issuedTime +
                ", persistTime=" + persistTime +
                ", lastChangeTime=" + lastChangeTime +
                ", candidates=" + candidates +
                ", voteState=" + voteState +
                ", grayStateCount=" + grayStateCount +
                '}';
    }
}
