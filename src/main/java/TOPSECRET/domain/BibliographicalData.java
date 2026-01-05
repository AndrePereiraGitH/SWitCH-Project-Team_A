package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BibliographicalData {

    // Attributes
    private String title;
    private Profile[] authors;
//    private List<String> authors;
    private Locale language;
//    private Publisher publisher;              TODO descomentar após classe Publisher ser criada
    private String publisher;
    private LocalDate publishDate;
    private Genre[] genre;
//    private List<String> genre;
    private int numberOfPages;
    private String chunksOfText;

    // Constructor
//    public BibliographicalData(String title, Profile[] authors, Locale language, Publisher publisher, LocalDate publishDate,
//                               Genre[] genre, int numberOfPages, String chunksOfText) {
    public BibliographicalData(String title, Profile[] authors, Locale language, String publisher, LocalDate publishDate,
                               Genre[] genre, int numberOfPages, String chunksOfText) {
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
    public Profile[] getAuthors() {
        return authors;
    }

    public Genre[] getGenres() {
        return genre;
    }

}
