package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictureTest {
    @Test
    void shouldCreatePicture() {
        Picture picture = new Picture("a.png");
        assertEquals("a.png", picture.get_picture());
    }

    @Test
    void shouldThrowWhenPictureIsNull(){
        assertThrows(IllegalArgumentException.class, () -> new Picture(null));

    }
    @Test
    void shouldThrowWhenPictureIsBlank(){
        assertThrows(IllegalArgumentException.class, () -> new Picture(" "));
    }
}