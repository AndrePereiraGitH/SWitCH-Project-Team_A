package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;

import java.util.List;

/**
 * Mapper class responsible for converting {@link Publisher} domain objects
 * into {@link US028PublisherDTO} data transfer objects.
 */
public class US028PublisherMapper {

    /**
     * Converts a {@link Publisher} into a {@link US028PublisherDTO}.
     *
     * @param publisher the publisher to convert
     * @return the corresponding DTO.
     */
    public static US028PublisherDTO toDTO(Publisher publisher) {
            if (publisher == null) {
                throw new IllegalArgumentException ("Publisher cannot be null");
            }

            return new US028PublisherDTO(
                    publisher.getName(),
                    publisher.id()
            );
        }

    /**
     * Converts a list of {@link Publisher} objects into a list of DTOs.
     *
     * @param publishers the list of publishers to convert
     * @return a list of {@link US028PublisherDTO}.
     */
    public static List<US028PublisherDTO> toDTOs(List<Publisher> publishers) {
        if (publishers == null) {
            throw new IllegalArgumentException ("Publishers List cannot be null");
        }

        return publishers.stream()
                .map(US028PublisherMapper::toDTO)
                .toList();
    }
}
