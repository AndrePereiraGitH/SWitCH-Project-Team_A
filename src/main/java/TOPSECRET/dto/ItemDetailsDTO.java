package TOPSECRET.dto;


import TOPSECRET.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailsDTO {

    public String condition;
    public String publication;

    public ItemDetailsDTO(String condition, String publication) {
        this.condition = condition;
        this.publication = publication;
    }

    public static List<ItemDetailsDTO> toDTOs(List<Item> items){
        List<ItemDetailsDTO> listOfItemsDTO = new ArrayList<>();

        for(Item item: items){
            listOfItemsDTO.add(toDTO(item));
        }
        return listOfItemsDTO;
    }

    public static ItemDetailsDTO toDTO(Item item){
        String conditionDTO = item.getCondition().toString();
        String titleDTO = item.getPublication().getBibliographicalData().getTitle();

        ItemDetailsDTO itemDTO = new ItemDetailsDTO(conditionDTO,titleDTO);

        return itemDTO;
    }
}
