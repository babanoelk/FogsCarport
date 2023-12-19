package app.services;

public class Svg {

    private static final String svgTemplate = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\" width=\"%s\" \n" +
            "     height=\"%s\" preserveAspectRatio=\"xMinYMin\">";

    private static final String svgRecTemplate = "<rect x=\"%.2f\" y=\"%.2f\" height=\"%f\" width=\"%f\" style=\"%s\"></rect>";
    private static final String svgArrowDefs = "<defs>\n" +
            "            <marker\n" +
            "                    id=\"beginArrow\"\n" +
            "                    markerWidth=\"12\"\n" +
            "                    markerHeight=\"12\"\n" +
            "                    refX=\"0\"\n" +
            "                    refY=\"6\"\n" +
            "                    orient=\"auto\">\n" +
            "                <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "            </marker>\n" +
            "            <marker\n" +
            "                    id=\"endArrow\"\n" +
            "                    markerWidth=\"12\"\n" +
            "                    markerHeight=\"12\"\n" +
            "                    refX=\"12\"\n" +
            "                    refY=\"6\"\n" +
            "                    orient=\"auto\">\n" +
            "                <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "            </marker>\n" +
            "        </defs>";

    private static final String svgLine = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" stroke=\"#000\" stroke-width=\"2\" stroke-dasharray=\"10\"/>\n";
    private StringBuilder svg = new StringBuilder();


    public Svg(int x, int y, String viewBox, String width, String height) {
        svg.append(String.format(svgTemplate, x, y, viewBox, width, height));
        svg.append(String.format(svgArrowDefs));

    }

    public void addRectangle(double x, double y, double height, double width, String style) {
        svg.append(String.format(svgRecTemplate, x, y, height, width, style));
    }

    public void addLine(double x1, double y1, double x2, double y2) {
        svg.append(String.format(svgLine, x1, y1, x2, y2));
    }

    public void addArrow(int x1, int y1, int x2, int y2, String style) {
    }

    public void addText(int x, int y, int rotation, String text) {
    }

    public void addSvg(Svg innerSvg) {
        svg.append(innerSvg.toString());
    }

    @Override
    public String toString() {
        return svg.append("</svg>").toString();
    }
}
