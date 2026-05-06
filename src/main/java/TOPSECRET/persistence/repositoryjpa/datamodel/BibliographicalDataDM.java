package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JPA data model representing bibliographical data for persistence.
 * <p>
 * This class is mapped to the {@code bibliographical_data} table and is used
 * exclusively for database interaction. It is part of the infrastructure layer
 * and should not contain business logic.
 * <p>
 * Conversion between this persistence model and the domain model is handled
 * by a dedicated assembler (e.g., {@code BibliographicalDataAssembler}).
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bibliographical_data")
public class BibliographicalDataDM {

    @EmbeddedId
    private BibliographicalDataId id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "original_language")
    private String originalLanguage;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "chunks_of_text")
    private String chunksOfText;

    @ElementCollection
    @CollectionTable(
            name = "bibliographical_data_authors",
            joinColumns = @JoinColumn(name = "bibliographical_data_id")
    )
    @Column(name = "author_id", nullable = false)
    private List<AuthorId> authorIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "bibliographical_data_genres",
            joinColumns = @JoinColumn(name = "bibliographical_data_id")
    )
    @Column(name = "genre_id", nullable = false)
    private List<GenreId> genreIds = new ArrayList<>();

    
    /**
     * Constructs a persistence model instance with all required fields.
     *
     * @param id                unique identifier (must not be null)
     * @param title             title of the work (must not be null)
     * @param originalLanguage  original language (must not be null)
     * @param publicationDate   publication date (nullable)
     * @param chunksOfText      textual content (nullable)
     * @param authorIds         list of author identifiers
     * @param genreIds          list of genre identifiers
     * @throws NullPointerException if id, title, or originalLanguage are null
     */
    public BibliographicalDataDM(
            BibliographicalDataId id,
            String title,
            String originalLanguage,
            LocalDate publicationDate,
            String chunksOfText,
            List<AuthorId> authorIds,
            List<GenreId> genreIds
    ) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.originalLanguage = originalLanguage;
        this.publicationDate = publicationDate;
        this.chunksOfText = chunksOfText;
        this.authorIds = new ArrayList<>(Objects.requireNonNull(authorIds, "Author ids cannot be null"));
        this.genreIds = new ArrayList<>(Objects.requireNonNull(genreIds, "Genre ids cannot be null"));
    }

}
