package TOPSECRET.dto;


import TOPSECRET.domain.ValueObjects.PublicationTypeId;


/**
 * DTO representing PublicationType for US004.
 */
public record US004PublicationTypeDTO(
        PublicationTypeId id,
        String mediaFormat
) {

    /**
     * Constructs a US004PublicationTypeDTO with validation.
     *
     * @param id unique identifier of the publication type
     * @param mediaFormat media format description
     * @throws IllegalArgumentException if id is null or mediaFormat is null/blank
     */
    public US004PublicationTypeDTO {
        if (id == null) {
            throw new IllegalArgumentException("PublicationTypeId cannot be null.");
        }
        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty.");
        }
    }
}
