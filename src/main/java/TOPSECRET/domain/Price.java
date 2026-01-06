package TOPSECRET.domain;

import java.math.BigDecimal;

public class Price {

    private BigDecimal _price;

    public Price(BigDecimal price) {

        if(price.scale()>2){
            throw new IllegalArgumentException("Price value is invalid");
        }
        if(price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price value can not be equal or lower than 0");
        }

        _price = price;
    }

    public BigDecimal getPrice() {
        BigDecimal copyPrice = _price;
        return copyPrice;
    }





}
