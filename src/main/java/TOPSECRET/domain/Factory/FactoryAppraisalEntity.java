package TOPSECRET.domain.Factory;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;

import java.util.List;

/**
 * US004
 * Factory responsible for creating AppraisalEntity instances.
 * Isolates instantiation logic from repository layer.
 */
public class FactoryAppraisalEntity {

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
