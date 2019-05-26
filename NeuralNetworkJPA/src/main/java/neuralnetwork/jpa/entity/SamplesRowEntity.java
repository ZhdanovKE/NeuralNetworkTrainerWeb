package neuralnetwork.jpa.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * JPA entity class holding a samples row's data.
 * @author Konstantin Zhdanov
 */
@Entity
@Table(name = "SAMPLES_ROWS")
public class SamplesRowEntity implements Serializable {
    private static final long serialVersionUID = 5402984811379082292L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SAMPLES_ID")
    private SamplesEntity samples;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "ROWS_VALUES",
            joinColumns = @JoinColumn(name = "ROW_ID")
    )
    @Column(name = "R_VALUE")
    @OrderColumn(name = "V_INDEX")
    private List<Double> rowValues;

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

    public SamplesEntity getSamples() {
        return samples;
    }

    public void setSamples(SamplesEntity samples) {
        this.samples = samples;
    }
    
    /**
     * Get the list of values for this row.
     * @return {@link List} of {@link Double} values.
     */
    public List<Double> getRowValues() {
        return rowValues;
    }

    /**
     * Set this row's values.
     * @param rowValues {@link List} of {@link Double} values.
     */
    public void setRowValues(List<Double> rowValues) {
        this.rowValues = rowValues;
    }
}
