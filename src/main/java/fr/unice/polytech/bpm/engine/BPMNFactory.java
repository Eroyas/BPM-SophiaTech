package fr.unice.polytech.bpm.engine;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.DeploymentBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A BPM Factory to create a business proccess engine
 */
public class BPMNFactory {

    private List<String> resources;
    private String businessProccess;

    private BPMNFactory(String businessProccess) {
        this.businessProccess = businessProccess;
        this.resources = new ArrayList<String>();
    }

    /**
     * Get a new BPMN factory
     * @param businessProccess
     * @return
     */
    public static BPMNFactory create(String businessProccess) {
        return new BPMNFactory(businessProccess);
    }

    /**
     * Add a diagram to the factory
     * @param fileName
     * @return
     */
    public BPMNFactory withDiagram(String fileName) {
        this.resources.add(fileName);
        return this;
    }

    /**
     * Builds a process engine from the current context
     * @return
     */
    public ProcessEngine build() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // Add resources
        addResources(processEngine, resources);

        return processEngine;
    }

    /**
     * Adds resources to the repository service of the process engine
     * @param processEngine
     * @param fileNames
     */
    private static void addResources(ProcessEngine processEngine, List<String> fileNames) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        fileNames.forEach(fileName -> deployment.addClasspathResource(fileName));
        deployment.deploy();
    }
}
