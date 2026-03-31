package TOPSECRET.controller;



import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import java.util.List;


public class US004ToRegisterAppraisalEntityController {

    private IGenreRepository _genreRepository;
    private IPublicationTypeRepository _publicationTypeRepository;
    private IAppraisalEntityRepository _appraisalEntityRepository;


    public US004ToRegisterAppraisalEntityController(
            IGenreRepository genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            IAppraisalEntityRepository appraisalEntityRepository) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }

        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepositoryMem cannot be null.");
        }

        if (appraisalEntityRepository == null) {
            throw new IllegalArgumentException("AppraisalEntityRepository cannot be null.");
        }

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityRepository = appraisalEntityRepository;
    }


    public List<GenreDetailsDTO_US004> getAllGenres() {

        List<Genre> genres = _genreRepository.getAllGenres();

        return GenreDetailsDTO_US004.genreToDTO(genres);
    }


    public List<PublicationTypeDTO_US004> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                _publicationTypeRepository.getAllPublicationTypes();

        List<PublicationTypeDTO_US004> result =
                PublicationTypeDTO_US004.publicationTypesToDTO(publicationTypes);

        return result;
    }


    public AppraisalEntity registerAppraisalEntity(
            String name,
            Contact contact,
            List<GenreDetailsDTO_US004> selectedGenresDTO,
            List<PublicationTypeDTO_US004> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre must be selected.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType must be selected.");
        }


        return _appraisalEntityRepository.createAndAdd(
                name,
                contact,
                selectedGenresDTO,
                selectedPublicationTypesDTO
        );
    }

}
