package TOPSECRET.domain;

/**
 *Represents the physical characteristics of a given sample, including its dimensions,
 * weight, and number of pages.
 * <p>
 * This class encapsulates basic physical metadata and validates required values
 * when using the parameterized constructor.
 */

public class PhysicalAttributes {

    private Dimensions _dimensions;
    private double _weight;
    private int _numberOfPages;

    /**
     * Creates an empty {@code PhysicalAttributes} instance.
     * <p>
     * This constructor does not perform validation and leaves fields uninitialized
     * (default values apply) until they are set elsewhere.
     */
    public PhysicalAttributes() {
    }

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

}
