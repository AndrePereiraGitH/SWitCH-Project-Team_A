package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuctionLinkProvider;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.auction.AuctionMapper;
import TOPSECRET.dto.auction.request.CreateAuctionDTO;
import TOPSECRET.dto.auction.request.PlaceBidDTO;
import TOPSECRET.dto.auction.response.AuctionDTO;
import TOPSECRET.services.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing {@link Auction} resources.
 *
 * <p>Exposes endpoints for creating auctions, placing bids, and querying auctions
 * by different bibliographical criteria (genre, author, publisher, publication sample).</p>
 *
 * <p>All responses include HATEOAS links based on the requesting user's permissions,
 * as defined by {@link AuctionLinkProvider}.</p>
 *
 * <p>Authorization is enforced at the service layer via the auction authorization policy.
 * This controller is a temporary solution — authentication will be handled by a dedicated
 * security framework in a future sprint.</p>
 *
 * @see AuctionService
 * @see AuctionLinkProvider
 */
@RestController
@Tag(name = "Auctions", description = "Auction management endpoints")
@RequestMapping(path = "/auctions")
@AllArgsConstructor
public class AuctionRESTController {

    private final AuctionService auctionService;
    private final ListingService listingService;
    private final PublicationSampleService pubSampleService;
    private final BibliographicalDataService bdService;
    private final EditionService editionService;
    private final AuctionLinkProvider auctionLinkProvider;

    /**
     * Creates a new auction and returns the persisted resource with HATEOAS links.
     *
     * @param requestDTO the inbound DTO containing the auction creation data; must not be {@code null}
     * @param userId     the identifier of the user requesting the operation
     * @return {@code 201 Created} with the created {@link AuctionDTO} and HATEOAS links,
     *         {@code 400 Bad Request} if the request data is invalid,
     *         {@code 403 Forbidden} if the user lacks permission,
     *         {@code 404 Not Found} if the listing does not exist,
     *         {@code 500 Internal Server Error} for unexpected errors
     */
    @PostMapping
    public ResponseEntity<AuctionDTO> createAuction(@RequestBody @Valid CreateAuctionDTO requestDTO,
                                                    @RequestParam String userId){

        try{
            ListingId listingId = AuctionMapper.toListingId(requestDTO.listingId());
            Price initialPrice = AuctionMapper.toPrice(requestDTO.initialPrice().toString());
            Price buyoutPrice = AuctionMapper.toPrice(requestDTO.buyoutPrice().toString());
            UserId sellerId = AuctionMapper.toUserId(userId);

            Listing listing = listingService.findById(listingId);

            Auction auction = auctionService.createAndSave(
                    listingId,
                    requestDTO.startDateTime(),
                    requestDTO.endDateTime(),
                    initialPrice,
                    buyoutPrice,
                    sellerId);

            AuctionDTO responseDTO = AuctionMapper.toDTO(auction, listing);

            List<Link> links = auctionLinkProvider.getLinksForAuction(sellerId);

            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .createAuction(requestDTO, userId))
                    .withSelfRel());

            responseDTO.add(links);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Places a bid on an existing auction.
     *
     * @param auctionId  the identifier of the auction to bid on
     * @param requestDTO the inbound DTO containing the bid value; must not be {@code null}
     * @param bidderId   the identifier of the user placing the bid
     * @return {@code 204 No Content} if the bid was placed successfully,
     *         {@code 400 Bad Request} if the bid value is invalid or violates bidding rules,
     *         {@code 403 Forbidden} if the user lacks permission,
     *         {@code 404 Not Found} if the auction or listing does not exist,
     *         {@code 500 Internal Server Error} for unexpected errors
     */
    @PostMapping("/{auctionId}/bids")
    public ResponseEntity<Void> placeBid(@PathVariable String auctionId,
                                         @RequestBody @Valid PlaceBidDTO requestDTO,
                                         @RequestParam String bidderId) {
        try {
            AuctionId id = AuctionMapper.toAuctionId(auctionId);
            UserId bidderUserId = AuctionMapper.toUserId(bidderId);
            Price value = AuctionMapper.toPrice(requestDTO.value().toString());

            auctionService.placeBid(id, bidderUserId, value);

            return ResponseEntity.noContent().build();

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Retrieves a list of auctions, optionally filtered by genre, author, publisher,
     * or bibliographical data, and optionally sorted by auction deadline.
     *
     * <p>The filtering chain varies by criterion:</p>
     * <ul>
     *   <li><b>genre</b> — genre → bibliographical data → publication samples → listings → auctions</li>
     *   <li><b>author</b> — author → bibliographical data → publication samples → listings → auctions</li>
     *   <li><b>publisher</b> — publisher → editions → publication samples → listings → auctions</li>
     *   <li><b>bibliographical data</b> — bibliographical data -> publication sample → listings → auctions</li>
     *   <li><b>none</b> — returns all auctions</li>
     * </ul>
     *
     * <p>The {@code sortByDeadline} parameter is only applicable when filtering by genre.
     * For all other filters, results are returned in default order.</p>
     *
     * <p>Each returned {@link AuctionDTO} includes HATEOAS links based on the requesting
     * user's permissions, as well as a self-referential link.</p>
     *
     * @param genreId          the genre identifier to filter by; optional
     * @param authorId         the author identifier to filter by; optional
     * @param publisherId      the publisher identifier to filter by; optional
     * @param bibliographicalDataId    the bibliographical data identifier to filter by; optional
     * @param sortByDeadline whether to sort results by auction deadline ascending;
     *                       only applicable when filtering by genre; defaults to {@code false}
     * @param userId         the identifier of the requesting user
     * @return {@code 200 OK} with the list of {@link AuctionDTO} and HATEOAS links,
     *         {@code 400 Bad Request} if any parameter is invalid,
     *         {@code 404 Not Found} if a referenced listing does not exist,
     *         {@code 500 Internal Server Error} for unexpected errors
     */
    @GetMapping
    public ResponseEntity<List<AuctionDTO>> getAuctions(@RequestParam(required = false) String genreId,
                                                        @RequestParam(required = false) String authorId,
                                                        @RequestParam(required = false) String publisherId,
                                                        @RequestParam(required = false) String bibliographicalDataId,
                                                        @RequestParam(required = false, defaultValue = "false") boolean sortByDeadline,
                                                        @RequestParam String userId) {
        try {
            UserId id = AuctionMapper.toUserId(userId);
            List<Auction> auctions;

            if (genreId != null) {
                GenreId genreIdVO = AuctionMapper.toGenreId(genreId);
                List<BibliographicalDataId> bdIds = bdService.findAllByGenreId(genreIdVO);
                List<PublicationSampleId> pubSampleIds = pubSampleService.findIdsByBibliographicalDataIdIn(bdIds);
                List<Listing> listings = listingService.findByAuctionAndPublicationSampleIdIn(pubSampleIds);
                List<ListingId> listingIds = listings.stream().map(Listing::id).toList();
                auctions = sortByDeadline
                        ? auctionService.findByListingIdInOrderByAuctionDeadlineAsc(listingIds)
                        : auctionService.findByListingIdIn(listingIds);

            } else if (authorId != null) {
                AuthorId authorIdVO = AuctionMapper.toAuthorId(authorId);
                List<BibliographicalDataId> bdIds = bdService.findIdByAuthorId(authorIdVO);
                List<PublicationSampleId> pubSampleIds = pubSampleService.findIdsByBibliographicalDataIdIn(bdIds);
                List<Listing> listings = listingService.findByAuctionAndPublicationSampleIdIn(pubSampleIds);
                List<ListingId> listingIds = listings.stream().map(Listing::id).toList();
                auctions = auctionService.findByListingIdIn(listingIds);

            } else if (publisherId != null) {
                PublisherID publisherIdVO = AuctionMapper.toPublisherId(publisherId);
                List<EditionId> editionIds = editionService.findIdsByPublisherId(publisherIdVO);
                List<PublicationSampleId> pubSampleIds = pubSampleService.findIdsByEditionIdIn(editionIds);
                List<Listing> listings = listingService.findByAuctionAndPublicationSampleIdIn(pubSampleIds);
                List<ListingId> listingIds = listings.stream().map(Listing::id).toList();
                auctions = auctionService.findByListingIdIn(listingIds);

            } else if (bibliographicalDataId != null) {
                BibliographicalDataId bdIdVO = AuctionMapper.toBibliographicalDataId(bibliographicalDataId);
                List<PublicationSampleId> pubSampleIds = pubSampleService.findIdsByBibliographicalDataId(bdIdVO);
                List<Listing> listings = listingService.findByAuctionAndPublicationSampleIdIn(pubSampleIds);
                List<ListingId> listingIds = listings.stream().map(Listing::id).toList();
                auctions = auctionService.findByListingIdIn(listingIds);

            } else {
                auctions = auctionService.findAll();
            }

            Map<ListingId, Listing> listingMap = auctions.stream()
                    .collect(Collectors.toMap(
                            Auction::getListingId,
                            auction -> {
                                return listingService.findById(auction.getListingId());
                            },
                            (existing, replacement) -> existing
                    ));

            List<AuctionDTO> responseDTOs = AuctionMapper.toDTOs(auctions, listingMap);

            List<Link> links = auctionLinkProvider.getLinksForAuction(id);

            responseDTOs.forEach(dto -> {
                dto.add(links);
                dto.add(linkTo(methodOn(AuctionRESTController.class)
                        .getAuctions(genreId, authorId, publisherId, bibliographicalDataId, sortByDeadline, userId))
                        .withSelfRel());
            });

            return ResponseEntity.ok(responseDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
