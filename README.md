# NeuralNetworkTrainerWeb
Web application for creating and training neural networks

The project depends on the following projects:
1. NeuralNetworkTrainer - neural network classes
2. NeuralNetworkCommons - common operations on neural networks.

The project uses WildFly 16 as application server.

The project consists of three modules:
1. JPA module -- 
contains the persistence unit and JPA entities. The persistence unit uses pre-configured MySQL datasource.
2. EJB module -- contains a facade for performing CRUD operations on the JPA entities.
3. WEB module -- contains JSF pages for creating, viewing/changing networks and the index page showing all
available networks. Contains CDI beans for performing logic and uses EJB beans for interacting with the database.

Currently, the following features are implemented:
1. Creating a neural network of a specific structure and initializing it
2. Viewing and changing neural networks' weights and biases
3. Deleting neural networks
4. Uploading neural networks as binary or text files
5. Downloading neural networks as binary or text files
