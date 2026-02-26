package TOPSECRET.domain;

public class GenreFactory {
    public Genre newGenre(String _genreType){
        return new Genre(_genreType);
    }
}
