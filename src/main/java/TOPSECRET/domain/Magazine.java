package TOPSECRET.domain;

public class Magazine {
    private Code _issn;
    private Item _item;

    public Magazine(Code issn, Item item) {
        if (issn == null) throw new IllegalArgumentException("ISSN cannot be null");
        if (item == null) throw new IllegalArgumentException("Item cannot be null");

        this._issn = issn;
        this._item = item;
    }
    public Code getIssn() {
        return _issn;
    }
    public Item getItem() {
        return _item;
    }
}
