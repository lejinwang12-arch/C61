package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comparator = c;
    }

    public T max() {
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxElement = null;
        for (T element : this) {
            if (maxElement == null || c.compare(element, maxElement) > 0) {
                maxElement = element;
            }
        }
        return maxElement;
    }
}
