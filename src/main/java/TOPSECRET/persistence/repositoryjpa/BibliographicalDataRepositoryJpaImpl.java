package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.assembler.BibliographicalDataAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IBibliographicalDataRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-based implementation of {@link IBibliographicalDataRepository}.
 * <p>
 * This class acts as an adapter between the domain repository interface and
 * the underlying Spring Data JPA repository ({@link IBibliographicalDataRepositoryJpa}).
 * It is responsible for delegating persistence operations and converting
 * between domain models and persistence entities using the
 * {@link BibliographicalDataAssembler}.
 */
@Repository
public class BibliographicalDataRepositoryJpaImpl implements IBibliographicalDataRepository {

    /**
     * Spring Data JPA repository used for database access.
     */
    private IBibliographicalDataRepositoryJpa bibliographicalDataRepositoryJpa;

    /**
     * Assembler responsible for mapping between domain objects and JPA entities.
     */
    private BibliographicalDataAssembler bibliographicalDataAssembler;

    /**
     * Constructs the repository implementation with required dependencies.
     *
     * @param bibliographicalDataRepositoryJpa JPA repository for persistence operations
     * @param bibliographicalDataAssembler assembler for converting between domain and entity models
     */
    public BibliographicalDataRepositoryJpaImpl(IBibliographicalDataRepositoryJpa bibliographicalDataRepositoryJpa, BibliographicalDataAssembler bibliographicalDataAssembler) {
        this.bibliographicalDataRepositoryJpa =
                Objects.requireNonNull(bibliographicalDataRepositoryJpa, "bibliographicalDataRepositoryJpa must not be null");

        this.bibliographicalDataAssembler =
                Objects.requireNonNull(bibliographicalDataAssembler, "bibliographicalDataAssembler must not be null");
    }

    /**
     * @InheritDoc
     */
    @Override
    @Transactional
    public BibliographicalData save(BibliographicalData bdCreated) throws InstanceAlreadyExistsException {

        Objects.requireNonNull(bdCreated, "Created bibliographical data is null");

        if (bibliographicalDataRepositoryJpa.existsById(bdCreated.id())){
            throw new InstanceAlreadyExistsException("Bibliographical Data already exists");
        }

        BibliographicalDataDM bdDM = bibliographicalDataAssembler.toDM(bdCreated);

        BibliographicalDataDM bdDMSaved = bibliographicalDataRepositoryJpa.save(bdDM);

        return bibliographicalDataAssembler.toDomain(bdDMSaved);

    }

    /**
     * @InheritDoc
     */
    @Override
    public List<BibliographicalData> findAll() {
        return bibliographicalDataRepositoryJpa.findAll()
                .stream()
                .map(bibliographicalDataAssembler::toDomain)
                .toList();
    }

    /**
     * @InheritDoc
     */
    @Override
    public Optional<BibliographicalData> findById(
            BibliographicalDataID bibliographicalDataId
    ) {
        Objects.requireNonNull(
                bibliographicalDataId,
                "BibliographicalDataId must not be null"
        );

        return bibliographicalDataRepositoryJpa
                .findById(bibliographicalDataId)
                .map(bibliographicalDataAssembler::toDomain);
    }


    /**
     * @InheritDoc
     */
    @Override
    public List<BibliographicalDataID> findAllByGenreId(GenreID genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }

        return bibliographicalDataRepositoryJpa
                .findByGenreIdsContaining(genreId)
                .stream()
                .map(BibliographicalDataDM::getId)
                .toList();
    }

    /**
     * @InheritDoc
     */
    @Override
    public Optional<BibliographicalDataID> findIdByTitle(String bdTitle) {
        Objects.requireNonNull(bdTitle, "Title must not be null");

        if (bdTitle.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }

        return bibliographicalDataRepositoryJpa
                .findByTitleIgnoreCase(bdTitle.trim())
                .map(BibliographicalDataDM::getId);
    }


    /**
     * @InheritDoc
     */
    @Override
    public List<BibliographicalDataID> findIdByAuthorId(AuthorId authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null.");
        }

        return bibliographicalDataRepositoryJpa
                .findByAuthorIdsContaining(authorId)
                .stream()
                .map(BibliographicalDataDM::getId)
                .toList();
    }

    /**
     * @InheritDoc
     */
    @Override
    public List<String> getAllTitles() {
        return bibliographicalDataRepositoryJpa.findAll()
                .stream()
                .map(BibliographicalDataDM::getTitle)
                .toList();
    }

}
