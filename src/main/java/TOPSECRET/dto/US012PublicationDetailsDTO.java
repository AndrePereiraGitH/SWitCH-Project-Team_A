package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.List;

/**
 * Data Transfer Object (DTO) for publication details.
 * <p>
 * This record encapsulates the essential information about a publication
 * for transfer between application layers. All fields are required and
 * validated at construction time — a {@link IllegalArgumentException} is
 * thrown if any parameter is {@code null}.
 * </p>
 *
 * @param pubTitle        the title of the publication; must not be {@code null}
 * @param authorName      the list of author names; must not be {@code null}
 * @param code            the unique publication code (e.g. ISBN); must not be {@code null}
 * @param publicationType the binding or format type of the publication (e.g. hardcover, paperback); must not be {@code null}
 * @throws IllegalArgumentException if any parameter is {@code null}
 * @see PublicationSample
 */
public record US012PublicationDetailsDTO(String pubTitle, List<String> authorName, String code, String publicationType) {

    public US012PublicationDetailsDTO {
        if (pubTitle == null) throw new IllegalArgumentException("pubTitle cannot be null");
        if (authorName == null) throw new IllegalArgumentException("authorName cannot be null");
        if (code == null) throw new IllegalArgumentException("code cannot be null");
        if (publicationType == null) throw new IllegalArgumentException("publicationType cannot be null");
    }
}