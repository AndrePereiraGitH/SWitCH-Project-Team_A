package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.controller.controllerREST.root.linkProvider.ListingLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.request.ListingRequestDTO;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.services.BibliographicalDataService;
import TOPSECRET.services.EditionService;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Listings", description = "Listing management endpoints")
@RequestMapping(path = "/listings")
public class ListingRESTController {

    private BibliographicalDataService biblioDataService;
    private PublicationSampleService publicationService;
    private ListingService listingService;
    private EditionService editionService;
    private ListingLinkProvider listingLinkProvider;

    @Autowired
    public ListingRESTController(BibliographicalDataService biblioDataService, PublicationSampleService publicationService, ListingService listingService, EditionService editionService, ListingLinkProvider listingLinkProvider) {
        this.biblioDataService = biblioDataService;
        this.publicationService = publicationService;
        this.listingService = listingService;
        this.editionService = editionService;
        this.listingLinkProvider = listingLinkProvider;

    }

    /**
     * Creates a new listing for a given user.
     *
     * <p>This endpoint receives listing data, converts the request DTO values into
     * domain objects, creates and saves the listing, retrieves the related
     * bibliographical data, and returns the created listing as a response DTO.</p>
     *
     * @param listingRequestDTO the request body containing the listing information
     * @param userId the identifier of the user creating the listing
     * @return {@code 201 CREATED} with the created listing,
     *         {@code 403 FORBIDDEN} if the user is not allowed to create it,
     *         {@code 400 BAD REQUEST} if the request contains invalid data,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @PostMapping("")
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

            List<PublicationSample> publicationSamples = itemsId.stream().map(publicationService::findById).toList();

           List<BibliographicalData> biblioDatas = getBibliographicalData(publicationSamples);

            ListingResponseDTO result = ListingMapper.toResponseDTO(listing, biblioDatas);

            List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userId));
            links.add(linkTo(methodOn(ListingRESTController.class).findById(listing.id().getId(), userId)).withSelfRel());
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (AccessDeniedException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponseDTO> findById(@PathVariable String id, @RequestParam UserId userId) {
        ListingId listingId = ListingMapper.toListingId(id);

        try {
            Listing listing = listingService.findById(listingId);

            List<PublicationSample> pubSamples = listing.getItemsId().stream()
                    .map(publicationSampleId -> publicationService.findById(publicationSampleId))
                    .toList();

            List<BibliographicalData> bibliographicalDatas = getBibliographicalData(pubSamples);

            ListingResponseDTO dto = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

            List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userId));
            links.add(linkTo(methodOn(ListingRESTController.class).findById(id, userId)).withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("")
    public ResponseEntity<List<ListingResponseDTO>> findAll(@RequestParam String userId) {

        try {
            UserId userId1 = ListingMapper.toUserId(userId);

            List<Listing> allListings = listingService.findAll();

            List<ListingResponseDTO> allDTO = new ArrayList<>();

            for (Listing listing : allListings) {
                List<PublicationSample> pubSamples = getPublicationSamples(listing);

                List<BibliographicalData> biblioDataList = getBibliographicalData(pubSamples);

                ListingResponseDTO dto = ListingMapper.toResponseDTO(listing, biblioDataList);

                List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userId1));
                links.add(linkTo(methodOn(ListingRESTController.class).findById(listing.id().getId(), userId1)).withSelfRel());
                dto.add(links);

                allDTO.add(dto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(allDTO);
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @GetMapping("/direct-sale/{genreId}")
    public ResponseEntity<List<ListingResponseDTO>> getAllDirectSaleByGenre(@PathVariable String genreId, @RequestParam String userId) {

        try {
            GenreId genId = ListingMapper.toGenreId(genreId);
            UserId userId1 = ListingMapper.toUserId(userId);

            List<BibliographicalDataId> genreBiblioData = biblioDataService.findAllByGenreId(genId);
            List<PublicationSampleId> genrePublicationSamples = publicationService.findIdsByBibliographicalDataIdIn(genreBiblioData);
            List<Listing> genreListings = listingService.findAllDirectSaleAndPublicationSampleIdIn(genrePublicationSamples);

            List<ListingResponseDTO> dtos = new ArrayList<>();
            for (Listing listing : genreListings) {
                List<BibliographicalData> bibliographicalDatas = new ArrayList<>();

                for (PublicationSampleId pubSampleId : listing.getItemsId()) {
                    PublicationSample pubSample = publicationService.findById(pubSampleId);
                    BibliographicalData bd = biblioDataService.findById(pubSample.getBibliographicalDataID());
                    bibliographicalDatas.add(bd);
                }
                ListingResponseDTO dto = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

                List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userId1));
                links.add(linkTo(methodOn(ListingRESTController.class).getAllDirectSaleByGenre(genreId, userId)).withSelfRel());
                dto.add(links);

                dtos.add(dto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/direct-sale/{publisher-id}")
    public ResponseEntity<List<ListingResponseDTO>> getAllDirectSaleByPublisher() {

        return null;

    }

    private List<BibliographicalData> getBibliographicalData(List<PublicationSample> pubSamples) throws InstanceNotFoundException {
        List<BibliographicalData> bibliographicalDatas = new ArrayList<>();

        for (PublicationSample pubSample : pubSamples) {
            BibliographicalData bd = biblioDataService.findById(
                    pubSample.getBibliographicalDataID()
            );
            bibliographicalDatas.add(bd);
        }
        return bibliographicalDatas;
    }

    private List<PublicationSample> getPublicationSamples(Listing listing) {

        List<PublicationSample> pubSamples = new ArrayList<>();

        for (PublicationSampleId publicationSampleId : listing.getItemsId()) {
            PublicationSample pubSample = publicationService.findById(publicationSampleId);
            pubSamples.add(pubSample);
        }
        return pubSamples;
    }

}
