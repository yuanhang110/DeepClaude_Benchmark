import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class CustomSet<T> {
    private final Set<T> set;

    CustomSet() {
        this.set = new HashSet<>();
    }

    CustomSet(Collection<T> data) {
        Objects.requireNonNull(data, "Collection cannot be null");
        this.set = new HashSet<>(data);
    }

    boolean isEmpty() {
        return set.isEmpty();
    }

    boolean contains(T element) {
        return set.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        Objects.requireNonNull(other, "Other set cannot be null");
        for (T element : set) {
            if (other.contains(element)) {
                return false;
            }
        }
        return true;
    }

    boolean add(T element) {
        Objects.requireNonNull(element, "Element cannot be null");
        return set.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomSet<?> other = (CustomSet<?>) obj;
        return set.equals(other.set);
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        CustomSet<T> intersection = new CustomSet<>();
        for (T element : set) {
            if (other.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        CustomSet<T> union = new CustomSet<>(this.set);
        for (T element : other.set) {
            union.add(element);
        }
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        CustomSet<T> difference = new CustomSet<>();
        for (T element : set) {
            if (!other.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }

    boolean isSubset(CustomSet<T> other) {
        for (T element : other.set) {
            if (!this.contains(element)) {
                return false;
            }
        }
        return true;
    }
}
