package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Repository responsible for storing and providing access to {@link BibliographicalData}.
 *
 * <p>
 * This repository maintains an internal list of bibliographical records and offers
 * read‑only access to the stored publications. The internal collection is defensively
 * copied to ensure that external modifications do not affect the repository state.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Store an initial collection of {@code BibliographicalData} objects</li>
 *     <li>Provide read‑only access to all stored publications</li>
 * </ul>
 *
 * <h2>Immutability</h2>
 * <p>
 * The repository does not expose its internal list directly. Methods returning collections
 * always provide unmodifiable copies to preserve encapsulation and prevent unintended
 * external changes.
 * </p>
 */

public class BibliographicalDataRepositoryMem implements IBibliographicalDataRepository {

    private BibliographicalDataFactory _bibliographicalDataFactory;

    private final List<BibliographicalData> _bdList;

    // Constructor -----------------------------------------------------------------------------------------------------

    /**
     * Creates a new repository with an initial collection of bibliographical records.
     *
     * <p>
     * The provided list is defensively copied to guarantee that subsequent modifications
     * to the original list do not affect the repository.
     * </p>
     *
     * @param bdList the initial list of {@link BibliographicalData} to store;
     *               must not be {@code null}
     * @throws NullPointerException if {@code bdList} is {@code null}
     */

    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory, List<BibliographicalData> bdList) {
        _bdList = new ArrayList<>(bdList);// throws automatically NullPointerException if bdList = null
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }

    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory) {
        _bdList = new ArrayList<>();
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }
    /**
     * Creates a new BibliographicalData and adds it to the repository.
     *
     * @param title the title of the publication (must not be null)
     * @param authors the list of authors (must not be null)
     * @param originalLanguage the original language of the publication
     * @param publicationDate the publication date
     * @param genres the list of genres (must not be null)
     * @param chunksOfText the textual content (must not be null)
     * @return the created BibliographicalData
     * @throws IllegalArgumentException if title, authors, genres or chunksOfText is null
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

    /**
     * Returns an unmodifiable view of all stored bibliographical publications.
     *
     * <p>
     * The returned list is a read‑only copy, ensuring that callers cannot modify
     * the internal repository state.
     * </p>
     *
     * @return an unmodifiable list containing all {@link BibliographicalData} records
     */

    // Get all publications --------------------------------------------------------------------------------------------
    @Override
    public List<BibliographicalData> getAllPublicationsBD() {
        return List.copyOf(_bdList);
    }

}


