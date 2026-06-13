package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.LibraryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ILibraryJPARepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-based implementation of the {@link ILibraryRepository} interface.
 *
 * <p>This repository is responsible for persisting and retrieving {@link Library}
 * aggregates using Spring Data JPA. It delegates data access operations to
 * {@link ILibraryJPARepository} and handles the conversion between domain objects
 * and persistence data models through the {@link LibraryAssembler}.</p>
 *
 * <p>This implementation enforces domain constraints such as ensuring that a user
 * can only have a single library.</p>
 *
 * <p>All methods inherit their contracts from {@link ILibraryRepository}.</p>
 */
@Repository
public class LibraryRepositoryJPAImpl implements ILibraryRepository {

    private final ILibraryJPARepository _jpaRepository;

    private final LibraryAssembler _assembler;

    public LibraryRepositoryJPAImpl(ILibraryJPARepository _jpaRepository, LibraryAssembler _assembler) {
        this._jpaRepository = Objects.requireNonNull(_jpaRepository, "jpaRepository must not be null");
        this._assembler = Objects.requireNonNull(_assembler, "assembler must not be null");
    }



    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Library save(Library library) throws InstanceAlreadyExistsException {
        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }

        if (existsByUserId(library.getUserId())) {
            throw new InstanceAlreadyExistsException("This user already has a library");
        }

        LibraryDM libraryDM = _assembler.toDM(library);

        LibraryDM savedLibraryDM = _jpaRepository.save(libraryDM);

        return _assembler.toDomain(savedLibraryDM);
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return _jpaRepository.findByUserId(userId).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Library> findAll() {
        return _jpaRepository.findAll()
                .stream()
                .map(_assembler::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Library> findById(LibraryId id) {
        return _jpaRepository.findById(id)
                .map(_assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Library> findByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return _jpaRepository.findByUserId(userId)
                .map(_assembler::toDomain);
    }
}
