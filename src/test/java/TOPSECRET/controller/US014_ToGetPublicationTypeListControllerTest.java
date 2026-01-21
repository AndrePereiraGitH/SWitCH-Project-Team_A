package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US014_ToGetPublicationTypeListControllerTest {

    @Test
    void PublicationTypeListControllerEmptyConstructorTest() {
        // Arrange
        PublicationTypeRepository publicationTypeRepository = new PublicationTypeRepository();

        US014_ToGetPublicationTypeListController controller = new US014_ToGetPublicationTypeListController(
                publicationTypeRepository);

        // Act
        List<PublicationType> result = controller.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void PublicationTypeListControllerOneElementConstructorTest() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationType type = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "details book"
        );

        repository.getAllPublicationTypes();
        repository.addAndValidatePublicationType(type);

        US014_ToGetPublicationTypeListController controller =
                new US014_ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationType> result = controller.getAllPublicationTypes();

        // Assert
        assertEquals(1, result.size());
        assertEquals(type, result.get(0));
    }

    @Test
    void PublicationTypeListControllerTwoElementConstructorTest() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationType type1 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.PAPERBACK,
                "details book"
        );
        PublicationType type2 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.HARDBACK,
                "details book"
        );

        repository.getAllPublicationTypes();
        repository.addAndValidatePublicationType(type1);
        repository.addAndValidatePublicationType(type2);

        US014_ToGetPublicationTypeListController controller =
                new US014_ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationType> result = controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(type1));
        assertTrue(result.contains(type2));
    }

}