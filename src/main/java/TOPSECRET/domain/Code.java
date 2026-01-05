package TOPSECRET.domain;

public class Code {

    // Attributes

    private final String isbn10;
    private final String isbn13;
    private final String issn;

    // Constructor

    public Code(String isbn10, String isbn13, String issn) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.issn = issn;
    }

    public static Code fromIsbn10(String isbn10) {
        if (isbn10 == null || isbn10.isBlank()) {
            throw new IllegalArgumentException("ISBN10 cannot be null or blank");
        }
        return new Code(isbn10, null, null);
    }

    public static Code fromIsbn13(String isbn13) {
        if (isbn13 == null || isbn13.isBlank()) {
            throw new IllegalArgumentException("ISBN13 cannot be null or blank");
        }
        return new Code(null, isbn13, null);
    }

    public static Code fromIssn(String issn) {
        if (issn == null || issn.isBlank()) {
            throw new IllegalArgumentException("ISSN cannot be null or blank");
        }
        return new Code(null, null, issn);
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getIssn() {
        return issn;
    }
}
