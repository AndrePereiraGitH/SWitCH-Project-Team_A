package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;

import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Publisher} domain objects
 * into {@link ResponsePublisherDTO} application-layer representations.
 *
 * <p>This class isolates mapping logic between the domain and DTO layer,
 * ensuring separation of concerns and preventing leakage of domain models
 * into higher layers.</p>
 *
 * <p>This is a stateless utility-style mapper and should not be instantiated.</p>
 */
public final class PublisherMapper {

    private PublisherMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a {@link Publisher} into a {@link ResponsePublisherDTO}.
     *
     * @param publisher the publisher to convert; must not be null
     * @return the corresponding {@link ResponsePublisherDTO}
     * @throws NullPointerException if publisher is null
     */
    public static ResponsePublisherDTO toDTO(Publisher publisher) {
        Objects.requireNonNull(publisher, "Publisher cannot be null");

        return new ResponsePublisherDTO(
                publisher.getName(),
                publisher.id().toString()
        );
    }

    /**
     * Converts a list of {@link Publisher} objects into a list of {@link ResponsePublisherDTO}.
     * Stream over the list, mapping each Publisher to a PublisherDTO via toDTO(),
     * which also handles individual null checks. Collected into an unmodifiable list.
     *
     * @param publishers the list of publishers; must not be null and must not contain null elements
     * @return an immutable list of {@link ResponsePublisherDTO}
     * @throws NullPointerException if publishers is null or any element is null
     */
    public static List<ResponsePublisherDTO> toDTOList(List<Publisher> publishers) {
        Objects.requireNonNull(publishers, "Publishers list cannot be null");

        return publishers.stream()
                .map(PublisherMapper::toDTO)
                .toList();
    }

    /**
     * Creates a {@link Contact} Value Object from the information
     * contained in the request DTO.
     *
     * @param pubDTO the request DTO containing contact information
     * @return the constructed Contact object
     * @throws NullPointerException if pubDTO is null
     * @throws IllegalArgumentException if any Value Object data is invalid
     */
    public static Contact toContact(RequestPublisherDTO pubDTO) {

        Objects.requireNonNull(pubDTO, "RequestPublisherDTO cannot be null");

        CityId cityId = new CityId(pubDTO.cityId());
        Address address = new Address(pubDTO.street(), pubDTO.number(), pubDTO.zipCode(), cityId);

        return new Contact(pubDTO.phoneNumber(), pubDTO.extension(), toEmail(pubDTO), address);
    }
    /**
     * Creates an {@link Email} Value Object from the email value
     * contained in the request DTO.
     *
     * @param pubDTO the request DTO containing the email information
     * @return the constructed Email object
     * @throws IllegalArgumentException if the email format is invalid
     */
    public static Email toEmail(RequestPublisherDTO pubDTO) {

        Objects.requireNonNull(pubDTO, "RequestPublisherDTO cannot be null");

        String[] split = pubDTO.email().split("@");

        return new Email(split[0], split[1]);
    }
}