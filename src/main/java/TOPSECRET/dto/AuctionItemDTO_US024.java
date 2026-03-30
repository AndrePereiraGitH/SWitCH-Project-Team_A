package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a single {@link PublicationSample} item
 * that is currently listed on auction (US024).
 *
 * <p>Carries only the flat, presentation-ready fields needed by the UI layer,
 * decoupling it from the domain model structure.</p>
 */
public class AuctionItemDTO_US024 {

    /** The title of the publication associated with this auction item. */
    public String title;

    /** The edition code of the publication (e.g. ISBN or internal code). */
    public String code;

    /** A human-readable description of the item's physical condition. */
    public String condition;

    /** The username of the user who owns and is selling this item. */
    public String seller;

    /**
     * Constructs a new {@code AuctionItemDTO_US024} with all required fields.
     *
     * @param title     the publication title; must not be {@code null}
     * @param code      the edition code; must not be {@code null}
     * @param condition the condition description; must not be {@code null}
     * @param seller    the seller's username; must not be {@code null}
     */
    public AuctionItemDTO_US024(String title, String code, String condition, String seller) {
        this.title = title;
        this.code = code;
        this.condition = condition;
        this.seller = seller;
    }

    /**
     * Converts a list of {@link PublicationSample} domain objects into a list
     * of {@code AuctionItemDTO_US024} instances.
     *
     * <p>Each element is converted individually via {@link #toDTO(PublicationSample)}.</p>
     *
     * @param listOfItemsOnAuctionByPublication the list of domain objects to convert;
     *                                          must not be {@code null}
     * @return a {@link List} of {@code AuctionItemDTO_US024} in the same order
     *         as the input; never {@code null}, but may be empty
     */
//    public static List<AuctionItemDTO_US024> toDTOs(List<PublicationSample> listOfItemsOnAuctionByPublication) {
//
//        List<AuctionItemDTO_US024> listAuctionItemDTO = new ArrayList<>();
//
//        for (PublicationSample item : listOfItemsOnAuctionByPublication) {
//            listAuctionItemDTO.add(toDTO(item));
//        }
//
//        return listAuctionItemDTO;
//    }

    /**
     * Converts a single {@link PublicationSample} domain object into an
     * {@code AuctionItemDTO_US024}.
     *
     * <p>Extracts the following fields from the domain object:</p>
     * <ul>
     *   <li><b>title</b> — from {@code getBibliographicalData().getTitle()}</li>
     *   <li><b>code</b> — from {@code getEdition().getCode().toString()}</li>
     *   <li><b>condition</b> — from {@code getCondition().getDescription()}</li>
     *   <li><b>seller</b> — from {@code getOwner().getUserName()}</li>
     * </ul>
     *
     * @param publicationSample the domain object to convert; must not be
     *                          {@code null}, and its nested properties
     *                          (edition, condition, owner) must not be {@code null}
     * @return a new {@code AuctionItemDTO_US024} populated with data from
     *         the given {@link PublicationSample}
     */
//    public static AuctionItemDTO_US024 toDTO(PublicationSample publicationSample) {
//
//        String titleDTO = publicationSample.getBibliographicalData().getTitle();
//        String codeDTO = publicationSample.getEdition().getCode().toString();
//        String conditionDTO = publicationSample.getCondition().getDescription();
//        String sellerDTO = publicationSample.getOwner().getUserName();
//
//        return new AuctionItemDTO_US024(titleDTO, codeDTO, conditionDTO, sellerDTO);
//    }
}