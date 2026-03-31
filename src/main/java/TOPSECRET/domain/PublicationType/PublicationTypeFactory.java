package TOPSECRET.domain.PublicationType;


/**
 * Factory responsible for creating PublicationType instances.
 *
 * <p>
 * Encapsulates the creation logic and delegates validation
 * to the PublicationType entity.
 * </p>
 */
public class PublicationTypeFactory {

    /**
     * Creates a new PublicationType.
     *
     * @param mediaFormat the publication format
     * @return a new PublicationType instance
     */
    public PublicationType create(String mediaFormat)
    {
        return new PublicationType(mediaFormat);
    }
}
