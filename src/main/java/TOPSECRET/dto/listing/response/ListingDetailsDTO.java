package TOPSECRET.dto.listing.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ListingDetailsDTO extends RepresentationModel<ListingDetailsDTO> {
   private  String id;
   private String listingTitle;
   private List<String> authors;
   private List<String> genre;
   private BigDecimal price;
   private String condition;
   private String saleType;
   private String picture;
   private String shortDescription;
   private String sellerId;
   private String publisher;
   private String isbn;
   private Integer year;
   private Integer pages;
   private String language;

}
