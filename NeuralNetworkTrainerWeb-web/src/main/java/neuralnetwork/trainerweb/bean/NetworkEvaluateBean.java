package neuralnetwork.trainerweb.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import neuralnetwork.train.NeuralNetworkEvaluator;

/**
 * JSF-managed bean for evaluating neural network's output for input.
 * @author Konstantin Zhdanov
 */
@Named
@ViewScoped
public class NetworkEvaluateBean implements Serializable {
    private static final long serialVersionUID = -8750272327799128119L;
    
    private NetworkBean network;
    
    private int numberTestCases;
    
    private double[][] testCases;
    
    private double[][] testCasesOutputs;
    
    /**
     * Initialize this bean with the {@link NetworkBean} from the faces context.
     */
    @PostConstruct
    public void initialize() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        network = app.evaluateExpressionGet(fc, "#{networkBean}", NetworkBean.class);
        
        numberTestCases = 1;
        if (network == null || network.nn == null)  {
            System.out.println("Network is null");
            testCases = new double[numberTestCases][0];
            testCasesOutputs = new double[numberTestCases][0];
        }
        else {
            testCases = new double[1][network.nn.getNumberInputs()];
            testCasesOutputs = new double[1][network.nn.getNumberOutputs()];
        }
    }

    /**
     * Number of test cases.
     * @return {@code int} value of the number of test cases.
     */
    public int getNumberTestCases() {
        return numberTestCases;
    }

    /**
     * Set the number of test cases.
     * @param numberTestCases {@code int} value of the number of test cases.
     */
    public void setNumberTestCases(int numberTestCases) {
        this.numberTestCases = numberTestCases;
    }
    
    /**
     * The neural network's inputs for test case with index {@code idx}.
     * @param idx Index (starting with zero) of the test case.
     * @return Array {@code double[]} of neural network's inputs for the test 
     * case with index {@code idx}.
     */
    public double[] getTestCase(int idx) {
        return testCases[idx];
    }
    
    /**
     * The neural network's outputs for test case with index {@code idx}.
     * @param idx Index (starting with zero) of the test case.
     * @return Array {@code double[]} of neural network's outputs for the test 
     * case with index {@code idx}.
     */
    public double[] getTestCaseOutputs(int idx) {
        return testCasesOutputs[idx];
    }
    
    /**
     * Add another test case.
     */
    public void addTestCase() {
        if (network.nn == null) {
            return;
        }
        double[][] newTestCases = new double[numberTestCases + 1][testCases[0].length];
        System.arraycopy(testCases, 0, newTestCases, 0, numberTestCases);
        testCases = newTestCases;
        
        double[][] newTestCasesOutputs = new double[numberTestCases + 1]
                [testCasesOutputs[0].length];
        System.arraycopy(testCasesOutputs, 0, newTestCasesOutputs, 0, numberTestCases);
        testCasesOutputs = newTestCasesOutputs;
        
        numberTestCases++;
    }
    
    /**
     * Remove the last test case. If there's only one test case left, this
     * method does nothing.
     */
    public void removeTestCase() {
        if (network.nn == null) {
            return;
        }
        if (numberTestCases == 1) {
            return;
        }
        numberTestCases--;
        double[][] newTestCases = new double[numberTestCases][testCases[0].length];
        System.arraycopy(testCases, 0, newTestCases, 0, numberTestCases);
        testCases = newTestCases;
        
        double[][] newTestCasesOutputs = new double[numberTestCases]
                [testCasesOutputs[0].length];
        System.arraycopy(testCasesOutputs, 0, newTestCasesOutputs, 0, numberTestCases);
        testCasesOutputs = newTestCasesOutputs;
    }
    
    /**
     * Perform evaluation of the test cases.
     */
    public void evaluate() {
        if (network.nn == null) {
            return;
        }
        NeuralNetworkEvaluator evaluator = new NeuralNetworkEvaluator(network.nn);
        for (int i = 0; i < testCases.length; i++) { 
            testCasesOutputs[i] = evaluator.getOutput(testCases[i]);
        }
    }
}
