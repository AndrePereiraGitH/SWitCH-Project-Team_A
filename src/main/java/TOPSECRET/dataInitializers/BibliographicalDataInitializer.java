package TOPSECRET.dataInitializers;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class BibliographicalDataInitializer implements ApplicationRunner {

    private final IBibliographicalDataRepository repository;
    private final BibliographicalDataFactory bibliographicalDataFactory;

    @Override
    public void run(ApplicationArguments args) throws InstanceAlreadyExistsException {
        log.info("[BibliographicalDataInitializer] Seeding bibliographical data...");

        seedBibliographicalData(
                new BibliographicalDataId("bd-001"),
                "The Lord of the Rings",
                LocalDate.of(1954, 7, 29),
                Locale.ENGLISH,
                new AuthorId("AUTH1"),
                new GenreId("GENRE4"),  // Fantasy
                "One Ring to rule them all."
        );

        seedBibliographicalData(
                new BibliographicalDataId("bd-002"),
                "Dom Quixote",
                LocalDate.of(1605, 1, 16),
                new Locale("es"),
                new AuthorId("AUTH3"),
                new GenreId("GENRE8"),  // Historical Fiction
                "En un lugar de La Mancha..."
        );

        seedBibliographicalData(
                new BibliographicalDataId("bd-003"),
                "Os Lusíadas",
                LocalDate.of(1572, 1, 1),
                new Locale("pt"),
                new AuthorId("AUTH3"),
                new GenreId("GENRE1"),  // Drama
                "As armas e os barões assinalados..."
        );

        seedBibliographicalData(
                new BibliographicalDataId("bd-004"),
                "Foundation",
                LocalDate.of(1951, 5, 1),
                Locale.ENGLISH,
                new AuthorId("AUTH2"),
                new GenreId("GENRE2"),  // Science Fiction
                "It was warm in Trantor that day..."
        );

        log.info("[BibliographicalDataInitializer] Done — 4 bibliographical data seeded.");
    }

    private void seedBibliographicalData(
            BibliographicalDataId id,
            String title,
            LocalDate publicationDate,
            Locale originalLanguage,
            AuthorId authorId,
            GenreId genreId,
            String chunksOfText
    ) throws InstanceAlreadyExistsException {

        BibliographicalData bibliographicalData =
                bibliographicalDataFactory.create(
                        id,
                        title,
                        List.of(authorId),
                        originalLanguage,
                        publicationDate,
                        List.of(genreId),
                        chunksOfText
                );

        repository.save(bibliographicalData);
    }
}