package TOPSECRET.domain;

import TOPSECRET.domain.ValueObjects.Contact;

/**
 * Factory responsible for creating {@link Publisher} instances.
 *
 * <p>This factory centralizes the creation logic of {@code Publisher} objects.
 * It performs minor input normalization before delegating validation
 * to the {@link Publisher} constructor.</p>
 *
 * <p>Currently, the factory trims leading and trailing whitespace from
 * the {@code name} and {@code vatNumber} fields if they are not {@code null}.</p>
 */
public class PublisherFactory {

    /**
     * Creates a new {@link Publisher} instance using the provided attributes.
     *
     * <p>The {@code name} and {@code vatNumber} values are trimmed before
     * being passed to the {@link Publisher} constructor. Any validation
     * of these values is performed by the {@link Publisher} class itself.</p>
     *
     * @param name the publisher's name
     * @param vatNumber the publisher's VAT number
     * @param contact the contact information associated with the publisher
     * @return a new {@link Publisher} instance
     */
    public Publisher create(String name, String vatNumber, Contact contact) {
        return new Publisher(
                name == null ? null : name.trim(),
                vatNumber == null ? null : vatNumber.trim(),
                contact
        );
    }
}