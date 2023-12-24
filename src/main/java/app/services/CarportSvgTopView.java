package app.services;

public class CarportSvgTopView {
    private Svg carportSvg;
    private int length;
    private int width;
    private int beams;
    private int rafters;
    private int posts;

    private double startX;
    private double startY;
    private double secondX;
    private double secondY;
    private double thirdX;
    private double thirdY;
    private double fourthX;
    private double fourthY;


    public CarportSvgTopView(int length, int width) {

        this.length = length;
        this.width = width;
        String viewBox = String.format("0 0 %d %d", length + 10, width + 10);
        carportSvg = new Svg(0, 0, viewBox, "100%", "auto");

        //Tegningen af billedets størrelse
        carportSvg.addRectangle(0, 0, length, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        beams = addBeams();
        rafters = addRafters();
        posts = addPost();
        addLines();
    }

    private void addLines() {
        carportSvg.addLine(startX, startY, secondX, secondY);
        carportSvg.addLine(thirdX, thirdY, fourthX, fourthY);
    }

    public int getBeams() {
        return beams;
    }

    public int getRafters() {
        return rafters;
    }

    public int getPosts() {
        return posts;
    }

    //Remmene
    private int addBeams() {
        carportSvg.addRectangle(0, 35, 4.5, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0, (int) (length - 35 - 4.5), 4.5, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        return 2;
    }


    //Spærene tegnes her
    private int addRafters() {
        int amountOfRafters = 0;

        for (double i = 0; i < width; i += 55) {
            if ((width == 240 && i == 220) || (width == 390 && i == 385)) {
                carportSvg.addRectangle(width - 27.5, 0, length, 4.5, "stroke-width:1px; stroke:#000000; fill: #ffffff");
                amountOfRafters += 1;
            } else {
                carportSvg.addRectangle(i, 0, length, 4.5, "stroke-width:1px; stroke:#000000; fill: #ffffff");
                amountOfRafters += 1;
            }
        }
        carportSvg.addRectangle(width, 0, length, 4.5, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        return amountOfRafters + 1;
    }

    private int addPost() {
        int amountOfPosts = 0;
        int distanceBetweenPosts = 240;
        double postWidth = 4.5;
        double postHeight = 4.5;

        if (length <= 240) {
            startX = 27.5;
            startY = 35;
            secondX = length;
            secondY = width - 35 - 4.5;

            thirdX = 30;
            thirdY = width - 35 - 4.5;
            fourthX = length + 2;
            fourthY = 35;

            carportSvg.addRectangle(0 + 27.5, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(length, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(0 + 27.5, width - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(length, width - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            amountOfPosts = 4;

        } else if (length > width) {
            startX = 27.5;
            startY = 35;
            secondX = width - 27.5;
            secondY = length - 35 - 4.5;

            thirdX = width - 27.5;
            thirdY = 35;
            fourthX = 27.5;
            fourthY = length - 35 - 4.5;

            carportSvg.addRectangle(0 + 27.5, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(width - 27.5, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");

            carportSvg.addRectangle(0 + 27.5, length - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(width - 27.5, length - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            amountOfPosts = 4;

        } else {

            startX = 27.5;
            startY = 35;
            secondX = 27.5 + distanceBetweenPosts;
            secondY = length - 35 - 4.5;

            thirdX = 27.5 + distanceBetweenPosts;
            thirdY = 35;
            fourthX = 27.5;
            fourthY = width - 35 - 4.5;

            ///Øverste to
            carportSvg.addRectangle(0 + 27.5, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(27.5 + distanceBetweenPosts, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(0 + 27.5, width - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(27.5 + distanceBetweenPosts, width - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            amountOfPosts = 4;
        }

        if (length > 450 && !(length > width)) {
            carportSvg.addRectangle(length - 100, 35, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            carportSvg.addRectangle(length - 100, width - 35 - 4.5, postWidth, postHeight, "stroke-width:1px; stroke:#000000; fill: #000");
            amountOfPosts += 2;
        }
        return amountOfPosts;
    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
