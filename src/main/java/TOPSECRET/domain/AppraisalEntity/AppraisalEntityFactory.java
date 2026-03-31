package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.List;

/**
 * Factory responsible for creating AppraisalEntity instances.
 *
 * <p>
 * Encapsulates the creation logic of AppraisalEntity,
 * ensuring that all required parameters are provided.
 * </p>
 */
public class AppraisalEntityFactory {

    /**
     * Creates a new AppraisalEntity.
     *
     * @param name name of the entity
     * @param contact contact information
     * @param genres list of genres
     * @param publicationTypes list of publication types
     * @return a new AppraisalEntity instance
     */
    public AppraisalEntity create(String name,
                                  Contact contact,
                                  List<PublicationTypeId> publicationTypes,
                                  List<GenreID> genres)
            {

            return new AppraisalEntity(
                    name,
                    contact,
                    publicationTypes,
                    genres
            );
    }
}
