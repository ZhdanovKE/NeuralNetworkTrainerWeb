package neuralnetwork.trainerweb.bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * CDI bean global to the application containing settings.
 * @author Konstantin Zhdanov
 */
@Named
@ApplicationScoped
public class SettingsBean {
    private final long maxFileUploadSizeInBytes = 10485760;
    
    /**
     * Maximum size (in bytes) of a file to upload on the site.
     * @return {@code long} value of the maximum size in bytes.
     */
    public long getMaxFileUploadSizeInBytes() {
        return maxFileUploadSizeInBytes;
    }
}
