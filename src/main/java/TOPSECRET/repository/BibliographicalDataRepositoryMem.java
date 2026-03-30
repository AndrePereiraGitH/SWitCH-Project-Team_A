package TOPSECRET.repository;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public BibliographicalData createAndAdd(String title, List<Author> authors, Locale originalLanguage, LocalDate publicationDate,
                                            List<Genre> genres, String chunksOfText) throws InstanceAlreadyExistsException
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



    // -----------------------------------------------------------------------------------------------------------------



    // Get all publications --------------------------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public List<BibliographicalData> getAllPublicationsBD() {
        return List.copyOf(_bdList);
    }

}


