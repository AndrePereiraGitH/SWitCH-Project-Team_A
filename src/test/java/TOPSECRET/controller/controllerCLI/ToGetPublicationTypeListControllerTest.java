package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US014PublicationTypeDTO;
import TOPSECRET.dto.US014PublicationTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetPublicationTypeListControllerTest {

    private IPublicationTypeRepository repository;
    private US014PublicationTypeMapper mapper;

    private ToGetPublicationTypeListController sut;

    @BeforeEach
    void setUp() {

        repository = mock(IPublicationTypeRepository.class);
        mapper = mock(US014PublicationTypeMapper.class);

    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetPublicationTypeListController(null, mapper)
        );
    }

    @Test
    void shouldThrowExceptionWhenMapperIsNull() {

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetPublicationTypeListController(repository, null)
        );
    }

    @Test
    void findAllWhenRepositoryIsEmptyReturnsEmptyList() {

        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        sut = new ToGetPublicationTypeListController(repository, mapper);

        // Act
        List<US014PublicationTypeDTO> result = sut.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void findAllPublicationTypesOnePublicationTypeReturnsOneDTO() {

        // Arrange
        PublicationType typeDouble = mock(PublicationType.class);
        PublicationTypeId idDouble = mock(PublicationTypeId.class);

        List<PublicationType> publicationTypeList = List.of(typeDouble);

        US014PublicationTypeDTO dtoDouble = mock(US014PublicationTypeDTO.class);

        when(repository.findAll()).thenReturn(publicationTypeList);
        when(mapper.toDTOList(publicationTypeList)).thenReturn(List.of(dtoDouble));
        when(dtoDouble.id()).thenReturn("PT1");
        when(dtoDouble.mediaFormat()).thenReturn("Book");

        sut = new ToGetPublicationTypeListController(repository, mapper);

        // Act
        List<US014PublicationTypeDTO> result = sut.findAll();
        US014PublicationTypeDTO dto = result.get(0);

        // Assert
        assertEquals(1, result.size());
        assertEquals("PT1", dto.id());
        assertEquals("Book", dto.mediaFormat());
    }

    @Test
    void findAllPublicationTypesMultipleReturnsListDTOs() {

        // Arrange
        PublicationType t1Double = mock(PublicationType.class);
        PublicationType t2Double = mock(PublicationType.class);

        PublicationTypeId idDouble1 = mock(PublicationTypeId.class);
        PublicationTypeId idDouble2 = mock(PublicationTypeId.class);

        List<PublicationType> publicationTypeList = List.of(t1Double, t2Double);

        US014PublicationTypeDTO dto1 = mock(US014PublicationTypeDTO.class);
        US014PublicationTypeDTO dto2 = mock(US014PublicationTypeDTO.class);

        List<US014PublicationTypeDTO> dtoList = List.of(dto1, dto2);

        when(repository.findAll()).thenReturn(List.of(t1Double, t2Double));
        when(mapper.toDTOList(publicationTypeList)).thenReturn(dtoList);

        when(dto1.mediaFormat()).thenReturn("Book");
        when(dto2.mediaFormat()).thenReturn("Magazine");

        sut = new ToGetPublicationTypeListController(repository, mapper);

        // Act
        List<US014PublicationTypeDTO> result = sut.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.mediaFormat().equals("Book")));
        assertTrue(result.stream().anyMatch(dto -> dto.mediaFormat().equals("Magazine")));

    }
}