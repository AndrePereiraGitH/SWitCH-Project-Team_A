package TOPSECRET.dto;

import java.math.BigDecimal;
import java.util.List;

public class DirectSaleDTO_US023 {

    public final String publicationTitle;
    public final List<String> authors;
    public final BigDecimal price;
    public final String listingTitle;
    public final String seller;

    public DirectSaleDTO_US023(String publicationTitle, List<String> authors, BigDecimal price, String listingTitle, String _seller) {
        this.publicationTitle = publicationTitle;
        this.authors = authors;
        this.price = price;
        this.listingTitle = listingTitle;
        this.seller = _seller;
    }
}