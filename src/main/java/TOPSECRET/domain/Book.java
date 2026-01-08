package TOPSECRET.domain;

public class Book {

    private Item _item;
    private Code _isbn10;
    private Code _isbn13;

    // Book constructor
    public Book(Item item, Code isbn10, Code isbn13) {

        if (item == null) throw new IllegalArgumentException("Invalid Item");

        this._item = item;
        this._isbn10 = isbn10;
        this._isbn13 = isbn13;
    }


    public static Book noCodes(Item item) {
        return new Book(item, null, null);
    }


    public static Book withIsbn10(Item item, Code isbn10) {
        if (isbn10 == null) throw new IllegalArgumentException("Invalid ISBN10");
        return new Book(item, isbn10, null);
    }


    public static Book withIsbn13(Item item, Code isbn13) {
        if (isbn13 == null) throw new IllegalArgumentException("Invalid ISBN13");
        return new Book(item, null, isbn13);
    }


    public static Book withBoth(Item item, Code isbn10, Code isbn13) {
        if (isbn10 == null) throw new IllegalArgumentException("Invalid ISBN10");
        if (isbn13 == null) throw new IllegalArgumentException("Invalid ISBN13");
        return new Book(item, isbn10, isbn13);
    }


    public Item getItem() {return _item;}

    public Code getIsbn10() {return _isbn10;}

    public Code getIsbn13() {return _isbn13;}

}
