package TOPSECRET.dto.listing.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Outbound DTO representing detailed information about a listing.
 *
 * <p>This DTO is used to expose listing data together with related publication,
 * author, genre, publisher, edition, buyer, and status information.</p>
 *
 * <p>It extends {@link RepresentationModel} to support HATEOAS links in API
 * responses.</p>
 *
 * <p>The DTO contains both display values, such as author names and publisher
 * name, and identifier values, such as author IDs and publisher ID, so clients
 * can both render listing details and apply filters using stable identifiers.</p>
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ListingDetailsDTO extends RepresentationModel<ListingDetailsDTO> {
   private String id;
   private String listingTitle;

   private List<String> authorIds;
   private List<String> authors;

   private List<String> genre;

   private String price;
   private LocalDate publicationDate;
   private String condition;
   private String saleType;
   private String picture;
   private String shortDescription;
   private String sellerId;

   private String publisherId;
   private String publisher;

   private String isbn;
   private Integer year;
   private Integer pages;
   private String language;
   private List<String> bibliographicalDataId;
   private String buyer;
   private String publicationStatus;

}
