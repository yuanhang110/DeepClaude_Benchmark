import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

class CustomSet<T> {
    private final Set<T> elements = new HashSet<>();

    CustomSet() {
    }

    CustomSet(Collection<T> data) {
        elements.addAll(data);
    }

    boolean isEmpty() {
        return elements.isEmpty();
    }

    boolean contains(T element) {
        return elements.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        for (T element : elements) {
            if (other.elements.contains(element)) {
                return false;
            }
        }
        return true;
    }

    boolean add(T element) {
        return elements.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomSet<?> other = (CustomSet<?>) obj;
        return elements.equals(other.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        CustomSet<T> intersection = new CustomSet<>();
        for (T element : elements) {
            if (other.elements.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        CustomSet<T> union = new CustomSet<>();
        union.elements.addAll(elements);
        union.elements.addAll(other.elements);
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        CustomSet<T> difference = new CustomSet<>();
        for (T element : elements) {
            if (!other.elements.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }

    boolean isSubset(CustomSet<T> other) {
        for (T element : elements) {
            if (!other.elements.contains(element)) {
                return false;
            }
        }
        return true;
    }
}
