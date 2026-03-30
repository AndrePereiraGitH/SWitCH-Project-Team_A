package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.List;


/**
 * Repository interface responsible for managing {@link PublicationType} entities.
 * Provides methods for creating, storing, and retrieving publication types.
 */
public interface IPublicationTypeRepository {


    PublicationType create(String mediaFormat);


    PublicationType addToList(PublicationType pubType);


    List<PublicationType> getAllPublicationTypes();


    PublicationType findByAttributes(String mediaFormat);
}
