package TOPSECRET.domain.ValueObjects;
/**
 * Immutable value object representing physical dimensions.
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
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return Double.compare(_height, that._height) == 0
                && Double.compare(_width, that._width) == 0
                && Double.compare(_depth, that._depth) == 0;
    }
}
