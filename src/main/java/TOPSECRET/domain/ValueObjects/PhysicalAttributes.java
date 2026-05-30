package TOPSECRET.domain.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 *Represents the physical characteristics of a given sample, including its dimensions,
 * weight, and number of pages.
 * <p>
 * This class encapsulates basic physical metadata and validates required values
 * when using the parameterized constructor.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhysicalAttributes {

    private Dimensions _dimensions;
    private double _weight;
    private int _numberOfPages;

    /**
     * Creates a {@code PhysicalAttributes} instance with validated physical data.
     *
     * @param dimensions    the dimensions of the item; must not be {@code null}
     * @param weight        the weight of the item; must be greater than {@code 0}
     * @param numberOfPages the number of pages; must be greater than {@code 0}
     * @throws IllegalArgumentException if {@code dimensions} is {@code null}
     * @throws IllegalArgumentException if {@code weight <= 0} or {@code numberOfPages <= 0}
     */
    public PhysicalAttributes(Dimensions dimensions, double weight, int numberOfPages) {

        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions cannot be null");
        }

        if (weight <= 0 || numberOfPages <= 0) {
            throw new IllegalArgumentException("weight and number of pages must be grater then 0");
        }

        this._dimensions = dimensions;
        this._weight = weight;
        this._numberOfPages = numberOfPages;
    }

    public Dimensions getDimensions() {
        return _dimensions;
    }

    public double getWeight() {
        return _weight;
    }

    public int getNumberOfPages() {
        return _numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalAttributes that = (PhysicalAttributes) o;
        return Double.compare(_weight, that._weight) == 0
                && _numberOfPages == that._numberOfPages
                && Objects.equals(_dimensions, that._dimensions);
    }
}
