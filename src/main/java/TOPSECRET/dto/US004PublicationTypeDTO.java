package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublicationTypeId;

/**
 * DTO representing PublicationType for US004.
 *
 * <p>
 * Carries identity and media format to the UI layer.
 * </p>
 */
public class US004PublicationTypeDTO {

    private PublicationTypeId _id;
    private String _mediaFormat;

    /**
     * Creates a PublicationTypeDTO.
     */
    public US004PublicationTypeDTO(PublicationTypeId id, String mediaFormat) {
        if (id == null) {
            throw new IllegalArgumentException("PublicationTypeId cannot be null.");
        }
        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty.");
        }

        this._id = id;
        this._mediaFormat = mediaFormat;
    }

    public String getMediaFormat() {
        return _mediaFormat;
    }

    public PublicationTypeId getId(){
        return _id;
    }
}
