package neuralnetwork.trainerweb.bean;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    
    public List<String> getSamples() {
        return repository.getNamesList();
    }
}
