import java.util.ArrayList;
import java.util.Collection;

class CustomSet<T> {
    private ArrayList<T> elements;
    CustomSet() {
        elements = new ArrayList<>();
    }

    CustomSet(Collection<T> data) {
        elements = new ArrayList<>();
        for (T item : data) {
            if (!elements.contains(item)) {
                elements.add(item);
            }
        }
    }

    boolean isEmpty() {
        return elements.isEmpty();
    }

    boolean contains(T element) {
        return elements.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        for (T element : elements) {
            if (other.contains(element)) {
                return false;
            }
        }
        return true;
    }

    boolean add(T element) {
        if (elements.contains(element)) {
            return false;
        }
        elements.add(element);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CustomSet)) {
            return false;
        }
        CustomSet<?> other = (CustomSet<?>) obj;
        return this.elements.containsAll(other.elements) && other.elements.containsAll(this.elements);
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        CustomSet<T> intersection = new CustomSet<>();
        for (T element : elements) {
            if (other.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        CustomSet<T> union = new CustomSet<>(this.elements);
        for (T element : other.elements) {
            union.add(element);
        }
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        CustomSet<T> difference = new CustomSet<>();
        for (T element : elements) {
            if (!other.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }

    boolean isSubset(CustomSet<T> other) {
        return other.set.containsAll(this.set);
    }
}
