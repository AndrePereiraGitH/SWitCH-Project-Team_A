package TOPSECRET.controller.controllerREST;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for listing-related endpoints.
 *
 * <p>This controller exposes operations to create listings, retrieve listings
 * by identifier, retrieve all listings, and retrieve direct-sale listings by
 * genre. It coordinates the listing, publication sample, bibliographical data,
 * and HATEOAS link services to build complete listing response DTOs.</p>
 */
@RestController
@Tag(name = "Listings", description = "Listing management endpoints")
@RequestMapping(path = "/listings")
public class ListingRESTController {

    private final BibliographicalDataService biblioDataService;
    private final PublicationSampleService publicationService;
    private final ListingService listingService;
    private final EditionService editionService;
    private final ListingLinkProvider listingLinkProvider;

    /**
     * Creates a new {@code ListingRESTController}.
     *
     * @param biblioDataService the service used to retrieve bibliographical data
     * @param publicationService the service used to retrieve publication samples
     * @param listingService the service used to manage listings
     * @param editionService the service used to manage editions
     * @param listingLinkProvider the provider used to generate listing HATEOAS links
     */
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
    public ResponseEntity<ListingResponseDTO> createListing(@RequestBody ListingRequestDTO listingRequestDTO, @RequestParam String userId) {

        UserId userIdVO = ListingMapper.toUserId(userId);
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
                    userIdVO,
                    pictures
            );

            List<PublicationSample> publicationSamples = itemsId.stream().map(publicationService::findById).toList();

            List<BibliographicalData> biblioDatas = getBibliographicalData(publicationSamples);

            ListingResponseDTO result = ListingMapper.toResponseDTO(listing, biblioDatas);

            List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userIdVO));
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

    /**
     * Finds a listing by its identifier.
     *
     * <p>This endpoint retrieves the listing, loads the related publication samples
     * and bibliographical data, converts the result to a response DTO, and adds the
     * available HATEOAS links for the requesting user.</p>
     *
     * @param id the identifier of the listing to retrieve
     * @param userId the identifier of the user requesting the listing
     * @return {@code 200 OK} with the listing DTO,
     *         {@code 404 NOT_FOUND} if the listing, user, or related data is not found,
     *         {@code 400 BAD_REQUEST} if the identifier is invalid,
     *         or {@code 500 INTERNAL_SERVER_ERROR} for unexpected errors
     */
    @GetMapping("/{id}")
    public ResponseEntity<ListingResponseDTO> findById(@PathVariable String id, @RequestParam String userId) {
        ListingId listingId = ListingMapper.toListingId(id);
        UserId userIdVO = ListingMapper.toUserId(userId);

        try {
            Listing listing = listingService.findById(listingId);

            List<PublicationSample> pubSamples = listing.getItemsId().stream()
                    .map(publicationService::findById)
                    .toList();

            List<BibliographicalData> bibliographicalDatas = getBibliographicalData(pubSamples);

            ListingResponseDTO dto = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

            List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userIdVO));
            links.add(linkTo(methodOn(ListingRESTController.class).findById(id, userId)).withSelfRel());
            dto.add(links);

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Retrieves all listings available in the system.
     *
     * <p>For each listing, this endpoint loads its publication samples and related
     * bibliographical data, converts the listing to a response DTO, and adds the
     * available HATEOAS links for the requesting user.</p>
     *
     * @param userId the identifier of the user requesting the listings
     * @return {@code 200 OK} with the list of listing DTOs,
     *         {@code 404_NOT_FOUND} if the user or related data is not found,
     *         {@code 400_BAD_REQUEST} if the user identifier is invalid,
     *         or {@code 500_INTERNAL_SERVER_ERROR} for unexpected errors
     */
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
                links.add(linkTo(methodOn(ListingRESTController.class).findById(listing.id().getId(), userId)).withSelfRel());
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

    /**
     * Retrieves all direct-sale listings associated with a given genre.
     *
     * <p>This endpoint finds bibliographical data for the genre, retrieves the
     * related publication samples, searches for direct-sale listings containing
     * those samples, converts each listing to a response DTO, and adds the available
     * HATEOAS links for the requesting user.</p>
     *
     * @param genreId the identifier of the genre used to filter direct-sale listings
     * @param userId the identifier of the user requesting the listings
     * @return {@code 200 OK} with the list of matching listing DTOs,
     *         {@code 404_NOT_FOUND} if related data or the user is not found,
     *         or {@code 400_BAD_REQUEST} if one of the identifiers is invalid
     */
    @GetMapping("/direct-sale/genre/{genreId}")
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all direct-sale listings associated with a given publisher.
     *
     * @return currently {@code null}; this endpoint is not implemented yet
     */
    @GetMapping("/direct-sale/publisher/{publisherId}")
    public ResponseEntity<List<ListingResponseDTO>> getAllDirectSaleByPublisher(@PathVariable String publisherId, @RequestParam String userId) {

        try {
            UserId userId1 = ListingMapper.toUserId(userId);
            PublisherID publisherID = ListingMapper.toPublisherId(publisherId);
            List<EditionId> publisherEditions = editionService.findIdsByPublisherId(publisherID);
            List<PublicationSampleId> publisherPublications = publicationService.findIdsByEditionIdIn(publisherEditions);


            List<Listing> publisherListings = listingService.findAllDirectSaleAndPublicationSampleIdIn(publisherPublications);

            List<ListingResponseDTO> dtos = new ArrayList<>();

            for (Listing listing : publisherListings) {

                List<PublicationSample> pubs = getPublicationSamples(listing);
                List<BibliographicalData> bds = getBibliographicalData(pubs);

                ListingResponseDTO dto = ListingMapper.toResponseDTO(listing, bds);

                List<Link> links = new ArrayList<>(listingLinkProvider.getLinksForListings(userId1));
                links.add(linkTo(methodOn(ListingRESTController.class).getAllDirectSaleByPublisher(publisherId, userId)).withSelfRel());
                dto.add(links);

                dtos.add(dto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(dtos);

        } catch (NullPointerException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the bibliographical data associated with the given publication samples.
     *
     * @param pubSamples the publication samples whose bibliographical data should be loaded
     * @return a list containing the bibliographical data for each publication sample
     * @throws InstanceNotFoundException if any bibliographical data entry is not found
     */
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

    /**
     * Retrieves the publication samples associated with a listing.
     *
     * @param listing the listing whose publication samples should be loaded
     * @return a list containing the publication samples associated with the listing
     */
    private List<PublicationSample> getPublicationSamples(Listing listing) {

        List<PublicationSample> pubSamples = new ArrayList<>();

        for (PublicationSampleId publicationSampleId : listing.getItemsId()) {
            PublicationSample pubSample = publicationService.findById(publicationSampleId);
            pubSamples.add(pubSample);
        }
        return pubSamples;
    }

}