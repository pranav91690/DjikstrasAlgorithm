import java.io.File;
import java.util.*;

/**
 * Created by pranav on 6/29/15.
 */
public class Graph {
    // Data
    private int numOfNodes;
    private int numOfEdges;
    private Map<Integer, NodeG> nodes;

    // Constructor
    public Graph(int numOfNodes, int numOfEdges) {
        this.numOfNodes = numOfNodes;
        this.numOfEdges = numOfEdges;
        this.nodes = new HashMap<Integer, NodeG>();
    }

    // Methods
    // Returns the Nodes in the Graph
    public Map<Integer, NodeG> getNodes() {
        return nodes;
    }

    // To Add a Node to the Graph
    public void addNode(int index, NodeG node){
        //implement this
        nodes.put(index, node);
    }

    // Returns if the graph contains the node
    public boolean containsNode( int nodeIndex){
        return (nodes.containsKey(nodeIndex));
    }

    // Returns the Node given the Index
    public NodeG getNode( int nodeIndex){
        return (nodes.get(nodeIndex));
    }

    // Factory method to create graph from file
    public static Pair<Graph, Integer> createGraph(String inputFilePath) throws Exception{
        // Scanner Object
        Scanner fileScanner = new Scanner(new File(inputFilePath));

        // Source Node Number
        int sourceNodeIndex = new Scanner(fileScanner.nextLine()).nextInt();

        Scanner secondLineScanner = new Scanner(fileScanner.nextLine());
        // Total Nodes and Edges
        int numNodes = secondLineScanner.nextInt();
        int numEdges = secondLineScanner.nextInt();

        // Instantiate the Graph
        Graph graph = new Graph(numNodes, numEdges);

        // Add the Source Node
        NodeG sourceNode = new NodeG(sourceNodeIndex);
        graph.addNode(sourceNodeIndex, sourceNode);

        while (fileScanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(fileScanner.nextLine());
            int fromNodeIndex = lineScanner.nextInt();
            int toNodeIndex = lineScanner.nextInt();
            int weight = lineScanner.nextInt();

            // Check if from Node Exists
            NodeG fromNode = null;
            if (graph.containsNode(fromNodeIndex)) {
                fromNode = graph.getNode(fromNodeIndex);
            } else {
                fromNode = new NodeG(fromNodeIndex);
                graph.addNode(fromNodeIndex, fromNode);
            }

            // Check if to Node Exists
            NodeG toNode = null;
            if (graph.containsNode(toNodeIndex)) {
                toNode = graph.getNode(toNodeIndex);
            } else {
                toNode = new NodeG(toNodeIndex);
                graph.addNode(toNodeIndex, toNode);
            }

            // Add the Edge to the Respective Nodes
            fromNode.addEdge(toNode, weight);
            toNode.addEdge(fromNode, weight);
        }
        return (new Pair<Graph, Integer>(graph, sourceNodeIndex));
    }

    // Create a Random Graph
    public static Graph createRandomGraph(int numNodes, float density, int sourceNodeIndex){

        // Max Possible Edges for the Given Density
        int numEdges = (int)((density/100)*(numNodes)*(numNodes-1)/2);
        // Create the Graph
        Graph graph = new Graph(numNodes, numEdges);
        // Add the Source Node
        NodeG sourceNode = new NodeG(sourceNodeIndex);
        graph.addNode(sourceNodeIndex, sourceNode);

        // Till we find a Connected Graph
        do {
            // Counter for Edge Count
            int edgeCounter = 0;
            while (edgeCounter <= numEdges) {
                // Randomly Generate the two and from node indexes
                int fromNodeIndex, toNodeIndex;
                fromNodeIndex = Main.random(numNodes);
                boolean differentNodes = false;
                do {
                    toNodeIndex = Main.random(numNodes);
                    if(toNodeIndex != fromNodeIndex){
                        differentNodes = true;
                    }
                } while (!differentNodes);
                // Randomly Generate Weight for the edge
                int weight = Main.random(1000);

                // Check if from Node Exists
                NodeG fromNode = null;
                if (graph.containsNode(fromNodeIndex)) {
                    fromNode = graph.getNode(fromNodeIndex);
                } else {
                    fromNode = new NodeG(fromNodeIndex);
                    graph.addNode(fromNodeIndex, fromNode);
                }

                // Check if to Node Exists
                NodeG toNode = null;
                if (graph.containsNode(toNodeIndex)) {
                    toNode = graph.getNode(toNodeIndex);
                } else {
                    toNode = new NodeG(toNodeIndex);
                    graph.addNode(toNodeIndex, toNode);
                }

                // Add the Edge to the Respective Nodes if a Edge does not already exist
                if (!graph.edgeExists(toNode, fromNode)) {
                    fromNode.addEdge(toNode, weight);
                    toNode.addEdge(fromNode, weight);
                    // Increase the num of edges by 2
                    edgeCounter = edgeCounter + 2;
                }
            }

            // Check if the Graph is Connected else generate graph again
        }while (!graph.isConnected(sourceNodeIndex));
        // Return the Graph
        return graph;
    }


//    // to check the if a path exists between two Nodes
    public boolean edgeExists(NodeG toNode, NodeG fromNode){
        for(Edge e : toNode.getEdges()){
            if (e.getTarget() == fromNode){
                return true;
            }
        }
        return false;
    }
//
//    // to check if the graph is connected or not?
    public boolean isConnected(int rootIndex){
        // Create an Array to store the Visited or Not State of a node
        boolean[] visited = new boolean[numOfNodes];
        // Create a Stack
        Stack<NodeG> stack = new Stack<NodeG>();
        stack.push(nodes.get(rootIndex));
        while(!stack.isEmpty()){
            // While the Stack is empty
            NodeG node = stack.pop();

            // Only continue if the node is not visited
            if (!visited[node.getValue()]){
                // Mark as Visited
                visited[node.getValue()] = true;
                for(Edge e : node.getEdges()){
                    NodeG target = e.getTarget();
                    if(!visited[target.getValue()]){
                        stack.push(target);
                    }
                }
            }
        }

        // Make sure all the Nodes are Reachable from the Root
        for(boolean status : visited){
            if(!status){
                return false;
            }
        }
        return true;
    }
//

    public void computeShortestPathTree(int sourceNodeIndex, char dataStructureType){
        // Performance Measurement
        long start,stop;
        start = System.currentTimeMillis();
        
        // Make minDistance for the From Node Zero
        NodeG fromNode = nodes.get(sourceNodeIndex);
        fromNode.setDistanceFromOrigin(0);

        switch (dataStructureType){
            case 'P' : {
                //------------------------- Priority Queue ------------------------------------------------------------------------------------------
                // Build the Default Priority  Queue
                PriorityQueue<NodeG> priorityQueue = new PriorityQueue<NodeG>(NodeG.DIST_COMPARATOR);

                // Add the From node to the Priority Queue
                priorityQueue.add(fromNode);

                while(!priorityQueue.isEmpty()){
                    NodeG min = priorityQueue.poll();

                    for (Edge edge : min.getEdges()) {
                        NodeG targetNode = edge.getTarget();

                        // Update the minimum distance for the Node if new Distance is less than the previous distance
                        if( min.getDistanceFromOrigin() + edge.getWeight() < targetNode.getDistanceFromOrigin()){
                            priorityQueue.remove(targetNode);
                            targetNode.setDistanceFromOrigin(min.getDistanceFromOrigin() + edge.getWeight());
                            priorityQueue.add(targetNode);
                        }
                    }
                }
                //-----------------------------------------------------------------------------------------------------------------------------------
            }

            case 'F' : {
                //------------------------- Fibonacci Heap ------------------------------------------------------------------------------------------
                // Build the Fibonacci Heap
                FibonacciHeap fHeap = new FibonacciHeap(numOfNodes);

                // Add the Node to F Heap
                fHeap.insert(fromNode);

                while (!fHeap.isEmpty()){
                    NodeG min = fHeap.removeMin();

                    for (Edge edge : min.getEdges()) {
                        NodeG targetNode = edge.getTarget();

                        // Update the minimum distance for the Node if new Distance is less than the previous distance
                        if( min.getDistanceFromOrigin() + edge.getWeight() < targetNode.getDistanceFromOrigin()){
                            fHeap.decreaseKey(targetNode, min.getDistanceFromOrigin() + edge.getWeight());
                        }
                    }
                }
                //-----------------------------------------------------------------------------------------------------------------------------------
            }

            case 'L' : {
                //------------------------- Leftist tree ------------------------------------------------------------------------------------------
                // Build the Leftist Tree
                LeftistTree leftistTree = new LeftistTree(numOfNodes);

                // Add the From Node to the Leftist Tree DataStructure
                leftistTree.put(fromNode);

                while (!leftistTree.isEmpty()){
                    NodeG min = leftistTree.removeMin();

                    for (Edge edge : min.getEdges()) {
                        NodeG targetNode = edge.getTarget();

                        // Update the minimum distance for the Node if new Distance is less than the previous distance
                        if( min.getDistanceFromOrigin() + edge.getWeight() < targetNode.getDistanceFromOrigin()){
                            leftistTree.remove(targetNode);
                            targetNode.setDistanceFromOrigin(min.getDistanceFromOrigin() + edge.getWeight());
                            leftistTree.put(targetNode);
                        }
                    }
                }
                //-----------------------------------------------------------------------------------------------------------------------------------
            }
        }


        // Print the Running Time
        stop = System.currentTimeMillis();
        System.out.println("Algorithm Running Time " + (stop-start) +  " ms");
    }
}
