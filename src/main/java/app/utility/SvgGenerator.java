package app.utility;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.StringWriter;

public class SvgGenerator {

    // Metode til at generere en SVG-streng baseret på specificerede dimensioner
    public static String generateSvg(double length, double width) throws SVGGraphics2DIOException, org.apache.batik.svggen.SVGGraphics2DIOException {

        // Trin 1: Opret DOMImplementation og Document
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

        // Trin 2: Initialisér SVGGraphics2D
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(document);

        // Trin 3: Tegn forskellige dele af SVG-grafikken
        drawLengthLine(svgGraphics2D,length,width);
        drawStolpe(svgGraphics2D, length, width);
        drawSpaer(svgGraphics2D, length, width);
        drawRem(svgGraphics2D, length, width);



        // Trin 4: Stream SVG-data til en StringWriter
        StringWriter writer = new StringWriter();
        svgGraphics2D.stream(writer, true);

        // Trin 5: Returner den genererede SVG-streng
        return writer.toString();
    }

    private static void drawStolpe(SVGGraphics2D svgGraphics2D, double length, double width) {
        int stolpebrede = 50;
        int afstandenMellemStolperne = 240;

        // Beregn antallet af stolper baseret på tilgængelig plads
        double antalstolper = Math.ceil(length / afstandenMellemStolperne);


        // Sørg for, at der tegnes mindst 2 stolper
        antalstolper = Math.max(2, antalstolper);

        // Beregn den nøjagtige afstand mellem stolper
        double distanceBetweenPosts = (length - 100) / (antalstolper - 1);

        for (int i = 0; i < (int) antalstolper; i++) {
            int x;

            svgGraphics2D.setPaint(Color.BLACK);

            // Sørg for, at den sidste stolpe ender præcist på slutningen
            if (i == antalstolper - 1) {
                x = (int) (length - stolpebrede - 20);
            } else {
                x = (int) (100 + i * distanceBetweenPosts);
            }

            // Sørg for, at stolperne ikke strækker sig ud over den maksimale længde
            int stolpeWidth = Math.min(stolpebrede, (int) (length - x));

            // Hvis stolpeWidth bliver 0 eller negativ, spring over tegningen over
            if (stolpeWidth > 0) {
                svgGraphics2D.drawRect(x, 50, stolpeWidth, stolpebrede);

                // Sørg for, at højden af stolperne forbliver inden for remmen
                int stolpeHeight = (int) Math.min(stolpebrede, width);
                svgGraphics2D.drawRect(x, (int) (50 + width - stolpeHeight), stolpeWidth, stolpeHeight);
            }
        }
    }

    private static void drawSpaer(SVGGraphics2D svgGraphics2D, double length, double width) {
        int afstanmellemspaer = 55;
        int antalspaer = Math.max(2, (int) Math.ceil(length / afstanmellemspaer));
        int start = 0;
        int distanceBetweenSpaer = (int) ((length - start) / (antalspaer - 1));

        int yOffset = 0; // Vertical offset for text

        for (int i = 0; i < antalspaer; i++) {
            int x = start + i * distanceBetweenSpaer;
            svgGraphics2D.setPaint(Color.GRAY);
            svgGraphics2D.drawLine(x, 50, x, (int) (50 + width));


            // Vis koordinaterne som tekst ved spærene
        }
    }

    private static void drawRem(SVGGraphics2D svgGraphics2D, double length, double width) {
        int remY = 50;
        int remThickness = 20; // Justér denne værdi efter ønsket tykkelse
        svgGraphics2D.setPaint(Color.LIGHT_GRAY);
        svgGraphics2D.drawRect(0, remY, (int) length + 3, remThickness);
        svgGraphics2D.drawRect(0, (int) (remY + width - remThickness), (int) length + 3, remThickness);
    }

    private static void drawLengthLine(SVGGraphics2D svgGraphics2D, double length, double width) {
        int remY = 50; // Y-coordinate for the dashed line
        int dashLength = 10; // Length of each dash in the dashed line

        // Create a Stroke object for the dashed line
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{dashLength}, 0);

        // Set the Stroke for the graphics object
        svgGraphics2D.setStroke(dashed);

        // Draw dashed line for the length
        svgGraphics2D.drawLine(0, remY, (int) length, remY);

        // Reset the Stroke to the default
        svgGraphics2D.setStroke(new BasicStroke());

        // Display length values as text
        svgGraphics2D.drawString("0 cm længde", 5, remY - 10);

        // Calculate the precise X-coordinate for the text at the maximum length
        int maxLengthTextX = (int) (length - svgGraphics2D.getFontMetrics().getStringBounds("" + (int) length, svgGraphics2D).getWidth()) - 5;

        svgGraphics2D.drawString("" + (int) length + " cm længde", maxLengthTextX, remY - 10);
    }





    private static void drawDashedLine(SVGGraphics2D svgGraphics2D, int x1, int y1, int x2, int y2, int dashLength) {
        int dashCount = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / dashLength;

        for (int i = 0; i < dashCount; i++) {
            int dashStartX = (int) (x1 + (i * dashLength) * (x2 - x1) / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
            int dashStartY = (int) (y1 + (i * dashLength) * (y2 - y1) / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
            int dashEndX = (int) (x1 + ((i + 1) * dashLength) * (x2 - x1) / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
            int dashEndY = (int) (y1 + ((i + 1) * dashLength) * (y2 - y1) / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));

            svgGraphics2D.drawLine(dashStartX, dashStartY, dashEndX, dashEndY);
        }
    }




}