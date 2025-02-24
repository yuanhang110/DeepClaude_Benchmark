import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class CustomSet<T> {
    private final List<T> elements;

    CustomSet() {
        elements = new ArrayList<>();
    }

    CustomSet(Collection<T> data) {
        this();
        for (T element : data) {
            add(element);
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
        if (contains(element)) {
            return false;
        }
        return elements.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomSet<?> other = (CustomSet<?>) obj;
        return elements.size() == other.elements.size() && elements.containsAll(other.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
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
        CustomSet<T> union = new CustomSet<>();
        for (T element : elements) {
            union.add(element);
        }
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
        for (T element : elements) {
            if (!other.contains(element)) {
                return false;
            }
        }
        return true;
    }
}
