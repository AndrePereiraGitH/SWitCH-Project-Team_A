package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample;

import java.util.ArrayList;
import java.util.List;


public class AuctionItemDTO_US024 {

    public String title;
    public String code;
    public String condition;
    public String seller;

    public AuctionItemDTO_US024(String title, String code, String condition, String seller) {
        this.title = title;
        this.code = code;
        this.condition = condition;
        this.seller = seller;
    }

    public static List<AuctionItemDTO_US024> toDTOs(List<PublicationSample> listOfItemsOnAuctionByPublication){

        List<AuctionItemDTO_US024> listAuctionItemDTO = new ArrayList<>();

        for(PublicationSample item: listOfItemsOnAuctionByPublication){
            listAuctionItemDTO.add(toDTO(item));
        }
        return listAuctionItemDTO;
    }

    public static AuctionItemDTO_US024 toDTO(PublicationSample publicationSample){

        String titleDTO = publicationSample.getBibliographicalData().getTitle();

        String codeDTO = publicationSample.getEdition().getCode().toString();

        String conditionDTO = publicationSample.getCondition().getDescription();

        String sellerDTO = publicationSample.getOwner().getUserName();

        AuctionItemDTO_US024 aucDTO = new AuctionItemDTO_US024(titleDTO,codeDTO,conditionDTO,sellerDTO);

        return aucDTO;
    }
}
