package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublicationTypeId;


/**
 * DTO representing PublicationType for US004.
 */
public record US004PublicationTypeDTO(
        PublicationTypeId id,
        String mediaFormat
) {

    public US004PublicationTypeDTO {
        if (id == null) {
            throw new IllegalArgumentException("PublicationTypeId cannot be null.");
        }
        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty.");
        }
    }
}
