package TOPSECRET.domain;
/**
 * Represents a magazine identified by an ISSN and associated with an Item.
 * Invariants:
 * - issn != null
 * - item != null
 */
public class Magazine {
    private final Code _issn;
    private final Item _item;

    public Magazine(Code issn, Item item) {
        if (issn == null) throw new IllegalArgumentException("ISSN cannot be null");
        if (issn.getIssn() == null || issn.getIssn().isBlank()){ throw new IllegalArgumentException("Code must contain a valid ISSN");}
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
