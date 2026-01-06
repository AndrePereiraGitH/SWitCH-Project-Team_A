package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private User buyer;
    private Cart cart;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        buyer = new User();
        cart = new Cart(buyer);
/*
        item1 = new Item(
                Condition.NEW,
                1.2,
                List.of("pic1.jpg"),
                new Dimensions(10, 20, 5),
                TypeOfBinding.HARDCOVER,
                "1st Edition",
                new BibliographicalData("Author 1", "Title 1", 2020)
        );

        item2 = new Item(
                Condition.USED,
                2.0,
                List.of("pic2.jpg"),
                new Dimensions(15, 25, 8),
                TypeOfBinding.PAPERBACK,
                "2nd Edition",
                new BibliographicalData("Author 2", "Title 2", 2018)
        );

 */
    }


    // Cart tem 1 Buyer
    @Test
    void cartShouldBeCreatedWithBuyer() {
        assertEquals(buyer, cart.getBuyer());
    }

    // Cart deve iniciar vazio
    @Test
    void cartShouldStartEmpty() {
        assertTrue(cart.getItemsList().isEmpty());
    }

    // Cart deve iniciar sem transaction
    @Test
    void cartShouldStartWithNoTransaction() {
      //  assertNull(cart.getTransaction());
    }

    // Cart deve adicionar itens
    @Test
    void cartShouldAddItem() {
        cart.addItem(item1);

        assertEquals(1, cart.getItemsList().size());
        assertTrue(cart.getItemsList().contains(item1));
    }

    // Cart deve remover itens
    @Test
    void cartShouldRemoveItem() {
        cart.addItem(item1);
        cart.removeItem(item1);

        assertTrue(cart.getItemsList().isEmpty());
    }
}
