package TOPSECRET.domain;

import java.math.BigDecimal;
import java.util.Objects;

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
        return _price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(_price, price._price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_price);
    }
}
