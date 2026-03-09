package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.PublicationSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) for publication details.
 * <p>
 * This class encapsulates the essential information about a publication
 * for transfer between application layers, including the title, authors,
 * code (ISBN), and publication type.
 * </p>
 *
 * @see PublicationSample
 */
public class PublicationDetailsDTO_US012 {

    public String pubTitle;
    public List<String> authorName;
    public String code;
    public String publicationType;

    /**
     * Constructs a new PublicationDetailsDTO_US012 with the specified details.
     *
     * @param pubTitle the title of the publication
     * @param authorName the list of author names
     * @param code the publication code (ISBN)
     * @param publicationType the type of binding for the publication
     */
    public PublicationDetailsDTO_US012(String pubTitle, List<String> authorName, String code, String publicationType) {
        this.pubTitle = pubTitle;
        this.authorName = authorName;
        this.code = code;
        this.publicationType = publicationType;
    }

    /**
     * Converts a list of PublicationSample domain objects to a list of DTOs.
     * <p>
     * This method iterates through the provided list of publications and
     * transforms each one into a PublicationDetailsDTO_US012 object.
     * </p>
     *
     * @param publications the list of PublicationSample objects to convert
     * @return a list of PublicationDetailsDTO_US012 objects containing the converted data
     * @see #toDTO(PublicationSample)
     */
    public static List<PublicationDetailsDTO_US012> toDTOs(List<PublicationSample> publications) {
        List<PublicationDetailsDTO_US012> listOfPublicationsDTO = new ArrayList<>();

        for (PublicationSample pub : publications) {
            PublicationDetailsDTO_US012 pubDTO = toDTO(pub);
            listOfPublicationsDTO.add(pubDTO);
        }

        return listOfPublicationsDTO;
    }

    /**
     * Converts a single PublicationSample domain object to a DTO.
     * <p>
     * This method extracts the following information from the publication:
     * <ul>
     *   <li>Title from the bibliographical data</li>
     *   <li>Author names from the authors' profiles</li>
     *   <li>Publication type (binding type) from the edition</li>
     *   <li>Code (ISBN) from the edition</li>
     * </ul>
     * </p>
     *
     * @param publication the PublicationSample object to convert
     * @return a PublicationDetailsDTO_US012 containing the extracted publication details
     */
    public static PublicationDetailsDTO_US012 toDTO(PublicationSample publication) {
        String titleDTO = publication.getBibliographicalData().getTitle();
        List<Author> listOfAuthors = publication.getBibliographicalData().getAuthors();
        List<String> authorsDTO = new ArrayList<>();

        for (Author author : listOfAuthors) {
            authorsDTO.add(author.getProfile().getName());
        }

        String typeDTO = String.valueOf(publication.getEdition().getPublicationType().getMediaFormat());
        String codeDTO = String.valueOf(publication.getEdition().getCode());

        PublicationDetailsDTO_US012 pubDTO = new PublicationDetailsDTO_US012(titleDTO, authorsDTO, codeDTO, typeDTO);

        return pubDTO;
    }
}