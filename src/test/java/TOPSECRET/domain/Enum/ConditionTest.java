package TOPSECRET.domain.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionTest {

    @Test
    void shouldReturnCorrectDescriptions() {
        assertEquals("New", Condition.NEW.getDescription());
        assertEquals("Like New", Condition.LIKE_NEW.getDescription());
        assertEquals("Very Good", Condition.VERY_GOOD.getDescription());
        assertEquals("Good", Condition.GOOD.getDescription());
        assertEquals("Acceptable", Condition.ACCEPTABLE.getDescription());
        assertEquals("Poor", Condition.POOR.getDescription());
    }

    @Test
    void shouldContainAllExpectedValues() {
        assertEquals(6, Condition.values().length);
    }

}