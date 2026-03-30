package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.dto.*;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.repository.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import java.util.List;

public class US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    private final IBibliographicalDataRepository _bdRepo;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;

    // ====== Constructor ==============================================================================================
    public US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(IBibliographicalDataRepository bdRepo,
                                                                             IListingRepository listingRepo,
                                                                             IPublicationSampleRepository pubSampleRepo) {
        this._bdRepo = bdRepo;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
    }

    // =================================================================================================================

    public List<BibliographicalDataDTO_US023> getAllPublicationBD() {

        // Gets all the bibliographicalData on repository
        List<BibliographicalData> listOfAllPublicationBD = _bdRepo.getAllPublicationsBD();

        // Converts to DTOs
        List<BibliographicalDataDTO_US023> listOfAllBibliographicalDataDTO = BibliographicalDataDTO_US023.toDTOs(listOfAllPublicationBD);

        return listOfAllBibliographicalDataDTO;
    }


    public List<PublicationSampleDTO_US023> findListOfItemsOnDirectSale(BibliographicalData publication) {

        // Gets all the listings for direct sale
        List<Listing> listOfListings = _listingRepo.findAllDirectSaleListings();

        // Gets all the publication samples related to the publication
        List<PublicationSample> listOfItems = _pubSampleRepo.findItemsByListingAndPublication(listOfListings, publication);

        // Converts to DTOs
        List<PublicationSampleDTO_US023> listOfPublicationSampleDTO = PublicationSampleDTO_US023.toDTOs(listOfItems);

        return listOfPublicationSampleDTO;
    }
}
