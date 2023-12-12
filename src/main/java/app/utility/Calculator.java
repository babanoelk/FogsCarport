package app.utility;

import app.dtos.DTOCarportWithIdLengthWidthHeight;
import app.dtos.DTOShedIdLengthWidth;
import app.entities.Carport;
import app.entities.Shed;

public class Calculator {

    private static float carportPricePerSqCM = 1200;

    private static float shedPricePerSqMeter = 500;

    public static float carportPriceCalculator(DTOCarportWithIdLengthWidthHeight carport){
        float price;

        float sqMeter = (carport.getLength()/100) * (carport.getWidth()/100);
        price = sqMeter * carportPricePerSqCM;

        return price;
    }

    public static float shedPriceCalculator(DTOShedIdLengthWidth shed){
        float price;

        float sqMeter = (shed.getLength()/100) * (shed.getWidth()/100);
        price = sqMeter * shedPricePerSqMeter;
        return price;
    }


    /*public static float carportPriceCalculator2(DTOCarportWithIdLengthWidthHeight carport){
        float price;

        float carportSqMeter = (carport.getLength()/100) * (carport.getWidth()/100);
        price = carportSqMeter * carportPricePerSqCM;

        if(carport.getShed() != null){
            float shedSqMeter = (carport.getShed().getLength()/100) * (carport.getShed().getWidth()/100);
            price += shedSqMeter* shedPricePerSqMeter;
        }

        return price;
    }*/


}
