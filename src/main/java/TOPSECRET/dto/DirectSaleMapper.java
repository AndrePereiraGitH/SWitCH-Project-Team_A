/*package TOPSECRET.dto;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;
import TOPSECRET.dto.DirectSaleDTO;

import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper {

    public DirectSaleDTO toDTO(DirectSale ds) {

        Item firstItem = ds.getListing().getItems().get(0);
        String title = firstItem.getPublication().getBibliographicalData().getTitle();
        return new DirectSaleDTO(title);
    }

    public List<DirectSaleDTO> toDTO(List<DirectSale> directSales) {
        List<DirectSaleDTO> dtos = new ArrayList<>();
        for (DirectSale ds : directSales) {
            dtos.add(toDTO(ds));
        }
        return dtos;
    }
}*/