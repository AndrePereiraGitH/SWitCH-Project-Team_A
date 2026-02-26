package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for GetPublicationTypeListController.
 *
 * <p>This test class verifies that the controller correctly retrieves
 * domain objects from the repository and converts them into DTOs.</p>
 *
 * <p>The tests focus on:</p>
 * <ul>
 *     <li>Correct list size</li>
 *     <li>Correct data mapping</li>
 *     <li>Empty list behavior</li>
 * </ul>
 */
class GetPublicationTypeListControllerTest {

    @Test
    void getAllPublicationTypesWhenRepositoryIsEmptyReturnsEmptyList() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationTypesOnePublicationTypeReturnsOneDTO() {
        // Arrange
        PublicationTypeRepository repository = new PublicationTypeRepository();

        PublicationType type =
                repository.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Hardback");

        repository.addToList(type);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);
        assertEquals("BOOK", dto._mediaFormat);
        assertEquals("HARDBACK", dto._typeOfBinding);
        assertEquals("Hardback", dto._details);
    }

    @Test
    void getAllPublicationTypesMultiplePublicationTypesReturnsListDTOs() {
        PublicationTypeRepository repository = new PublicationTypeRepository();

        PublicationType t1 =
                repository.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Hardback");

        PublicationType t2 =
                repository.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.PAPERBACK,
                        "Paperback");

        repository.addToList(t1);
        repository.addToList(t2);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository);

        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d._details.equals("Hardback")));
        assertTrue(result.stream().anyMatch(d -> d._details.equals("Paperback")));
    }
}