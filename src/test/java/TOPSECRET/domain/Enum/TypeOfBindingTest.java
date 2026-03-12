package TOPSECRET.domain.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeOfBindingTest {

    @Test
    void shouldReturnCorrectLabelInToString() {
        assertEquals("Hardback", TypeOfBinding.HARDBACK.toString());
        assertEquals("Paperback", TypeOfBinding.PAPERBACK.toString());
        assertEquals("Saddle Stitching", TypeOfBinding.SADDLE_STICHING.toString());
        assertEquals("Perfect Binding", TypeOfBinding.PERFECT_BINDING.toString());
        assertEquals("Wire-O Binding", TypeOfBinding.WIRE_O_BINDING.toString());
        assertEquals("Case Binding", TypeOfBinding.CASE_BINDING.toString());
        assertEquals("Other", TypeOfBinding.OTHER.toString());
    }

}