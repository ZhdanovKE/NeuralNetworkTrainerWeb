package neuralnetwork.trainerweb.bean;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.commons.samples.SamplesRepository;
import neuralnetwork.trainerweb.bean.repository.SamplesRepositoryBean;

/**
 * CDI-managed bean of available samples. 
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class SamplesListBean {
    private final SamplesRepositoryBean repository;
    
    @Inject
    public SamplesListBean(SamplesRepositoryBean repository) {
        this.repository = repository;
    }
    
    /**
     * Get the list of the names of all samples in the repository.
     * @return {@link List} of {@link String} names of the samples.
     */
    public List<String> getSamplesNames() {
        return repository.getNamesList();
    }
    
    /**
     * Get the samples for provided name from the repository.
     * @param name {@link String} name of the samples to look up.
     * @return {@link SamplesRepository} for the provided name or {@code null}
     * if there are no samples for the {@code name}.
     */
    public SamplesRepository<Double> getSamplesForName(String name) {
        return repository.get(name);
    }
}
