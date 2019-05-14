package neuralnetwork.trainerweb.bean;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * CDI-managed bean of available networks to be used in JSF.
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class NetworkListBean  {
    
    private final NeuralNetworkRepositoryBean repository;
    
    @Inject
    public NetworkListBean(NeuralNetworkRepositoryBean repository) {
        this.repository = repository;
    }
    
    public List<NamedNeuralNetwork> getNetworks() {
        return repository.getObjectsList();
    }
}
