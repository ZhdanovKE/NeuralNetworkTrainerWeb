package neuralnetwork.trainerweb.bean.repository;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.commons.repository.NamedObjectRepository;

/**
 * CDI-managed repository bean that provides access to the available 
 * Neural Networks.
 * @author Konstantin Zhdanov
 */
@Dependent
public class NeuralNetworkRepositoryBean extends NamedObjectRepository<NamedNeuralNetwork> 
        implements Serializable{
    
    private static final long serialVersionUID = -754029538755690090L;
     
    public NeuralNetworkRepositoryBean() {
        super.add("Network 1", new NamedNeuralNetwork(1, new int[]{2, 3}, 4, "Network 1"));
        super.add("Network 2", new NamedNeuralNetwork(2, new int[]{3, 4}, 5, "Network 2"));
        super.add("Network 3", new NamedNeuralNetwork(3, new int[]{4, 5}, 6, "Network 3"));
    }
}
