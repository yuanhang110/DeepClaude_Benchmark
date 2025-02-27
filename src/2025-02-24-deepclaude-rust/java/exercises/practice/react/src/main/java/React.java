import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {
    private static final List<ComputeCell<?>> allComputeCells = new ArrayList<>();

    public static class Cell<T> {
        public T getValue() {
            throw new UnsupportedOperationException("Please implement the Cell.getValue() method");
        }
    }

    public static class InputCell<T> extends Cell<T> {
        private T value;
        private final List<Runnable> listeners = new ArrayList<>();

        public InputCell(T initialValue) {
            this.value = initialValue;
        }

        @Override
        public T getValue() {
            return value;
        }

        public void setValue(T newValue) {
            if (!Objects.equals(value, newValue)) {
                value = newValue;
                React.beginPropagation();
                for (Runnable listener : listeners) {
                    listener.run();
                }
                React.endPropagation();
            }
        }

        public void addListener(Runnable listener) {
            listeners.add(listener);
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> function;
        private final List<Cell<T>> dependencies;
        private final List<Consumer<T>> callbacks = new ArrayList<>();
        private final List<Runnable> listeners = new ArrayList<>();
        private T value;
        private T previousValue;

        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> dependencies) {
            this.function = function;
            this.dependencies = dependencies;
            for (Cell<T> cell : dependencies) {
                if (cell instanceof InputCell) {
                    ((InputCell<T>) cell).addListener(this::recompute);
                } else if (cell instanceof ComputeCell) {
                    ((ComputeCell<T>) cell).addListener(this::recompute);
                }
            }
            recompute();
            React.registerComputeCell(this);
        }

        @Override
        public T getValue() {
            return value;
        }

        private void recompute() {
            List<T> inputs = dependencies.stream()
                .map(Cell::getValue)
                .toList();
            T newValue = function.apply(inputs);
            if (!Objects.equals(value, newValue)) {
                value = newValue;
                for (Runnable listener : listeners) {
                    listener.run();
                }
            }
        }

        public void addListener(Runnable listener) {
            listeners.add(listener);
        }

        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }

        void savePreviousValue() {
            previousValue = value;
        }

        void fireCallbacksIfChanged() {
            if (!Objects.equals(previousValue, value)) {
                for (Consumer<T> callback : callbacks) {
                    callback.accept(value);
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

    public static void beginPropagation() {
        allComputeCells.forEach(ComputeCell::savePreviousValue);
    }

    public static void endPropagation() {
        allComputeCells.forEach(ComputeCell::fireCallbacksIfChanged);
    }

    static <T> void registerComputeCell(ComputeCell<T> cell) {
        allComputeCells.add(cell);
    }
}
