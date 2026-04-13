package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for converting {@link Publisher} domain objects
 * into their corresponding {@link US027PublisherDTO} representations for US027.
 * <p>
 * This mapper provides methods to convert a single Publisher or a list of
 * Publishers, ensuring that inputs are valid and that no null elements are
 * processed. It is used exclusively in the application layer to prepare data
 * for output.
 * </p>
 *
 * <p>This class cannot be instantiated.</p>
 */
public class US027PublisherMapper {

    private US027PublisherMapper() {
        throw new UnsupportedOperationException("Utility class");
    }


    /**
     * Converts a {@link Publisher} domain object into a {@link US027PublisherDTO}.
     *
     * <p>This method is used when preparing the data to be returned to the user
     * in the context of US027. Only the relevant fields are extracted from the
     * domain object.</p>
     *
     * @param publisher the Publisher to convert; must not be {@code null}
     * @return a new {@link US027PublisherDTO} containing the Publisher's id and name
     * @throws NullPointerException if {@code publisher} is {@code null}
     */

    public static US027PublisherDTO toDTO(Publisher publisher) {

        if  (publisher == null) {
            throw new NullPointerException("Publisher cannot be null");
        }

        PublisherID publisherId = publisher.id();
        String name = publisher.getName();

        US027PublisherDTO publisherDTO = new US027PublisherDTO(publisherId, name);

        return publisherDTO;
    }

    /**
     * Converts a list of {@link Publisher} objects into an immutable list of
     * {@link US027PublisherDTO} instances.
     *
     * <p>This method validates that the list is not {@code null} and that it does
     * not contain {@code null} elements. It is typically used when returning the
     * full list of registered Publishers to the user in US027.</p>
     *
     * @param listOfAllPublishers the list of Publisher objects to convert; must not be {@code null}
     * @return an immutable list of {@link US027PublisherDTO} objects
     * @throws NullPointerException     if {@code publishers} is {@code null}
     * @throws IllegalArgumentException if any element in the list is {@code null}
     */

    public static List<US027PublisherDTO> toDTOs(List<Publisher> listOfAllPublishers) {

        if  (listOfAllPublishers == null) {
            throw new NullPointerException("List of publishers cannot be null");
        }

        List<US027PublisherDTO> listOfPublishersDTO = new ArrayList<>();

        for (Publisher publisher : listOfAllPublishers) {
            if (publisher == null) {
                throw new IllegalArgumentException("List of publishers must not contain null elements");
            }
            listOfPublishersDTO.add(toDTO(publisher));
        }

        return List.copyOf(listOfPublishersDTO);
    }
}
