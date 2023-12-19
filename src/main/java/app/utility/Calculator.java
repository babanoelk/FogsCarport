package app.utility;

import app.entities.Carport;
import app.entities.Shed;

public class Calculator {

    private static float carportPricePerSqCM = 1200;

    private static float shedPricePerSqMeter = 500;

    public static float carportPriceCalculator(Carport carport){
        float price;

        float sqMeter = (carport.getLength()/100) * (carport.getWidth()/100);
        price = sqMeter * carportPricePerSqCM;

        return price;
    }

    public static float shedPriceCalculator(Shed shed){
        float price;

        float sqMeter = (shed.getLength()/100) * (shed.getWidth()/100);
        price = sqMeter * shedPricePerSqMeter;
        return price;
    }

    public static float discountCalculator(float totalPrice, float discountPercentage){
        discountPercentage = Math.max(0,Math.min(discountPercentage, 100));
        float discountedPrice = totalPrice - (totalPrice * discountPercentage/100);

        return discountedPrice;
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

    public static int piecesOfPost(Carport carport){

        int maxLengthBetweenPost = 240;
        int minNumberOfPosts = 4;
        //hvis bredden på shed er 100% skal der være 5 stolper og hvis bredden er 50% skal der være 4 stolper
        int shedPosts = 5;
        int totalPost = 0;

        int carportLength = carport.getLength();

        if(carportLength > maxLengthBetweenPost && carportLength < maxLengthBetweenPost * 2){
            totalPost += minNumberOfPosts + 2;
        } else if (carportLength > maxLengthBetweenPost * 2) {
            totalPost += minNumberOfPosts + 4;
        }

        if (carport.getShed() != null){
            if(carport.getShed().getWidth() == carport.getWidth()){
                totalPost += shedPosts;
            }else{
                totalPost += shedPosts -1;
            }
        }
        return totalPost;
    }

    public static int numberOfRafter(Carport carport){
        int maxLengthBetweenRafts = 60;
        int result = carport.getLength() / maxLengthBetweenRafts;
        if(carport.getLength() % maxLengthBetweenRafts != 0) {
            result++;
        }
        return result;
    }


}
