package deque;
import java.util.Comparator;
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }
    public T max() {
        if (this.isEmpty()) {
            return null;
        } else {
            T max = this.get(0);
            for (T i:this) {
                int num = comparator.compare(max, i);
                if (num < 0) { //max < i
                    max = i;
                }
            }
            return max;
        }
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        } else {
            T max = (T) this.get(0);
            for (T i:this) {
                int num = c.compare(max, i);
                if (num < 0) { //max < i
                    max = i;
                }
            }
            return max;
        }
    }
}
