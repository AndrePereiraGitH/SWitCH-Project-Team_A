package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.List;

/**
 * Data Transfer Object (DTO) for publication sample.
 * <p>
 * This record encapsulates the essential information about an item
 * for transfer between application layers. All fields are required and
 * validated at construction time — a {@link IllegalArgumentException} is
 * thrown if any parameter is {@code null}.
 * </p>
 *
 * @param pubTitle        the title of the publication; must not be {@code null}
 * @param authorName      the list of author names; must not be {@code null}
 * @param code            the unique publication code (e.g. ISBN); must not be {@code null}
 * @param publicationType the media format type of the publication (e.g. book or magazine); must not be {@code null}
 * @param seller          the owner of the publication; must not be {@code null}
 * @param pubStatus       the publication status (e.g. reading, read, sold, for_sale); must not be {@code null}
 * @param condition       the condition (e.g. new, like_new, good) of the publication; must not be {@code null}
 *
 * @throws IllegalArgumentException if any parameter is {@code null}
 * @see PublicationSample
 */
public record US023PublicationSampleDTO(String pubTitle, List<String> authorName, String code, String publicationType,
                                        String seller, String pubStatus, String condition) {

    public US023PublicationSampleDTO {
        if (pubTitle == null) throw new IllegalArgumentException("pubTitle cannot be null");
        if (authorName == null) throw new IllegalArgumentException("authorName cannot be null");
        if (code == null) throw new IllegalArgumentException("code cannot be null");
        if (publicationType == null) throw new IllegalArgumentException("publicationType cannot be null");
        if (seller == null) throw new IllegalArgumentException("seller cannot be null");
        if (pubStatus == null) throw new IllegalArgumentException("pubStatus cannot be null");
        if (condition == null) throw new IllegalArgumentException("condition cannot be null");
    }
}