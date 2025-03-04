import java.util.Collection;

class CustomSet<T> {
    private final HashSet<T> set;
    CustomSet() {
        set = new HashSet<>();
    }

    CustomSet(Collection<T> data) {
        set = new HashSet<>(data);
    }

    boolean isEmpty() {
        return set.isEmpty();
    }

    boolean contains(T element) {
        return set.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        for (T element : set) {
            if (other.contains(element)) {
                return false;
            }
        }
        return true;
    }

    boolean add(T element) {
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
        CustomSet<T> union = new CustomSet<>(set);
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
            if (!set.contains(element)) {
                return false;
            }
        }
        return true;
    }
}
