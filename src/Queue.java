/**
 * Created by pranav on 7/14/15.
 */
public class Queue<T> {
    Node<T> start;
    Node<T> end;
    int size;

    public void push(T data){
        Node<T> node = new Node<T>();
        node.data = data;
        node.next = null;
        if(!isEmpty()){
            end.next = node;
            end = node;
        }else{
            start = node;
            end = node;
        }
        size++;
    }

    public T pop(){
        if(size != 0){
            size--;
            if(start != end) {
                T value = start.data;
                start = start.next;
                return value;
            }else{
                T value = start.data;
                start = null;
                end = null;
                return value;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return (size == 0);
    }
}
