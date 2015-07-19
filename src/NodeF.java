/**
 * Created by pranav on 7/6/15.
 */
public class NodeF {
    // No of Children
    int degree;
    // Can point to any child
    NodeF child;
    // Data Value
    NodeG data;
    // Pointer to Left Sibling
    NodeF leftSibling;
    // Pointer to Right Sibling
    NodeF rightSibling;
    // Pointer to Parent
    NodeF parent;
    // To implement Cascading Cut Property
    boolean childCut;

    // Constructor

    public NodeF( NodeG data ) {
        this.degree = 0;
        this.childCut = false;
        this.parent = null;
        this.data = data;
        this.leftSibling = this;
        this.rightSibling = this;
        this.child = null;
    }
}
