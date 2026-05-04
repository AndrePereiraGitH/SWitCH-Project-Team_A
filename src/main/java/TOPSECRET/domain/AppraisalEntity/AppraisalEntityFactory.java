package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Factory responsible for creating AppraisalEntity instances.
 *
 * <p>
 * Encapsulates the creation logic of AppraisalEntity,
 * ensuring that all required parameters are provided.
 * </p>
 */
@Service
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
                                  List<GenreId> genres)
            {

            return new AppraisalEntity(
                    name,
                    contact,
                    publicationTypes,
                    genres
            );
    }

    /**
     * Recreates a AppraisalEntity from DataBase data.
     *
     * @param id appraisal entity id
     * @param name name of the entity
     * @param contact contact information
     * @param genres list of genres
     * @param publicationTypes list of publication types
     * @return a AppraisalEntity instance
     */
    public AppraisalEntity create(AppraisalEntityId id,
                                  String name,
                                  Contact contact,
                                  List<PublicationTypeId> publicationTypes,
                                  List<GenreId> genres)
    {

        return new AppraisalEntity(
                id,
                name,
                contact,
                publicationTypes,
                genres
        );
    }
}
