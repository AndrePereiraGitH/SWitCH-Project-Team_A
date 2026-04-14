package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.*;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
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
public class US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    private final IBibliographicalDataRepository _bdRepo;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;
    private final US023BibliographicalDataMapper _bdMapper;
    private final US023PublicationSampleMapper _pubSampleMapper;

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
    public US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(IBibliographicalDataRepository bdRepo,
                                                                             IListingRepository listingRepo,
                                                                             IPublicationSampleRepository pubSampleRepo,
                                                                             US023BibliographicalDataMapper bdMapper,
                                                                             US023PublicationSampleMapper pubSampleMapper) {
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
        this._pubSampleMapper = pubSampleMapper;
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

        List<BibliographicalData> listOfAllPublicationBD = _bdRepo.getAllPublicationsBD();

        List<US023BibliographicalDataDTO> listOfAllBibliographicalDataDTO = _bdMapper.toDTOs(listOfAllPublicationBD);

        return listOfAllBibliographicalDataDTO;
    }

    /**
     * Finds all publication samples that are currently on direct sale for the
     * specified publication of a given publication.
     *
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Retrieve all direct sale listings from the listing repository.</li>
     *     <li>Extract unique publication sample IDs from these listings.</li>
     *     <li>Query the publication sample repository for items matching the
     *     given publication ID and the extracted IDs.</li>
     *     <li>Convert the resulting publication samples to DTOs for presentation.</li>
     * </ol>
     *
     * @param bdId the ID of the publication for which to find direct sale items
     * @return a list of {@link US023PublicationSampleDTO} representing the publication
     * samples on direct sale
     * @throws InstanceNotFoundException if no publication samples are found or
     *                                   the repository encounters an error
     */
    public List<US023PublicationSampleDTO> findListOfItemsOnDirectSale(BibliographicalDataID bdId) throws InstanceNotFoundException {

        // Gets all the listings for direct sale
        List<Listing> listOfDSListings = _listingRepo.findAllDirectSaleListings();

        // Gets all the publicationIds for each listing
        // Later, this logic should be moved to an application service
        List<PublicationSampleId> listOfPubSampleIds = new ArrayList<>();

        for (Listing listing : listOfDSListings) {
            for (PublicationSampleId psId : listing.getItemsId()) {
                if (!listOfPubSampleIds.contains(psId)) { // to avoid duplicates
                    listOfPubSampleIds.add(psId);
                }
            }
        }

        // Gets all the publication samples related to the publicationId
        List<PublicationSample> listOfPubSample = _pubSampleRepo.findItemsByIdsAndPublication(listOfPubSampleIds, bdId);

        // Converts to DTOs
        List<US023PublicationSampleDTO> listOfPubSampleDTO = _pubSampleMapper.toDTOs(listOfPubSample);

        return listOfPubSampleDTO;
    }
}
