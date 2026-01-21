package TOPSECRET.dto;

import java.math.BigDecimal;
import java.util.List;

public class DirectSaleDTO {

    private final String _publicationTitle;
    private final List<String> _authors;
    private final BigDecimal _price;
    private final String _listingTitle;
    private final String _seller;

    public DirectSaleDTO(String publicationTitle, List<String> authors, BigDecimal price, String listingTitle, String _seller) {
        this._publicationTitle = publicationTitle;
        this._authors = authors;
        this._price = price;
        this._listingTitle = listingTitle;
        this._seller = _seller;
    }
}
