package TOPSECRET.domain;

import java.awt.*;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class Item {
         //Temporary! With the classes that are missing as String

    private Condition _condition;
    private double _weight;
    private List<String> _pictures;
    private Dimensions _dimension;
    private String _typeOfBinding;
    private String _edition;
    private BibliographicalData _bibliographicalData;

    public Item(Condition condition, double weight, List<String> pictures, Dimensions dimension, String typeOfBinding, String edition, BibliographicalData bibliographicalData) {

        if (weight <= 0.0 )
           throw new IllegalArgumentException("Weight must be superior to 0.");

        if ( Validator.isNullOrBlank(edition))
            throw new IllegalArgumentException("Edition cannot be null or empty");
        if (condition == null)
            throw new IllegalArgumentException("Condition cannot be Null");
        if (pictures == null)
            throw new IllegalArgumentException (" Pictures cannot be Null");    // Can it be null?
        if (dimension == null)
            throw new IllegalArgumentException (" Dimensions cannot be Null");
        if ( typeOfBinding == null)
            throw new IllegalArgumentException("typeOfBinding cannot be null");
        if ( bibliographicalData == null)
            throw new IllegalArgumentException("BibliographicalData cannot be null");

        this._condition = condition;
        this._weight = weight;
        this._pictures = List.copyOf(pictures);   // To protect pictures
        this._dimension = dimension;
        this._typeOfBinding = typeOfBinding;
        this._edition = edition;
        this._bibliographicalData = bibliographicalData;
    }



//     FINAL! WITH THE CLASSES THAT ARE MISSING
//    // Attributes:
//    private Condition _condition;
//    private double _weight;
//    private List<Pictures> _pictures;
//    private Dimensions _dimension;
//    private TypeOfBinding _typeOfBinding;
//    private String _edition;
//    private BibliographicalData _bibliographicalData;
//
//
//    //Construtor Item:
//    public Item(Condition condition, double _weight, List<Pictures> pictures, Dimensions dimension, TypeOfBinding typeOfBinding, String edition, BibliographicalData bibliographicalData) {
//
//        if (weight <= 0.0 )
//           throw new IllegalArgumentException("Weight must be superior to 0.");
//
//        if ( Validator.isNullOrBlank(edition))
//            throw new IllegalArgumentException("Edition cannot be null or empty");
//
//     if (condition == null)
//            throw new IllegalArgumentException("Condition cannot be Null");
//        if (pictures == null)
//            throw new IllegalArgumentException (" Pictures cannot be Null");    // Can it be null?
//        if (dimension == null)
//            throw new IllegalArgumentException (" Dimensions cannot be Null");
//        if ( typeOfBinding == null)
//            throw new IllegalArgumentException("typeOfBinding cannot be null");
//        if ( bibliographicalData == null)
//            throw new IllegalArgumentException("BibliographicalData cannot be null");

//
//        this._condition = condition;
//        this.weight = _weight;
//        this._pictures = pictures;
//        this._dimension = dimension;
//        this._typeOfBinding = typeOfBinding;
//        this._edition = edition;
//        this._bibliographicalData = bibliographicalData;
//    }
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
//    public TypeOfBinding get_typeOfBinding() {
//        return _typeOfBinding;
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



