package TOPSECRET.domain;
/**
 * Immutable value object representing physical dimensions.
 *
 * Invariants:
 * - height > 0
 * - width  > 0
 * - depth  > 0
 */
public class Dimensions {
    private final double _height;
    private final double _width;
    private final double _depth;

    public Dimensions(double height, double width, double depth) {
        if (height <= 0 || width <= 0 || depth <= 0) {
            throw new IllegalArgumentException("Height, width, and depth must be greater than zero.");
        }
        this._height = height;
        this._width = width;
        this._depth = depth;
    }

    public double getHeight() {
        return this._height;
    }
    public double getWidth() {
        return this._width;
    }
    public double getDepth() {
        return this._depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return Double.compare(this._height, that._height) == 0
                && Double.compare(this._width, that._width) == 0
                && Double.compare(this._depth, that._depth) == 0;
    }
}
