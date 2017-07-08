package week3;

public class BruteOriginal {
    private int numberOfSegments;
    private LineSegment[] segments;

    public BruteOriginal(Point[] points)    // finds all line numberOfSegments containing 4 points
    {

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException("cant input null points");
            for (int j = 0; j < i; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("cant input duplicated points");
            }
        }
        numberOfSegments = 0;

        int counter, countTo;
        double slope = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length - 3; j++) {
                counter = 0;
                countTo = 3;
                while (counter < countTo) {
                    if (j + counter != i) {
                        if ((countTo - counter) == 3)
                            slope = points[i].slopeTo(points[j + counter]);
                        double currentSlope = points[i].slopeTo(points[j + counter]);
                        if (currentSlope != slope)
                            break;
                        if (counter == countTo - 1) {
                            Point[] line = new Point[4];
                            line[0] = points[i];
                            for (int k = 1; k < line.length; k++) {
                                if (points[i].compareTo(points[j + counter]) == 0) {
                                    counter--;
                                    line[k] = points[j + counter];
                                    counter--;
                                } else {
                                    line[k] = points[j + counter];
                                    counter--;
                                }
                            }
                            LineSegment newSegment = createLineSegment(line);
                            if (segments == null) {
                                numberOfSegments++;
                                segments = new LineSegment[numberOfSegments];
                                segments[0] = newSegment;
                            } else {
                                addNewSegment(newSegment);
                            }
                            break;
                        }
                        counter++;
                    } else {
                        countTo = 4;
                        counter++;
                    }
                }
            }
        }
    }

    private void addNewSegment(LineSegment newSegment) {
        boolean exist = false;
        for (LineSegment segment : segments)
            if (segment.toString().equals(newSegment.toString()))
                exist = true;
        if (exist)
            return;
        numberOfSegments++;
        LineSegment[] newSegments = new LineSegment[numberOfSegments];
        for (int v = 0; v < segments.length; v++)
            newSegments[v] = segments[v];
        newSegments[numberOfSegments - 1] = newSegment;
        segments = newSegments;
    }

    private LineSegment createLineSegment(Point[] p) {
        for (int i = 0; i < p.length; i++)
            for (int j = i; j > 0; j--)
                if (p[j].compareTo(p[j - 1]) < 0) {
                    exch(p, j);
                }

        return new LineSegment(p[0], p[p.length - 1]);
    }

    private void exch(Point[] p, int j) {
        Point swap = p[j];
        p[j] = p[j - 1];
        p[j - 1] = swap;
    }

    public int numberOfSegments()        // the number of line numberOfSegments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line numberOfSegments
    {
        return segments;
    }
}
