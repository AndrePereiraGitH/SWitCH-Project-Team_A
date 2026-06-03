package TOPSECRET.dto.listing.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ListingResponseExploreDTO extends RepresentationModel<ListingResponseExploreDTO> {
    private String id;
    private String listingTitle;
    private List<String> authors;
    private List<String> genre;
    private String price;
    private String condition;
    private String saleType;
}
