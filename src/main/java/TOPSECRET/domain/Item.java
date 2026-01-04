package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.TypeOfBinding;

import java.util.List;

public class Item {
         //Temporary! With the classes that are missing as String

    private final Condition _condition;
    private final double _weight;
    private final List<String> _pictures;
    private final Dimensions _dimension;
    private final TypeOfBinding _binding;
    private final String _edition;
    private final BibliographicalData _bibliographicalData;

    public Item(Condition condition, double weight, List<String> pictures, Dimensions dimension, TypeOfBinding binding, String edition, BibliographicalData bibliographicalData) {

        if (weight <= 0.0 )
           throw new IllegalArgumentException("Weight must be greater than 0.");
        if ( Validator.isNullOrBlank(edition))
            throw new IllegalArgumentException("Edition is required");
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be Null");
        if (pictures == null)
            throw new IllegalArgumentException (" Pictures cannot be Null");    // Can it be null? I don't think so
        if (dimension == null)
            throw new IllegalArgumentException (" Dimensions cannot be Null");
        if ( binding == null)
            throw new IllegalArgumentException(" binding cannot be null");
        if ( bibliographicalData == null)
            throw new IllegalArgumentException("BibliographicalData cannot be null");

        this._condition = condition;
        this._weight = weight;
        this._pictures = List.copyOf(pictures);   // To protect pictures
        this._dimension = dimension;
        this._binding = binding;
        this._edition = edition;
        this._bibliographicalData = bibliographicalData;
    }



//     FINAL! WITH THE CLASSES THAT ARE MISSING
//    // Attributes:
//    private Condition _condition;
//    private double _weight;
//    private List<Pictures> _pictures;
//    private Dimensions _dimension;
//    private TypeOfBinding _binding;
//    private String _edition;
//    private BibliographicalData _bibliographicalData;
//
//
//    //Construtor Item:
//    public Item(Condition condition, double _weight, List<Pictures> pictures, Dimensions dimension, TypeOfBinding binding, String edition, BibliographicalData bibliographicalData) {
//
//          if (weight <= 0.0 )
//            throw new IllegalArgumentException("Weight must be greater than 0.");
//        if ( Validator.isNullOrBlank(edition))
//            throw new IllegalArgumentException("Edition is required");
//        if (condition == null)
//            throw new IllegalArgumentException("Condition cannot be Null");
//        if (pictures == null)
//            throw new IllegalArgumentException (" Pictures cannot be Null");    // Can it be null? I dont think so
//        if (dimension == null)
//            throw new IllegalArgumentException (" Dimensions cannot be Null");
//        if ( binding == null)
//            throw new IllegalArgumentException(" binding cannot be null");
//        if ( bibliographicalData == null)


//
//        this._condition = condition;
//        this.weight = _weight;
//        this._pictures = List.copyOf(pictures);
//        this._dimension = dimension;
//        this._typeOfBinding = binding;
//        this._edition = edition;
//        this._bibliographicalData = bibliographicalData;
//
//
//    public Condition get_condition() {
//        return _condition;
//    }
//
//    public double getWeight() {
//        return _weight;
//    }
//
//    public List<Pictures> get_pictures() {
//        return List.copyOf(_pictures);
//    }
//
//    public Dimensions get_dimension() {
//        return _dimension;
//    }
//
//    public TypeOfBinding get_binding() {
//        return _binding;
//    }
//
//    public BibliographicalData get_bibliographicalData() {
//        return _bibliographicalData;
//    }
//
//    public String get_edition() {
//        return _edition;
//    }
}



