package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US027ListingDTO;
import TOPSECRET.dto.US027ListingMapper;
import TOPSECRET.dto.US027PublisherDTO;
import TOPSECRET.dto.US027PublisherMapper;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.services.PublisherService;

import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for US027 — Retrieve all items on direct sale for a given publisher.
 *
 * <p>
 * This controller orchestrates the interaction between repositories and DTO mappers in order to:
 * <ul>
 *     <li>List all publishers available in the system</li>
 *     <li>Retrieve all listings that correspond to direct sale for a specific publisher</li>
 * </ul>
 * </p>
 *
 * <p>
 * It does not contain domain logic; instead, it coordinates domain operations by:
 * <ul>
 *     <li>Querying repositories for domain entities and identifiers</li>
 *     <li>Delegating transformation of domain objects into DTOs to the appropriate mappers</li>
 * </ul>
 * </p>
 */
public class ToGetListOfItemsOnDirectSaleOfGivenPublisherController {

    private final PublisherService _publisherService;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;
    private final IEditionRepository _editionRepo;
    private final US027ListingMapper _mapper;

    // =================================================================================================================
    // ========= Constructor ===========================================================================================
    // =================================================================================================================
    /**
     * Creates a new controller responsible for retrieving publishers and direct-sale listings
     * associated with a given publisher.
     *
     * @param publisherService the repository used to retrieve publishers
     * @param listingRepo the repository used to retrieve listings
     * @param pubSampleRepo the repository used to retrieve publication samples
     * @param editionRepo the repository used to retrieve editions
     * @param mapper the mapper responsible for converting listings into DTOs
     */
    public ToGetListOfItemsOnDirectSaleOfGivenPublisherController(PublisherService publisherService,
                                                                  IListingRepository listingRepo,
                                                                  IPublicationSampleRepository pubSampleRepo,
                                                                  IEditionRepository editionRepo,
                                                                  US027ListingMapper mapper) {

        this._publisherService = publisherService;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
        this._editionRepo = editionRepo;
        this._mapper = mapper;
    }

    // ================================================================================================================

    /**
     * Retrieves all publishers stored in the system and converts them into DTOs.
     *
     * @return a list of {@link US027PublisherMapper} representing all publishers
     */
    public List<US027PublisherDTO> findAllPublishers() throws AccessDeniedException {

        List<Publisher> listOfAllPublishers = _publisherService.findAll();

        return US027PublisherMapper.toDTOs(listOfAllPublishers);
    }

    /**
     * Retrieves all items on direct sale associated with the given publisher.
     *
     * <p>
     * The retrieval process follows these steps:
     * <ol>
     *     <li>Find all {@link EditionId} values belonging to the given publisher</li>
     *     <li>Find all {@link PublicationSampleId} values associated with those editions</li>
     *     <li>Retrieve all direct-sale {@link Listing} objects that reference those publication samples</li>
     *     <li>Convert the resulting listings into DTOs</li>
     * </ol>
     * </p>
     *
     * @param publisherId the unique identifier of the publisher whose direct-sale items should be retrieved
     * @return a list of {@link US027ListingDTO} representing the direct-sale listings of the given publisher
     * @throws InstanceNotFoundException if the publisher does not exist or no related data is found
     */
    public List<US027ListingDTO> findListOfItemsOnDirectSaleByPublisherId(PublisherID publisherId) throws IllegalArgumentException, InstanceNotFoundException {

        if (publisherId == null) throw new IllegalArgumentException("publisherId cannot be null");

        List<EditionId> listOfEditionIds = _editionRepo.findIdsByPublisherId(publisherId);

        List<PublicationSampleId> listOfPubSampleIds = _pubSampleRepo.findIdsByEditionIdIn(listOfEditionIds);

        List<Listing> listOfListings = _listingRepo.findAllByDirectSaleAndPublicationSampleIdIn(listOfPubSampleIds);

        return _mapper.toDTOs(listOfListings);
    }
}