/**
 * Created by pranav on 7/15/15.
 */
public class Stack<T> {
    Node<T> top;
    int size;

    public void push(T data){
        Node<T> node = new Node<T>();
        node.data = data;
        if(!isEmpty()){
            node.next = top;
            top = node;
        }else{
            node.next = null;
            top = node;
        }
        size++;
    }

    public T pop(){
        if(!isEmpty()){
            T value = top.data;
            if(size == 1) {
                size--;
                top = null;
                return value;
            }else{
                size--;
                top = top.next;
                return value;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return (size == 0);
    }
}
