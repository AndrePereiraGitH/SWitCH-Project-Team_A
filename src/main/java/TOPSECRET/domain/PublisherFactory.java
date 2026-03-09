package TOPSECRET.domain;

/**
 * Factory responsible for creating Publisher instances.
 */
public class PublisherFactory {

    public Publisher create(String name, String vatNumber, Contact contact) {
        return new Publisher(
                name == null ? null : name.trim(),
                vatNumber == null ? null : vatNumber.trim(),
                contact
        );
    }
}