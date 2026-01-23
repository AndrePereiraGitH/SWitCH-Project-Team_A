package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

public class PublicationTypeDTO_US014 {

    public String _mediaFormat;
    public String _typeOfBinding;
    public String _details;

    public PublicationTypeDTO_US014(PublicationType publicationType) {
        this._mediaFormat = publicationType.getMediaFormat().name();
        this._typeOfBinding = publicationType.getTypeOfBinding().name();
        this._details = publicationType.getDetails();
    }
}

