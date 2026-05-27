package TOPSECRET.controller.controllerREST;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.request.ListingRequestDTO;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.services.BibliographicalDataService;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RestController
@Tag(name = "Listings", description = "Listing management endpoints")
@RequestMapping(path = "/listings")
public class ListingRESTController {

    private BibliographicalDataService biblioDataService;
    private PublicationSampleService publicationService;
    private ListingService listingService;

    @Autowired
    public ListingRESTController(BibliographicalDataService biblioDataService, PublicationSampleService publicationService, ListingService listingService) {
        this.biblioDataService = biblioDataService;
        this.publicationService = publicationService;

    }

    @PostMapping("/")
    public ResponseEntity<ListingResponseDTO> createListing(@RequestBody ListingRequestDTO listingRequestDTO, @RequestParam UserId userId) {

        List<PublicationSampleId> itemsId = listingRequestDTO.getPublicationIds().stream().map(ListingMapper::toPublicationId).toList();
        SaleType saleType = ListingMapper.toSaleType(listingRequestDTO.getSaleType());
        Price price = ListingMapper.toPrice(listingRequestDTO.getPrice());

        List<Picture> pictures = listingRequestDTO.getPictures().stream().map(ListingMapper::toPicture).toList();

        try {


            Listing listing = listingService.createAndSave(
                    listingRequestDTO.getListingTitle(),
                    itemsId,
                    saleType,
                    price,
                    listingRequestDTO.getShortDescription(),
                    userId,
                    pictures
            );
            // TODO waiting for bibliographicalData Service and PublicationSample Service

//            List<BibliographicalData> bibliographicalDatas = biblioDataService.
//
//            ListingResponseDTO result = ListingMapper.toResponseDTO(listing, );


            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AccessDeniedException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




//    @GetMapping("/direct-sale/{genreId}")
//    public ResponseEntity<ListingResponseDTO> getAllDirectSaleByGenre(@PathVariable String genreId) {
//
//    }

}
