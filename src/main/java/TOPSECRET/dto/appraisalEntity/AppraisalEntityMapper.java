package TOPSECRET.dto.appraisalEntity;


import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.reponse.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;

import java.util.List;
import java.util.Objects;

/**
 * Utility mapper responsible for converting AppraisalEntity request DTOs
 * into domain Value Objects used during AppraisalEntity creation.
 *
 * <p>
 * This class provides helper methods to construct domain objects such as
 * {@link Contact} and {@link Email} from request data.
 * </p>
 *
 * <p>
 * This class cannot be instantiated.
 * </p>
 */
public final class AppraisalEntityMapper {

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always thrown because this is
     * a utility class
     */
    private AppraisalEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a {@link Contact} Value Object from the given request DTO.
     *
     * <p>
     * This method also creates the associated {@link Email},
     * {@link CityId}, and {@link Address} objects required for the
     * Contact construction.
     * </p>
     *
     * @param dto the request DTO containing contact information
     * @return the constructed Contact object
     * @throws IllegalArgumentException if the provided data is invalid
     */
    public static Contact toContact(RequestAppraisalEntityDTO dto) {

        Objects.requireNonNull(dto, "RequestAppraisalEntityDTO cannot be null");

        return new Contact(dto.phoneNumber(), dto.extension(), toEmail(dto), toAddress(dto));
    }

    /**
     * Creates an {@link Email} Value Object from the email value
     * contained in the request DTO.
     *
     * @param dto the request DTO containing the email information
     * @return the constructed Email object
     * @throws IllegalArgumentException if the email format is invalid
     */
    public static Email toEmail(RequestAppraisalEntityDTO dto) {

        Objects.requireNonNull(dto, "RequestAppraisalEntityDTO cannot be null");

        String[] split = dto.email().split("@");

        return new Email(split[0], split[1]);
    }

    /**
     * Creates an {@link Address} Value Object from the address value
     * contained in the request DTO.
     *
     * <p>
     * This method also creates the associated{@link CityId},
     * object required for the Address construction.
     * </p>
     *
     * @param dto the request DTO containing address information
     * @return the constructed Address object
     * @throws IllegalArgumentException if the provided data is invalid
     */
    public static Address toAddress(RequestAppraisalEntityDTO dto) {

        Objects.requireNonNull(dto, "RequestAppraisalEntityDTO cannot be null");

        CityId cityId = new CityId(dto.cityId());

        return new Address(dto.street(), dto.number(), dto.zipCode(), cityId);
    }

    /**
     * Converts a list of Strings into a list of {@link PublicationTypeId}.
     *
     * <p>Each String is wrapped into a new {@code PubTypeId} instance,
     * ensuring strong typing and enforcing the use of Value Objects
     * in the domain layer.</p>
     *
     * @param ids list of publication type identifiers as Strings
     * @return list of {@code PubTypeId} created from the provided values
     */
    public static List<PublicationTypeId> toPubTypeIds(RequestAppraisalEntityDTO ids) {

        Objects.requireNonNull(ids, "RequestAppraisalEntityDTO cannot be null");

        return ids.publicationTypeIds().stream()
                .map(PublicationTypeId::new)
                .toList();
    }

    /**
     * Converts a list of Strings into a list of {@link GenreId}.
     *
     * <p>Each String is transformed into a {@code GenreId} Value Object,
     * ensuring type safety and consistency across the domain model.</p>
     *
     * @param ids list of genre identifiers as Strings
     * @return list of {@code GenreId} created from the provided values
     */
    public static List<GenreId> toGenreIds(RequestAppraisalEntityDTO ids) {

        Objects.requireNonNull(ids, "RequestAppraisalEntityDTO cannot be null");

        return ids.genreIds().stream()
                .map(GenreId::new)
                .toList();
    }

    /**
     * Converts an AppraisalEntity domain object
     * into its API response representation.
     *
     * @param entity domain entity
     * @return response DTO
     */
    public static ResponseAppraisalEntityDTO toResponse(AppraisalEntity entity){

        Objects.requireNonNull(entity, "AppraisalEntity can't be null");

        return new ResponseAppraisalEntityDTO(entity.id().getId(), entity.getName(),

                entity.getPublicationTypeIds()
                        .stream()
                        .map(PublicationTypeId::getId)
                        .toList(),
                entity.getGenreIds()
                        .stream()
                        .map(GenreId::getID)
                        .toList()

        );
    }
}
