import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {

    public static class Cell<T> {
        private T value;
        private final List<ComputeCell<?>> observers = new ArrayList<>();

        public T getValue() {
            return value;
        }

        protected void setValue(T value) {
            this.value = value;
            notifyObservers();
        }

        protected void addObserver(ComputeCell<?> observer) {
            if (observer != null) {
                observers.add(observer);
            }
        }

        private void notifyObservers() {
            for (ComputeCell<?> observer : new ArrayList<>(observers)) {
                if (observer != null) {
                    observer.recompute();
                }
            }
        }
    }

    public static class InputCell<T> extends Cell<T> {
        public InputCell(T initialValue) {
            setValue(initialValue);
        }

        public void setValue(T newValue) {
            super.setValue(newValue);
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> function;
        private final List<Cell<T>> dependencies;
        private final List<Consumer<T>> callbacks = new ArrayList<>();
        private T lastCallbackValue;

        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> dependencies) {
            if (function == null || dependencies == null) {
                throw new IllegalArgumentException("Function and dependencies cannot be null");
            }
            this.function = function;
            this.dependencies = Collections.unmodifiableList(new ArrayList<>(dependencies));
            for (Cell<T> cell : this.dependencies) {
                if (cell != null) {
                    cell.addObserver(this);
                }
            }
            recompute();
        }

        public void addCallback(Consumer<T> callback) {
            if (callback != null) {
                callbacks.add(callback);
            }
        }

        public void removeCallback(Consumer<T> callback) {
            if (callback != null) {
                callbacks.remove(callback);
            }
        }

        void recompute() {
            List<T> inputs = new ArrayList<>();
            for (Cell<T> cell : dependencies) {
                inputs.add(cell.getValue());
            }
            T newValue = function.apply(inputs);
            if (!Objects.equals(newValue, getValue())) {
                setValue(newValue);
                if (!Objects.equals(newValue, lastCallbackValue)) {
                    for (Consumer<T> callback : callbacks) {
                        callback.accept(newValue);
                    }
                    lastCallbackValue = newValue;
                }
            }
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
        return new ComputeCell<>(function, cells);
    }
}
