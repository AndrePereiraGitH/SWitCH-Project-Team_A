package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BibliographicalData {

    // Attributes
    private String title;
    private List<Profile> authors;
    private Locale language;
    private Publisher publisher;
    private LocalDate publishDate;
    private List<Genre> genre;
    private int numberOfPages;
    private String chunksOfText;

    // Constructor
    public BibliographicalData(String title, List<Profile> authors, Locale language, Publisher publisher, LocalDate publishDate,
                               List<Genre> genre, int numberOfPages, String chunksOfText) {
        this.title = title;
        this.authors = authors;
        this.language = language;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.genre = genre;
        this.numberOfPages = numberOfPages;
        this.chunksOfText = chunksOfText;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public List<Profile> getAuthors() {
        return authors;
    }

    public Locale getLanguage() {
        return language;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public List<Genre> getGenres() {
        return genre;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getChunksOfText() {
        return chunksOfText;
    }
}
