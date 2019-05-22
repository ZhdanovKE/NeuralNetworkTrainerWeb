package neuralnetwork.trainerweb.bean.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.commons.util.NeuralNetworkFileUtils;
import neuralnetwork.trainerweb.bean.repository.NeuralNetworkRepositoryBean;

/**
 * CDI bean for downloading the chosen neural network as a binary or text file.
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class FileDownloadBean {
    
    @Inject
    private NeuralNetworkRepositoryBean repository;
    
    private String nameToDownload;
    
    private boolean saveAsText;

    /**
     * Get the name of the network from the repository to be downloaded 
     * as a file.
     * @return {@link String} name of the network.
     */
    public String getNameToDownload() {
        return nameToDownload;
    }

    /**
     * Set the name of the network from the repository to be downloaded 
     * as a file.
     * @param nameToDownload {@link String} name of the network to be downloaded.
     */
    public void setNameToDownload(String nameToDownload) {
        this.nameToDownload = nameToDownload;
    }

    /**
     * Whether download the chosen network as a text file or not.
     * @return {@code true} - the network will be downloaded as a text file,
     * {@code false} - the network will be downloaded as a binary file.
     */
    public boolean isSaveAsText() {
        return saveAsText;
    }

    /**
     * Set whether the chosen network will be downloaded as a text file or not.
     * @param saveAsText {@code true} - the network will be downloaded as a text file,
     * {@code false} - the network will be downloaded as a binary file.
     */
    public void setSaveAsText(boolean saveAsText) {
        this.saveAsText = saveAsText;
    }
    
    /**
     * Perform downloading of the chosen network in the chosen format (text / binary).
     */
    public void download() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
  
        NamedNeuralNetwork namedNN = repository.get(nameToDownload);
        if (namedNN == null) {
            fc.addMessage(null, new FacesMessage("Error while downloading neural network."));
            return;
        }

        String fileName = nameToDownload.trim().replaceAll("\\s+", "_");
        fileName = saveAsText ? (fileName + ".txt") : (fileName + ".dat");
        String contentType = saveAsText ? "text/plain" : "application/octet-stream";
        
        ec.responseReset();
        ec.setResponseContentType(contentType);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        int contentLength;
        File tempFile = null;
        try {
            tempFile = File.createTempFile("download", ".tmp");
            if (saveAsText) {
                NeuralNetworkFileUtils.saveWithNameAsText(namedNN, 
                        namedNN.getName(), tempFile.getAbsolutePath());
            }
            else {
                NeuralNetworkFileUtils.saveWithName(namedNN, 
                        namedNN.getName(), tempFile.getAbsolutePath());
            }

            contentLength = (int)tempFile.length();
            ec.setResponseContentLength(contentLength);

            byte[] buffer = new byte[1024];
            int len;

            try (OutputStream output = ec.getResponseOutputStream()) {
                try (FileInputStream fis = new FileInputStream(tempFile)) {
                    while ((len = fis.read(buffer)) > 0) {
                        output.write(buffer, 0, len);
                    }
                }
            }
        }
        catch(IOException e) {
            fc.addMessage(null, new FacesMessage("Error while downloading neural network."));
        }
        finally {
            fc.responseComplete();
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }
}
