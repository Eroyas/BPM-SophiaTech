package fr.unice.polytech.bpm.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Get student schedule and find proper date
 */
public class ListPreRegistered implements JavaDelegate {

    /**
     * Compute the possible days for a Sophia Tech Forum for the students
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Liste des pré-inscrits:");
        delegateExecution.setVariable("pre-registered", "amadeus,air-france");
        System.out.println(delegateExecution.getVariable("pre-registered"));
    }
}
