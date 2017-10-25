package fr.unice.polytech.bpm.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Get student schedule and find proper date
 */
public class GetCompanyVoteResult implements JavaDelegate {

    /**
     * Compute the possible days for a Sophia Tech Forum for the students
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Récupération des votes utilisateurs");
        delegateExecution.setVariable("vote-company", "amadeus:10,air-france:10");
        System.out.println("Le système a récupéré les votes utilisateurs");
    }
}
