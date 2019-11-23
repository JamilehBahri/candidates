package bahri.jamileh.candidates.domain;


//import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

//@MappedSuperclass
//public abstract class AbstractTally extends AbstractDomain {
public abstract class AbstractTally {

     private int electionId;

     private String candidateId;

     private LocalDateTime startTime;

     private LocalDateTime persistTime;


    public AbstractTally(long id, int electionId, String candidateId,
                         LocalDateTime startTime, LocalDateTime persistTime) {
//        super(id);
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.startTime = startTime;
        this.persistTime = persistTime;
    }

    protected AbstractTally(int electionId, String candidateId,
                            LocalDateTime startTime, LocalDateTime persistTime) {
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.startTime = startTime;
        this.persistTime = persistTime;
    }

    public AbstractTally(){

    }
    public int getElectionId() {
        return electionId;
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
        AbstractTally that = (AbstractTally) o;
        return Objects.equals(getElectionId(), that.getElectionId()) &&
                Objects.equals(getCandidateId(), that.getCandidateId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getElectionId(), getCandidateId());
    }
}
