import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {

    public static abstract class Cell<T> {
        public abstract T getValue();
    }

    public static class InputCell<T> extends Cell<T> {
        private T value;
        private List<ComputeCell<?>> dependents = new ArrayList<>();

        @Override
        public T getValue() {
            return value;
        }

        public void setValue(T newValue) {
            if (Objects.equals(value, newValue)) {
                return;
            }
            value = newValue;
            List<ComputeCell<?>> queue = new ArrayList<>(dependents);
            Set<ComputeCell<?>> changed = new LinkedHashSet<>();
            while (!queue.isEmpty()) {
                ComputeCell<?> cell = queue.remove(0);
                boolean changedFlag = cell.recompute();
                if (changedFlag) {
                    changed.add(cell);
                    queue.addAll(cell.dependents);
                }
            }
            for (ComputeCell<?> cell : changed) {
                cell.fireCallbacks();
            }
        }

        public void addDependent(ComputeCell<?> dependent) {
            dependents.add(dependent);
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private T currentValue;
        private final Function<List<T>, T> function;
        private final List<Cell<T>> dependencies;
        private List<Consumer<T>> callbacks = new ArrayList<>();
        private List<ComputeCell<?>> dependents = new ArrayList<>();

        public ComputeCell(Function<List<T>, T> function, List<? extends Cell<T>> dependencies) {
            this.function = function;
            this.dependencies = dependencies;
            recompute();
        }

        @Override
        public T getValue() {
            return currentValue;
        }

        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }

        public boolean recompute() {
            List<T> inputs = new ArrayList<>();
            for (Cell<T> dependency : dependencies) {
                inputs.add(dependency.getValue());
            }
            T newValue = function.apply(inputs);
            if (!Objects.equals(currentValue, newValue)) {
                currentValue = newValue;
                return true;
            }
            return false;
        }

        void fireCallbacks() {
            T value = currentValue;
            for (Consumer<T> callback : callbacks) {
                callback.accept(value);
            }
        }

        public void addDependent(ComputeCell<?> dependent) {
            dependents.add(dependent);
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        InputCell<T> cell = new InputCell<>();
        cell.value = initialValue;
        return cell;
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<? extends Cell<T>> cells) {
        ComputeCell<T> computeCell = new ComputeCell<>(function, cells);
        for (Cell<T> cell : cells) {
            if (cell instanceof InputCell) {
                ((InputCell<T>) cell).addDependent(computeCell);
            } else if (cell instanceof ComputeCell) {
                ((ComputeCell<T>) cell).addDependent(computeCell);
            }
        }
        return computeCell;
    }
}
