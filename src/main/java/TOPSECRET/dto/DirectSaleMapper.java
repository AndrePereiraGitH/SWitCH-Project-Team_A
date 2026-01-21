/*package TOPSECRET.dto;

import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper {

    public List<DirectSaleDTO> toDTO(List<DirectSale> listOfDirectSale) {
        List<DirectSaleDTO> dtoList = new ArrayList<>();

        for (DirectSale ds : listOfDirectSale) {
            String details = ds.getDetails();
            DirectSaleDTO dto = new DirectSaleDTO(details);
            dtoList.add(dto);
        }
        return dtoList;
    }
}*/