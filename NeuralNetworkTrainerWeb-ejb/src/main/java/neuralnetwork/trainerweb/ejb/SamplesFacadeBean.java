package neuralnetwork.trainerweb.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import neuralnetwork.commons.samples.SamplesRepository;
import neuralnetwork.jpa.entity.SamplesEntity;

/**
 * EJB facade for performing CRUD operations with the samples database.
 * @author Konstantin Zhdanov
 */
@Stateless
public class SamplesFacadeBean implements SamplesFacadeBeanLocal {
    
    @PersistenceContext(unitName = "neuralnetworksamplesPU")
    private EntityManager em;
    
    @Override
    public SamplesEntity findByName(String name) {
        List<SamplesEntity> entityList = em.
                createNamedQuery("SamplesEntity.findByName", 
                SamplesEntity.class).
                setParameter("name", name).
                getResultList();
        return entityList.isEmpty() ? null : entityList.get(0);
    }
    
    @Override
    public List<SamplesRepository<Double>> findAll() {
        List<SamplesEntity> entityList = 
                em.createNamedQuery("SamplesEntity.findAll", 
                        SamplesEntity.class).getResultList();
        List<SamplesRepository<Double>> namedNNList = new LinkedList<>();
        entityList.forEach(e -> namedNNList.add(e.getRepository()));
        return namedNNList;
    }

    @Override
    public List<String> findAllNames() {
        return em.createNamedQuery("SamplesEntity.findAllNames", String.class).
                getResultList();
    }
    
    @Override
    public void persist(SamplesEntity entity) {
        em.persist(entity);
    }
    
    @Override
    public void persist(String name, SamplesRepository<Double> repository) {
        if (repository == null) {
            throw new NullPointerException("Samples repository cannot be null");
        }
        SamplesEntity entity = findByName(name);
        if (entity == null) {
            entity = new SamplesEntity();
            entity.setName(name);
            entity.setRepository(repository);
            em.persist(entity);
        }
        else {
            em.detach(entity);
            entity.setRepository(repository);
            em.merge(entity);
        }
    }
    
    @Override
    public SamplesEntity merge(SamplesEntity entity) {
        return em.merge(entity);
    }
    
    @Override
    public void remove(SamplesEntity entity) {
        em.remove(entity);
    }

    @Override
    public void remove(String name) {
        em.remove(findByName(name));
    }

    @Override
    public int count() {
        return em.createNamedQuery("SamplesEntity.count").getFirstResult();
    }
}
