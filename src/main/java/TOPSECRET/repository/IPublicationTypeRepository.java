package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType;

import java.util.List;

public interface IPublicationTypeRepository {

    PublicationType create(String mediaFormat);
    PublicationType addToList(PublicationType pubType);
    List<PublicationType> getAllPublicationTypes();
    PublicationType findByAttributes(String mediaFormat);


}
