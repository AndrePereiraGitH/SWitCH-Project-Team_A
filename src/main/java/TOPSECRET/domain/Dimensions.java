package TOPSECRET.domain;

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
}
