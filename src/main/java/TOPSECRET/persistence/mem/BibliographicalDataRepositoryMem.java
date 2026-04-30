package TOPSECRET.persistence.mem;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreId;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * In-memory implementation of {@link IBibliographicalDataRepository}.
 * <p>
 * This repository stores {@link BibliographicalData} instances in an internal list and
 * provides operations for creating and retrieving bibliographical data records.
 * </p>
 * <p>
 * Bibliographical data records are created through a {@link BibliographicalDataFactory}
 * and are kept only during the application's runtime.
 * </p>
 */

public class BibliographicalDataRepositoryMem implements IBibliographicalDataRepository {

    private BibliographicalDataFactory _bibliographicalDataFactory;

    private final List<BibliographicalData> _bdList;

    // Constructor -----------------------------------------------------------------------------------------------------

    /**
     * Constructs an in-memory bibliographical data repository with the given factory
     * and initial list of bibliographical data.
     *
     * @param bibliographicalDataFactory the factory used to create {@link BibliographicalData} instances
     * @param bdList the initial list of bibliographical data to populate the repository
     */
    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory, List<BibliographicalData> bdList) {
        _bdList = new ArrayList<>(bdList);// throws automatically NullPointerException if bdList = null
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }


    /**
     * Constructs an empty in-memory bibliographical data repository with the given factory.
     *
     * @param bibliographicalDataFactory the factory used to create {@link BibliographicalData} instances
     */
    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory) {
        _bdList = new ArrayList<>();
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BibliographicalData createAndAdd(String title, List<AuthorId> authors, Locale originalLanguage, LocalDate publicationDate,
                                            List<GenreId> genres, String chunksOfText) throws InstanceAlreadyExistsException
    {
        if(title == null){
            throw new IllegalArgumentException("title cannot be null");
        }
        if(authors == null){
            throw new IllegalArgumentException("authors cannot be null");
        }
        if(genres == null){
            throw new IllegalArgumentException("genres cannot be null");
        }
        if(chunksOfText == null){
            throw new IllegalArgumentException("chunksOfText cannot be null");
        }

        BibliographicalData newBibliographicalData = _bibliographicalDataFactory.create(title, authors, originalLanguage, publicationDate, genres, chunksOfText);

        _bdList.add(newBibliographicalData);

        return newBibliographicalData;
    }

    /**
     * {@inheritDoc}
     */
    public Optional<BibliographicalData> findBibliographicalDataById(BibliographicalDataID bibliographicalDataId){

        if(bibliographicalDataId == null){
            throw new NullPointerException("BibliographicalDataId must not be null");
        }

        for(BibliographicalData bd : _bdList){
            if(bd.id().equals(bibliographicalDataId)){
                return Optional.of(bd);
            }
        }

        return Optional.empty();
    }


    /**
     * {@inheritDoc}
     */
    public Optional<BibliographicalDataID> findBibliographicalDataIdByTitle(String bdTitle){

        if(bdTitle == null){
            throw new NullPointerException("Title must not be null");
        }

        if(bdTitle.isBlank()){
            throw new IllegalArgumentException("Title must not be blank");
        }

        String normalizedBdTitle = bdTitle.toLowerCase().trim();

        for(BibliographicalData bd : _bdList){
            if(bd.getTitle().toLowerCase().trim().equals(normalizedBdTitle)){
                return Optional.of(bd.id());
            }
        }

        return Optional.empty();
    }
    public List<BibliographicalDataID> findByGenreId(GenreId genreId){

        if (genreId == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }

        List<BibliographicalDataID> result = new ArrayList<>();

        for (BibliographicalData bd : _bdList) {
            if (bd.getGenres().contains(genreId)) {
                result.add(bd.id());
            }
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<BibliographicalDataID> findBibliographicalDataIdByAuthorId(AuthorId authorId) {

        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null.");
        }

        List<BibliographicalDataID> bdsOfAuthor = _bdList.stream()
                .filter(bd -> bd.getAuthors().contains(authorId))
                .map(BibliographicalData::id)
                .toList();

        return bdsOfAuthor;
    }


    // -----------------------------------------------------------------------------------------------------------------



    // Get all publications --------------------------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public List<BibliographicalData> getAllPublicationsBD() {
        return List.copyOf(_bdList);
    }


    /**
     * {@inheritDoc}
     */
    public List<String> getAllTitles(){
        List<String> listOfTitles = new ArrayList<>();

        for(BibliographicalData bd: _bdList){
            String title = bd.getTitle();
            listOfTitles.add(title);
        }

        return listOfTitles;
    }

}


