package TOPSECRET.domain;

import java.math.BigDecimal;

public class PriceValidator {

    public static double validatePrice(double price){

        BigDecimal checkPrice = BigDecimal.valueOf(price);

        if(checkPrice.scale()>2){
            throw new IllegalArgumentException("Price value is invalid");
        }
        if(checkPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price value can not be equal or lower than 0");
        }

        // Ask PO if there is any upper price limit

        return price;
    }


}
