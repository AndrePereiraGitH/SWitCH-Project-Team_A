package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.TypeOfBinding;

import java.util.List;
/**
 * Represents an item with physical attributes and bibliographic information.
 * Invariants:
 * - weight > 0
 * - edition not null/blank
 * - condition, pictures, dimensions, binding, bibliographicalData not null
 */
public class Item {

    private Condition _condition;
    private double _weight;
    private List<Picture> _picture;
    private Dimensions _dimension;
    private TypeOfBinding _binding;
    private String _edition;
    private BibliographicalData _bibliographicalData;


    public Item(Condition condition, double weight, List<Picture> picture,
                Dimensions dimension, TypeOfBinding binding, String edition,
                BibliographicalData bibliographicalData) {

        if (weight <= 0.0)
            throw new IllegalArgumentException("Weight must be greater than 0.");
        if (edition == null || edition.isBlank())
            throw new IllegalArgumentException("Edition is required");
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be null");
        if (picture == null)
            throw new IllegalArgumentException("Pictures cannot be null");
        if (dimension == null)
            throw new IllegalArgumentException("Dimensions cannot be null");
        if (binding == null)
            throw new IllegalArgumentException("Binding cannot be null");
        if (bibliographicalData == null)
            throw new IllegalArgumentException("BibliographicalData cannot be null");

        this._condition = condition;
        this._weight = weight;
        this._picture = List.copyOf(picture);
        this._dimension = dimension;
        this._binding = binding;
        this._edition = edition;
        this._bibliographicalData = bibliographicalData;
    }

    public Condition getCondition() {
        return _condition;
    }

    public double getWeight() {
        return _weight;
    }

    public List<Picture> getPictures() {
        return _picture;
    }

    public Dimensions getDimension() {
        return _dimension;
    }

    public TypeOfBinding getBinding() {
        return _binding;
    }

    public BibliographicalData getBibliographicalData() {
        return _bibliographicalData;
    }

    public String getEdition() {
        return _edition;
    }
}



