package com.appsdeveloperblog;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Math operations in Calculator class")
class CalculatorTest {

    Calculator calculator;

    @BeforeAll
    static void setup() {
        System.out.println("Executing @BeforeAll method");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("Executing @AfterAll method");
    }

    @BeforeEach
    void beforeEachTestMethod() {
        System.out.println("Executing @BeforeEach method");
        calculator = new Calculator();
    }

    @AfterEach
    void afterEachTestMethod() {
        System.out.println("Executing @AfterEach method");
    }

    // test<System Under Test>_<Condition or State Change>_<Expected Result>

    @DisplayName("Test 4/2 = 2")
    @Test
    void testIntegerDivision_WhenFourIsDividedByTwo_ShouldReturnTwo() {

        System.out.println("Running Test 4/2 = 2");

        // AAA

        // Arrange or Given
        int dividend = 4;
        int divisor = 2;
        int expectedResult = 2;

        // Act or When
        int actualResult = calculator.integerDivision(dividend, divisor);

        // Assert or Then
        assertEquals(expectedResult, actualResult, "4/2 did not produce 2");
    }

    // @Disabled("TODO: Still need to work on it")
    @DisplayName("Division by zero")
    @Test
    void testIntegerDivision_WhenDividendIsDividedByZero_ShouldThrowArithmeticException() {

        System.out.println("Running Division by zero");
        // Arrange
        int dividend = 4;
        int divisor = 0;
        String expectedExceptionMessage = "/ by zero";

        // Act & Assert
        ArithmeticException actualException = assertThrows(ArithmeticException.class, () -> {
            // Act
            calculator.integerDivision(dividend, divisor);
        }, "Division by zero should have thrown an ArithmeticException");

        // Assert
        assertEquals(expectedExceptionMessage, actualException.getMessage(),
                "Unexpected exception message");
    }

    @DisplayName("Test 33-1=32")
    @Test
    void integerSubtraction() {

        System.out.println("Running Test 33-1=32");

        int minuend = 33;
        int subtrahend = 1;
        int expectedResult = 32;
        int actualResult = calculator.integerSubtraction(minuend, subtrahend);
        assertEquals(expectedResult, actualResult,
                () -> minuend + "-" + subtrahend + " did not produce " + expectedResult);
    }
}