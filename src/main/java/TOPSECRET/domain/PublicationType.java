package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;

import java.util.Objects;

public class PublicationType {

    /**
     Represents the publication type of an item.
     It contains parameters like media format, type of binding and details
     @param mediaFormat identifies the type of item, such as book or magazine
     @param typeOfBinding identifies how the item was binded. (e.g., paperback, hardback)
     @param details a small description necessary to describe the publication type
     All parameters are necessary for the creation of a publication type, and will be validated.
     */

    private MediaFormat _mediaFormat;
    private TypeOfBinding _typeOfBinding;
    private String _details;

    protected PublicationType (MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        if(mediaFormat==null){
            throw new IllegalArgumentException("Media Format can't be null");
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

    // --------------------------------- US004 starts ---------------------------------

    /**
     * Returns the media format.
     * @return MediaFormat
     */
    public MediaFormat getMediaFormat() {
        return _mediaFormat;
    }


    /**
     * Returns the type of binding.
     * @return Type of binding.
     */
    public TypeOfBinding getTypeOfBinding() {
        return _typeOfBinding;
    }


    /**
     * Returns the publication type details.
     * @return Publication details.
     */
    public String getDetails() {
        return _details;
    }

    // --------------------------------- US004 ends ---------------------------------

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PublicationType that = (PublicationType) obj;
        return  _typeOfBinding == that._typeOfBinding && _details.equals(that._details);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_typeOfBinding, _details);
    }

}
