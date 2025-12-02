package deque;

public class LinkedListDeque<LochNess> implements Deque <LochNess> {

    /*creat IntNode class*/
    private class stuffNode{

        public stuffNode prev;
        public LochNess item;
        public stuffNode next;

        public stuffNode(LochNess item, stuffNode prev, stuffNode next){
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

    }

    private stuffNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new stuffNode(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    @Override
    public void addFirst(LochNess value){
        size += 1;
        sentinel.next = new stuffNode(value,sentinel,sentinel.next);
        if(size==1){sentinel.prev = sentinel.next;}
    }
    @Override
    public  void addLast(LochNess value){
        size += 1;
        sentinel.prev = new stuffNode(value,sentinel.prev,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
    }
    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        stuffNode temp = sentinel.next;
        for(int i=0;i<size;i++){
            System.out.print(temp.item+" ");
            temp=temp.next;
        }
        System.out.println();

    }
    @Override
    public LochNess removeFirst(){
        if(size==0){
            return null;
        }else{
            size -= 1;
            LochNess first = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            return first;
        }

    }
    @Override
    public LochNess removeLast(){
        if(size==0){
            return null;
        }else {
            size -= 1;
            LochNess last = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            return last;
        }
    }
    @Override
    public LochNess get(int index){
        stuffNode temp = sentinel.next;
        for(int i=0;i<index;i++){
            temp=temp.next;
        }
        return temp.item;
    }

    public LochNess getRecursive(int index){
        return getItem(sentinel.next,index);
    }

    private LochNess getItem(stuffNode p, int index){
        if(index==0){
            return p.item;
        }else{
            return getItem(p.next,index-1);
        }
    }

}
