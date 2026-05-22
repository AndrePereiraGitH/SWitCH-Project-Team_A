package TOPSECRET.dto.library;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;

import java.util.List;
import java.util.Objects;

public final class LibraryMapper {


    public static ResponseLibraryDTO toDTO(Library library) {
        Objects.requireNonNull(library, "Library cannot be null");

        return new ResponseLibraryDTO(
                library.getLibraryName()
        );
    }

    public static List<ResponseLibraryDTO> toDTOList(List<Library> libraries) {
        Objects.requireNonNull(libraries, "Libraries list cannot be null");

        return libraries.stream()
                .map(LibraryMapper::toDTO)
                .toList();
    }
}