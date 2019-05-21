package neuralnetwork.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import neuralnetwork.NamedNeuralNetwork;

/**
 * JPA entity for {@link NamedNeuralNetwork} class.
 * @author Konstantin Zhdanov
 */
@Entity
@Table(name = "NEURAL_NETWORK")
@NamedQueries({
    @NamedQuery(name = "NeuralNetworkEntity.findAll", 
            query = "select n from NeuralNetworkEntity n"),
    @NamedQuery(name = "NeuralNetworkEntity.findByName", 
            query = "select n from NeuralNetworkEntity n where n.name = :name"),
    @NamedQuery(name = "NeuralNetworkEntity.deleteByName", 
            query = "delete from NeuralNetworkEntity n where n.name = :name"),
    @NamedQuery(name = "NeuralNetworkEntity.count", 
            query = "select count(n.id) from NeuralNetworkEntity n")
})
public class NeuralNetworkEntity implements Serializable {
    
    private static final long serialVersionUID = 57333489352858929L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Lob
    @Column(name = "NETWORK", nullable = false)
    private NamedNeuralNetwork network;

    /**
     * Create empty entity.
     */
    public NeuralNetworkEntity() {
    }
    
    /**
     * Id value (database-specific) of this entity.
     * @return {@code long} value of the id column in the database.
     */
    public long getId() {
        return id;
    }

    /**
     * Set the value of the id column for this entity.
     * @param id {@code long} value of the id column in the database.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Name of the network stored in this entity.
     * @return {@code String} name of the network stored in this entity.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the network stored in this entity.
     * @param name {@code String} name of the network stored in this entity.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the instance of {@link NamedNeuralNetwork} stored in this entity.
     * @return Instance of {@link NamedNeuralNetwork} stored in this entity.
     */
    public NamedNeuralNetwork getNetwork() {
        return network;
    }

    /**
     * Set the network to be stored in this entity.
     * @param network Instance of {@link NamedNeuralNetwork} to be stored in 
     * this entity.
     */
    public void setNetwork(NamedNeuralNetwork network) {
        this.network = network;
    }
}
