package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.MediaFormat;

public class PublicationType {

    private MediaFormat _mediaFormat;
    private TypeOfBinding _typeOfBinding;
    private String _details;

    public PublicationType (MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        if (mediaFormat == null){
            throw new IllegalArgumentException("Media format can´t be null");
        }

        if (typeOfBinding == null){
            throw new IllegalArgumentException("Type of binding can´t be null");
        }

        if (details == null || details.isBlank()){
            throw new IllegalArgumentException("Details can´t be null or blank");
        }

        this._mediaFormat = mediaFormat;
        this._typeOfBinding = typeOfBinding;
        this._details = details;
    }

    public MediaFormat getMediaFormat() { return _mediaFormat; }
    public TypeOfBinding getTypeOfBinding() { return _typeOfBinding; }
    public String getDetails() { return _details; }

}
