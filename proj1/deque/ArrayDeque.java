package deque;

public class ArrayDeque {
    private int[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque() {
        items = new int[8];
        size = 0;
        nextfirst = 3;
        nextlast = 6;
    }

    public void addFirst(int item) {
        if (size == items.length) {
            resize(4*items.length);
        }
        size++;
        items[nextfirst] = item;
        nextfirst = getFront(nextfirst);
    }

    public void addLast(int item) {
        if (size == items.length) {
            resize(4*items.length);
        }
        size++;
        items[nextlast] = item;
        nextlast = getBack(nextlast);
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        int index =getBack(nextfirst);
       for (int i = 0; i < size; i++) {
           System.out.println(items[index] + " ");
           index = getBack(index);
       }
       System.out.println();
    }

    public int removeFirst() {
        if (size == 0) {return -1;}
        boolean condition = (items.length >= 16) && ((size-1)/items.length < 0.25);
        if(condition){
            resize(items.length/4);
        }
        size--;
        nextfirst = getFront(nextfirst);
        int value = items[nextfirst];
        items[nextfirst] = 0;
        return value;
    }

    public int removeLast() {
        if (size == 0) {return -1;}
        boolean condition = (items.length >= 16) && ((size-1)/items.length < 0.25);
        if(condition){
            resize(items.length/4);
        }
        size--;
        nextlast = getFront(nextlast);
        int value = items[nextlast];
        items[nextlast] = 0;
        return value;
    }

    public int get(int index){
        if(index<0 || index>=size){return -1;}
        int position = index+getFront(nextfirst);
        if(position>=size){
            position = position-items.length-1;
        }
        return items[position];
    }

    //resize
    private void resize(int capacity) {
        int[] temp = new int[capacity];
        int index = getBack(nextfirst);

        for(int i = 0;i<size;i++) {
            temp[i] = items[index];
            index = getBack(index);
        }
        this.items = temp;
        }

    private int getFront(int index){
        if(index == 0){
            return items.length-1;
        }else  {
            return index-1;
        }
    }

    private int getBack(int index){
        if(index == items.length-1){
            return 0;
        }else  {
            return index+1;
        }
    }

}
