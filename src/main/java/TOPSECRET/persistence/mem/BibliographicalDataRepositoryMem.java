package TOPSECRET.persistence.mem;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
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

    private final List<BibliographicalData> _bdList;

    /**
     * Constructor for in-memory bibliographical data repository.
     */
    public BibliographicalDataRepositoryMem() {
        _bdList = new ArrayList<>();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BibliographicalData save(BibliographicalData bibliographicalDataCreated) throws InstanceAlreadyExistsException {

        for(BibliographicalData bd : _bdList){
            if (bd.id().equals(bibliographicalDataCreated.id())){
                throw new InstanceAlreadyExistsException("Bibliographical Data already exists");
            }
        }
        _bdList.add(bibliographicalDataCreated);

        return bibliographicalDataCreated;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BibliographicalData> findById(BibliographicalDataID bibliographicalDataId){

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
    @Override
    public Optional<BibliographicalDataID> findIdByTitle(String bdTitle){

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

    @Override
    public List<BibliographicalDataID> findAllByGenreId(GenreID genreId){

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
    public List<BibliographicalDataID> findIdByAuthorId(AuthorId authorId) {

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
    public List<BibliographicalData> findAll() {
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


