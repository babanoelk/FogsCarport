package app.utility;

import app.entities.Carport;
import app.entities.Shed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {


    Carport carport = new Carport(1, 200, 300, 300);
    Shed shed = new Shed(1, 2, 100, 150);

    @Test
    void carportPriceCalculator() {
        float expectedPrice = 20000;
        //assertEquals(expectedPrice, Calculator.carportPriceCalculator(carport));
    }

    @Test
    void shedPriceCalculator() {
        // Example test - replace with your actual method and expected value
        float expectedPrice = 4000; // This should be the expected price for your shed
        //assertEquals(expectedPrice, Calculator.shedPriceCalculator(shed));
    }

    @Test
    void carportPriceCalculator2() {
        // Replace with a meaningful test
        float expectedValue = 10; // This should be the expected value
        //assertEquals(expectedValue, Calculator.carportPriceCalculator2(carport));
    }

}