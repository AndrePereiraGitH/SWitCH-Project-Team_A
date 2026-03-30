package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.Contact;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import java.util.List;

/**
 * Repository interface for managing AppraisalEntity objects.
 *
 * Defines the contract for creating and storing entities,
 * ensuring validation and prevention of duplicates.
 */
public interface IAppraisalEntityRepository {

    AppraisalEntity createAndAdd(String name,
                                 Contact contact,
                                 List<GenreDetailsDTO_US004> selectedGenresDTO,
                                 List<PublicationTypeDTO_US004> selectedPublicationTypesDTO);


}
