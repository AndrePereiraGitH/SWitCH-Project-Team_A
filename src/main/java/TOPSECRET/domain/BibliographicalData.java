package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BibliographicalData {

    // Attributes
    private String title;
//    private Profile[] authors;                TODO descomentar após classe Profile ser criada
    private List<String> authors;
    private Locale language;                    // substitui Language (que seria um enum) por Locale
//    private Publisher publisher;              TODO descomentar após classe Publisher ser criada
    private String publisher;
    private LocalDate publishDate;
//    private Genre[] genre;                    TODO descomentar após classe Genre ser criada
    private List<String> genre;
    private int numberOfPages;
    private String chunksOfText;

    // Constructor
//    public BibliographicalData(String title, Profile[] authors, Locale language, Publisher publisher, LocalDate publishDate,
//                               Genre[] genre, int numberOfPages, String chunksOfText) {
    public BibliographicalData(String title, List<String> authors, Locale language, String publisher, LocalDate publishDate,
                               List<String> genre, int numberOfPages, String chunksOfText) {
        this.title = title;
        this.authors = authors;
        this.language = language;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.genre = genre;
        this.numberOfPages = numberOfPages;
        this.chunksOfText = chunksOfText;
    }
}
