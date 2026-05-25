package TOPSECRET.dto.listing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


/**
 * Data transfer object used to receive the information required to create
 * or update a listing.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@NotNull
@NotBlank
public class ListingRequestDTO {
    private String listingTitle;
    private List<String> publicationIds;
    private String saleType;
    private String price;
    private String shortDescription;
    private List<String> pictures;
    private LocalDate publicationDate;
}
