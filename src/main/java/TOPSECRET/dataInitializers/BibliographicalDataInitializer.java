package TOPSECRET.dataInitializers;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class BibliographicalDataInitializer implements ApplicationRunner {

    private final IBibliographicalDataRepository repository;
    private final BibliographicalDataFactory bibliographicalDataFactory;

    @Override
    public void run(ApplicationArguments args) throws InstanceAlreadyExistsException {

        log.info("[BibliographicalDataInitializer] Seeding bibliographical data...");

        seedBibliographicalData(
                "The Lord of the Rings",
                LocalDate.of(1954, 7, 29),
                Locale.ENGLISH,
                "One Ring to rule them all."
        );

        seedBibliographicalData(
                "Dom Quixote",
                LocalDate.of(1605, 1, 16),
                new Locale("es"),
                "En un lugar de La Mancha..."
        );

        seedBibliographicalData(
                "Os Lusíadas",
                LocalDate.of(1572, 1, 1),
                new Locale("pt"),
                "As armas e os barões assinalados..."
        );

        log.info("[BibliographicalDataInitializer] Done — 3 bibliographical data seeded.");
    }

    private void seedBibliographicalData(
            String title,
            LocalDate publicationDate,
            Locale originalLanguage,
            String chunksOfText
    ) throws InstanceAlreadyExistsException {

        BibliographicalData bibliographicalData =
                bibliographicalDataFactory.create(
                        title,
                        List.of(
                                new AuthorId("AUTH1")
                        ),
                        originalLanguage,
                        publicationDate,
                        List.of(
                                new GenreId("GENRE1")
                        ),
                        chunksOfText
                );

        repository.save(bibliographicalData);
    }
}