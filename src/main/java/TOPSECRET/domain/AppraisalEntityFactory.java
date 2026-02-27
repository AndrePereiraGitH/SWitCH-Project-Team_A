package TOPSECRET.domain;

import java.util.List;

/**
 * US004
 * Factory responsible for creating AppraisalEntity instances.
 * Isolates instantiation logic from repository layer.
 */
public class AppraisalEntityFactory {

    /**
     * US004
     * Creates a new AppraisalEntity instance.
     *
     * @param name AppraisalEntity name
     * @param contact Contact information
     * @param genres List of genres (1..*)
     * @param publicationTypes List of publication types (1..*)
     * @return Created AppraisalEntity
     */
    public static AppraisalEntity create(String name, Contact contact, List<Genre> genres, List<PublicationType> publicationTypes) {

        return new AppraisalEntity(name, contact, publicationTypes, genres);
    }
}
