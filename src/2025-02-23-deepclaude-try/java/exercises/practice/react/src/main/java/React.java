import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {
    public static class Cell<T> {
        protected T value;
        protected List<ComputeCell<T>> dependents = new ArrayList<>();

        public T getValue() {
            return value;
        }

        protected void notifyDependents() {
            for (ComputeCell<T> dependent : dependents) {
                dependent.recompute();
            }
        }
    }

    public static class InputCell<T> extends Cell<T> {
        public InputCell(T initialValue) {
            this.value = initialValue;
        }

        public void setValue(T newValue) {
            if (newValue == null && this.value == null) return;
            if (newValue != null && newValue.equals(this.value)) return;
            
            this.value = newValue;
            notifyDependents();
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> computeFunction;
        private final List<Cell<T>> inputs;
        private final List<Consumer<T>> callbacks = new ArrayList<>();
        private T lastValue;

        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
            this.computeFunction = function;
            this.inputs = cells;
            for (Cell<T> cell : cells) {
                cell.dependents.add(this);
            }
            recompute();
            this.lastValue = this.value;
        }

        protected void recompute() {
            List<T> inputValues = new ArrayList<>();
            for (Cell<T> cell : inputs) {
                inputValues.add(cell.getValue());
            }
            
            T newValue = computeFunction.apply(inputValues);
            if (newValue == null && this.value == null) return;
            if (newValue != null && newValue.equals(this.value)) return;
            
            this.value = newValue;
            if ((lastValue == null && value != null) || 
                (lastValue != null && !lastValue.equals(value))) {
                lastValue = value;
                for (Consumer<T> callback : callbacks) {
                    callback.accept(value);
                }
            }
            notifyDependents();
        }

        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
        return new ComputeCell<>(function, cells);
    }
}
