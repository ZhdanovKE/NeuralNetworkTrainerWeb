package neuralnetwork.trainerweb.ejb;

import java.util.List;
import javax.ejb.Local;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.jpa.entity.NeuralNetworkEntity;

/**
 * Local interface for NeuralNetworkFacade EJB for interacting with
 * database.
 * @author Konstantin Zhdanov
 */
@Local
public interface NeuralNetworkFacadeLocal {

    /**
     * Number of neural networks stored in the database.
     * @return {@code int} value of the number of neural networks in the database.
     */
    int count();

    /**
     * List of all {@link NamedNeuralNetwork} instances stored in the database.
     * @return {@link List} containing instances of {@link NamedNeuralNetwork}
     * stored in the database. If the database contains no instances, an empty
     * {@link List} is returned.
     */
    List<NamedNeuralNetwork> findAll();

    /**
     * Get the JPA entity {@link NeuralNetworkEntity} stored in the database 
     * and associated with a {@link NamedNeuralNetwork} with particular 
     * {@code name}.
     * @param name {@link String} name of the network associated with the 
     * JPA entity to be returned.
     * @return Instance of {@link NeuralNetworkEntity} or null if no instance is
     * stored in the database for the {@code name}.
     */
    NeuralNetworkEntity findByName(String name);

    /**
     * Merge {@code entity} with the entity stored in the database and return the
     * merged version.
     * @param entity {@link NeuralNetworkEntity} to be merged.
     * @return Merged {@link NeuralNetworkEntity} instance.
     */
    NeuralNetworkEntity merge(NeuralNetworkEntity entity);

    /**
     * Save {@code namedNN} in the database. If the database doesn't contain 
     * an entity associated with the name of {@code namedNN}, then a new entity
     * is created and saved. If the database already contains
     * an entity associated with the name of {@code namedNN}, then the entity is
     * updated to contain the passed {@code namedNN}.
     * @param namedNN {@link NamedNeuralNetwork} to be saved into the database.
     */
    void persist(NamedNeuralNetwork namedNN);

    /**
     * Remove the JPA entity associated with {@code namedNN} from the database.
     * @param namedNN {@link NamedNeuralNetwork} associated with the JPA entity
     * to be deleted.
     * @throws  NullPointerException if {@code namedNN} is null.
     */
    void remove(NamedNeuralNetwork namedNN);

    /**
     * Remove the JPA entity associated with {@link NamedNeuralNetwork} with 
     * {@code name} from the database.
     * @param name {@link String} name of the network associated with the JPA entity
     * to be deleted.
     * @throws IllegalArgumentException if {@code name} is null or doesn't correspond
     * to any network in the database.
     */
    void remove(String name);

    /**
     * Remove JPA {@code entity} from the database.
     * @param entity {@link NeuralNetworkEntity} to be deleted.
     * @throws IllegalArgumentException if {@code entity} is null or not stored
     * in the database.
     */
    void remove(NeuralNetworkEntity entity);
    
}
