package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class BibliographicalDataFactoryTest {

        @Test
        void create_shouldReturnValidBibliographicalData() {

            // Arrange

            AuthorId author = mock(AuthorId.class);
            GenreID genre = mock(GenreID.class);

            try(MockedConstruction<BibliographicalData> mocked =
                    mockConstruction(BibliographicalData.class,
                            (mock, context) -> {})){

                BibliographicalDataFactory factory =
                        new BibliographicalDataFactory();

                // Act
                BibliographicalData result = factory.create(
                        "Title",
                        List.of(author),
                        Locale.ENGLISH,
                        LocalDate.of(2010, 10, 10),
                        List.of(genre),
                        "Some text"
                );

                // Assert
                assertNotNull(result);
                assertEquals(1, mocked.constructed().size());

                BibliographicalData constructedBibliographicalData = mocked.constructed().get(0);
                assertSame(result, constructedBibliographicalData);
            }
        }


}