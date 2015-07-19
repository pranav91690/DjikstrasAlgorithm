import java.util.HashMap;

/**
 * Created by pranav on 7/6/15.
 */
public class FibonacciHeap {
    // DATA
    NodeF minTree;
    // Node Addresses
    NodeF[] nodeAddresses;

    // Methods
    // Constructor
    public FibonacciHeap(int numOfNodes) {
        this.nodeAddresses = new NodeF[numOfNodes];
    }

    // Is empty??
    public boolean isEmpty(){
        return (minTree == null);
    }

    // INSERT
    public void insert(NodeG data){
        // Create a Fibonacci Node
        NodeF node = new NodeF(data);
        // Create an Entry in the Node Addresses
        nodeAddresses[data.getValue()] = node;

        // Add the Node to the Top Level List
        if (minTree == null) {
            // Initialize the Min Node
            minTree = node;
        }else {
            // Add the New Node to the Sibling list
            NodeF temp = minTree.rightSibling;
            minTree.rightSibling = node;
            node.leftSibling = minTree;

            node.rightSibling = temp;
            temp.leftSibling = node;

            // Update the Min Tree
            minTree = ( minimumOf(minTree, node) ) ? minTree : node;
        }
    }

    // MELD - Combine 2 top level circular linked lists into one circular list
    public void meld(NodeF subTreeRoot1, NodeF subTreeRoot2){
        // Set 1
        NodeF temp = subTreeRoot1.rightSibling;
        subTreeRoot1.rightSibling = subTreeRoot2;

        // Set2
        NodeF temp2 = subTreeRoot2.leftSibling;
        subTreeRoot2.leftSibling = subTreeRoot1;

        temp2.rightSibling = temp;
        temp.leftSibling = temp2;
    }

    // REMOVEMIN - Remove the root of the Min Tree
    public NodeG removeMin(){
        NodeF min = minTree;

        // Disconnect the Min tree and it's children from the Top Level List
        disconnectTree(min);

        // Remove the Node from the Node Addresses Table
        nodeAddresses[min.data.getValue()] = null;

        // Check if the Min Tree has any Children
        if (min.child != null) {
            if (min.rightSibling != min) {
                // Case for If the Min Tree is not only node in the Top level List
                makeChildrenTopLevelNodes(min.child);
                // Meld the Removed Nodes Children with the remaining
                meld(min.rightSibling, min.child);
                // Update Min tree
                updateMinTree(min.rightSibling);
            } else {
                // Case for when Min tree is the only member in the Top Level list
                // add the children to the Top Level List
                makeChildrenTopLevelNodes(min.child);
                // Find the MinTree among the Child Nodes
                updateMinTree(min.child);
                }
        }else{
            if (min.rightSibling != min) {
                // Update Min tree
                updateMinTree(min.rightSibling);
            }else {
                minTree = null;
                return min.data;
            }
        }

        // Do PairWise Combine of all the Remaining Top Level Trees
        if(minTree.rightSibling != minTree) {
            pairWiseCombine();
        }

        // Return the MinTree Data
        return min.data;
    }

    // To Remove a Tree from a List
    public void disconnectTree(NodeF node){

        // if the Node has a parent
        if(node.parent != null){
            // Decrease the Degree of the Parent
            node.parent.degree--;
            // Make the Child Cut Field True
            node.parent.childCut = true;

            // If the Node to be removed is parent's node child node, change that to any other child node
            if (node.parent.child == node) {
                if(node == node.rightSibling) {
                    // Case of only one child
                    node.parent.child = null;
                }else{
                    // Case of more than one child
                    node.parent.child = node.rightSibling;
                }
            }
        }

        // Disconnect the Tree from it's Sibling List
        node.leftSibling.rightSibling = node.rightSibling;
        node.rightSibling.leftSibling = node.leftSibling;
    }

    // Set the Min Tree
    public void updateMinTree(NodeF firstMember){
        minTree = firstMember;
        NodeF node = firstMember.rightSibling;

        // Find the minTree
        while(node != firstMember) {
            if (node.data.getDistanceFromOrigin() < minTree.data.getDistanceFromOrigin()) {
                minTree = node;
            }
            node = node.rightSibling;
        }
    }

    // Make the Parent Null for all Child Nodes While Remove Min
    public void makeChildrenTopLevelNodes(NodeF firstChild){
        NodeF node = firstChild;
        do {
            // Make the Parent and ChildCut Field Null as it's now Top Level Node
            node.parent = null;
            node.childCut = false;

            node = node.rightSibling;
        }while (node != firstChild);
    }

    // PairWise Combine of the Remaining Trees
    public void pairWiseCombine(){
        // Create an Map for storing the Degrees
        HashMap<Integer,NodeF> degrees = new HashMap<Integer, NodeF>();
        NodeF node = minTree;
        do {
            // Make the node in sequence the active node
            NodeF activeNode = node;
            // Store the degree of the node in temp variable
            int activeIndex = node.degree;
            boolean finished = false;
            if (activeIndex < 1) {
                break;
            }
                do {
                try {
                    if (activeIndex < 1) {
                        break;
                    }
                    if (degrees.containsKey(activeIndex)) {

                        // It means a value already exists
                        NodeF existingNode = degrees.get(activeIndex);

                        // Get the Parent anc Child of the Two Nodes
                        NodeF parent = (minimumOf(existingNode, activeNode)) ? existingNode : activeNode;
                        NodeF child = (minimumOf(existingNode, activeNode)) ? activeNode : existingNode;

                        // Combine these two tress
                        makeChildOf(parent, child);

                        // Remove the entry from the old index
                        degrees.remove(activeIndex);

                        // Increase the Index and Check Again
                        activeIndex++;

                        // Make the Parent Node the Active Node
                        activeNode = parent;
                    } else {
                        // As the Current Index Has no Value, fill it with the Active Node
                        degrees.put(activeIndex, activeNode);
                        finished = true;
                    }
                } catch (Exception ex) {
                    System.out.println("Array out of bound");
                }
                } while (!finished);
//            }
            // Move Through the Original Top Level List
            node = node.rightSibling;
        } while (node != minTree);
    }

    public void makeChildOf(NodeF parent, NodeF child){

        // Fill the Missing link for the Child Node
        child.leftSibling.rightSibling = child.rightSibling;
        child.rightSibling.leftSibling = child.leftSibling;

        if(parent.child != null) {
            // Get the First Child of the Parent
            NodeF firstChild = parent.child;

            // Add the child node to this sibling circular linked list
            NodeF temp = firstChild.rightSibling;

            firstChild.rightSibling = child;
            child.leftSibling = firstChild;

            child.rightSibling = temp;
            temp.leftSibling = child;
        }else{
            parent.child = child;
            child.rightSibling = child;
            child.leftSibling = child;
        }

        // Update the Parent of the Child to the current Parent
        child.parent = parent;

        // Increase the degree of the Parent
        parent.degree++;

        // Make the Child Cut Field False as a Child is added
        parent.childCut = false;
    }


    // Returns the Min Tree Given Two Trees in index 0 and other tree in index 1 in an Array
    public boolean minimumOf(NodeF node1, NodeF node2){
        return (node1.data.getDistanceFromOrigin() <= node2.data.getDistanceFromOrigin());
    }

    // Remove Arbitrary Remove
    public void remove(NodeG data){
        NodeF node = nodeAddresses[data.getValue()];

        if ( node != minTree ){
            // Remove the Node and it's children from the List
            disconnectTree(node);

            // Add the Children, if there to the Top Level List
            if(node.child != null) {
                makeChildrenTopLevelNodes(node.child);
                meld(minTree, node.child);
            }

            // Cascade Cut
            cascadeCut(node);

            //
            nodeAddresses[node.data.getValue()] = null;
        }else {
            removeMin();
        }
    }

    public void cascadeCut(NodeF node) {
        while (node.parent.childCut) {
            // Disconnect the Parent from the Tree
            disconnectTree(node.parent);

            // Insert minTree the Top level Tree
            insertBack(node.parent);

            // if root is reached, break out of the loop
            if(node.parent.parent == null){
                break;
            }

            // Move on to the Parent Node's Parent
            node = node.parent.parent;
        }
    }

    public void decreaseKey(NodeG data, int amount){
        NodeF node = nodeAddresses[data.getValue()];

        if(node != null) {
            if (node.parent != null) {
                if (node.parent.data.getDistanceFromOrigin() > amount) {
                    // Remove the Node and it's children from the List
                    disconnectTree(node);

                    // Cascade Cut
                    cascadeCut(node);

                    // Change it's key
                    node.data.setDistanceFromOrigin(amount);

                    // Insert back the Node
                    insertBack(node);

                    // Update the Min Tree
                    if(minTree.data.getDistanceFromOrigin() > amount){
                        minTree = node;
                    }
                } else {
                    // No need to remove, just update the New amount
                    node.data.setDistanceFromOrigin(amount);
                }

            } else {
                // Update the Amount
                node.data.setDistanceFromOrigin(amount);

                // Update the Min Tree
                if(minTree.data.getDistanceFromOrigin() > node.data.getDistanceFromOrigin()){
                    minTree = node;
                }
            }
        }else{
            data.setDistanceFromOrigin(amount);
            insert(data);
        }
    }

    public void insertBack(NodeF node){
        // Set Node Properties to Top Level
        node.parent = null;
        node.childCut = false;

        // Add the New Node to the Sibling list
        if(minTree!=null) {
            NodeF temp = minTree.rightSibling;
            minTree.rightSibling = node;
            node.leftSibling = minTree;

            node.rightSibling = temp;
            temp.leftSibling = node;
        }else {
            minTree = node;
        }
    }
}
