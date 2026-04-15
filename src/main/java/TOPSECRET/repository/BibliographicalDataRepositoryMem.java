package TOPSECRET.repository;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

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

    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory, List<BibliographicalData> bdList) {
        _bdList = new ArrayList<>(bdList);
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }

    public BibliographicalDataRepositoryMem(BibliographicalDataFactory bibliographicalDataFactory) {
        _bdList = new ArrayList<>();
        _bibliographicalDataFactory = bibliographicalDataFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BibliographicalData createAndAdd(String title, List<AuthorId> authors, Locale originalLanguage, LocalDate publicationDate,
                                            List<GenreID> genres, String chunksOfText) throws InstanceAlreadyExistsException {
        if (title == null)
            throw new IllegalArgumentException("title cannot be null");
        if (authors == null)
            throw new IllegalArgumentException("authors cannot be null");
        if (genres == null)
            throw new IllegalArgumentException("genres cannot be null");
        if (chunksOfText == null)
            throw new IllegalArgumentException("chunksOfText cannot be null");

        BibliographicalData newBibliographicalData = _bibliographicalDataFactory.create(title, authors, originalLanguage, publicationDate, genres, chunksOfText);
        _bdList.add(newBibliographicalData);
        return newBibliographicalData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BibliographicalData> findBibliographicalDataById(BibliographicalDataID bibliographicalDataId) {
        if (bibliographicalDataId == null)
            throw new NullPointerException("BibliographicalDataId must not be null");

        for (BibliographicalData bd : _bdList) {
            if (bd.id().equals(bibliographicalDataId))
                return Optional.of(bd);
        }
        return Optional.empty();
    }

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
    @Override
    public List<BibliographicalData> findByAuthorId(AuthorId authorId) {
        if (authorId == null)
            throw new IllegalArgumentException("authorId must not be null");

        List<BibliographicalData> result = new ArrayList<>();
        for (BibliographicalData bd : _bdList) {
            if (bd.getAuthors().contains(authorId))
                result.add(bd);
        }
        return result;
    }
}