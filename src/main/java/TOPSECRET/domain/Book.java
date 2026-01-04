package TOPSECRET.domain;

public class Book {

    private Item _item;
    private Code _isbn10;
    private Code _isbn13;
    private BibliographicalData _information;

    public Book(Item item, Code isbn10, Code isbn13, BibliographicalData information) {

        if (item == null) throw new IllegalArgumentException("Invalid Item");
        if (information == null) throw new IllegalArgumentException("Invalid Bibliographical Data");

        this._item = item;
        this._isbn10 = isbn10;
        this._isbn13 = isbn13;
        this._information = information;
    }

    public Item getItem() {return _item;}

    public Code getIsbn10() {return _isbn10;}

    public Code getIsbn13() {return _isbn13;}

    public BibliographicalData getInformation() {return _information;}
}
