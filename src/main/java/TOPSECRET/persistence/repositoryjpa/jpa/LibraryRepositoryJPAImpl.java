package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.LibraryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

@Repository
public class LibraryRepositoryJPAImpl implements ILibraryRepository {

    private final ILibraryJPARepository _jpaRepository;

    private final LibraryAssembler _assembler;

    public LibraryRepositoryJPAImpl(ILibraryJPARepository _jpaRepository, LibraryAssembler _assembler) {
        this._jpaRepository = _jpaRepository;
        this._assembler = _assembler;
    }

    @Override
    public Library getLibraryByUserId(UserId userId) throws InstanceNotFoundException {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        LibraryDM libraryDM = _jpaRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("This user does not have a library")
                );

        return _assembler.toDomain(libraryDM);
    }

    @Transactional
    @Override
    public Library save(Library library) throws InstanceAlreadyExistsException {
        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }

        if (verifyIfUserHasALibrary(library.getUserId())) {
            throw new InstanceAlreadyExistsException("This user already has a library");
        }

        LibraryDM libraryDM = _assembler.toDM(library);

        LibraryDM savedLibraryDM = _jpaRepository.save(libraryDM);

        return _assembler.toDomain(savedLibraryDM);
    }

    @Override
    public boolean verifyIfUserHasALibrary(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return _jpaRepository.findByUserId(userId).isPresent();
    }
}
