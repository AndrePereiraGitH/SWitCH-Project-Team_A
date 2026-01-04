//package TOPSECRET.domain;
//
//import TOPSECRET.domain.Enum.Condition;
//import TOPSECRET.domain.Enum.TypeOfBinding;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MagazineTest {
//
//    @Test
//    void shouldCreateMagazine() {
//        Code issn = new Code("1234-5678");
//        Item item = validItem();
//
//        Magazine magazine = new Magazine(issn, item);
//
//        assertSame(issn, magazine.getIssn());
//        assertSame(item, magazine.getItem());
//    }
//
//    @Test
//    void shouldThrowWhenIssnIsNull() {
//        Item item = validItem();
//
//        assertThrows(IllegalArgumentException.class, () -> new Magazine(null, item));
//    }
//
//    @Test
//    void shouldThrowWhenItemIsNull() {
//        Code issn = new Code("1234-5678");
//
//        assertThrows(IllegalArgumentException.class, () -> new Magazine(issn, null));
//    }
//
//    private Item validItem() {
//        BibliographicalData bib = new BibliographicalData(
//                "Title",
//                List.of("Author"),
//                Locale.ENGLISH,
//                "Publisher",
//                LocalDate.of(2020, 1, 1),
//                List.of("Genre"),
//                100,
//                "Text"
//        );
//
//        return new Item(
//                Condition.NEW,
//                0.1,
//                List.of(), // pictures != null
//                new Dimensions(1, 1, 1),
//                TypeOfBinding.HARDBACK,
//                "1st edition",
//                bib
//        );
//    }
//}
