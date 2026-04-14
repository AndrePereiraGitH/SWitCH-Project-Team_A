package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;

import java.util.List;

/**
 * Data Transfer Object (DTO) for bibliographical data (publication).
 * <p>
 * This record encapsulates the essential information about a publication
 * for transfer between application layers. All fields are required and
 * validated at construction time — a {@link IllegalArgumentException} is
 * thrown if any parameter is {@code null}.
 * </p>
 *
 * @param bdId            the identifier of the publication; must not be {@code null}
 * @param pubTitle        the title of the publication; must not be {@code null}
 * @param authorName      the list of author names; must not be {@code null}
 * @throws IllegalArgumentException if any parameter is {@code null}
 * @see BibliographicalData
 */
public record US023BibliographicalDataDTO(BibliographicalDataID bdId, String pubTitle, List<String> authorName) {

    public US023BibliographicalDataDTO {
        if (bdId == null) throw new IllegalArgumentException("bibliographicalDataID is null");
        if (pubTitle == null) throw new IllegalArgumentException("pubTitle cannot be null");
        if (authorName == null) throw new IllegalArgumentException("authorName cannot be null");
    }
}