package TOPSECRET.dto.appraisalEntity;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.response.ResponseAppraisalEntityDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AppraisalEntityMapper}.
 *
 * <p>Verifies correct mapping between {@link RequestAppraisalEntityDTO}
 * and domain objects, including validation of inputs and conversions
 * of value objects and entity-to-DTO transformation.</p>
 */
class AppraisalEntityMapperTest {

    @Test
    void ToEmailShouldCreateEmailUsernameWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("john", result.getUserName());
    }

    @Test
    void ToEmailShouldCreateEmailDomainWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("test.com", result.getDomain());
    }

    @Test
    void ToContactShouldCreatePhoneNumber() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("912345678", result.getPhoneNum());
    }

    @Test
    void ToContactShouldCreateExtension() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("123", result.getExtension());
    }

    @Test
    void ToContactShouldCreateEmail() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getEmail());
    }

    @Test
    void ToContactShouldCreateAddress() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getAddress());
    }

    @Test
    void shouldThrowExceptionWhenUtilityClassConstructorIsCalled()
            throws Exception {

        // ARRANGE
        Constructor<AppraisalEntityMapper> constructor =
                AppraisalEntityMapper.class
                        .getDeclaredConstructor();

        constructor.setAccessible(true);

        // ACT + ASSERT
        InvocationTargetException result =
                assertThrows(
                        InvocationTargetException.class,
                        constructor::newInstance
                );

        assertInstanceOf(
                UnsupportedOperationException.class,
                result.getCause()
        );
    }

    @Test
    void ToContactShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toContact(dto)
        );
    }

    @Test
    void ToEmailShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toEmail(dto)
        );
    }

    @Test
    void ToAddressShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toAddress(dto)
        );
    }

    @Test
    void ToPubTypeIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of("A", "B"),
                        List.of()
                );

        // ACT
        List<PublicationTypeId> result = AppraisalEntityMapper.toPubTypeIds(dto);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void ToPubTypeIdsShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toPubTypeIds(dto)
        );
    }

    @Test
    void ToGenreIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of("Drama", "Action")
                );

        // ACT
        List<GenreId> result = AppraisalEntityMapper.toGenreIds(dto);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void ToGenreIdsShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toGenreIds(dto)
        );
    }

    @Test
    void ToDTOShouldMapAllFieldsCorrectly() {

        // ARRANGE
        AppraisalEntity entity = mock(AppraisalEntity.class);
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        PublicationTypeId pub1 = mock(PublicationTypeId.class);
        GenreId genre1 = mock(GenreId.class);

        when(entity.id()).thenReturn(id);
        when(id.getId()).thenReturn("1");

        when(entity.getName()).thenReturn("name");

        when(pub1.getId()).thenReturn("PUB1");
        when(genre1.getID()).thenReturn("GEN1");

        when(entity.getPublicationTypeIds()).thenReturn(List.of(pub1));
        when(entity.getGenreIds()).thenReturn(List.of(genre1));

        // ACT
        ResponseAppraisalEntityDTO result = AppraisalEntityMapper.toDTO(entity);

        // ASSERT
        assertEquals("1", result.getId());
        assertEquals("name", result.getName());
    }

    @Test
    void ToDTOShouldThrowExceptionWhenEntityIsNull() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toDTO(null)
        );
    }
}