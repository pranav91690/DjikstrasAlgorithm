/**
 * Created by pranav on 7/15/15.
 */
public class LeftistTree {
    NodeL root;
    int size;
    NodeL[] nodeAddresses;

    // Constructor
    public LeftistTree(int numOfNodes) {
        this.nodeAddresses = new NodeL[numOfNodes];
    }

    //Put
    public void put(NodeG data){
        // Create a New Leftist tree Node
        NodeL node = new NodeL(data);
        // Store the Addresses
        nodeAddresses[data.getValue()] = node;
        if(root != null) {
            root = meld(root, node);
        }else {
            root = node;
        }
        // Increase the Size
        ++size;
    }

    //removeMin
    public NodeG removeMin(){
        NodeL min = root;
        --size;
        if (size != 0) {
            root = meld(root.leftChild, root.rightChild);
            root.parent = null;
        }else {
            root = null;
        }
        // Update the node Address Table
        nodeAddresses[min.data.getValue()] = null;
        return min.data;
    }

    //Meld
    public NodeL meld(NodeL root1, NodeL  root2){
        NodeL root, child;
        if(root1.s == 0){
            root = root2;
            return root;
        }

        if(root2.s == 0){
            root = root1;
            return root;
        }

        root  = (minRoot(root1, root2)) ? root1 : root2;
        child = (minRoot(root1, root2)) ? root2 : root1;


        root.rightChild = meld(root.rightChild, child);

        // Swap the Right and Left and Right Children
        if (root.leftChild.s < root.rightChild.s) {
            NodeL temp = root.rightChild;
            root.rightChild = root.leftChild;
            root.leftChild = temp;
        }

        // Increase the S by 1
        root.s = root.rightChild.s + 1;

        // Assign the Parent
        root.rightChild.parent = root;
        root.leftChild.parent = root;

        return root;
    }

    //Initialize
    public void initialize(Queue<NodeL> queue){
        while (queue.size == 1){
            NodeL leftistTree1 = queue.pop();
            NodeL leftistTree2 = queue.pop();
            queue.push(meld(leftistTree1,leftistTree2));
        }

        // Push the Remaining Leftist Tree into the root
        root = queue.pop();
        size = queue.size;
    }

    // Arbitary Remove
    public void remove(NodeG data){
        NodeL node = nodeAddresses[data.getValue()];
        if (node!=null) {
            if (node.parent != null) {
                // Non Root Remove
                NodeL children;
                if (node.s > 1) {
                    children = meld(node.leftChild, node.rightChild);
                }else if (node.leftChild.s > 0) {
                        children = node.leftChild;
                }else {
                    children = null;
                }

                // Based on which child the node is, put an external node there
                if(node.parent.rightChild == node){
                    node.parent.rightChild = new NodeL();
                }else{
                    node.parent.leftChild = new NodeL();
                }

                NodeL parent = node.parent;
                int previousS;
                boolean noChangeInS = false;
                do {
                    previousS = parent.s;
                    if(parent.leftChild.s < parent.rightChild.s){
                        // Swap
                        NodeL temp = parent.rightChild;
                        parent.rightChild = parent.leftChild;
                        parent.leftChild = temp;
                    }

                    // New S value
                    parent.s = parent.rightChild.s + 1;

                    // Check if there is change in S
                    if(parent.s != previousS){
                        if (parent.parent == null){
                            // it means it's root
                            noChangeInS = true;
                        }else {
                            // Move on to it's parent
                            parent = parent.parent;
                        }
                    }else {
                        noChangeInS = true;
                    }
                } while (!noChangeInS);

                // If Children are not null, meld them with the root
                if(children != null) {
                    root = meld(root, children);
                }
                --size;
            }else {
                removeMin();
            }
            // Update the Node Addresses
            nodeAddresses[node.data.getValue()] = null;
        }
    }

    // Compare Roots
    public boolean minRoot(NodeL root1, NodeL root2){
        // Return the Smaller of the Nodes
        return (root1.data.getDistanceFromOrigin() <= root2.data.getDistanceFromOrigin());
    }

    public boolean isEmpty(){
        return (size == 0);
    }
}
