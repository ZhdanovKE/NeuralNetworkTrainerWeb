package neuralnetwork.trainerweb.bean;

import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import neuralnetwork.NeuralNetwork;

/**
 * CDI-managed bean of available networks.
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class NetworkListBean {
    private final List<NeuralNetwork> networks;
    
    public NetworkListBean() {
        networks = Arrays.asList(
                new NeuralNetwork(1, new int[]{2, 3}, 4),
                new NeuralNetwork(2, new int[]{3, 4}, 5),
                new NeuralNetwork(3, new int[]{4, 5}, 6)
        );
    }
    
    public List<NeuralNetwork> getNetworks() {
        return networks;
    }
    
    public String sayHello() {
        return "Hello";
    }
}
