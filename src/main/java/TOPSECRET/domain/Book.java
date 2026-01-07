package TOPSECRET.domain;

public class Book {

    private Item _item;
    private Code _isbn10;
    private Code _isbn13;
    private BibliographicalData _information;

    // Book constructor
    public Book(Item item, Code isbn10, Code isbn13, BibliographicalData information) {

        if (item == null) throw new IllegalArgumentException("Invalid Item");
        if (information == null) throw new IllegalArgumentException("Invalid Bibliographical Data");

        this._item = item;
        this._isbn10 = isbn10;
        this._isbn13 = isbn13;
        this._information = information;
    }


    public static Book noCodes(Item item, BibliographicalData information) {
        return new Book(item, null, null, information);
    }


    public static Book withIsbn10(Item item, Code isbn10, BibliographicalData information) {
        if (isbn10 == null) throw new IllegalArgumentException("Invalid ISBN10");
        // optional: if (!isbn10.isIsbn10()) throw ...
        return new Book(item, isbn10, null, information);
    }


    public static Book withIsbn13(Item item, Code isbn13, BibliographicalData information) {
        if (isbn13 == null) throw new IllegalArgumentException("Invalid ISBN13");
        // optional: if (!isbn13.isIsbn13()) throw ...
        return new Book(item, null, isbn13, information);
    }


    public static Book withBoth(Item item, Code isbn10, Code isbn13, BibliographicalData information) {
        if (isbn10 == null) throw new IllegalArgumentException("Invalid ISBN10");
        if (isbn13 == null) throw new IllegalArgumentException("Invalid ISBN13");
        return new Book(item, isbn10, isbn13, information);
    }


    public Item getItem() {return _item;}

    public Code getIsbn10() {return _isbn10;}

    public Code getIsbn13() {return _isbn13;}

    public BibliographicalData getInformation() {return _information;}
}
