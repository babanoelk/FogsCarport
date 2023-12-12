package app.utility;

import app.dtos.DTOCarportWithIdLengthWidthHeight;
import app.dtos.DTOShedIdLengthWidth;
import app.entities.Carport;
import app.entities.Shed;

public class Calculator {

    private static float carportPricePerSqMeter = 1200;

    private static float shedpPricePerSqMeter = 500;

    public static float carportPriceCalculator(DTOCarportWithIdLengthWidthHeight carport){
        float price;

        float sqMeter = carport.getLength() * carport.getWidth();
        price = sqMeter * carportPricePerSqMeter;

        return price;
    }

    public static float shedPriceCalculator(DTOShedIdLengthWidth shed){
        float price;

        float sqMeter = shed.getLength() * shed.getWidth();
        price = sqMeter * shedpPricePerSqMeter;
        return price;
    }


    /*public static float carportPriceCalculator2(DTOCarportWithIdLengthWidthHeight carport){
        float price;

        float carportSqMeter = carport.getLength() * carport.getWidth();
        price = carportSqMeter * carportPricePerSqMeter;

        if(carport.getShed() != null){
            float shedSqMeter = carport.getShed().getLength() * carport.getWidth();
            price += shedSqMeter*shedpPricePerSqMeter;
        }

        return price;
    }*/


}
