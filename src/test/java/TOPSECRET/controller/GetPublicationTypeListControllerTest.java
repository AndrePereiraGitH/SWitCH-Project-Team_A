package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.dto.PublicationTypeMapper_US014;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicationTypeListControllerTest {

    @Test
    void PublicationTypeListControllerEmptyConstructorTest() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        GetPublicationTypeListController controller = new GetPublicationTypeListController(
                repository, mapper);

        // Act
        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationTypesOneElementConstructorTest() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType type;

        repository.getAllPublicationTypes();
        type = repository.createPublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Hardback");
        repository.addToList(type);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository, mapper);

        // Act
        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        // Assert
        assertEquals(1, result.size());
        PublicationTypeDTO_US014 dto = result.get(0);
        assertEquals(MediaFormat.BOOK, dto._mediaFormat);
        assertEquals(TypeOfBinding.HARDBACK, dto._typeOfBinding);
        assertEquals("Hardback", dto._details);
    }

    @Test
    void getAllPublicationTypesTwoElementsConstructorTest() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType type1;

        PublicationType type2;

        type1 = repository.createPublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Hardback");
        type2 = repository.createPublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

        repository.addToList(type1);
        repository.addToList(type2);

        repository.getAllPublicationTypes();

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository, mapper);

        // Act
        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d ->
                        d._mediaFormat == MediaFormat.BOOK &&
                        d._typeOfBinding == TypeOfBinding.HARDBACK &&
                        d._details.equals("Hardback")));

        assertTrue(result.stream().anyMatch(d ->
                        d._mediaFormat == MediaFormat.BOOK &&
                        d._typeOfBinding == TypeOfBinding.PAPERBACK &&
                        d._details.equals("Paperback")));
    }

}