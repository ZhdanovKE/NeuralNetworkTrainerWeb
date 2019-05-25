package neuralnetwork.trainerweb.ejb;

import java.util.List;
import javax.ejb.Local;
import neuralnetwork.commons.samples.SamplesRepository;
import neuralnetwork.jpa.entity.SamplesEntity;

/**
 * Local interface for EJB {@link SamplesFacadeBean}.
 * @author Konstantin Zhdanov
 */
@Local
public interface SamplesFacadeBeanLocal {
    
    /**
     * Number of samples repositories stored in the database.
     * @return {@code int} value of the number of samples repositories in the database.
     */
    int count();

    /**
     * List of all {@link SamplesRepository} instances stored in the database.
     * @return {@link List} containing instances of {@link SamplesRepository}
     * stored in the database. If the database contains no instances, an empty
     * {@link List} is returned.
     */
    List<SamplesRepository<Double>> findAll();

    /**
     * Get the JPA entity {@link NSamplesEntity} stored in the database 
     * and associated with a {@link SamplesRepository} with particular 
     * {@code name}.
     * @param name {@link String} name of the samples repository associated with 
     * the JPA entity to be returned.
     * @return Instance of {@link SamplesEntity} or null if no instance is
     * stored in the database for the {@code name}.
     */
    SamplesEntity findByName(String name);

    /**
     * Get all names stored in the database.
     * @return {@link List} of {@link String} names of all objects stored in
     * the database.
     */
    List<String> findAllNames();
    
    /**
     * Merge {@code entity} with the entity stored in the database and return the
     * merged version.
     * @param entity {@link SamplesEntity} to be merged.
     * @return Merged {@link SamplesEntity} instance.
     */
    SamplesEntity merge(SamplesEntity entity);

    /**
     * Save the {@link SamplesEntity} into the database
     * @param entity {@link SamplesEntity} to be saved into the database.
     */
    void persist(SamplesEntity entity);

    /**
     * Save {@code repository} in the database under the name {@code name}. 
     * If the database doesn't contain 
     * an entity associated with the {@code name}, then a new entity
     * is created and saved. If the database already contains
     * an entity associated with the {@code name}, then the entity is
     * updated to contain the passed {@code repository}.
     * @param name {@link String} name to be associated with {@code repository}.
     * @param repository {@link SamplesRepository} to be saved into the database.
     */
    void persist(String name, SamplesRepository<Double> repository);

    /**
     * Remove JPA {@code entity} from the database.
     * @param entity {@link SamplesEntity} to be deleted.
     * @throws IllegalArgumentException if {@code entity} is null or not stored
     * in the database.
     */
    void remove(SamplesEntity entity);

    /**
     * Remove the JPA entity associated with {@link SamplesRepository} with 
     * {@code name} from the database.
     * @param name {@link String} name of the samples repository associated with 
     * the JPA entity to be deleted.
     * @throws IllegalArgumentException if {@code name} is null or doesn't correspond
     * to any samples repository in the database.
     */
    void remove(String name);
}
