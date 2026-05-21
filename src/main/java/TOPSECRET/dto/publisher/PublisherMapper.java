package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.dto.publisher.response.PublisherDTO;

import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Publisher} domain objects
 * into {@link PublisherDTO} application-layer representations.
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
     * Converts a {@link Publisher} into a {@link PublisherDTO}.
     *
     * @param publisher the publisher to convert; must not be null
     * @return the corresponding {@link PublisherDTO}
     * @throws NullPointerException if publisher is null
     */
    public static PublisherDTO toDTO(Publisher publisher) {
        Objects.requireNonNull(publisher, "Publisher cannot be null");

        return new PublisherDTO(
                publisher.getName(),
                publisher.id()
        );
    }

    /**
     * Converts a list of {@link Publisher} objects into a list of {@link PublisherDTO}.
     * Stream over the list, mapping each Publisher to a PublisherDTO via toDTO(),
     * which also handles individual null checks. Collected into an unmodifiable list.
     *
     * @param publishers the list of publishers; must not be null and must not contain null elements
     * @return an immutable list of {@link PublisherDTO}
     * @throws NullPointerException if publishers is null or any element is null
     */
    public static List<PublisherDTO> toDTOs(List<Publisher> publishers) {
        Objects.requireNonNull(publishers, "Publishers list cannot be null");

        return publishers.stream()
                .map(PublisherMapper::toDTO)
                .toList();
    }
}