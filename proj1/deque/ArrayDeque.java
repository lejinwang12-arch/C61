package deque;

public class ArrayDeque<LochNess> implements Deque <LochNess> {
    private LochNess[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque() {
        items = (LochNess[]) new Object[8];
        size = 0;
        nextfirst = 3;
        nextlast = 4;
    }
    @Override
    public void addFirst(LochNess item) {
        if (size == items.length) {
            resize(4*items.length);
        }
        size++;
        items[nextfirst] = item;
        nextfirst = getFront(nextfirst);
    }
    @Override
    public void addLast(LochNess item) {
        if (size == items.length) {
            resize(4*items.length);
        }
        size++;
        items[nextlast] = item;
        nextlast = getBack(nextlast);
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int index =getBack(nextfirst);
       for (int i = 0; i < size; i++) {
           System.out.println(items[index] + " ");
           index = getBack(index);
       }
       System.out.println();
    }
    @Override
    public LochNess removeFirst() {
        if (size == 0) {return null;}
        boolean condition = (items.length >= 16) && (items.length/(size-1)>4);
        if(condition){
            resize(items.length/4);
        }
        size--;
        nextfirst = getBack(nextfirst);
        LochNess value = items[nextfirst];
        items[nextfirst] = null;
        return value;
    }
    @Override
    public LochNess removeLast() {
        if (size == 0) {return null;}
        boolean condition = (items.length >= 16) && (items.length/(size-1)>4);
        if(condition){
            resize(items.length/4);
        }
        size--;
        nextlast = getFront(nextlast);
        LochNess value = items[nextlast];
        items[nextlast] = null;
        return value;
    }
    @Override
    public LochNess get(int index){
        if(index<0 || index>=size){return null;}
        int position = index+getBack(nextfirst);
        if(position >= items.length){
            position = position-items.length;
        }
        return items[position];
    }

    //resize
    private void resize(int capacity) {
        LochNess[] temp = (LochNess[]) new Object[capacity];
        int index = getBack(nextfirst);

        for(int i = 0;i<size;i++) {
            temp[i] = items[index];
            index = getBack(index);
        }
        this.items = temp;
        nextfirst = capacity -1;
        nextlast = size;
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
