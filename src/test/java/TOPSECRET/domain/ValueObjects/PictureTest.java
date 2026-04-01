package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictureTest {

    // ============ CONSTRUCTOR TESTS ============
    @Test
    void shouldCreatePicture() {
        // Arrange + Act
        Picture picture = new Picture("a.png");

        // Assert
        assertEquals("a.png", picture.getPicture());
    }

    @Test
    void shouldThrowWhenPictureIsNull(){
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Picture(null));

    }
    @Test
    void shouldThrowWhenPictureIsBlank(){
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Picture(" "));
    }

    // ========== GETTER TESTS ==========

    @Test
    void shouldReturnExactPictureValue() {
        // Arrange
        String path = "images/photo1.png";

        // Act
        Picture picture = new Picture(path);

        // Assert
        assertEquals(path, picture.getPicture());
    }

    // ========== EQUALS METHOD TESTS ==========

    @Test
    void equalsShouldReturnTrueForSameInstance() {
        // Arrange + Act
        Picture picture = new Picture("img.png");

        // Assert
        assertEquals(picture, picture);
    }

    @Test
    void equalsShouldReturnTrueForSameValue() {
        // Arrange + Act
        Picture p1 = new Picture("img.png");
        Picture p2 = new Picture("img.png");

        // Assert
        assertEquals(p1, p2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentValues() {
        // Arrange + Act
        Picture p1 = new Picture("img1.png");
        Picture p2 = new Picture("img2.png");

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange + Act
        Picture picture = new Picture("img.png");

        // Assert
        assertNotEquals(picture, null);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange + Act
        Picture picture = new Picture("img.png");

        // Assert
        assertNotEquals(picture, "not a picture");
    }
}