package neuralnetwork.trainerweb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * JSF bean for representing a named neural network.
 * @author Konstantin Zhdanov
 */
@Named
@ViewScoped 
public class NetworkBean implements Serializable {
    private static final long serialVersionUID = 2256786600647175923L;
    
    @Inject
    NeuralNetworkRepositoryBean repository;
    
    NamedNeuralNetwork nn;
    
    private String chosenName;
    
    private boolean saved;
    
    private List<double[][]> weightsForLayers;
    private List<double[]> biasesForLayers;
    
    private List<DataModel<double[]>> weightsForLayersModels;
    
    /**
     * Load network from the repository by the name {@code name}.
     * @param name {@code String} name of the network to be loaded.
     */
    public void initWithName(String name) {
        saved = false;
        if (    name == null || 
                name.isEmpty() || 
                (nn = repository.get(name)) == null ) {
            return;
        }

        initWeightsForLayers();
        initBiasesForLayers();
    }
    
    /**
     * Validate if network with name {@code value} exists if the repository.
     * @param context JSF context.
     * @param component JSF component.
     * @param value {@code String} name of the network.
     * @throws ValidatorException If no network with provided name exists.
     */
    public void validateName(FacesContext context, UIComponent component, Object value) 
                throws ValidatorException {
        String name = (String)value;
        if (!repository.containsName(name)) {
            throw new ValidatorException(new FacesMessage("Network with name \"" + name + 
                            "\" doesn't exist"));
        }
    }
    
    /**
     * Save user-entered values of weights and biases into the loaded network.
     */
    public void save() {
        copyWeightsAndBiasesToNetwork(nn);
        repository.add(chosenName, nn);
        setSaved(true);
    }
    
    /**
     * Set new value to the 'saved' property.
     * @param newValue {@code true} if network has been saved successfully,
     * {@code false} otherwise.
     */
    public void setSaved(boolean newValue) {
        saved = newValue;
    }
    
    /**
     * Whether the currently loaded network has been saved successfully.
     * @return {@code true} if network has been saved successfully,
     * {@code false} otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
    
    /**
     * Name of the loaded network.
     * @return Name of the loaded network.
     */
    public String getChosenName() {
        return chosenName;
    }

    /**
     * Change the name of the network to be loaded.
     * @param chosenName Name of the network to load.
     */
    public void setChosenName(String chosenName) {
        this.chosenName = chosenName;
        initWithName(chosenName);
    }
    
    /**
     * List of all available networks' names.
     * @return {@code List<String>} of networks' names.
     */
    public List<String> getAllNames() {
        return repository.getNamesList();
    }
    
    /**
     * Check is a network has been loaded successfully.
     * @return {@code true} if a networks has been loaded, {@code false} otherwise.
     */
    public boolean isExist() {
        return nn != null;
    }
    
    /**
     * Signature of the loaded network.
     * @return {@String} signature of the loaded network or an empty string if
     * no network has been loaded.
     */
    public String getSignature() {
        if (nn != null) {
            return nn.getSignature();
        }
        return "";
    }
    
    /**
     * Names of the layers with indices {@code layerNum - 1} and {@code layerNum}.
     * @param layerNum Index of the layer to the right. {@code 0} means the first
     * hidden layer.
     * @return Two-element array of {@String} names.
     */
    public String[] getLayerNamesForLayer(int layerNum) {
        String curLayerStr;
        String prevLayerStr;
        if (layerNum == 0) {
            // first hidden layer
            curLayerStr = "Layer 1";
            prevLayerStr = "Input layer";
        }
        else if (layerNum == nn.getNumberHiddenLayers()) {
            // output layer
            curLayerStr = "Output layer";
            prevLayerStr = "Layer " + (layerNum);
        }
        else {
            curLayerStr = "Layer " + (layerNum + 1);
            prevLayerStr = "Layer " + (layerNum);
        }
        return new String[] {prevLayerStr, curLayerStr};
    }
    
    /**
     * 2D array of weights for the loaded network between the layers with indices
     * {@code layerNum - 1} and {@code layerNum}.
     * @param layerNum Index of the layer to the right. {@code 0} means the first hidden
     * layer.
     * @return 2D array of weights between the layers or an empty array if no
     * network has been loaded.
     */
    public double[][] getWeightsForLayer(int layerNum) {
        if (nn == null) {
            return new double[0][0];
        }
        
        return weightsForLayers.get(layerNum);
    }
    
    /**
     * {@link DataModel} object holding 2D array of weights for the loaded 
     * network between the layers with indices {@code layerNum - 1} and {@code layerNum}.
     * @param layerNum Index of the layer to the right. {@code 0} means the first hidden
     * layer.
     * @return {@link DataModel} object holding 2D array of weights between 
     * the layers or an empty array if no network has been loaded.
     */
    public DataModel<double[]> getWeightsModelForLayer(int layerNum) {
        if (nn == null) {
            return new ArrayDataModel<>();
        }
        
        return weightsForLayersModels.get(layerNum);
    }
    
    /**
     * Array of biases for the loaded network's layer with index {@code layerNum}.
     * @param layerNum Index of the layer. {@code 0} means the first hidden
     * layer.
     * @return Array of weights biases for the layer or an empty array if no
     * network has been loaded.
     */
    public double[] getBiasesForLayer(int layerNum) {
        if (nn == null) {
            return new double[0];
        }
        
        return biasesForLayers.get(layerNum);
    }
    
    /**
     * Number of the hidden layers.
     * @return The number of the hidden layers in the loaded network or {@code -1}
     * if no network has been loaded.
     */
    public int getNumberHiddenLayers() {
        if (nn == null) {
            return -1;
        }
        return nn.getNumberHiddenLayers();
    }
    
    /**
     * Size of the layer with index {@code layerNum}, starting with the first
     * hidden layer.
     * @param layerNum Index of the layer. {@code 0} means the first hidden layer.
     * The last layer is the output layer.
     * @return The size of the layer (number of neurons) or {@code 0} if no
     * network has been loaded.
     */
    public int getLayerSize(int layerNum) {
        if (nn == null) {
            return 0;
        }
        if (layerNum == nn.getNumberHiddenLayers()) {
            return nn.getNumberOutputs();
        }
        return nn.getHiddenLayerSize(layerNum);
    }
    
    /**
     * Size of the layer previous to the layer with index {@code layerNum}, 
     * starting with the first hidden layer.
     * @param layerNum Index of the layer whose predecessor's size is sought. 
     * {@code 0} means the first hidden layer.
     * @return The size of the layer (number of neurons) or {@code 0} if no
     * network has been loaded.
     */
    public int getPrevLayerSizeForLayer(int layerNum) {
        if (nn == null) {
            return 0;
        }
        if (layerNum == 0) {
            return nn.getNumberInputs();
        }
        return nn.getHiddenLayerSize(layerNum - 1);
    }

    private void fillWeightsFromNetworkForLayer(double[][] layerWeights, int layerNum) {
        for (int fromNeuron = 0; fromNeuron < layerWeights.length; fromNeuron++) {
            for (int toNeuron = 0; toNeuron < layerWeights[fromNeuron].length; toNeuron++) {
                layerWeights[fromNeuron][toNeuron] = 
                        nn.getWeight(layerNum, fromNeuron, toNeuron);
            }
        }
    }
    
    private void fillBiasesFromNetworkForLayer(double[] layerBiases, int layerNum) {
        for (int toNeuron = 0; toNeuron < layerBiases.length; toNeuron++) {
            layerBiases[toNeuron] = nn.getBias(layerNum, toNeuron);
        }
    }

    private void initWeightsForLayers() {
        weightsForLayers = new ArrayList<>(nn.getNumberHiddenLayers() + 1);
        weightsForLayersModels = new ArrayList<>(nn.getNumberHiddenLayers() + 1);
        // input <-> first hidden
        double[][] layerWeights = new double[nn.getNumberInputs()]
                [nn.getHiddenLayerSize(0)];
        fillWeightsFromNetworkForLayer(layerWeights, 0);
        weightsForLayers.add(layerWeights);
        weightsForLayersModels.add(new ArrayDataModel<>(layerWeights));
        
        // hidden layer i <-> hidden layer i+1
        for (int layerNum = 1; layerNum < nn.getNumberHiddenLayers(); layerNum++) {
            layerWeights = new double[nn.getHiddenLayerSize(layerNum - 1)]
                [nn.getHiddenLayerSize(layerNum)];
            fillWeightsFromNetworkForLayer(layerWeights, layerNum);
            weightsForLayers.add(layerWeights);
            weightsForLayersModels.add(new ArrayDataModel<>(layerWeights));
        }
        
        // last hidden layer <-> output
        layerWeights = new double[nn.getHiddenLayerSize(nn.getNumberHiddenLayers() - 1)]
                [nn.getNumberOutputs()];
        fillWeightsFromNetworkForLayer(layerWeights, nn.getNumberHiddenLayers());
        weightsForLayers.add(layerWeights);
        weightsForLayersModels.add(new ArrayDataModel<>(layerWeights));
    }

    private void initBiasesForLayers() {
        biasesForLayers = new ArrayList<>(nn.getNumberHiddenLayers() + 1);
        double[] layerBiases;
        // hidden layers
        for (int layerNum = 0; layerNum < nn.getNumberHiddenLayers(); layerNum++) {
            layerBiases = new double[nn.getHiddenLayerSize(layerNum)];
            fillBiasesFromNetworkForLayer(layerBiases, layerNum);
            biasesForLayers.add(layerBiases);
        }
        
        // output
        layerBiases = new double[nn.getNumberOutputs()];
        fillBiasesFromNetworkForLayer(layerBiases, nn.getNumberHiddenLayers());
        biasesForLayers.add(layerBiases);
    }

    private void copyWeightsAndBiasesToNetwork(NamedNeuralNetwork nn) {
        if (nn == null) {
            return;
        }

        for (int layerNum = 0; layerNum <= nn.getNumberHiddenLayers(); layerNum++) {
            double[][] weights = weightsForLayers.get(layerNum);
            for (int prevNeuron = 0; prevNeuron < weights.length; prevNeuron++) {
                for (int neuron = 0; neuron < weights[prevNeuron].length; neuron++) {
                    nn.setWeight(layerNum, prevNeuron, neuron, weights[prevNeuron][neuron]);
                }
            }

            double[] biases = biasesForLayers.get(layerNum);
            for (int neuron = 0; neuron < biases.length; neuron++) {
                nn.setBias(layerNum, neuron, biases[neuron]);
            }
        }
    }
}
