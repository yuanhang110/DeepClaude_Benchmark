import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {

    public static class Cell<T> {
        public T getValue() {
            throw new UnsupportedOperationException("Please implement the Cell.getValue() method");
        }
    }

    public static class InputCell<T> extends Cell<T> {
        private T value;
        private List<Runnable> subscribers = new ArrayList<>();

        public InputCell(T initialValue) {
            this.value = initialValue;
        }

        @Override
        public T getValue() {
            return value;
        }

        public void setValue(T newValue) {
            if (!newValue.equals(value)) {
                value = newValue;
                subscribers.forEach(Runnable::run);
            }
        }

        public void addSubscriber(Runnable sub) {
            subscribers.add(sub);
        }

        public void removeSubscriber(Runnable sub) {
            subscribers.remove(sub);
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> function;
        private final List<Cell<T>> dependencies;
        private T value;
        private List<Consumer<T>> callbacks = new ArrayList<>();
        private List<Runnable> subscribers = new ArrayList<>();

        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> dependencies) {
            this.function = function;
            this.dependencies = dependencies;
            for (Cell<T> dependency : dependencies) {
                if (dependency instanceof InputCell) {
                    ((InputCell<T>) dependency).addSubscriber(this::recompute);
                } else if (dependency instanceof ComputeCell) {
                    ((ComputeCell<T>) dependency).addSubscriber(this::recompute);
                } else {
                    throw new IllegalArgumentException("Dependency must be InputCell or ComputeCell");
                }
            }
            recompute();
        }

        private void recompute() {
            List<T> inputs = new ArrayList<>();
            for (Cell<T> dep : dependencies) {
                inputs.add(dep.getValue());
            }
            T newValue = function.apply(inputs);
            if (value == null || !newValue.equals(value)) {
                T oldValue = value;
                value = newValue;
                subscribers.forEach(Runnable::run);
                if (oldValue != null) {
                    callbacks.forEach(cb -> cb.accept(newValue));
                }
            }
        }

        @Override
        public T getValue() {
            return value;
        }

        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }

        public void addSubscriber(Runnable sub) {
            subscribers.add(sub);
        }

        public void removeSubscriber(Runnable sub) {
            subscribers.remove(sub);
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
        return new ComputeCell<>(function, cells);
    }
}
