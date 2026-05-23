package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
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
    void shouldThrowExceptionWhenInstantiatingUtilityClass() throws Exception {

        Constructor<PublisherMapper> constructor =
                PublisherMapper.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        InvocationTargetException ex =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        assertEquals("Utility class", ex.getCause().getMessage());
    }

    @Test
    void shouldConvertPublisherToDTO() {

        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Test Publisher");
        when(publisher.id()).thenReturn(publisherID);
        when(publisherID.toString()).thenReturn("PUB-1");

        ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

        assertEquals("Test Publisher", result.getName());
        assertEquals("PUB-1", result.getPublisherId());
    }

    @Test
    void shouldThrowExceptionWhenPublisherIsNull() {

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTO(null)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldConvertListOfPublishersToDTOList() {

        Publisher p1 = mock(Publisher.class);
        Publisher p2 = mock(Publisher.class);

        PublisherID id1 = mock(PublisherID.class);
        PublisherID id2 = mock(PublisherID.class);

        when(p1.getName()).thenReturn("Publisher One");
        when(p2.getName()).thenReturn("Publisher Two");

        when(p1.id()).thenReturn(id1);
        when(p2.id()).thenReturn(id2);

        when(id1.toString()).thenReturn("PUB-1");
        when(id2.toString()).thenReturn("PUB-2");

        List<Publisher> publishers = List.of(p1, p2);

        List<ResponsePublisherDTO> result =
                PublisherMapper.toDTOList(publishers);

        assertEquals(2, result.size());
        assertEquals("Publisher One", result.get(0).getName());
        assertEquals("Publisher Two", result.get(1).getName());
    }

    @Test
    void shouldThrowExceptionWhenListOfPublishersIsNull() {

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOList(null)
                );

        assertEquals("Publishers list cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListContainsNullPublisher() {

        Publisher validPublisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(validPublisher.getName()).thenReturn("Test");
        when(validPublisher.id()).thenReturn(id);
        when(id.toString()).thenReturn("PUB-1");

        List<Publisher> publishers = Arrays.asList(
                validPublisher,
                null
        );

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOList(publishers)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnImmutableList() {

        Publisher publisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Publisher");
        when(publisher.id()).thenReturn(id);
        when(id.toString()).thenReturn("PUB-1");

        List<ResponsePublisherDTO> result =
                PublisherMapper.toDTOList(List.of(publisher));

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(mock(ResponsePublisherDTO.class))
        );
    }

    @Test
    void toEmailShouldCreateEmailUsernameWhenEmailIsValid() {

        // ARRANGE
        RequestPublisherDTO dto =
                new RequestPublisherDTO(
                        "name",
                        "123456789",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234-123",
                        "1"
                );

        // ACT
        Email result = PublisherMapper.toEmail(dto);

        // ASSERT
        assertEquals("john", result.getUserName());
    }

    @Test
    void toEmailShouldCreateEmailDomainWhenEmailIsValid() {

        // ARRANGE
        RequestPublisherDTO dto =
                new RequestPublisherDTO(
                        "name",
                        "123456789",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234-123",
                        "1"
                );

        // ACT
        Email result = PublisherMapper.toEmail(dto);

        // ASSERT
        assertEquals("test.com", result.getDomain());
    }

    @Test
    void shouldThrowExceptionWhenRequestPublisherDTOIsNullInToEmail() {

        // ACT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toEmail(null)
                );

        // ASSERT
        assertEquals(
                "RequestPublisherDTO cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoPhone() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("911111111", contact.getPhoneNum());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoExtension() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("1", contact.getExtension());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoEmailUsername() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("john", contact.getEmail().getUserName());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoEmailDomain() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("gmail.com", contact.getEmail().getDomain());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoStreet() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("Main Street", contact.getAddress().getStreet());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoNumber() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("10", contact.getAddress().getNumber());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoZipCode() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("1000-001", contact.getAddress().getZipCode());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoCityId() {

        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher",
                "123456789",
                "911111111",
                "1",
                "john@gmail.com",
                "Main Street",
                "10",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("1", contact.getAddress().getCityID().getCityID());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDTOIsNullInToContact() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> PublisherMapper.toContact(null)
        );
    }
}