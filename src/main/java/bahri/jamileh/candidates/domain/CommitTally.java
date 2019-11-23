package bahri.jamileh.candidates.domain;

import java.time.LocalDateTime;
import java.util.Set;

public class CommitTally extends AbstractTally {

     private Set<String> adherents;

     private Set<String> opponents;

     private boolean tallyResult;


    public CommitTally(long id, int electionId, String candidateId,
                       LocalDateTime startTime, LocalDateTime persistTime) {
        super(id, electionId, candidateId, startTime, persistTime);
    }

    public CommitTally(int electionId, String candidateId, LocalDateTime startTime, LocalDateTime persistTime,
                       Set<String> adherents, Set<String> opponents, boolean tallyResult) {
        super(electionId, candidateId, startTime, persistTime);
        this.adherents = adherents;
        this.opponents = opponents;
        this.tallyResult = tallyResult;
    }

    public CommitTally(){

    }

    public Set<String> getAdherents() {
        return adherents;
    }

    public Set<String> getOpponents() {
        return opponents;
    }

    public boolean isTallyResult() {
        return tallyResult;
    }

    @Override
    public String toString() {
        return "CommitTally{" +
//                "id=" + getId() +
                ", electionId=" + getElectionId() +
                ", candidateId='" + getCandidateId() + '\'' +
                ", startTime=" + getStartTime() +
                ", persistTime=" + getPersistTime() +
                ", adherents=" + adherents +
                ", opponents=" + opponents +
                ", tallyResult=" + tallyResult +
                '}';
    }
}
