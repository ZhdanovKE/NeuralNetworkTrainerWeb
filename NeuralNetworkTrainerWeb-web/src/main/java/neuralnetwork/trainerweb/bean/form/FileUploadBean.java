package neuralnetwork.trainerweb.bean.form;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.commons.util.NeuralNetworkFileUtils;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * JSF bean for uploading neural network file and reading a network from it.
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class FileUploadBean {
    
    private static final int RAND_SUFFIX_SIZE = 5; 
    private static final int MAX_SAVE_FAIL_NUM = 100;
    
    @Inject
    private NeuralNetworkRepositoryBean repository;
    
    private Part file;

    /**
     * Get {@link Part} object containing {@code NeuralNetwork}.
     * @return {@link Part} object chosen by user.
     */
    public Part getFile() {
        return file;
    }

    /**
     * Set {@link Part} containing {@code NeuralNetwork} to be uploaded.
     * @param file {@link Part} containing {@code NeuralNetwork}.
     */
    public void setFile(Part file) {
        this.file = file;
    }
    
    /**
     * Upload {@code NeuralNetwork} from user-provided file.
     */
    public void upload() {
        if (file == null) {
            return;
        }
            
        try {
            File tempFile = File.createTempFile("uploadedNN", ".tmp");
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            NeuralNetwork nn;
            if (isTextFile(file)) {
                nn = NeuralNetworkFileUtils.loadFromTextFile(tempFile.getAbsolutePath());
            }
            else {
                nn = NeuralNetworkFileUtils.load(tempFile.getAbsolutePath());
            }
            NamedNeuralNetwork namedNN;
            if (nn instanceof NamedNeuralNetwork) {
                namedNN = (NamedNeuralNetwork)nn;
            }
            else {
                String newName = getUniqueName(extractName(
                        file.getSubmittedFileName()));
                if (newName == null) {
                    throw new IOException();
                }
                namedNN = new NamedNeuralNetwork(nn, newName);
            }
            String newName = getUniqueName(namedNN.getName());
            if (newName == null) {
                throw new IOException();
            }
            namedNN.setName(newName);
            repository.add(namedNN.getName(), namedNN);
        }
        catch (IOException | IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage("Error reading file: " + 
                            file.getSubmittedFileName())
            );
        }
    }
    
    private String getUniqueName(String firstName){
        int nNameFailed = 0;
        while (repository.containsName(firstName) && nNameFailed < MAX_SAVE_FAIL_NUM) {
            firstName = firstName + getRandomStringOfSize(RAND_SUFFIX_SIZE);
            nNameFailed++;
        }
        if (repository.containsName(firstName)) {
            return null;
        }
        return firstName;
    }
    
    private String getRandomStringOfSize(int size) {
        char[] randChars = new char[size];
        for (int i = 0; i < size; i++) {
            randChars[i] = (char)('a' + (int)(Math.random()*26));
        }
        return new String(randChars);
    }
    
    private String extractName(String submittedFileName) {
        int dotIdx = submittedFileName.lastIndexOf('.');
        return submittedFileName.substring(0, dotIdx);
    }
    
    private String extractExtension(String submittedFileName) {
        int dotIdx = submittedFileName.lastIndexOf('.');
        return submittedFileName.substring(dotIdx + 1);
    }

    private boolean isTextFile(Part file) {
        return extractExtension(file.getSubmittedFileName()).trim().
                toLowerCase().equals("txt");
    }
}
