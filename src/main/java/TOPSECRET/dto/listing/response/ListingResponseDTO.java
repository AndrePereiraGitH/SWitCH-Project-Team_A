package TOPSECRET.dto.listing.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO used to represent the response data of a listing.
 *
 * <p>This class contains the information returned to the client when a listing
 * is requested, including its basic details, related publication titles,
 * seller information, pictures and publication date.</p>
 *
 * <p>It extends {@link RepresentationModel} to support HATEOAS links.</p>
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ListingResponseDTO extends RepresentationModel<ListingResponseDTO> {
    private String id;
    private String listingTitle;
    private List<String> publicationTitles;
    private String saleType;
    private String price;
    private String description;
    private String sellerId;
    private List<String> pictures;
    private LocalDate publicationDate;

}
