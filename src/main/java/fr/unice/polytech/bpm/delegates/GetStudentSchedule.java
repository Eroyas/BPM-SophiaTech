package fr.unice.polytech.bpm.delegates;

import fr.unice.polytech.bpm.process.SophiaTechForumProcess;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Get student schedule and find proper date
 */
public class GetStudentSchedule implements JavaDelegate {

    /**
     * Compute the possible days for a Sophia Tech Forum for the students
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Calcule de dates possibles pour les étudiants");
        delegateExecution.setVariable("student-availability", "Le 26 octobre 2017");
        System.out.println("Le système a trouvé des dates possibles pour les étudiants");
    }
}
