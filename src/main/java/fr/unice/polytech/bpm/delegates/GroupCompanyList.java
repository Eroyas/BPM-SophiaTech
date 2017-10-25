package fr.unice.polytech.bpm.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Get student schedule and find proper date
 */
public class GroupCompanyList implements JavaDelegate {

    /**
     * Compute the possible days for a Sophia Tech Forum for the students
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Regroupement des listes");
        delegateExecution.setVariable("registered", "bob,alice,amadeus");
        System.out.println("Bob, Alice, Amadeus");
    }
}
