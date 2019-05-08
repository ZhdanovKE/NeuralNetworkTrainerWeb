package neuralnetwork.trainerweb.bean;

import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * CDI-managed bean of available samples. 
 * @author Konstantin Zhdanov
 */
@Named
@RequestScoped
public class SamplesListBean {
    private final List<String> samples;
    
    public SamplesListBean() {
        samples = Arrays.asList(
                "Sample 1",
                "Sample 2",
                "Sample 3",
                "Sample 4"
        );
    }
    
    public List<String> getSamples() {
        return samples;
    }
}
