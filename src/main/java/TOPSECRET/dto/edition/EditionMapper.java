package TOPSECRET.dto.edition;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import TOPSECRET.dto.edition.response.EditionIdResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Mapper responsible for converting between {@link CreateEditionDTO} request objects,
 * {@link Edition} domain objects, and {@link EditionDTO} response objects.
 *
 * <p>This class is the only component that is aware of both the API layer
 * and the domain layer. It centralizes all conversion logic, ensuring that:</p>
 * <ul>
 *   <li>Domain value objects are never exposed through the API boundary</li>
 *   <li>Request primitives are correctly wrapped into domain value objects</li>
 *   <li>Domain objects are flattened into API-safe response representations</li>
 * </ul>
 *
 * <p>All methods are static — this class is stateless and holds no dependencies.</p>
 */
public class EditionMapper {

    private EditionMapper() {}


    /**
     * Extracts and converts the {@link Code} value object from a {@link CreateEditionDTO}.
     *
     * <p>Only one of isbn10, isbn13, or issn will be used — whichever is non-blank.
     * If none are provided, an empty {@link Code} is returned and domain validation will enforce the rule.</p>
     *
     * @param dto the request DTO; must not be {@code null}
     * @return a {@link Code} value object, or an empty {@link Code} (via {@code Code.empty()}) if no code is provided
     */
    public static Code toCode(CreateEditionDTO dto) {
        if (dto.isbn13() != null && !dto.isbn13().isBlank()) {
            return Code.fromIsbn13(dto.isbn13());
        } else if (dto.isbn10() != null && !dto.isbn10().isBlank()) {
            return Code.fromIsbn10(dto.isbn10());
        } else if (dto.issn() != null && !dto.issn().isBlank()) {
            return Code.fromIssn(dto.issn());
        }
        return Code.empty();
    }

    /**
     * Extracts and converts the {@link PhysicalAttributes} value object from a {@link CreateEditionDTO}.
     *
     * @param dto the request DTO; must not be {@code null}
     * @return a {@link PhysicalAttributes} value object
     */
    public static PhysicalAttributes toPhysicalAttributes(CreateEditionDTO dto) {
        Dimensions dimensions = new Dimensions(dto.height(), dto.width(), dto.depth());
        return new PhysicalAttributes(dimensions, dto.weight(), dto.numberOfPages());
    }

    /**
     * Extracts and converts the {@link PublicationTypeId} value object from a {@link CreateEditionDTO}.
     *
     * @param publicationTypeId the request DTO; must not be {@code null}
     * @return a {@link PublicationTypeId} value object
     */
    public static PublicationTypeId toPublicationTypeId(String publicationTypeId) {
        return new PublicationTypeId(publicationTypeId);
    }

    /**
     * Extracts and converts the {@link PublisherID} value object from a {@link CreateEditionDTO}.
     *
     * @param publisherId the request DTO; must not be {@code null}
     * @return a {@link PublisherID} value object
     */
    public static PublisherID toPublisherId(String publisherId) {
        return new PublisherID(publisherId);
    }

    /**
     * Extracts and parses the edition language from a {@link CreateEditionDTO}.
     *
     * @param language the request DTO; must not be {@code null}
     * @return a {@link Locale} parsed from the language tag
     */
    public static Locale toEditionLanguage(String language) {
        return Locale.forLanguageTag(language);
    }

    /**
     * Extracts and converts the {@link TypeOfBinding} enum from a {@link CreateEditionDTO}.
     *
     * @param typeOfBinding the request DTO; must not be {@code null}
     * @return a {@link TypeOfBinding} enum value
     * @throws IllegalArgumentException if the binding type is invalid
     */
    public static TypeOfBinding toTypeOfBinding(String typeOfBinding) {
        return TypeOfBinding.fromString(typeOfBinding);
    }

    public static EditionId toEditionId(String editionId) {
        return new EditionId(editionId);
    }

    /**
     * Converts an {@link Edition} domain object into an {@link EditionDTO} response.
     *
     * <p>All domain value objects are unwrapped into plain primitives.
     * The code is represented as a single string using {@link Code#toString()}.</p>
     *
     * @param edition the domain object to convert; must not be {@code null}
     * @return an {@link EditionDTO} representing the edition's data
     * @throws NullPointerException if {@code edition} is null
     */
    public static EditionDTO toDTO(Edition edition) {
        Objects.requireNonNull(edition, "Edition cannot be null");
        return new EditionDTO(
                edition.id().getID(),
                edition.getEditionNumber(),
                edition.getCode().toString(),
                edition.getPublicationTypeID().getId(),
                edition.getPublisherID().getId(),
                edition.getMediaFormat(),
                edition.getEditionLanguage().toLanguageTag(),
                edition.getEditionDate(),
                edition.getTypeOfBinding().toString(),
                edition.getPhysicalAttributes().getDimensions().getHeight(),
                edition.getPhysicalAttributes().getDimensions().getWidth(),
                edition.getPhysicalAttributes().getDimensions().getDepth(),
                edition.getPhysicalAttributes().getWeight(),
                edition.getPhysicalAttributes().getNumberOfPages()
        );
    }

    /**
     * Converts an {@link EditionId} value object into an
     * {@link EditionIdResponseDTO}.
     *
     * <p>This method is intended for lightweight API responses
     * where only the edition identifier is required, avoiding
     * the overhead of exposing the complete edition data.</p>
     *
     * @param editionId the edition identifier to convert;
     *                  must not be {@code null}
     * @return an {@link EditionIdResponseDTO} containing the
     *         identifier value of the provided edition
     * @throws NullPointerException if {@code editionId} is {@code null}
     */
    public static EditionIdResponseDTO toEditionIdDTO(EditionId editionId) {

        Objects.requireNonNull(editionId, "EditionId cannot be null");

        EditionIdResponseDTO dto = new EditionIdResponseDTO(editionId.getID());

        return dto;

    }

    /**
     * Converts a list of {@link Edition} domain objects into a list of {@link EditionDTO} responses.
     *
     * @param editions the list of domain objects to convert; must not be {@code null}
     * @return a list of {@link EditionDTO} representing the given editions
     * @throws NullPointerException if {@code editions} is null
     */
    public static List<EditionDTO> toDTOs(List<Edition> editions) {
        Objects.requireNonNull(editions, "Editions list cannot be null");
        List<EditionDTO> dtos = new ArrayList<>();
        for (Edition edition : editions) {
            dtos.add(toDTO(edition));
        }
        return dtos;
    }
    /**
     * Converts a String representation of a user identifier into a {@link UserId}.
     *
     * @param userId the user identifier as a String
     * @return the corresponding {@link UserId}
     * @throws IllegalArgumentException if the provided identifier is invalid
     */
    public static UserId toUserId(String userId) {
        return new UserId(userId);
    }
}