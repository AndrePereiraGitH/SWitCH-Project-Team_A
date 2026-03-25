package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.dto.AuctionItemDTO_US024;
import TOPSECRET.dto.BibliographicalDataDTO_US024;
import TOPSECRET.repository.BibliographicalDataRepositoryMem;
import TOPSECRET.repository.IAuctionRepository;

import java.util.List;

/**
 * Controller responsible for handling the retrieval of auction items
 * associated with a given publication (US024).
 *
 * <p>This controller acts as an intermediary between the UI layer and the
 * repositories, coordinating the fetching of bibliographical data and
 * auction items, and converting domain objects into DTOs for presentation.</p>
 */
public class US024GetTheListOfItemsOnAuctionOfAGivenPublicationController {

    private BibliographicalDataRepositoryMem _bdRepo;
    private IAuctionRepository _aucRepo;

    /**
     * Constructs a new instance of the controller with the required repositories.
     *
     * @param _bdRepo  the repository used to access bibliographical data of publications
     * @param _aucRepo the repository used to access auction-related data
     */
    public US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(
            BibliographicalDataRepositoryMem _bdRepo,
            IAuctionRepository _aucRepo) {
        this._bdRepo = _bdRepo;
        this._aucRepo = _aucRepo;
    }

    /**
     * Retrieves the bibliographical data of all available publications.
     *
     * <p>Fetches all publications from the bibliographical data repository
     * and converts them into a list of DTOs suitable for presentation.</p>
     *
     * @return a {@link List} of {@link BibliographicalDataDTO_US024} representing
     *         all publications and their bibliographical information;
     *         returns an empty list if no publications are found
     */
    public List<BibliographicalDataDTO_US024> getAllPublicationBD() {

        List<BibliographicalData> listOfAllPublicationBD = _bdRepo.getAllPublicationsBD();

        List<BibliographicalDataDTO_US024> listOfAllBiblioDataDTO =
                BibliographicalDataDTO_US024.toDTOS(listOfAllPublicationBD);

        return listOfAllBiblioDataDTO;
    }

    /**
     * Retrieves the list of auction items associated with a given publication.
     *
     * <p>Uses the provided publication identifier to query the auction repository
     * for all samples of that publication currently on auction, then converts
     * the results into a list of DTOs for presentation.</p>
     *
     * @param bdPublicationDTO the identifier of the publication whose auction
     *                         items are to be retrieved; must not be {@code null}
     * @return a {@link List} of {@link AuctionItemDTO_US024} representing the
     *         auction items for the specified publication;
     *         returns an empty list if no items are found
     */
    public List<AuctionItemDTO_US024> getListOfItemsOnAuctionByGivenPublicationDTO(
            String bdPublicationDTO) {

        if(bdPublicationDTO == null){
            throw new NullPointerException("The publication can not be null");
        }

        if(bdPublicationDTO.isEmpty()){
            throw new IllegalArgumentException("The publication can not be empty");
        }

        List<PublicationSample> listOfItemsOnAuctionByPublication =
                _aucRepo.getListOfItemsOnAuctionByGivenPublication(bdPublicationDTO);

        List<AuctionItemDTO_US024> listAuctionItemDTO =
                AuctionItemDTO_US024.toDTOs(listOfItemsOnAuctionByPublication);

        return listAuctionItemDTO;
    }
}