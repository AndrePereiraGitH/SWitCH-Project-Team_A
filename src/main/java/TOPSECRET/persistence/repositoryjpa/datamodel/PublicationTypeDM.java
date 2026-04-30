package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publication_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicationTypeDM {

    @EmbeddedId
    private PublicationTypeId id;

    @Column(name = "media_format", nullable = false)
    private String mediaFormat;

    /**
     * Constructs a PublicationTypeDM with all required fields.
     *
     * @param id the publication type identifier
     * @param mediaFormat the publication format (e.g., Book, Magazine)
     * @throws IllegalArgumentException if id is null or mediaFormat is invalid
     */
    public PublicationTypeDM (
            PublicationTypeId id,
            String mediaFormat
    ) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty");
        }

        this.id = id;
        this.mediaFormat = mediaFormat;
    }
}