package neuralnetwork.trainerweb.bean.repository;

import java.io.Serializable;
import java.util.Arrays;
import javax.enterprise.context.SessionScoped;
import neuralnetwork.commons.repository.NamedObjectRepository;
import neuralnetwork.commons.samples.SamplesRepository;

/**
 * CDI-managed repository that provides access to the available samples.
 * @author Konstantin Zhdanov
 */
@SessionScoped
public class SamplesRepositoryBean 
        extends NamedObjectRepository<SamplesRepository<Double>>
        implements Serializable {
    private static final long serialVersionUID = -5990089220125627257L;
    
    public SamplesRepositoryBean() {
        SamplesRepository<Double> repo1 = new SamplesRepository<>();
        repo1.add(Arrays.asList(0.1, 4.9, -2.3, 5.0));
        repo1.add(Arrays.asList(0.7, 2.1, 3.4, 4.0));
        SamplesRepository<Double> repo2 = new SamplesRepository<>();
        repo2.add(Arrays.asList(4.0, -2.0, 5.0));
        repo2.add(Arrays.asList(0.0, 2.0, 4.0));
        SamplesRepository<Double> repo3 = new SamplesRepository<>();
        repo3.add(Arrays.asList(0.4, -3.0));
        repo3.add(Arrays.asList(0.0, 1.0));
        
        super.add("Samples 1", repo1);
        super.add("Samples 2", repo2);
        super.add("Samples 3", repo3);
    }
}
