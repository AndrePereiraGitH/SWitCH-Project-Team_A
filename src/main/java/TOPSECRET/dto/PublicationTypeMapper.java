package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeMapper {

    public PublicationTypeDTO toDTO(PublicationType publicationType) {
        return new PublicationTypeDTO(publicationType);
    }

    public List<PublicationTypeDTO> toDTO(List<PublicationType> publicationTypes) {
        List<PublicationTypeDTO> result = new ArrayList<>();

        for (PublicationType pT : publicationTypes) {
            result.add(new PublicationTypeDTO(pT));
        }

        return result;
    }

}
