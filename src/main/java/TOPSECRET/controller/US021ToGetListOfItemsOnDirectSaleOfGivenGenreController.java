package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US021DirectSaleDTO;
import TOPSECRET.dto.US021DirectSaleMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for US021 - get the list of items on direct sale of a given genre.
 */
public class US021ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private final IGenreRepository genreRepository;
    private final IListingRepository listingRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final US021DirectSaleMapper mapper;

    /**
     * Constructor.
     *
     * @param genreRepository genre repository
     * @param listingRepository listing repository
     * @param publicationSampleRepository publication sample repository
     * @param bibliographicalDataRepository bibliographical data repository
     * @param mapper mapper to convert Listing to DTO
     */
    public US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
            IGenreRepository genreRepository,
            IListingRepository listingRepository,
            IPublicationSampleRepository publicationSampleRepository,
            IBibliographicalDataRepository bibliographicalDataRepository,
            US021DirectSaleMapper mapper) {

        if (genreRepository == null || listingRepository == null ||
                publicationSampleRepository == null || bibliographicalDataRepository == null ||
                mapper == null) {
            throw new IllegalArgumentException("Repositories and Mapper cannot be null");
        }

        this.genreRepository = genreRepository;
        this.listingRepository = listingRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.mapper = mapper;
    }

    /**
     * Returns all available genres.
     *
     * @return list of all genres
     */
    // Gets all the genres so that the user chooses
    public List<Genre> getAllGenres(){
        return genreRepository.getAllGenres();
    }


    /**
     * Returns the list of items on direct sale of a given genre.
     *
     * @param selectedGenre genre selected by the buyer
     * @return list of DTOs matching the given genre
     * @throws IllegalArgumentException if selectedGenre is null
     */
    public List<US021DirectSaleDTO> getListOfItemsOnDirectSale(Genre selectedGenre) {
        if (selectedGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        // Step 1: Get all the direct sale listings
        List<Listing> directSaleListings = listingRepository.findAllDirectSaleListings();
        List<Listing> resultList = new ArrayList<>();

        // Step 2: only get the listings that have the chosen genre
        for (Listing listing : directSaleListings) {
            for (PublicationSampleId pubSampleId : listing.getItemsId()) {

                // Step 2.1: get PublicationSample
                Optional<PublicationSample> psOpt = publicationSampleRepository.findPublicationSampleById(pubSampleId);
                if (psOpt.isEmpty()) {
                    continue;
                }

                // Step 2.2: get BibliographicalData
                BibliographicalDataID bdId = psOpt.get().getBibliographicalDataID();
                Optional<BibliographicalData> bdOpt = bibliographicalDataRepository.findBibliographicalDataById(bdId);
                if (bdOpt.isEmpty()) {
                    continue;
                }

                // Step 2.3: verifies if it has the genre
                if (bdOpt.get().hasGenre(selectedGenre)) {
                    resultList.add(listing);
                    break; // this break avoid duplicates, if various items of listing have the same genre
                }
            }
        }

        // Step 3: convert to DTOs and return
        return mapper.toDTOs(resultList);
    }
  }
