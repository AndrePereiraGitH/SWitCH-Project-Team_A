package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.Genre;

import java.util.ArrayList;
import java.util.List;

public class BibliographicalDataDTO_US024 {

    public String _publicationTitle;
    public List<String> _authors;
    public List<String> _publicationGenre;

    public BibliographicalDataDTO_US024(String publicationTitle, List<String> authors, List<String> publicationGenre) {
        _publicationTitle = publicationTitle;
        _authors = authors;
        _publicationGenre = publicationGenre;
    }

    public static List<BibliographicalDataDTO_US024> toDTOS(List<BibliographicalData> listOfAllPublicationBD){
        List<BibliographicalDataDTO_US024> listOfAllBiblioDataDTO = new ArrayList<>();

        for(BibliographicalData bd: listOfAllPublicationBD){
            listOfAllBiblioDataDTO.add(toDTO(bd));
        }

        return listOfAllBiblioDataDTO;
    }

    public static BibliographicalDataDTO_US024 toDTO(BibliographicalData bdPublication){

        if(bdPublication==null){
            throw new NullPointerException("Publication can not be null");
        }

        String publicationTitleDTO = bdPublication.getTitle();

        List<Author> authors = bdPublication.getAuthors();
        List<String> authorsDTO = new ArrayList<>();
        for(Author author: authors){
            authorsDTO.add(author.getProfile().getName());
        }

        List<Genre> genres = bdPublication.getGenres();
        List<String> genresDTO = new ArrayList<>();
        for(Genre genre: genres){
            genresDTO.add(genre.getTitle());
        }

        BibliographicalDataDTO_US024 bdDTO = new BibliographicalDataDTO_US024(publicationTitleDTO,authorsDTO,genresDTO);

        return bdDTO;
    }
}
