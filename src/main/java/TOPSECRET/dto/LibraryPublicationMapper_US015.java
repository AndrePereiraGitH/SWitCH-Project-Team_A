
package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class LibraryPublicationMapper_US015 {

    // Creates 1 DTO for 1 Publication
    public static LibraryPublicationDTO_US015 toDTO(Publication publication) {
        String titleDTO = publication.getBibliographicalData().getTitle();

        List<String> authorsDTO = new ArrayList<>();
        for (Author author : publication.getBibliographicalData().getAuthors()) {
            authorsDTO.add(author.getProfile().getName());
        }

        String typeDTO = String.valueOf(publication.getPublicationType().getTypeOfBinding());
        String codeDTO = String.valueOf(publication.getCode());

        return new LibraryPublicationDTO_US015(titleDTO, authorsDTO, codeDTO, typeDTO);
    }

    // Creates Publication List
    public static List<LibraryPublicationDTO_US015> toDTO(List<Publication> publications) {
        List<LibraryPublicationDTO_US015> listDTO = new ArrayList<>();

        for (Publication pub : publications) {
            listDTO.add(toDTO(pub));
        }

        return listDTO;
    }


}

