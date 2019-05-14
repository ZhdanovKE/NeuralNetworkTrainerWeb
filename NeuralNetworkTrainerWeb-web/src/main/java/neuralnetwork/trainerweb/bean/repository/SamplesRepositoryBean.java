package neuralnetwork.trainerweb.bean.repository;

import javax.enterprise.context.Dependent;
import neuralnetwork.commons.repository.NamedObjectRepository;
import neuralnetwork.commons.samples.SamplesRepository;

/**
 * CDI-managed repository that provides access to the available samples.
 * @author Konstantin Zhdanov
 */
@Dependent
public class SamplesRepositoryBean extends NamedObjectRepository<SamplesRepository<Double>>{
    
    public SamplesRepositoryBean() {
        super.add("Samples 1", new SamplesRepository<>());
        super.add("Samples 2", new SamplesRepository<>());
        super.add("Samples 3", new SamplesRepository<>());
    }
}
