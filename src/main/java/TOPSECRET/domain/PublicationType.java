package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.MediaFormat;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicationType that = (PublicationType) o;
        return _mediaFormat == that._mediaFormat && _typeOfBinding == that._typeOfBinding && _details.equals(that._details);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_mediaFormat, _typeOfBinding, _details);
    }

    public MediaFormat getMediaFormat() { return _mediaFormat; }
    public TypeOfBinding getTypeOfBinding() { return _typeOfBinding; }
    public String getDetails() { return _details; }

}
