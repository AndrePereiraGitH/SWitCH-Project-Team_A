package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.*;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

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

    private final IBibliographicalDataRepository _bdRepo;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;
    private final US023BibliographicalDataMapper _bdMapper;
    private final US023ListingMapper _listingMapper;

    // =================================================================================================================
    // ====== Constructor ==============================================================================================
    // =================================================================================================================

    /**
     * Constructs a new controller for managing the retrieval of bibliographical data
     * and publication samples on direct sale for US023.
     *
     * @param bdRepo          the repository for bibliographical data
     * @param listingRepo     the repository for listings
     * @param pubSampleRepo   the repository for publication samples
     * @param bdMapper        the mapper for converting bibliographical data to DTOs
     * @param pubSampleMapper the mapper for converting publication samples to DTOs
     */
    public ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(IBibliographicalDataRepository bdRepo,
                                                                        IListingRepository listingRepo,
                                                                        IPublicationSampleRepository pubSampleRepo,
                                                                        US023BibliographicalDataMapper bdMapper,
                                                                        US023ListingMapper pubSampleMapper) {
        if (bdRepo == null) {
            throw new IllegalArgumentException("bdRepo cannot be null");
        }
        if (listingRepo == null) {
            throw new IllegalArgumentException("listingRepo cannot be null");
        }
        if (pubSampleRepo == null) {
            throw new IllegalArgumentException("pubSampleRepo cannot be null");
        }
        if (bdMapper == null) {
            throw new IllegalArgumentException("bdMapper cannot be null");
        }
        if (pubSampleMapper == null) {
            throw new IllegalArgumentException("pubSampleMapper cannot be null");
        }

        this._bdRepo = bdRepo;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
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
    public List<US023BibliographicalDataDTO> getAllPublicationBD() throws InstanceNotFoundException {

        List<BibliographicalData> listOfAllPublicationBD = _bdRepo.findAll();

        List<US023BibliographicalDataDTO> listOfAllBibliographicalDataDTO = _bdMapper.toDTOs(listOfAllPublicationBD);

        return listOfAllBibliographicalDataDTO;
    }

    /**
     * Retrieves all direct‑sale listings that contain publication samples linked to the
     * given {@link BibliographicalDataId}, and converts them into {@link US023ListingDTO}.
     *
     * @param bdId the bibliographical data identifier
     * @return a list of listing DTOs
     * @throws InstanceNotFoundException if any ListingId does not correspond to an existing Listing
     */
    public List<US023ListingDTO> findListOfItemsOnDirectSale(BibliographicalDataId bdId) throws InstanceNotFoundException {

        if (bdId == null) {
            throw new IllegalArgumentException("bdId cannot be null");
        }

        List<PublicationSampleId> listOfPubSample = _pubSampleRepo.findIdsByBibliographicalDataId(bdId);

        List<ListingId> listOfDSListingIds = _listingRepo.findIdsByPublicationSampleIdIn(listOfPubSample);

        List<US023ListingDTO> listOfListingDTO = _listingMapper.toDTOs(listOfDSListingIds);

        return listOfListingDTO;
    }
}
