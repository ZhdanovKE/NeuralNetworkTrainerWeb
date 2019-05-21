package neuralnetwork.trainerweb.bean.form;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * CDI-managed bean performing deletion of neural network from the repository.
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class NetworkRemoveBean {
    
    @Inject
    private NeuralNetworkRepositoryBean repository;
            
    private String nameToDelete;

    /**
     * Name of the network to be removed from the repository.
     * @return {@link String} name of the network to be removed.
     */
    public String getNameToDelete() {
        return nameToDelete;
    }

    /**
     * Set the name of the network to be removed from the repository.
     * @param nameToDelete {@link String} name of the network to be removed.
     */
    public void setNameToDelete(String nameToDelete) {
        this.nameToDelete = nameToDelete;
    }
    
    /**
     * Perform removal of the neural network stored under the chosen in the form 
     * name in the repository. If the removal fails, an error message is added to
     * {@link FacesContext}
     * @return Redirect to the same view that sent the request.
     */
    public String remove() {
        if (!repository.remove(nameToDelete)) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Failed to delete network '" + nameToDelete +"'."));
        }
        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + 
                "?faces-redirect=true";
    }
}
