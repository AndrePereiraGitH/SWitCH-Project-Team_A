package TOPSECRET.dto;


import TOPSECRET.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDTO_US024 {

    public String condition;
    public String publication;

    public ItemDTO_US024(String condition, String publication) {
        this.condition = condition;
        this.publication = publication;
    }

    public static ItemDTO_US024 toDTO(Item item){
        String conditionDTO = item.getCondition().toString();
        String titleDTO = item.getPublication().getBibliographicalData().getTitle();

        ItemDTO_US024 itemDTO = new ItemDTO_US024(conditionDTO,titleDTO);

        return itemDTO;
    }


    public static List<ItemDTO_US024> getListOfItemsAuctionDTO(List<Item> items){
        List<ItemDTO_US024> listOfAuctionsDTO = new ArrayList<>();
        for(Item item: items){
            listOfAuctionsDTO.add(toDTO(item));
        }
        return listOfAuctionsDTO;
    }


}
