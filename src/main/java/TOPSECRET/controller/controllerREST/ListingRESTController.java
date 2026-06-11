package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.ListingLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.request.ListingRequestDTO;
import TOPSECRET.dto.listing.response.ListingDetailsDTO;
import TOPSECRET.services.*;
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
    private final AuthorService authorService;
    private final GenreService genreService;
    private final ListingLinkProvider listingLinkProvider;
    private final PublisherService publisherService;

    private enum LinkContext {
        OWNER,
        PUBLIC
    }

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
    public ListingRESTController(BibliographicalDataService biblioDataService, PublicationSampleService publicationService, ListingService listingService, EditionService editionService, AuthorService authorService, GenreService genreService, ListingLinkProvider listingLinkProvider, PublisherService publisherService) {
        this.biblioDataService = biblioDataService;
        this.publicationService = publicationService;
        this.listingService = listingService;
        this.editionService = editionService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.listingLinkProvider = listingLinkProvider;
        this.publisherService = publisherService;
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
    public ResponseEntity<ListingDetailsDTO> createListing(@RequestBody ListingRequestDTO listingRequestDTO, @RequestParam String userId) {

        try {

            UserId userIdVO = ListingMapper.toUserId(userId);

            if (listingRequestDTO.getPublicationIds() == null || listingRequestDTO.getPublicationIds().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<PublicationSampleId> itemsId = listingRequestDTO.getPublicationIds().stream().map(ListingMapper::toPublicationId).toList();
            SaleType saleType = ListingMapper.toSaleType(listingRequestDTO.getSaleType());
            Price price = ListingMapper.toPrice(listingRequestDTO.getPrice());
            List<Picture> pictures = listingRequestDTO.getPictures() == null ? List.of() : listingRequestDTO.getPictures().stream().map(ListingMapper::toPicture).toList();

            Listing listing = listingService.createAndSave(
                    listingRequestDTO.getListingTitle(),
                    itemsId,
                    saleType,
                    price,
                    listingRequestDTO.getShortDescription(),
                    userIdVO,
                    pictures
            );

            ListingDetailsDTO dto = convertIntoListingDetailsDTO(listing, LinkContext.OWNER, userIdVO);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
     * @return {@code 200 OK} with the listing DTO,
     *         {@code 404 NOT_FOUND} if the listing, user, or related data is not found,
     *         {@code 400 BAD_REQUEST} if the identifier is invalid,
     *         or {@code 500 INTERNAL_SERVER_ERROR} for unexpected errors
     */
    @GetMapping("/{id}")
    public ResponseEntity<ListingDetailsDTO> findById(
            @PathVariable String id
    ) {
        try {
            ListingId listingId = ListingMapper.toListingId(id);

            Listing listing = listingService.findById(listingId);

            ListingDetailsDTO dto = convertIntoListingDetailsDTO(listing, LinkContext.PUBLIC, null);

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
     * @return {@code 200 OK} with the list of listing DTOs,
     *         {@code 404_NOT_FOUND} if the user or related data is not found,
     *         {@code 400_BAD_REQUEST} if the user identifier is invalid,
     *         or {@code 500_INTERNAL_SERVER_ERROR} for unexpected errors
     */
    @GetMapping("")
    public ResponseEntity<List<ListingDetailsDTO>> findAll() {

        try {
            List<Listing> allListings = listingService.findAll();

            List<ListingDetailsDTO> allDTO = new ArrayList<>();

            for (Listing listing : allListings) {
                allDTO.add(convertIntoListingDetailsDTO(listing, LinkContext.PUBLIC, null));
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
     * Retrieves all listings (direct sale and auction) associated with the given
     * bibliographical data identifier.
     *
     * <p>Resolves the publication samples linked to the bibliographical data entry,
     * then returns all listings that contain any of those samples, regardless of
     * sale type.</p>
     *
     * <p>No authentication is required — this endpoint is publicly accessible.</p>
     *
     * @param bibliographicalDataId the unique identifier of the bibliographical data
     *                              (publication) used to filter listings; must not be
     *                              {@code null} or blank
     * @return {@code 200 OK} with the list of matching {@link ListingDetailsDTO} objects,
     *         {@code 400 BAD REQUEST} if the identifier is invalid,
     *         {@code 404 NOT FOUND} if no related data is found,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/by-publication/{bibliographicalDataId}")
    public ResponseEntity<List<ListingDetailsDTO>> findByBibliographicalDataId
    (@PathVariable String bibliographicalDataId) {

        try{
            BibliographicalDataId bdId = ListingMapper.toBibliographicalDataId(bibliographicalDataId);

            List<PublicationSampleId> allPublicationIdsWithSpecificBdId = publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId));

            List<Listing> allListingsWithSpecificBdId = listingService.findByPublicationSampleIdIn(allPublicationIdsWithSpecificBdId);

            List<ListingDetailsDTO> dtos = new ArrayList<>();

            for (Listing listing : allListingsWithSpecificBdId) {
                dtos.add(convertIntoListingDetailsDTO(listing, LinkContext.PUBLIC, null));
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
     * Retrieves all direct-sale listings associated with a given genre.
     *
     * <p>This endpoint finds bibliographical data for the genre, retrieves the
     * related publication samples, searches for direct-sale listings containing
     * those samples, converts each listing to a response DTO, and adds the available
     * HATEOAS links for the requesting user.</p>
     *
     * @param genreId the identifier of the genre used to filter direct-sale listings
     * @return {@code 200 OK} with the list of matching listing DTOs,
     *         {@code 404_NOT_FOUND} if related data or the user is not found,
     *         or {@code 400_BAD_REQUEST} if one of the identifiers is invalid
     */
    @GetMapping("/direct-sale/genre/{genreId}")
    public ResponseEntity<List<ListingDetailsDTO>> getAllDirectSaleByGenre(@PathVariable String genreId) {

        try {
            GenreId genId = ListingMapper.toGenreId(genreId);

            List<BibliographicalDataId> genreBiblioData = biblioDataService.findAllByGenreId(genId);
            List<PublicationSampleId> genrePublicationSamples = publicationService.findIdsByBibliographicalDataIdIn(genreBiblioData);
            List<Listing> genreListings = listingService.findAllDirectSaleAndPublicationSampleIdIn(genrePublicationSamples);

            List<ListingDetailsDTO> dtos = new ArrayList<>();
            for (Listing listing : genreListings) {
                dtos.add(convertIntoListingDetailsDTO(listing, LinkContext.PUBLIC, null));
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
     * <p>This endpoint finds editions for the publisher, retrieves the related
     * publication samples, searches for direct-sale listings containing those
     * samples, converts each listing to a details DTO, and adds public HATEOAS
     * links.</p>
     *
     * @param publisherId the identifier of the publisher used to filter listings
     * @return {@code 200 OK} with the list of matching listing DTOs,
     *         {@code 404 NOT_FOUND} if related data is not found,
     *         {@code 400 BAD_REQUEST} if the publisher identifier is invalid,
     *         or {@code 500 INTERNAL_SERVER_ERROR} for unexpected errors
     */
    @GetMapping("/direct-sale/publisher/{publisherId}")
    public ResponseEntity<List<ListingDetailsDTO>> getAllDirectSaleByPublisher(@PathVariable String publisherId) {

        try {
            PublisherID publisherID = ListingMapper.toPublisherId(publisherId);
            List<EditionId> publisherEditions = editionService.findIdsByPublisherId(publisherID);
            List<PublicationSampleId> publisherPublications = publicationService.findIdsByEditionIdIn(publisherEditions);

            List<Listing> publisherListings = listingService.findAllDirectSaleAndPublicationSampleIdIn(publisherPublications);

            List<ListingDetailsDTO> dtos = new ArrayList<>();

            for (Listing listing : publisherListings) {

                dtos.add(convertIntoListingDetailsDTO(listing, LinkContext.PUBLIC, null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(dtos);

        } catch (NullPointerException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
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

    /**
     * Retrieves the publishers associated with the given editions.
     *
     * @param editions the editions whose publishers should be loaded
     * @return a list containing the distinct publishers associated with the editions
     */
    private List<Publisher> getPublishers(List<Edition> editions) {
        return editions.stream()
                .map(Edition::getPublisherID)
                .distinct()
                .map(publisherService::findById)
                .toList();
    }

    /**
     * Retrieves the editions associated with the given publication samples.
     *
     * @param pubSamples the publication samples whose editions should be loaded
     * @return a list containing the editions associated with the publication samples
     */
    private List<Edition> getEditions(List<PublicationSample> pubSamples) {
        return  pubSamples.stream()
                .map(pub -> editionService.findById(pub.getEditionID()))
                .toList();
    }

    /**
     * Retrieves the genres associated with the given bibliographical data.
     *
     * @param bibliographicalDatas the bibliographical data whose genres should be loaded
     * @return a list containing the distinct genres associated with the bibliographical data
     */
    private List<Genre> getGenres(List<BibliographicalData> bibliographicalDatas) {

        return bibliographicalDatas.stream()
                .flatMap(bd -> bd.getGenres().stream())
                .distinct()
                .map(genreService::findById)
                .toList();
    }

    /**
     * Retrieves the authors associated with the given bibliographical data.
     *
     * @param bibliographicalDatas the bibliographical data whose authors should be loaded
     * @return a list containing the distinct authors associated with the bibliographical data
     */
    private List<Author> getAuthors(List<BibliographicalData> bibliographicalDatas) {

        return bibliographicalDatas.stream()
                .flatMap(bd -> bd.getAuthors().stream())
                .distinct()
                .map(authorService::findById)
                .toList();
    }

    /**
     * Converts a listing into a detailed response DTO and adds the appropriate
     * HATEOAS links according to the requested link context.
     *
     * @param listing the listing to convert
     * @param context the link context that determines whether owner links are added
     * @param userId the owner user identifier, required only when using owner links
     * @return the listing details DTO with the corresponding HATEOAS links
     * @throws InstanceNotFoundException if related listing data cannot be found
     */
    private ListingDetailsDTO convertIntoListingDetailsDTO(Listing listing, LinkContext context, UserId userId) throws InstanceNotFoundException {

        List<PublicationSample> pubSamples = getPublicationSamples(listing);
        List<BibliographicalData> bibliographicalDatas = getBibliographicalData(pubSamples);
        List<Author> authors = getAuthors(bibliographicalDatas);
        List<Genre> genres = getGenres(bibliographicalDatas);
        List<Edition> editions = getEditions(pubSamples);
        List<Publisher> publishers = getPublishers(editions);

        ListingDetailsDTO dto = ListingMapper.toListingDetailsDTO(
                listing,
                pubSamples,
                editions,
                authors,
                genres,
                publishers
        );

        List<Link> links = new ArrayList<>(listingLinkProvider.getPublicLinks());

        if (context == LinkContext.OWNER && userId != null) {
            links.addAll(listingLinkProvider.getOwnerLinks(userId));

            if (listing.getSaleType().equals(SaleType.AUCTION)) {
                links.addAll(listingLinkProvider.getAuctionLinks(userId));
            }
        }

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findById(listing.id().getId()))
                .withSelfRel());

        links.forEach(dto::add);

        return dto;
    }

}