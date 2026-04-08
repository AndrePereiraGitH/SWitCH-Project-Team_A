package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreID;


/**
 * DTO representing Genre for US004.
 *
 * <p>
 * Carries the identity and title of a Genre to the UI layer.
 * </p>
 */
public class US004GenreDTO {

    private GenreID _id;
    private String _title;

    /**
     * Creates a GenreDTO with the provided title.
     * @param title Genre title.
     */
    public US004GenreDTO(GenreID id, String title) {
        if (id == null) {
            throw new IllegalArgumentException("GenreID cannot be null.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }

        this._id = id;
        this._title = title;
    }

    public String getTitle() {
        return _title;
    }

    public GenreID getId() {
        return _id;
    }
}

