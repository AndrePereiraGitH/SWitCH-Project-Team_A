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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing {@link Auction} resources.
 *
 * <p>Exposes endpoints for creating auctions, placing bids, retrieving a single auction
 * by identifier, and querying auctions by different bibliographical criteria
 * (genre, author, publisher, bibliographical data).</p>
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

            Auction auction = auctionService.createAndSave(listingId, requestDTO.startDateTime(), requestDTO.endDateTime(),
                    initialPrice, buyoutPrice, sellerId);

            Listing listing = listingService.findById(listingId);
            AuctionDTO responseDTO = AuctionMapper.toDTO(auction, listing);

            List<Link> links = auctionLinkProvider.getPublicLinks();

            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .getAuctionById(auction.id().getId(), userId))
                    .withSelfRel());

            links.forEach(responseDTO::add);

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
     * bibliographical data, or listing, and optionally sorted by auction deadline.
     *
     * <p>Filtering logic is delegated to {@link AuctionService#findAuctions}, which
     * resolves the appropriate chain of repositories based on the provided criterion.</p>
     *
     * <p>The {@code sortByDeadline} parameter is only applicable when filtering by genre.
     * For all other filters, results are returned in default order.</p>
     *
     * <p>Each returned {@link AuctionDTO} includes HATEOAS links based on the requesting
     * user's permissions, as well as a self-referential link pointing to
     * {@code GET /auctions/{id}}.</p>
     *
     * @param genreId               the genre identifier to filter by; optional
     * @param authorId              the author identifier to filter by; optional
     * @param publisherId           the publisher identifier to filter by; optional
     * @param bibliographicalDataId the bibliographical data identifier to filter by; optional
     * @param listingId             the listing identifier to filter by; optional
     * @param sortByDeadline        whether to sort results by auction deadline ascending;
     *                              only applicable when filtering by genre; defaults to {@code false}
     * @return {@code 200 OK} with the list of {@link AuctionDTO} and HATEOAS links,
     *         {@code 400 Bad Request} if any parameter is invalid,
     *         {@code 404 Not Found} if a referenced listing does not exist,
     *         {@code 500 Internal Server Error} for unexpected errors
     */
    @GetMapping
    public ResponseEntity<List<AuctionDTO>> getAuctions(
            @RequestParam(required = false) String genreId,
            @RequestParam(required = false) String authorId,
            @RequestParam(required = false) String publisherId,
            @RequestParam(required = false) String bibliographicalDataId,
            @RequestParam(required = false) String listingId,
            @RequestParam(required = false, defaultValue = "false") boolean sortByDeadline) {

        try {
            List<Auction> auctions = auctionService.findAuctions(
                    genreId != null
                            ? AuctionMapper.toGenreId(genreId)
                            : null,
                    authorId != null
                            ? AuctionMapper.toAuthorId(authorId)
                            : null,
                    publisherId != null
                            ? AuctionMapper.toPublisherId(publisherId)
                            : null,
                    bibliographicalDataId != null
                            ? AuctionMapper.toBibliographicalDataId(bibliographicalDataId)
                            : null,
                    listingId != null
                            ? AuctionMapper.toListingId(listingId)
                            : null,
                    sortByDeadline);

            Map<ListingId, Listing> listingMap = auctions.stream()
                    .collect(Collectors.toMap(
                            Auction::getListingId,
                            auction -> listingService.findById(auction.getListingId()),
                            (existing, replacement) -> existing));

            List<AuctionDTO> responseDTOs = AuctionMapper.toDTOs(auctions, listingMap);

            responseDTOs.forEach(dto -> {
                dto.add(linkTo(methodOn(AuctionRESTController.class)
                        .getAuctionById(dto.getAuctionId(), null))
                        .withSelfRel());
                dto.add(auctionLinkProvider.getPublicLinks());
            });

            return ResponseEntity.ok(responseDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single auction by its unique identifier.
     *
     * <p>Returns the {@link AuctionDTO} representation of the auction, enriched with
     * HATEOAS links based on the requesting user's permissions, as well as a
     * self-referential link identifying the resource.</p>
     *
     * @param auctionId the unique identifier of the auction to retrieve
     * @param userId    the identifier of the requesting user; optional —
     *                  if provided, private HATEOAS links are included based on the user's permissions
     * @return {@code 200 OK} with the {@link AuctionDTO} and HATEOAS links,
     *         {@code 400 Bad Request} if the {@code auctionId} is invalid,
     *         {@code 404 Not Found} if the auction or its associated listing does not exist,
     *         {@code 500 Internal Server Error} for unexpected errors
     */
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionDTO> getAuctionById(@PathVariable String auctionId,
                                                     @RequestParam(required = false) String userId) {

        try {
            AuctionId auctionIdVO = AuctionMapper.toAuctionId(auctionId);
            Auction auction = auctionService.findById(auctionIdVO);
            Listing listing = listingService.findById(auction.getListingId());

            AuctionDTO responseDTO = AuctionMapper.toDTO(auction, listing);

            responseDTO.add(linkTo(methodOn(AuctionRESTController.class)
                    .getAuctionById(auctionId, userId))
                    .withSelfRel());

            responseDTO.add(auctionLinkProvider.getPublicLinks());

            if (userId != null) {
                UserId userIdVO = AuctionMapper.toUserId(userId);
                responseDTO.add(auctionLinkProvider.getPlaceBidLink(responseDTO, userIdVO));
            }

            return ResponseEntity.ok(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles OPTIONS requests for the auctions root endpoint ({@code /auctions}).
     *
     * <p>Returns the HTTP methods allowed for this endpoint in the {@code Allow} response
     * header, enabling CORS preflight requests and API discoverability. {@code GET} and
     * {@code OPTIONS} are always allowed; {@code POST} is included only when the requesting
     * user (if identified) has permission to create an auction. If no {@code userId} is
     * provided, the request is treated as anonymous and {@code POST} is omitted.</p>
     *
     * @param userId the identifier of the requesting user; may be {@code null} for
     *               anonymous/unregistered users
     * @return {@code 200 OK} with the {@code Allow} header set to {@code GET, OPTIONS},
     *         plus {@code POST} if the user is authorized to create an auction;
     *         {@code 404 Not Found} if the given {@code userId} does not match an existing user;
     *         {@code 400 Bad Request} if the given {@code userId} is invalid
     */
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot(@RequestParam(required = false) String userId) {

        Set<HttpMethod> allowedMethods = new HashSet<>(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

        try {
            if (userId != null) {
                UserId userIdVO = AuctionMapper.toUserId(userId);

                if (auctionService.canCreateAuction(userIdVO)) {
                    allowedMethods.add(HttpMethod.POST);
                }
            }

            return ResponseEntity.ok()
                    .allow(allowedMethods.toArray(new HttpMethod[0]))
                    .build();

        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles OPTIONS requests for the bid placement endpoint ({@code /auctions/{auctionId}/bids}).
     *
     * <p>Returns the HTTP methods allowed for this endpoint in the {@code Allow} response
     * header, enabling CORS preflight requests and API discoverability. {@code OPTIONS} is
     * always allowed; {@code POST} is included only when the requesting user (if identified)
     * has permission to place a bid. If no {@code userId} is provided, the request is treated
     * as anonymous and {@code POST} is omitted. This endpoint does not support {@code GET}.</p>
     *
     * @param userId    the identifier of the requesting user; may be {@code null} for
     *                  anonymous/unregistered users
     * @param auctionId the unique identifier of the auction; used for URL matching
     * @return {@code 200 OK} with the {@code Allow} header set to {@code OPTIONS},
     *         plus {@code POST} if the user is authorized to place a bid;
     *         {@code 404 Not Found} if the given {@code userId} does not match an existing user;
     *         {@code 400 Bad Request} if the given {@code userId} is invalid
     */
    @RequestMapping(value = "/{auctionId}/bids", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsBids(@RequestParam(required = false) String userId,
                                            @PathVariable String auctionId) {

        Set<HttpMethod> allowedMethods = new HashSet<>(List.of(HttpMethod.OPTIONS));

        try {
            if (userId != null) {
                UserId userIdVO = AuctionMapper.toUserId(userId);

                if (auctionService.canPlaceBid(userIdVO)) {
                    allowedMethods.add(HttpMethod.POST);
                }
            }

            return ResponseEntity.ok()
                    .allow(allowedMethods.toArray(new HttpMethod[0]))
                    .build();

        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
