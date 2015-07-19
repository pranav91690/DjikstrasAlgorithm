/**
 * Created by pranav on 7/15/15.
 */
public class NodeL {
    NodeL leftChild;
    NodeL rightChild;
    NodeL parent;
    NodeG data;
    int s;

    // Methods
    // Constructor
    public NodeL(NodeG data) {
        this.data = data;
        this.s = 1;
        this.rightChild = new NodeL();
        this.leftChild = new NodeL();
        this.parent = null;
    }

    // External Node
    public NodeL(){
        this.s = 0;
    }
}
