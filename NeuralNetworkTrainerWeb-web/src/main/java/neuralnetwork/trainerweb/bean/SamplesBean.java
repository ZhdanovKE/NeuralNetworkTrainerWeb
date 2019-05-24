package neuralnetwork.trainerweb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.commons.samples.SamplesRepository;
import neuralnetwork.trainerweb.bean.repository.SamplesRepositoryBean;

/**
 * JSF bean for representing a neural network's samples.
 * @author Konstantin Zhdanov
 */
@Named
@ViewScoped 
public class SamplesBean implements Serializable {
    private static final long serialVersionUID = 3719910951263925L;
    
    @Inject
    SamplesRepositoryBean repository;
    
    SamplesRepository<Double> samplesRepo;
    
    private String chosenName;
    
    private String newName;
    
    private boolean saved;
    
    /**
     * Load samples from the repository by the name {@code name}.
     * @param name {@code String} name of the samples to be loaded.
     */
    public void initWithName(String name) {
        saved = false;
        if (    name == null || 
                name.isEmpty() || 
                (samplesRepo = repository.get(name)) == null ) {
            return;
        }
        newName = name;
    }
    
    /**
     * Perform rename operation of the chosen samples with the entered name.
     * If the name stays the same, this method does nothing.
     * @return {@code null} if there was an error while renaming, {@link String}
     * name of the same view to be redirected to with the new name as a view 
     * parameter.
     */
    public String rename() {
        if (    newName == null ||
                newName.trim().isEmpty() ) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("New name cannot be empty."));
            newName = chosenName;
            return null;
        }
        else if (newName.trim().equals(chosenName.trim())) {
            return null;
        }
        else if (repository.containsName(newName)) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Other samples have the same name."));
            newName = chosenName;
            return null;
        }
        repository.rename(chosenName, newName.trim());
        chosenName = newName.trim();
        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + 
                "?faces-redirect=true&includeViewParams=true";
    }
    
    /**
     * Validate if samples with name {@code value} exist in the repository.
     * @param context JSF context.
     * @param component JSF component.
     * @param value {@code String} name of the samples.
     * @throws ValidatorException If no samples with provided name exist.
     */
    public void validateName(FacesContext context, UIComponent component, Object value) 
                throws ValidatorException {
        String name = (String)value;
        if (!repository.containsName(name)) {
            throw new ValidatorException(new FacesMessage("Samples with name \"" + name + 
                            "\" don't exist"));
        }
    }
    
    /**
     * Save user-entered values of samples into the loaded samples.
     */
    public void save() {
        repository.add(chosenName, samplesRepo);
        setSaved(true);
    }
    
    /**
     * Set new value to the 'saved' property.
     * @param newValue {@code true} if samples have been saved successfully,
     * {@code false} otherwise.
     */
    public void setSaved(boolean newValue) {
        saved = newValue;
    }
    
    /**
     * Whether the currently loaded samples have been saved successfully.
     * @return {@code true} if samples have been saved successfully,
     * {@code false} otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
    
    /**
     * Name of the loaded samples.
     * @return Name of the loaded samples.
     */
    public String getChosenName() {
        return chosenName;
    }

    /**
     * Change the name of the samples to be loaded.
     * @param chosenName Name of the samples to load.
     */
    public void setChosenName(String chosenName) {
        this.chosenName = chosenName;
        initWithName(chosenName);
    }
    
    /**
     * Get the new name to be set to the chosen samples.
     * @return {@link String} value to be used as the name of the
     * chosen samples after rename operation is performed.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Set the value to be used as the name of the
     * chosen samples after rename operation is performed. 
     * @param newName {@link String} value of the new name.
     */
    public void setNewName(String newName) {
        this.newName = newName;
    }
    
    /**
     * List of all available samples names.
     * @return {@code List<String>} of all samples names.
     */
    public List<String> getAllNames() {
        return repository.getNamesList();
    }
    
    /**
     * Check if samples have been loaded successfully.
     * @return {@code true} if samples have been loaded, {@code false} otherwise.
     */
    public boolean isExist() {
        return samplesRepo != null;
    }
    
    /**
     * Number of the variables in the loaded samples.
     * @return The number of the variables in the loaded samples or {@code -1}
     * if no samples have been loaded.
     */
    public int getNumberVariables() {
        if (samplesRepo == null) {
            return -1;
        }
        return samplesRepo.sampleSize();
    }
    
    /**
     * Get the number of the loaded samples' rows.
     * @return {@code int} value of the number of the samples in the loaded
     * samples repository, or {@code 0} if no samples have been loaded.
     */
    public int getNumberSamples() {
        if (samplesRepo == null) {
            return 0;
        }
        return samplesRepo.size();
    }
    
    /**
     * Get the list of the loaded samples' variables' names.
     * @return {@link List} containing {@link String} names of the variables
     * or an empty {@link List} if no samples have been loaded.
     */
    public List<String> getHeader() {
        if (samplesRepo == null) {
            return Collections.EMPTY_LIST;
        }
        return samplesRepo.getHeader();
    }
    
    /**
     * Get the values of each row of the loaded samples.
     * @return {@link List} of {@link List} of values for each row of the samples, or
     * an empty {@link List} if no samples have been loaded.
     */
    public List<List<Double>> getSamplesValues() {
        if (samplesRepo == null) {
            return Collections.EMPTY_LIST;
        }
        return samplesRepo.getAll();
    }
    
    /**
     * Add a zero-valued sample as the last sample.
     */
    public void addSample() {
        if (samplesRepo == null) {
            return;
        }
        if (samplesRepo.sampleSize() == 0) {
            return;
        }
        List<Double> newSample = new ArrayList<>(samplesRepo.sampleSize());
        for (int i = 0; i < samplesRepo.sampleSize(); i++) {
            newSample.add(0.0);
        }
        samplesRepo.add(newSample);
    }
    
    /**
     * Remove the last sample. If there's only one sample left or no samples
     * have been loaded in the repository, this method
     * does nothing.
     */
    public void removeSample() {
        if (samplesRepo == null) {
            return;
        }
        if (samplesRepo.size() == 1 || samplesRepo.sampleSize() == 0) {
            return;
        }
        samplesRepo.remove(samplesRepo.size() - 1);
    }
}
