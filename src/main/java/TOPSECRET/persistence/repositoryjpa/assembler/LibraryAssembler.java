package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between {@link LibraryDM}
 * persistence entities and {@link Library} domain objects.
 *
 * <p>This component acts as a translation layer between the persistence
 * model (JPA entities) and the domain model, ensuring that domain logic
 * remains independent of persistence concerns.</p>
 *
 * <p>It leverages a {@link LibraryFactory} to construct domain objects,
 * preserving domain invariants and encapsulation rules.</p>
 */
@Component
public class LibraryAssembler {

    private final LibraryFactory libraryFactory;

    /**
     * Constructs a {@code LibraryAssembler} with the required {@link LibraryFactory}.
     *
     * @param libraryFactory the factory used to create {@link Library} domain objects;
     *                       must not be {@code null}
     * @throws IllegalArgumentException if {@code libraryFactory} is {@code null}
     */

    public LibraryAssembler(LibraryFactory libraryFactory) {
        if (libraryFactory == null) {
            throw new IllegalArgumentException("LibraryFactory cannot be null");
        }
        this.libraryFactory = libraryFactory;
    }

    /**
     * Converts a {@link LibraryDM} persistence entity into a {@link Library} domain object.
     *
     * <p>This method maps the id, name, and user association directly
     * from the data model into the domain object using the {@link LibraryFactory}.
     * No transformation is required since value objects are preserved across layers.</p>
     *
     * @param libraryDM the persistence entity to convert; must not be {@code null}
     * @return the corresponding {@link Library} domain object
     * @throws IllegalArgumentException if {@code libraryDM} is {@code null}
     */
    public Library toDomainList(LibraryDM libraryDM) {
        if (libraryDM == null) {
            throw new IllegalArgumentException("LibraryDM cannot be null");
        }
        return libraryFactory.create(
                libraryDM.getId(),
                libraryDM.getLibraryName(),
                libraryDM.getUserId()
        );
    }

    /**
     * Converts a collection of {@link LibraryDM} entities into a list of
     * {@link Library} domain objects.
     *
     * <p>Each element in the provided iterable is individually transformed
     * using {@link #toDomainList(LibraryDM)}.</p>
     *
     * @param listDataModel an iterable collection of persistence entities;
     *                      must not be {@code null}
     * @return a list of corresponding {@link Library} domain objects;
     * never {@code null}, but may be empty
     */
    public List<Library> toDomainList(Iterable<LibraryDM> listDataModel) {
        List<Library> listDomain = new ArrayList<>();

        listDataModel.forEach(dm -> listDomain.add(toDomainList(dm)));

        return listDomain;
    }

    /**
     * Converts a {@link Library} domain object into a {@link LibraryDM}
     * persistence entity suitable for database storage.
     *
     * <p>This method extracts the identifier, name, and associated user
     * from the domain object and constructs a corresponding data model instance.</p>
     *
     * @param library the domain object to convert; must not be {@code null}
     * @return the corresponding {@link LibraryDM} persistence entity
     * @throws IllegalArgumentException if {@code library} is {@code null}
     */
    public LibraryDM toDM(Library library) {
        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }
        return new LibraryDM(
                library.id(),
                library.getLibraryName(),
                library.getUserId()
        );
    }
}