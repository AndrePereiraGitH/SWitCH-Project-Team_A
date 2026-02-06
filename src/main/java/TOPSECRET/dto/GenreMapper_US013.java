package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreMapper_US013 {
    public List<GenreDTO_US013> toDTO(List<Genre> genres) {
        List<GenreDTO_US013> GenresDTO = new ArrayList<>();
        for (Genre genre : genres) {
            GenresDTO.add(new GenreDTO_US013(genre));
        }
        return GenresDTO;
    }
}
