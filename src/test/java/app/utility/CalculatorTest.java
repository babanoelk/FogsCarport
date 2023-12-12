package app.utility;

import app.entities.Carport;
import app.entities.Shed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {


    Carport carport = new Carport(1, 300,300,300);
    Shed shed = new Shed(1,1,100,100);




    @Test
    void carportPriceCalculator() {
    }

    @Test
    void shedPriceCalculator() {
    }

    @Test
    void carportPriceCalculator2() {

        assertEquals(10,1);

    }
}