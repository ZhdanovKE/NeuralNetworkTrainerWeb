package neuralnetwork.trainerweb.bean.repository;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import neuralnetwork.commons.repository.NamedObjectRepository;
import neuralnetwork.commons.samples.SamplesRepository;
import neuralnetwork.jpa.entity.SamplesEntity;
import neuralnetwork.trainerweb.ejb.SamplesFacadeBeanLocal;

/**
 * CDI-managed repository that provides access to the available samples.
 * @author Konstantin Zhdanov
 */
@SessionScoped
public class SamplesRepositoryBean 
        extends NamedObjectRepository<SamplesRepository<Double>>
        implements Serializable {
    private static final long serialVersionUID = -5990089220125627257L;
    
    @EJB
    private SamplesFacadeBeanLocal samplesEJBfacade;
            
    public SamplesRepositoryBean() {
    }

    @Override
    public List<SamplesRepository<Double>> getObjectsList() {
        return samplesEJBfacade.findAll();
    }

    @Override
    public List<String> getNamesList() {
        return samplesEJBfacade.findAllNames();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return samplesEJBfacade.count();
    }

    @Override
    public void rename(String oldName, String newName) {
        if (oldName == null || newName == null) {
            throw new NullPointerException("Names cannot be null");
        }
        SamplesEntity entity = samplesEJBfacade.findByName(oldName);
        if (entity == null) {
            throw new IllegalArgumentException("No repository found for name: " +
                    oldName);
        }
        if (containsName(newName)) {
            throw new IllegalArgumentException("Repository with name " + newName +
                    " already exists");
        }
        entity.setName(newName);
        samplesEJBfacade.merge(entity);
    }

    @Override
    public boolean containsObject(SamplesRepository<Double> object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsName(String name) {
        return samplesEJBfacade.findByName(name) != null;
    }

    @Override
    public String getNameForObject(SamplesRepository<Double> object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SamplesRepository<Double> get(String name) {
        SamplesEntity entity = samplesEJBfacade.findByName(name);
        return entity == null ? null : entity.getRepository();
    }

    @Override
    public boolean remove(String name) {
        try {
            samplesEJBfacade.remove(name);
        }
        catch (EJBException e) {
            return false;
        }
        return true;
    }

    @Override
    public void add(String name, SamplesRepository<Double> object) {
        samplesEJBfacade.persist(name, object);
    }
}
