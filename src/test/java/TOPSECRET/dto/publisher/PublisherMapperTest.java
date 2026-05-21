package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.publisher.response.PublisherDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherMapper}.
 */
class PublisherMapperTest {

    @Test
    void shouldThrowExceptionWhenInstantiatingUtilityClass() {

        // ACT + ASSERT
        InvocationTargetException exception =
                assertThrows(
                        InvocationTargetException.class,
                        () -> {
                            Constructor<PublisherMapper> constructor = PublisherMapper.class.getDeclaredConstructor();
                            constructor.setAccessible(true);
                            constructor.newInstance();
                        }
                );

        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    void shouldConvertPublisherToDTO() {

        // ARRANGE
        Publisher publisher = mock(Publisher.class);
        PublisherID pubId = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Test Publisher");
        when(publisher.id()).thenReturn(pubId);

        // ACT
        PublisherDTO result = PublisherMapper.toDTO(publisher);

        // ASSERT
        assertEquals("Test Publisher", result.getName());
        assertEquals(publisher.id(), result.getPublisherId());
    }

    @Test
    void shouldThrowExceptionWhenPublisherIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTO(null)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldConvertListOfPublishersToDTOs() {

        // ARRANGE
        Publisher publisher1 = mock(Publisher.class);
        Publisher publisher2 = mock(Publisher.class);
        PublisherID publisherId1 = mock(PublisherID.class);
        PublisherID publisherId2 = mock(PublisherID.class);


        when(publisher1.getName()).thenReturn("Publisher One");
        when(publisher2.getName()).thenReturn("Publisher Two");
        when(publisher1.id()).thenReturn(publisherId1);
        when(publisher2.id()).thenReturn(publisherId2);


        List<Publisher> publishers = List.of(publisher1, publisher2);

        // ACT
        List<PublisherDTO> result = PublisherMapper.toDTOs(publishers);

        // ASSERT
        assertEquals(2, result.size());

        assertEquals("Publisher One", result.get(0).getName());
        assertEquals("Publisher Two", result.get(1).getName());
    }

    @Test
    void shouldThrowExceptionWhenListOfPublishersIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOs(null)
                );

        assertEquals("Publishers list cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListContainsNullPublisher() {

        // Arrays.asList allows null elements, unlike List.of()
        List<Publisher> publishers = Arrays.asList(null, null);

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOs(publishers)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnImmutableList() {

        // ARRANGE
        Publisher publisher = mock(Publisher.class);
        PublisherID pubId = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Publisher");
        when(publisher.id()).thenReturn(pubId);

        List<Publisher> publishers = List.of(publisher);

        // ACT
        List<PublisherDTO> result = PublisherMapper.toDTOs(publishers);

        // ASSERT
        assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(mock(PublisherDTO.class))
        );
    }
}