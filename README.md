# NeuralNetworkTrainerWeb
Web application for creating and training neural networks

The project depends on the following projects:
1. NeuralNetworkTrainer - neural network classes
2. NeuralNetworkCommons - common operations on neural networks.

The project uses WildFly 16 as application server.

The project consists of three modules:
1. JPA module -- 
contains two persistence units with their own JPA entities: one for storing neural networks and the other for storing training samples in separate databases. The persistence units use pre-configured MySQL datasources.
2. EJB module -- contains stateless facades for performing CRUD operations on the JPA entities.
3. WEB module -- contains JSF pages for creating, viewing/changing networks, viewing/changing training samples and the index page showing all available networks and training samples. The module contains CDI beans for performing logic and uses EJB beans for interacting with the databases.

Currently, the following features are implemented:
1. Creating a neural network of a specific structure and initializing it
2. Viewing and changing neural networks' weights and biases
3. Deleting neural networks
4. Uploading neural networks as binary or text files
5. Downloading neural networks as binary or text files
6. Uploading training samples from CSV files
7. Viewing and changing uploaded training samples
