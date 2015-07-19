/**
 * Created by pranav on 7/1/15.
 */
public class Edge {
    private NodeG target;
    private int  weight;

    public Edge(NodeG target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    public NodeG getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }
}
