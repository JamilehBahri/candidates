package bahri.jamileh.candidates.domain;

//import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

//@MappedSuperclass
//public abstract class AbstractConsensus  extends AbstractDomain{
public abstract class AbstractConsensus  {

     private int electionId;

     private int consensusId;

     private String candidateId;

     private LocalDateTime startTime;

     private LocalDateTime persistTime;

     public AbstractConsensus(){

     }

    public AbstractConsensus(long id, int electionId, int consensusId, String candidateId,
                             LocalDateTime startTime, LocalDateTime persistTime) {
//        super(id);
        this.electionId = electionId;
        this.consensusId = consensusId;
        this.candidateId = candidateId;
        this.startTime = startTime;
        this.persistTime = persistTime;
    }

    protected AbstractConsensus(int consensusId, int electionId, String candidateId,
                                LocalDateTime startTime, LocalDateTime persistTime) {
        this.consensusId = consensusId;
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.startTime = startTime;
        this.persistTime = persistTime;
    }


    public int getElectionId() {
        return electionId;
    }

    public int getConsensusId() {
        return consensusId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getPersistTime() {
        return persistTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConsensus that = (AbstractConsensus) o;
        return getConsensusId() == that.getConsensusId() &&
                getElectionId() == that.getElectionId() &&
                Objects.equals(getCandidateId(), that.getCandidateId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getConsensusId(), getElectionId(), getCandidateId());
    }
}
