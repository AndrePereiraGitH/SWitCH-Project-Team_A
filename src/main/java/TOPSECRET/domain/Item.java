package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;

import java.util.Objects;

public class Item {

    private final Condition _condition;
    private final double _weight;
    private final Dimensions _dimension;
    private final Publication _publication;



    public Item(Condition condition, double weight,
                Dimensions dimension, Publication publication) {
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be null");
        if (weight <= 0.0)
            throw new IllegalArgumentException("Weight must be greater than 0.");
        if (dimension == null)
            throw new IllegalArgumentException("Dimensions cannot be null");
        if (publication == null)
            throw new IllegalArgumentException("Publication cannot be null");

        this._condition = condition;
        this._weight = weight;
        this._dimension = dimension;
        this._publication = publication;
    }

    public Condition getCondition() {
        return _condition;
    }

    public double getWeight() {
        return _weight;
    }

    public Dimensions getDimension() {
        return _dimension;
    }

    public Publication getPublication() {
        return _publication;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(_weight, item._weight) == 0 && _condition == item._condition && Objects.equals(_dimension, item._dimension) && Objects.equals(_publication, item._publication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_condition, _weight, _dimension, _publication);
    }
}



