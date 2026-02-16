package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class PublicationMapper_US023 {

    // Converts a Publication entity into a PublicationDTO
    // Uses the PublicationDetails record to reduce coupling and avoid exposing the Publication entity.
    // Only returns publication title, authors, code and type.
    public static PublicationDTO_US023 toDTO(Publication publication) {

        // Retrieve the immutable record containing all publication data
        Publication.PublicationDetails details = publication.getDetails();

        // Extracts the details
        String titleDTO = details.bibliographicalData().getTitle();
        List<Author> listOfAuthors = details.bibliographicalData().getAuthors();
        List<String> authorsDTO = new ArrayList<>();
        for (Author author : listOfAuthors) {
            authorsDTO.add(author.getProfile().getName());
        }
        String type = String.valueOf(details.publicationType().getTypeOfBinding());
        String code = details.code() != null ? details.code().toString() : null;

        // Create and return the DTO
        return new PublicationDTO_US023(titleDTO, authorsDTO, code, type);
    }

    // Converts a list of Publication entities into a list of PublicationDTO, calling toDTO() for each one
    public static List<PublicationDTO_US023> getListOfPublicationsDTO(List<Publication> publications) {
        List<PublicationDTO_US023> listOfPublicationsDTO = new ArrayList<>();

        for (Publication pub : publications) {
            listOfPublicationsDTO.add(toDTO(pub));
        }

        return listOfPublicationsDTO;
    }




}

