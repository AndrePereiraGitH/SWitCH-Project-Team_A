package TOPSECRET.dto.listing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ListingRequestDTO {

    @NotBlank
    private String listingTitle;

    @NotEmpty
    private List<String> publicationIds;

    @NotBlank
    private String saleType;

    @NotBlank
    private String price;

    private String shortDescription;

    private List<String> pictures;

    @NotNull
    private LocalDate publicationDate;
}
