package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

public class PublicationTypeDTO_US014 {

    public MediaFormat _mediaFormat;
    public TypeOfBinding _typeOfBinding;
    public String _details;

    public PublicationTypeDTO_US014(PublicationType publicationType) {
        this._mediaFormat = publicationType.getMediaFormat();
        this._typeOfBinding = publicationType.getTypeOfBinding();
        this._details = publicationType.getDetails();
    }
}

