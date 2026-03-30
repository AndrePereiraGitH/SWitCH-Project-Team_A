//package TOPSECRET.repository;
//
//import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
//import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
//import TOPSECRET.domain.PublicationType.PublicationType;
//import TOPSECRET.domain.ValueObjects.PublicationTypeId;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//class PublicationTypeRepositoryTest {
//
//
//    @Test
//    void createPublicationTypeDelegatesToFactory()
//    {
//        // ARRANGE
//        PublicationTypeFactory factory = mock(PublicationTypeFactory.class);
//        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);
//
//        String mediaFormat = "Book";
//        PublicationTypeId pubId = PublicationTypeId.generate();
//        PublicationType expected = mock(PublicationType.class);
//
//        when(factory.create(pubId, mediaFormat)).thenReturn(expected);
//
//        // ACT
//        PublicationType actual = repo.create(pubId, mediaFormat);
//
//        assertEquals(expected, actual);
//        verify(factory).create(pubId, mediaFormat);
//    }
//
//    @Test
//    void invalidPublicationShouldThrowWhenCreating()
//    {
//        // ARRANGE
//        PublicationTypeFactory factory = mock(PublicationTypeFactory.class);
//        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);
//        PublicationTypeId pubId = PublicationTypeId.generate();
//        String mediaFormat = "Book";
//
//        when(factory.create(pubId, mediaFormat)).thenThrow(new IllegalArgumentException());
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class, () -> repo.create(pubId, mediaFormat));
//
//        verify(factory).create(pubId, mediaFormat);
//    }
//
//}