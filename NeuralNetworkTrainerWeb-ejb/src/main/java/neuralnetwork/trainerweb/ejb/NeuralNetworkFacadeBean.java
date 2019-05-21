package neuralnetwork.trainerweb.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.jpa.entity.NeuralNetworkEntity;

/**
 * Stateless EJB facade for performing database operations with 
 * NeuralNetwork entities.
 * @author Konstantin Zhdanov
 */
@Stateless
public class NeuralNetworkFacadeBean implements NeuralNetworkFacadeLocal {

    @PersistenceContext(unitName = "neuralnetworkPU")
    private EntityManager em;
    
    @Override
    public NeuralNetworkEntity findByName(String name) {
        List<NeuralNetworkEntity> entityList = em.
                createNamedQuery("NeuralNetworkEntity.findByName", 
                NeuralNetworkEntity.class).
                setParameter("name", name).
                getResultList();
        return entityList.isEmpty() ? null : entityList.get(0);
    }
    
    @Override
    public void persist(NamedNeuralNetwork namedNN) {
        NeuralNetworkEntity entity = findByName(namedNN.getName());
        
        if (entity != null) {
            entity.setNetwork(namedNN);
            em.merge(entity);
        }
        else {
            entity = new NeuralNetworkEntity();
            entity.setName(namedNN.getName());
            entity.setNetwork(namedNN);
            em.persist(entity);
        }
    }
    
    @Override
    public NeuralNetworkEntity merge(NeuralNetworkEntity entity) {
        return em.merge(entity);
    }
    
    @Override
    public void remove(NamedNeuralNetwork namedNN) {
        em.createNamedQuery("NeuralNetworkEntity.deleteByName").
                setParameter("name", namedNN.getName()).executeUpdate();
    }
    
    @Override
    public void remove(String name) {
        this.remove(findByName(name));
    }
    
    @Override
    public void remove(NeuralNetworkEntity entity) {
        em.remove(entity);
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("NeuralNetworkEntity.count").getFirstResult();
    }
    
    @Override
    public List<NamedNeuralNetwork> findAll() {
        List<NeuralNetworkEntity> entityList = 
                em.createNamedQuery("NeuralNetworkEntity.findAll", 
                        NeuralNetworkEntity.class).getResultList();
        List<NamedNeuralNetwork> namedNNList = new LinkedList<>();
        entityList.forEach(e -> namedNNList.add(e.getNetwork()));
        return namedNNList;
    }
}
