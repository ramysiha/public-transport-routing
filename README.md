** Name: ** Ramy Siha

** Email: ** ramysiha@gmail.com

** Date: ** 07/08/2021

** Revision: ** 1.0

## About The Project

This is a sample implementation for Berlin Public Transport system. It is a command line tool that allows the user to build a graph of 
different stations and then to be able to query the "route" from one station to another or to get a list of all "nearby" stations within a given time limit.

### Built With
- Java 1.8
- Gradle 
- Junit5


### Building the application

Application uses Gradle as it's build management system. 

` ./gradlew clean build `

The application jar will be generated at ./build/libs/public-transport.jar
The jar file is self contained and executable

### Running the application

Application can be ran directly through Gradle as follows:

` ./gradlew clean run `

You can also invoke the jar file directly as

` java -jar ./build/libs/public-transport.jar `

Sample Input:

```
8
A -> B: 240
A -> C: 70
A -> D: 120
C -> B: 60
D -> E: 480
C -> E: 240
B -> E: 210
E -> A: 300
route A -> B
nearby A, 130

```

Sample Output:

```
A -> C -> B: 130
C: 70, D: 120, B: 130

```

### Running JUnit tests

You may run JUnit tests from Gradle with the following command

` ./gradlew clean test `

      
### Implementation Details

The project is separated into 5 main packages

- com.here.fox: This is the main package, it contains the App class with the main function.


- com.here.fox.model: That package contains the Graph Singleton class that will hold the actual station and edge connections.
VectorNode is that class that represents the actual station and the different edges/connections it has. The Graph can only be constructed
once through the GraphBuilder and cannot be modified afterwards.


- com.here.fox.service: That package contains the RoutingService interface. Instances of the RoutingService interface are created through the RutingServiceFactory. The interface has the functions to perform the nearby & route operations.
The class contains NearbyResult & RoutingResult POJOs that will hold the routing and nearby lookup results


- com.here.fox.service.impl: Package contains implementation details for the RoutingService interface. The main implementation RoutingServiceImpl is based on the Dijkstra algorithm.


- com.here.fox.util: Package contains the CommandLineProcessor class, that class is designed to process and parse user input.


### Algorithm Details

- The currently used algorithm for finding the shortest path between two vectors is Dijkstra. 

- The current Graph implementation is based on the adjacency list instead of adjacency matrix, that choice was made since the network
graph is expected to be sparse with a low count of edges vs a high count of vectors hence the adjacency matrix was better suited.

- The current implementation uses PriorityQueue , internally in Java PriorityQueues are implemented by Priority Heap, hence the expected complexity of that algorithm should be O((v + e) log v) where e is the number of edges and v is the number of vectors. 

- Other shortest path routing algorithms such as Bellman–Ford or Floyd–Warshall were discarded, since it offers a worse run time complexity than Dijkstra with the added benefit of handling negative edges which is not a valid case for that specific project. 

### Current Limitations

- In the Dijkstra implementation, there is heavy usage of HashMaps instead of Arrays. there are different pros and cons in that case
    - HashMaps are actually ConcurrentHashMaps, hence they are expected to be thread safe contrary to the arrays
    - Although HashMaps should have an O(1) access, yet the arrays are expected to perform faster than it.
    - The original requirements was to be able to construct the through the command line, hence using Maps would be more convenient 
    - The solution can be enhanced from the Graph side to return an ordinal for each newly created Vector, that ordinal then
    can be used as an array index.
    
    
- 

## References

- The Algorithm Design Manual - Steven Skiena
- Effective Java (3rd Edition)


