package neuralnetwork.trainerweb.bean.form;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.init.Initializer;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * JSF Bean to be populated with values the user entered in the 'Add new network' 
 * form and to be used to create a concrete instance of network or load it from 
 * a file.
 * @author Konstantin Zhdanov
 */
@Named
@ViewScoped
public class AddNewNetworkFormBean implements Serializable {
    
    private static final long serialVersionUID = 6419740899543183818L;
    
    @Inject
    private NeuralNetworkRepositoryBean nnRepository;
    
    private String name;
    
    private int numInputs;
    
    private int numHiddenLayers;

    private List<Integer> hiddenLayerSizes;
    
    private int numOutputs;
    
    private boolean randomizeWeights;
    
    private boolean created;
    
    /**
     * Load default values.
     */
    public AddNewNetworkFormBean() {
        name = "New Network";
        numInputs = 1;
        numHiddenLayers++;
        hiddenLayerSizes = new LinkedList<>();
        hiddenLayerSizes.add(1);
        numOutputs = 1;
        randomizeWeights = false;
        created = false;
    }
    
    /*********************************** 
     ***********************************  
     *******GETTERS AND SETTERS********* 
     ***********************************  
     **********************************/
    
    /**
     * Get the network's name
     * @return {@code String} value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the network's name.
     * @param name New name to be used for creating a new network.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the number of input neurons.
     * @return {@code int} value of the number of input neurons.
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * Set the number of input neurons.
     * @param numInputs New number of input neurons.
     */
    public void setNumInputs(int numInputs) {
        this.numInputs = numInputs;
    }

    /**
     * Get the number of hidden layers.
     * @return {@code int} value of the number of hidden layers.
     */
    public int getNumHiddenLayers() {
        return numHiddenLayers;
    }

    /**
     * Set the number of hidden layers
     * @param numHiddenLayers New value of the number of hidden layers.
     */
    public void setNumHiddenLayers(int numHiddenLayers) {
        this.numHiddenLayers = numHiddenLayers;
    }

    /**
     * Get the list of hidden layers sizes.
     * @return {@code List<Integer>} containing the hidden layers sizes.
     */
    public List<Integer> getHiddenLayerSizes() {
        return hiddenLayerSizes;
    }

    /**
     * Set the list of hidden layers sizes.
     * @param hiddenLayerSizes New {@code List<Integer>} to be used as the hidden layers sizes.
     */
    public void setHiddenLayerSizes(List<Integer> hiddenLayerSizes) {
        this.hiddenLayerSizes = hiddenLayerSizes;
    }

    /**
     * Get the number of output neurons.
     * @return {@code int} value of the number of output neurons.
     */
    public int getNumOutputs() {
        return numOutputs;
    }

    /**
     * Set the number of output neurons.
     * @param numOutputs New value of the number of output neurons.
     */
    public void setNumOutputs(int numOutputs) {
        this.numOutputs = numOutputs;
    }
    
    /**
     * If the weights must be randomized.
     * @return {@code boolean} value of whether the weights of the network
     * must be randomize upon creation.
     */
    public boolean isRandomizeWeights() {
        return randomizeWeights;
    }

    /**
     * Set if the weights must be randomized.
     * @param randomizeWeights whether the weights of the network
     * must be randomize upon creation.
     */
    public void setRandomizeWeights(boolean randomizeWeights) {
        this.randomizeWeights = randomizeWeights;
    }

    /**
     * If a network with this instance's
     * values has been created successfully.
     * @return {@code boolean} value of whether a network with this instance's
     * values has been created successfully.
     */
    public boolean isCreated() {
        return created;
    }

    /**
     * Set the flag of whether a network with this instance's
     * values has been created successfully.
     * @param created {@code boolean} value to set.
     */
    public void setCreated(boolean created) {
        this.created = created;
    }
    
    /*********************************** 
     ***********************************  
     *******HIDDEN LAYERS METHODS******* 
     ***********************************  
     **********************************/
    
    /**
     * Add another hidden layer.
     */
    public void addHiddenLayer() {
        hiddenLayerSizes.add(1);
        numHiddenLayers++;
    }
    
    /**
     * Remove the last hidden layer unless it's the last hidden layer remaining.
     */
    public void removeHiddenLayer() {
        if (numHiddenLayers == 1) {
            return;
        }
        hiddenLayerSizes.remove(hiddenLayerSizes.size() - 1);
        numHiddenLayers--;
    }
    
    /*********************************** 
     ***********************************  
     ************VALIDATORS*************
     ***********************************  
     **********************************/
    
    /**
     * JSF validation method for checking the 'name' field of the form. 
     * To pass validation, the name must be non-empty, non-blank and
     * non-existing in the repository.
     * @param context JSF context.
     * @param component UI component.
     * @param value Value of the name.
     * @throws ValidatorException if the name is not correct.
     */
    public void validateName(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        String nameValue = (String)value;
        if (nameValue == null || nameValue.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage("Please provide a non-empty name."));
        }
        if (nnRepository.containsName(nameValue.trim())) {
            throw new ValidatorException(new FacesMessage("Name already exists. Choose another name."));
        }
        
    }
    
    /*********************************** 
     ***********************************  
     ************SUBMIT METHOD**********
     ***********************************  
     **********************************/
    
    /**
     * Process the user-filled form and create a new network.
     */
    public void create() {
        String trimmedName = name.trim();
        NamedNeuralNetwork newNN = new NamedNeuralNetwork(
                numInputs, 
                hiddenLayerSizes.stream().mapToInt(Integer::intValue).toArray(), 
                numOutputs, 
                trimmedName,
                randomizeWeights ? 
                        Initializer.ofStdRandomRange() : 
                        Initializer.of(0.0, 0.0)
        );
        nnRepository.add(trimmedName, newNN);
        created = true;
    }
}
