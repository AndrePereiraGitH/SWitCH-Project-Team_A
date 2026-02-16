package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link PhysicalAttributes}.
 * <p>
 * This test class validates:
 * <ul>
 *   <li>Successful object creation using both constructors.</li>
 *   <li>Validation behavior of the all-arguments constructor for invalid input values.</li>
 * </ul>
 */

class PhysicalAttributesTest {

   /**
    * Verifies that the empty constructor creates a non-null instance.
    */
   @Test
    void testEmptyConstructor() {
       // arrange + act
       PhysicalAttributes physicalAttributes = new PhysicalAttributes();
       // assert
       assertNotNull(physicalAttributes);
   }

   /**
    * Verifies that the all-arguments constructor creates a non-null instance
    * when valid values are provided.
    */
   @Test
    void testAllArgumentsConstructor() {
       // arrange
       Dimensions dimensions = new Dimensions(10.0, 10.0, 10.0);

       // act
       PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 2, 768);

       // assert
       assertNotNull(physicalAttributes);
   }

   /**
    * Provides invalid constructor argument combinations and their expected exception messages.
    *
    * @return a stream of invalid argument sets for
    *         {@link PhysicalAttributes#PhysicalAttributes(Dimensions, double, int)}
    */
   static Stream<Arguments> invalidPhysicalAttributesConstructorCases() {
      return Stream.of(
              Arguments.of(null, 10, 10, "Dimensions cannot be null"),
              Arguments.of(new Dimensions(10.0, 10.0, 10.0), 0, 6450, "weight and number of pages must be grater then 0"),
              Arguments.of(new Dimensions(10.0, 10.0, 10.0), -3, 6450, "weight and number of pages must be grater then 0"),
              Arguments.of(new Dimensions(10.0, 10.0, 10.0), 2, 0, "weight and number of pages must be grater then 0"),
              Arguments.of(new Dimensions(10.0, 10.0, 10.0), 2, -10, "weight and number of pages must be grater then 0")
      );
   }

   /**
    * Verifies that the all-arguments constructor throws {@link IllegalArgumentException}
    * when any provided argument is invalid.
    *
    * @param dimensions      the dimensions value used in the test case
    * @param weight          the weight value used in the test case
    * @param numberPages     the number of pages value used in the test case
    * @param expectedMessage the expected exception message
    */
   @ParameterizedTest()
   @MethodSource("invalidPhysicalAttributesConstructorCases")
   void allArgumentsConstructor_shouldThrow_IfIvalidArguments(Dimensions dimensions, double weight, int numberPages, String expectedMessage ) {
      // Act + assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PhysicalAttributes(dimensions, weight, numberPages));

      assertEquals(expectedMessage, exception.getMessage());
   }


}