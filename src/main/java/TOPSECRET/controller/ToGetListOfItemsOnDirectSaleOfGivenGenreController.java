package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US021DirSaleListingDTO;
import TOPSECRET.dto.US021DirSaleListingMapper;
import TOPSECRET.dto.US021GenreDTO;
import TOPSECRET.dto.US021GenreMapper;

import java.util.List;

/**
 * Controller for US021 - get the list of items on direct sale of a given genre.
 */
public class ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private final IGenreRepository genreRepository;
    private final IListingRepository listingRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final US021DirSaleListingMapper mapper;

    /**
     * Constructor.
     *
     * @param genreRepository genre repository
     * @param listingRepository listing repository
     * @param publicationSampleRepository publication sample repository
     * @param bibliographicalDataRepository bibliographical data repository
     * @param mapper mapper to convert Listing to DTO
     */
    public ToGetListOfItemsOnDirectSaleOfGivenGenreController(
            IGenreRepository genreRepository,
            IListingRepository listingRepository,
            IPublicationSampleRepository publicationSampleRepository,
            IBibliographicalDataRepository bibliographicalDataRepository,
            US021DirSaleListingMapper mapper) {

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
     * Returns all available genres as DTOs.
     *
     * @return list of all genres
     */
    public List<US021GenreDTO> getAllGenres() {
        return US021GenreMapper.toDTOList(genreRepository.findAll());
    }


    /**
     * Returns the list of items on direct sale of a given genre.
     *
     * @param selectedGenre genre selected by the buyer
     * @return list of DTOs matching the given genre
     * @throws IllegalArgumentException if selectedGenre is null
     */
    public List<US021DirSaleListingDTO> getListOfItemsOnDirectSale(GenreId selectedGenre) {
        if (selectedGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        List<BibliographicalDataID> bdIds =
                bibliographicalDataRepository.findAllByGenreId(selectedGenre);

        List<PublicationSampleId> pubSampleIds =
                publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds);

        List<Listing> directSaleListings =
                listingRepository.findAllByDirectSaleAndPublicationSampleIdIn(pubSampleIds);

        return mapper.toDTOs(directSaleListings);
    }
  }
