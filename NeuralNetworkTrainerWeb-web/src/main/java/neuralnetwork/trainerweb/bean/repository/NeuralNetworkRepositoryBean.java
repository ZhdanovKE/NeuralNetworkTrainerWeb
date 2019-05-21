package neuralnetwork.trainerweb.bean.repository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.commons.repository.NamedObjectRepository;
import neuralnetwork.jpa.entity.NeuralNetworkEntity;
import neuralnetwork.trainerweb.ejb.NeuralNetworkFacadeLocal;

/**
 * CDI-managed repository bean that provides access to the available 
 * Neural Networks.
 * @author Konstantin Zhdanov
 */
@SessionScoped
public class NeuralNetworkRepositoryBean extends NamedObjectRepository<NamedNeuralNetwork> 
        implements Serializable{
            
    private static final long serialVersionUID = -754029538755690090L;
    
    @EJB
    private NeuralNetworkFacadeLocal nnEJBFacade;
     
    public NeuralNetworkRepositoryBean() {
    }

    @Override
    public void add(String name, NamedNeuralNetwork object) {
        object.setName(name);
        nnEJBFacade.persist(object);
    }

    @Override
    public boolean remove(String name) {
        nnEJBFacade.remove(name);
        return true;
    }

    @Override
    public int size() {
        return nnEJBFacade.count();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public boolean containsName(String name) {
        return nnEJBFacade.findByName(name) != null;
    }

    @Override
    public NamedNeuralNetwork get(String name) {
        NeuralNetworkEntity entity = nnEJBFacade.findByName(name);
        return entity == null ? null : entity.getNetwork();
    }
    
    @Override
    public void rename(String oldName, String newName) {
        NeuralNetworkEntity entity = nnEJBFacade.findByName(oldName);
        entity.setName(newName);
        nnEJBFacade.merge(entity);
        super.onNameChange(entity.getNetwork(), oldName, newName);
    }

    @Override
    public boolean containsObject(NamedNeuralNetwork object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNameForObject(NamedNeuralNetwork object) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<String> getNamesList() {
        return nnEJBFacade.findAll().stream().
                map(NamedNeuralNetwork::getName).
                collect(Collectors.toList());
    }

    @Override
    public List<NamedNeuralNetwork> getObjectsList() {
        return nnEJBFacade.findAll();
    } 
}
