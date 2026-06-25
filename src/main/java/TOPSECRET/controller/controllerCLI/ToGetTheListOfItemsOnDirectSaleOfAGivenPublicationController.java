package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.*;
import TOPSECRET.dto.US023ListingDTO;
import TOPSECRET.dto.US023ListingMapper;
import TOPSECRET.services.BibliographicalDataService;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller for US023: "As a Buyer, I want to get the list of items on direct sale
 * of a given publication."
 *
 * <p>
 * This controller provides functionality to:
 * <ul>
 *     <li>Retrieve all bibliographical data available in the system.</li>
 *     <li>Find all publication samples that are currently on direct sale for a
 *         specified publication.</li>
 * </ul>
 *
 * <p>
 * It interacts with the repositories for bibliographical data, listings, and
 * publication samples, and uses mappers to convert domain entities to DTOs for
 * presentation purposes.
 */
public class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    private final BibliographicalDataService _bdService;
    private final ListingService listingService;
    private final PublicationSampleService _pubSampleService;
    private final US023BibliographicalDataMapper _bdMapper;
    private final US023ListingMapper _listingMapper;

    // =================================================================================================================
    // ====== Constructor ==============================================================================================
    // =================================================================================================================

    /**
     * Constructs a new controller for managing the retrieval of bibliographical data
     * and publication samples on direct sale for US023.
     *
     * @param bdService         the service for bibliographical data
     * @param listingService    the service for listings
     * @param pubSampleService  the service for publication samples
     * @param bdMapper          the mapper for converting bibliographical data to DTOs
     * @param pubSampleMapper   the mapper for converting publication samples to DTOs
     */
    public ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(BibliographicalDataService bdService,
                                                                        ListingService listingService,
                                                                        PublicationSampleService pubSampleService,
                                                                        US023BibliographicalDataMapper bdMapper,
                                                                        US023ListingMapper pubSampleMapper) {
        if (bdService == null) {
            throw new IllegalArgumentException("bdService cannot be null");
        }
        if (listingService == null) {
            throw new IllegalArgumentException("listingService cannot be null");
        }
        if (pubSampleService == null) {
            throw new IllegalArgumentException("pubSampleService cannot be null");
        }
        if (bdMapper == null) {
            throw new IllegalArgumentException("bdMapper cannot be null");
        }
        if (pubSampleMapper == null) {
            throw new IllegalArgumentException("pubSampleMapper cannot be null");
        }

        this._bdService = bdService;
        this.listingService = listingService;
        this._pubSampleService= pubSampleService;
        this._bdMapper = bdMapper;
        this._listingMapper = pubSampleMapper;
    }

    // =================================================================================================================


    /**
     * Retrieves all bibliographical data available in the system and converts
     * them to DTOs.
     *
     * <p>
     * This method is part of US023 to allow the buyer to select a publication
     * for which they want to see items on direct sale.
     *
     * @return a list of {@link US023BibliographicalDataDTO} representing all
     * bibliographical data
     * @throws InstanceNotFoundException if there is an issue retrieving the data
     */
    public List<US023BibliographicalDataDTO> findAllBibliographicalData() throws InstanceNotFoundException {

        List<BibliographicalData> listOfAllPublicationBD = _bdService.findAll();

        return _bdMapper.toDTOs(listOfAllPublicationBD);
    }

    /**
     * Retrieves all direct‑sale listings that contain publication samples linked to the
     * given {@link BibliographicalDataId}, and converts them into {@link US023ListingDTO}.
     *
     * @param bdId the bibliographical data identifier
     * @return a list of listing DTOs
     * @throws InstanceNotFoundException if any Listing does not correspond to an existing Listing
     */
    public List<US023ListingDTO> findListOfItemsOnDirectSale(BibliographicalDataId bdId) throws InstanceNotFoundException {

        if (bdId == null) {
            throw new IllegalArgumentException("bdId cannot be null");
        }

        List<PublicationSampleId> listOfPubSample = _pubSampleService.findIdsByBibliographicalDataIdIn(List.of(bdId));

        List<Listing> listOfDSListings = listingService.findAllDirectSaleAndPublicationSampleIdIn(listOfPubSample);

        return _listingMapper.toDTOs(listOfDSListings);
    }
}
