import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {

    public static class Cell<T> {
        protected List<ComputeCell<?>> observers = new ArrayList<>();

        public T getValue() {
            throw new UnsupportedOperationException("Please implement the Cell.getValue() method");
        }

        protected void addObserver(ComputeCell<?> observer) {
            observers.add(observer);
        }
    }

    public static class InputCell<T> extends Cell<T> {
        private T value;

        public InputCell(T initialValue) {
            this.value = initialValue;
        }

        @Override
        public T getValue() {
            return value;
        }

        public void setValue(T newValue) {
            if (!Objects.equals(newValue, value)) {
                value = newValue;
                List<ComputeCell<?>> changedCells = new ArrayList<>();
                for (ComputeCell<?> observer : observers) {
                    observer.update(changedCells);
                }
                for (ComputeCell<?> cell : changedCells) {
                    cell.fireCallbacks();
                }
            }
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> function;
        private final List<Cell<T>> dependencies;
        private T value;
        private final List<Consumer<T>> callbacks = new ArrayList<>();

        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> dependencies) {
            this.function = function;
            this.dependencies = new ArrayList<>(dependencies);
            List<T> inputs = new ArrayList<>();
            for (Cell<T> dep : dependencies) {
                inputs.add(dep.getValue());
            }
            this.value = function.apply(inputs);
        }

        @Override
        public T getValue() {
            return value;
        }

        public void update(List<ComputeCell<?>> changedCells) {
            List<T> inputs = new ArrayList<>();
            for (Cell<T> dep : dependencies) {
                inputs.add(dep.getValue());
            }
            T newValue = function.apply(inputs);
            if (!Objects.equals(newValue, value)) {
                value = newValue;
                changedCells.add(this);
                for (ComputeCell<?> observer : observers) {
                    observer.update(changedCells);
                }
            }
        }

        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }

        void fireCallbacks() {
            for (Consumer<T> callback : callbacks) {
                callback.accept(value);
            }
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
        ComputeCell<T> computeCell = new ComputeCell<>(function, cells);
        for (Cell<T> cell : cells) {
            cell.addObserver(computeCell);
        }
        return computeCell;
    }
}
