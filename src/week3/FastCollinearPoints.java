package week3;

public class FastCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segments;
    private final LineSegment[] immutableSegments;
    private PointPair[] repeatings;

    public FastCollinearPoints(final Point[] points)    // finds all line numberOfSegments containing 4 mutatedPoints
    {
        Point[] mutatedPoints = points.clone();

        for (int i = 0; i < mutatedPoints.length; i++) {
            if (mutatedPoints[i] == null)
                throw new NullPointerException("cant input null mutatedPoints");
            for (int j = 0; j < i; j++) {
                if (mutatedPoints[i].compareTo(mutatedPoints[j]) == 0)
                    throw new IllegalArgumentException("cant input duplicated mutatedPoints");
            }
        }
        numberOfSegments = 0;
        segments = new LineSegment[0];
        repeatings = new PointPair[0];
        boolean isThere = false;

        sortByDistance(mutatedPoints);
        Point[] pointsCopyWithoutI = new Point[mutatedPoints.length - 1];
        for (int i = 0; i < mutatedPoints.length; i++) {
            int copyCounter = 0;
            int notCopyCounter = 0;
            while (copyCounter < pointsCopyWithoutI.length) {
                if (copyCounter == i)
                    notCopyCounter++;
                pointsCopyWithoutI[copyCounter] = mutatedPoints[notCopyCounter];
                copyCounter++;
                notCopyCounter++;

            }
            margeSortPoints(pointsCopyWithoutI, mutatedPoints[i]);

            for (int j = 0; j < pointsCopyWithoutI.length - 2; j++) {
                double first = mutatedPoints[i].slopeTo(pointsCopyWithoutI[j]),
                        second = mutatedPoints[i].slopeTo(pointsCopyWithoutI[j + 1]),
                        third = mutatedPoints[i].slopeTo(pointsCopyWithoutI[j + 2]);

                if (first == second
                        && first == third) {
                    Point[] line = new Point[4];
                    line[0] = mutatedPoints[i];
                    for (int k = 1; k < line.length; k++)
                        line[k] = pointsCopyWithoutI[j + k - 1];

                    sortLineSegment(line);
                    isThere = false;
                    for (PointPair pp : repeatings)
                        if (pp.getStart().compareTo(line[0]) == 0
                                && pp.getEnd().compareTo(line[line.length - 1]) == 0)
                            isThere = true;

                    if (!isThere) {
                        if (segments == null) {
                            numberOfSegments++;
                            segments = new LineSegment[numberOfSegments];
                            segments[0] = new LineSegment(line[0], line[line.length - 1]);
                        } else {
                            addNewSegment(new LineSegment(line[0], line[line.length - 1]));
                        }
                    }
                }
            }

        }
        immutableSegments = segments.clone();
    }

    private static class PointPair {
        private final Point start;
        private final Point end;

        private PointPair(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public Point getStart() {
            return start;
        }

        public Point getEnd() {
            return end;
        }
    }

    private void sortByDistance(Point[] points) {
        Point[] aux = new Point[points.length];
        doSort(points, aux, 0, points.length - 1);

    }

    private void doSort(Point[] points, Point[] aux, int lo, int hi) {
        if (hi <= lo) return;

        int mid = lo + (hi - lo) / 2;
        doSort(points, aux, lo, mid);
        doSort(points, aux, mid + 1, hi);
        merge(points, aux, lo, mid, hi);
    }

    private void merge(Point[] a, Point[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0)
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    private void margeSortPoints(Point[] points, Point reference) {
        Point[] aux = new Point[points.length];
        doSort(points, aux, 0, points.length - 1, reference);
    }

    private void doSort(Point[] points, Point[] aux, int lo, int hi, Point reference) {
        if (hi <= lo) return;

        int mid = lo + (hi - lo) / 2;
        doSort(points, aux, lo, mid, reference);
        doSort(points, aux, mid + 1, hi, reference);
        merge(points, aux, lo, mid, hi, reference);
    }

    private void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Point reference) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (reference.slopeTo(aux[j]) > reference.slopeTo(aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
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

    private void sortLineSegment(Point[] p) {
        for (int i = 0; i < p.length; i++)
            for (int j = i; j > 0; j--)
                if (p[j].compareTo(p[j - 1]) < 0) {
                    exch(p, j);
                }

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
        return immutableSegments.clone();
    }

}
