import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by pranav on 6/29/15.
 */
public class NodeG {
    private int value;
    private ArrayList<Edge> edges;
    private int distanceFromOrigin;

    public int getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(int distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    // Constructor
    public NodeG(int value) {
        this.value = value;
        this.edges = new ArrayList<Edge>();
        distanceFromOrigin = Integer.MAX_VALUE;
    }

    public int getValue() {
        return value;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    // Add Edge
    public void addEdge(NodeG toNode, int weight){
        this.edges.add(new Edge(toNode, weight));
    }

    public static Comparator<NodeG> DIST_COMPARATOR = new Comparator<NodeG>() {
        @Override
        public int compare(NodeG e1, NodeG e2) {
            return (int)(e1.getDistanceFromOrigin() - e2.getDistanceFromOrigin());
        }
    };
}
