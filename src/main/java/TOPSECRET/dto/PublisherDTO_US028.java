package TOPSECRET.dto;

import TOPSECRET.domain.Publisher;

/**
 * Data Transfer Object (DTO) used in use case US028
 * to represent a Publisher.
 *
 * This class contains only the data needed by the UI.
 */
public class PublisherDTO_US028 {

    // Name of the publisher
    public String name;

    /**
     * Creates a PublisherDTO_US028 from a Publisher domain object.
     *
     * @param publisher the Publisher to extract data from
     */
    public PublisherDTO_US028(Publisher publisher) {

        // Copy only the required information
        this.name = publisher.getName();
    }
}
