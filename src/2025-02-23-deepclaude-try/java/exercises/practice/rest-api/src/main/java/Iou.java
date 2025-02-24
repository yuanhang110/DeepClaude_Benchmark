/**
 * POJO representing an IOU.
 * 
 * If this is in a user's "owed", then the user owes the person with {@link name} this {@link amount}.
 * If this is in a user's "owedBy", then {@link name} owes the user this {@link amount}.
 */
public class Iou {
    public final String name;
    public final double amount;

    public Iou(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }
}
import java.util.Objects;

/** POJO representing an IOU record. */
public class Iou {
    private final String name;
    private final double amount;

    public Iou(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String name() {
        return name;
    }

    public double amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iou iou = (Iou) o;
        return Double.compare(amount, iou.amount) == 0 && Objects.equals(name, iou.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount);
    }
}
