

/**
 * 循环队列
 * <p>
 * 注意：判空和判满的两种情况：
 * 情况1.另设一个标识位区别队列是空还是满
 * 情况2.少用一个元素空间，约定以"队列头指针在队尾指针的下一位位置上" 作为队列满的标志
 *
 * @param <T>
 */

public class CycQueue<T> {

    private Integer MAXSIZE = 100; //循环队列最大长度为7  0~6
    private Object[] arr;
    private Integer front;//头指针，若队列不为空，指向队头元素
    private Integer rear; //尾指针，若队列不为空，指向队列尾元素的下一个位置

    public CycQueue() {
        arr = new Object[MAXSIZE];
        front = rear = 0;
    }


    public Boolean isEmpty() {
        if (front == rear) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Integer QueueLength() {
        return (rear - front + MAXSIZE) % MAXSIZE; //求环形队列的元素个数
    }

    public Object GetHead() {
        return arr[front];
    }

    //入队前判满
    public Boolean EnQueue(Object e) {
        //队列头指针在队尾指针的下一位位置上  说明满了
        if ((rear + 1) % MAXSIZE == front) {
            return Boolean.FALSE;
        }
        arr[rear] = e;
        rear = (rear + 1) % MAXSIZE;
        return Boolean.TRUE;
    }

    //出队前判空
    public T DeQueue() {
        if (rear == front) {
            return null;
        }
        T e = (T) arr[front];
        front = (front + 1) % MAXSIZE;
        return e;
    }

}