package deque;

import java.util.Comparator;

public class MaxArrayDeque<LochNess> extends ArrayDeque<LochNess>{
    private Comparator<LochNess> comparator;

    public MaxArrayDeque(Comparator<LochNess> c) {
        super();
        this.comparator = c;
    }

    public LochNess max() {
        return max(comparator);
    }

    public LochNess max(Comparator<LochNess> c) {
        if (isEmpty()) {return null;}
        LochNess maxElement = null;
        for(LochNess element : this){
            if (maxElement == null || c.compare(element, maxElement) > 0) {
                maxElement = element;
            }
        }
        return maxElement;
    }
}
