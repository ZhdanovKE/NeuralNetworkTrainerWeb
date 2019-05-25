package neuralnetwork.jpa.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import neuralnetwork.commons.samples.SamplesRepository;

/**
 * JPA entity for the training samplesRows repository {@link SamplesRepository}.
 * @author Konstantin Zhdanov
 */
@Entity
@Table(name = "SAMPLES")
@NamedQueries({
    @NamedQuery(
            name = "SamplesEntity.findByName", 
            query = "select object(o) from SamplesEntity o where o.name = :name"
    ),
    @NamedQuery(
            name = "SamplesEntity.findAll", 
            query = "select object(o) from SamplesEntity o"
    ),
    @NamedQuery(
            name = "SamplesEntity.findAllNames", 
            query = "select o.name from SamplesEntity o"
    ),
    @NamedQuery(
            name = "SamplesEntity.removeByName", 
            query = "delete from SamplesEntity o where o.name = :name"
    ),
    @NamedQuery(
            name = "SamplesEntity.count", 
            query = "select count(o.id) from SamplesEntity o"
    )
})
public class SamplesEntity implements Serializable {
    private static final long serialVersionUID = 190368860604772039L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @Column(name = "NAME")
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, 
            fetch = FetchType.EAGER)
    @OrderColumn(name = "R_INDEX")
    private List<SamplesRowEntity> samplesRows;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "SAMPLES_HEADERS", 
            joinColumns = @JoinColumn(name = "SAMPLES_ID")
    )
    @Column(name = "HEADER")
    @OrderColumn(name = "H_INDEX")
    private List<String> samplesHeaders;

    /**
     * Get the id value of this entity.
     * @return {@code long} value of the id column in the database.
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id value for this entity.
     * @param id {@code long} value of the id column.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the name of this entity's repository.
     * @return {@link String} value of the name of the samples repository stored
     * in the database.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this entity's repository.
     * @param name {@link String} value of the name of the samples repository
     * to be set in the database.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Generate and return an instance of {@link SamplesRepository} holding
     * this entity's values and headers.
     * @return {@link SamplesRepository} filled with the data stored in this entity.
     */
    public SamplesRepository<Double> getRepository() {
        SamplesRepository<Double> repository = new SamplesRepository<>();
        samplesRows.forEach(sRow -> repository.add(sRow.getRowValues()));
        repository.setHeader(samplesHeaders);
        return repository;
    }

    /**
     * Initialize this entity with values and headers from the instance of 
     * {@link SamplesRepository}.
     * @param repository {@link SamplesRepository} which values and headers
     * will be copied into this entity.
     */
    public void setRepository(SamplesRepository<Double> repository) {
        setSamplesHeaders(repository.getHeader());
        List<List<Double>> samples = repository.getAll();
        samplesRows = new LinkedList<>();
        samples.stream().map((row) -> {
            SamplesRowEntity sRow = new SamplesRowEntity();
            sRow.setRowValues(row);
            return sRow;
        }).forEachOrdered((sRow) -> {
            samplesRows.add(sRow);
        });
        setSamplesRows(samplesRows);
    }

    /**
     * Get the list of {@link SamplesRowEntity} instances stored in
     * this entity.
     * @return {@link List} of {@link SamplesRowEntity} associated
     * with this entity as the samples repository's values.
     */
    public List<SamplesRowEntity> getSamplesRows() {
        return samplesRows;
    }

    /**
     * Set the list of {@link SamplesRowEntity} instances to be stored in
     * this entity.
     * @param samplesRows {@link List} of {@link SamplesRowEntity} to be associated
     * with this entity as the samples repository's values.
     */
    public void setSamplesRows(List<SamplesRowEntity> samplesRows) {
        this.samplesRows = samplesRows;
    }

    /**
     * Get the list of header values associated with this entity.
     * @return {@link List} of {@link String} values associated
     * with this entity as the samples repository's header values.
     */
    public List<String> getSamplesHeaders() {
        return samplesHeaders;
    }

    /**
     * Set the list of header values to be associated with this entity.
     * @param samplesHeaders {@link List} of {@link String} values to be associated
     * with this entity as the samples repository's header values.
     */
    public void setSamplesHeaders(List<String> samplesHeaders) {
        this.samplesHeaders = samplesHeaders;
    }
}
