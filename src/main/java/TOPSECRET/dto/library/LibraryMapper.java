package TOPSECRET.dto.library;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;

import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Library} domain objects
 * into {@link ResponseLibraryDTO} representations.
 *
 * <p>This class isolates mapping logic between the domain and DTO layers,
 * ensuring separation of concerns and preventing domain model leakage
 * into higher application layers.</p>
 *
 * <p>This is a stateless utility-style mapper and should not be instantiated.</p>
 */
public final class LibraryMapper {

    private LibraryMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ResponseLibraryDTO toDTO(Library library) {
        Objects.requireNonNull(library, "Library cannot be null");

        return new ResponseLibraryDTO(
                library.id().getId(), library.getLibraryName()
        );
    }

    public static List<ResponseLibraryDTO> toDTOList(List<Library> libraries) {
        Objects.requireNonNull(libraries, "Libraries list cannot be null");

        return libraries.stream()
                .map(LibraryMapper::toDTO)
                .toList();
    }
}