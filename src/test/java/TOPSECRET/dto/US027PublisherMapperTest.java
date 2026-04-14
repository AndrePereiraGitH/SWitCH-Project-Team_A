package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the US027PublisherMapper utility class.
 * <p>
 * These tests verify that the mapper correctly converts Publisher domain objects
 * into US027PublisherDTO instances.
 * The tests also ensure that invalid inputs (null publishers, null lists, or
 * lists containing null elements) trigger the expected exceptions, and that the
 * returned list is immutable.
 * </p>
 */
class US027PublisherMapperTest {

    // ===============================================
    // ================= Set Up ======================
    // ===============================================

    private PublisherID publisherIdDouble;
    private String name;
    private Publisher publisherDouble;

    @BeforeEach
    void setUp() {
        publisherIdDouble = mock(PublisherID.class);
        name = "Porto Editora";

        publisherDouble = mock(Publisher.class);
        when(publisherDouble.id()).thenReturn(publisherIdDouble);
        when(publisherDouble.getName()).thenReturn(name);
    }

    // =================================================================================================================
    // ================= CONSTRUCTOR ===================================================================================
    // =================================================================================================================

    @Test
    void constructor_ShouldThrowUnsupportedOperationExceptionWhenInstantiated() throws Exception {
        //Arrange
        Constructor<US027PublisherMapper> constructor = US027PublisherMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        //Act + Assert
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }


    // =================================================================================================================
    // ======== toDTO ==================================================================================================
    // =================================================================================================================
    
    @Test
    void toDTO_ShouldReturnDTOWhenPublisherIsValid() {
        // Act
        US027PublisherDTO dto = US027PublisherMapper.toDTO(publisherDouble);

        // Assert
        assertNotNull(dto);
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublisherIsNull() {
        // Assert
        assertThrows(NullPointerException.class, () -> US027PublisherMapper.toDTO(null));
    }

    // =================================================================================================================
    // ======== toDTOs =================================================================================================
    // =================================================================================================================

    @Test
    void toDTOs_ShouldConvertListCorrectly() {
        // Arrange
        Publisher newPublisherDouble = mock(Publisher.class);
        PublisherID newPublisherIdDouble = mock(PublisherID.class);
        when(newPublisherDouble.id()).thenReturn(newPublisherIdDouble);
        when(newPublisherDouble.getName()).thenReturn("Penguin Books");

        List<Publisher> publishers = List.of(publisherDouble, newPublisherDouble);

        // Act
        List<US027PublisherDTO> dtos = US027PublisherMapper.toDTOs(publishers);

        // Assert
        assertEquals(2, dtos.size());
    }

    @Test
    void toDTOs_ShouldReturnImmutableList() {
        // Arrange
        List<Publisher> publishers = List.of(publisherDouble);

        // Act
        List<US027PublisherDTO> dtos = US027PublisherMapper.toDTOs(publishers);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> dtos.add(
                new US027PublisherDTO(publisherIdDouble, "Casa das Letras")));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListIsNull() {
        // Assert
        assertThrows(NullPointerException.class, () -> US027PublisherMapper.toDTOs(null));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListContainsNullElement() {
        // Arrange
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(publisherDouble);
        publishers.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> US027PublisherMapper.toDTOs(publishers));
    }
}