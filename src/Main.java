import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        String switchArg = args[0];
        if (!switchArg.startsWith("-")) {
            System.out.println("Invalid args: " + switchArg);
            System.exit(-1);
        }

        Character runMode = switchArg.charAt(1);

        switch (runMode) {
            case 'r': {
                int n = Integer.parseInt(args[1]);
                float d = Float.parseFloat(args[2]);
                int x = Integer.parseInt(args[3]);

                // call for business logic
                runInRandomMode(n, d, x);
                break;
            }
            case 'l': {
                String fileName = args[1];

                //call for business logic
                runInUserMode('L', fileName);
                break;
            }
            case 'f': {
                String fileName = args[1];

                // business logic
                runInUserMode('F', fileName);
                break;
            }
            default:
                System.out.println("Bad switch case : -" + runMode);
        }
    }

    public static void runInUserMode(char dataStructureType, String filename) throws Exception{
        // User Input Mode
        Pair<Graph, Integer> graphWithSourceSet = Graph.createGraph(filename);
        Graph graph = graphWithSourceSet.fst;
        int sourceNodeNum = graphWithSourceSet.snd;
        // Run the Algorithm
        graph.computeShortestPathTree(sourceNodeNum, dataStructureType);

        // Print the Minimum Distances
        System.out.println("User Mode");
        for(Map.Entry<Integer,NodeG> entry : graph.getNodes().entrySet()){
            System.out.println(entry.getValue().getDistanceFromOrigin() + "//cost from node " + sourceNodeNum + " to " + entry.getKey());
        }
    }

    public static void runInRandomMode(int numOfNodes,float density, int startNode){
        // Random Mode
        Graph randomGraph = Graph.createRandomGraph(numOfNodes, density, startNode);
        // Run the Random Algorithm for Leftist Tree
        randomGraph.computeShortestPathTree(startNode, 'L');
        // Print the Minimum Distances
        System.out.println("Random Mode - Leftist Tree");
        for(Map.Entry<Integer,NodeG> entry : randomGraph.getNodes().entrySet()){
            // Print out the Distance
            System.out.println(entry.getValue().getDistanceFromOrigin() + "//cost from node " + startNode + " to " + entry.getKey());

            // Clear the Node Values for NExt DataStructure
            entry.getValue().setDistanceFromOrigin(Integer.MAX_VALUE);
        }

        // Run the Random Algorithm for Fibonacci Heap
        randomGraph.computeShortestPathTree(startNode, 'F');
        // Print the Minimum Distances
        System.out.println("Random Mode - Fibonacci Heap");
        for(Map.Entry<Integer,NodeG> entry : randomGraph.getNodes().entrySet()){
            // Print out the Distance
            System.out.println(entry.getValue().getDistanceFromOrigin() + "//cost from node " + startNode + " to " + entry.getKey());
        }
    }
    public static int random(int n){
        return (int)(Math.random()*n);
    }
}
