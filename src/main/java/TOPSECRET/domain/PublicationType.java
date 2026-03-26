package TOPSECRET.domain;


import java.util.Objects;

public class PublicationType {

    /**
     Represents the publication type of an item.
     It contains parameters like media format.
     @param mediaFormat identifies the type of item, such as book or magazine
     All parameters are necessary for the creation of a publication type, and will be validated.
     */

    private String _mediaFormat;

    protected PublicationType (String mediaFormat)
    {
        if(mediaFormat==null || mediaFormat.trim().isEmpty()){
            throw new IllegalArgumentException("Media Format can't be null");
        }

        this._mediaFormat = mediaFormat.trim();

    }

    // --------------------------------- US004 starts ---------------------------------

    /**
     * Returns the media format.
     * @return MediaFormat
     */
    public String getMediaFormat() {
        return _mediaFormat;
    }


    // --------------------------------- US004 ends ---------------------------------

   @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PublicationType that = (PublicationType) obj;
        return  _mediaFormat.equalsIgnoreCase(that._mediaFormat);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_mediaFormat.toLowerCase());
    }

}
