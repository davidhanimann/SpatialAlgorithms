
/**
 * A class which represents a Segment
 * 
 * @author Ross Purves 
 * @version 12.04.2016
 */
public class Segment
{
    // instance variables 
    private Point start, end;

    /**
     * Constructor for objects of class Segment
     */
    public Segment(Point start, Point end)
    {
        // initialise instance variables
        this.start = start;
        this.end = end;
    }

    /**
     * 
     * @return     the segment's length 
     */
    public double length()
    {
        return this.start.distance(this.end);
    }

    public Point getStart(){
        return this.start;
    }

    public Point getEnd(){
        return this.end;
    }

    public boolean intersects(Segment s2){
        Point a = this.getStart();
        Point b = this.getEnd();
        Point p = s2.getStart();
        Point q = s2.getEnd();

        double abp = a.determinant(b, p);
        double abq = a.determinant(b, q);
        // Deal first with collinearity
        if ((abp == 0) && (abq == 0)){
            Point minab = a.min(b);
            Point maxab = a.max(b);
            Point minpq = p.min(p);
            Point maxpq = q.max(q);
            // If the x and y range overlap, then the lines intersect
            // Note by using <= we treat touching lines as an intersection
            if ((minpq.getX() <= maxab.getX()) && (maxpq.getX() >= minab.getX())
                && (minpq.getY() <= maxab.getY()) && (maxpq.getY() >= minab.getY()))
                return true;
            else
                return false;
        }

        // We use the signum method here to check if determinants have the same sign -> it returns -1, 0 or 1 so deals
        // nicely with our three-valued logic. Otherwise we could do this with simple comparisons, but then we deal with all
        // three cases
        if (Math.signum(abp) == Math.signum(abq))
            return false; // Signs are the same
        double pqa = p.determinant(q, a);
        double pqb = p.determinant(q, b);
        if (Math.signum(pqa) == Math.signum(pqb))
            return false; // Signs are the same
        return true; // Orientations were different in both cases
        // Agian, we treat collinearity as a different orientation to
        // left or right, so touching lines intersect
    }
    
    public void status(){
        System.out.println("This segment is composed of two points:");
        start.status();
        end.status();
        System.out.println(" and has a length of:" + this.length());
    }
    
    public static void testSegments(){
        // [(3,3) – (6,6)] & [(3,1) – 7,7)] NO
        Segment s1 = new Segment(new Point(3,3), new Point(6,6));
        Segment s2 = new Segment(new Point(3,1), new Point(7,7));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");
        

        //[(3,3) – (6,3)] & [(3,1) – 7,1)] NO
        s1 = new Segment(new Point(3,3), new Point(6,3));
        s2 = new Segment(new Point(3,1), new Point(7,1));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

        //[(3,3) – (6,6)] & [(5,5) – 8,8)] YES
        s1 = new Segment(new Point(3,3), new Point(6,6));
        s2 = new Segment(new Point(5,5), new Point(8,8));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

        //[(1,10) – (10,10)] & [(5,10) – 11,10)] YES
        s1 = new Segment(new Point(1,10), new Point(10,10));
        s2 = new Segment(new Point(5,10), new Point(11,10));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

        //[(4,4) – (10,10)] & [(9,9) – 11,11)] YES
        s1 = new Segment(new Point(4,4), new Point(10,10));
        s2 = new Segment(new Point(9,9), new Point(11,11));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

        //[(4,4) – (4,8)] & [(4,3) – 4,1)] NO
        s1 = new Segment(new Point(4,4), new Point(4,8));
        s2 = new Segment(new Point(4,3), new Point(4,1));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

        //[(4,4) – (4,8)] & [(3,1) – 4,4)] YES
        s1 = new Segment(new Point(4,4), new Point(4,8));
        s2 = new Segment(new Point(3,1), new Point(4,4));
        s1.status();
        s2.status();
        System.out.println("Intersection:" + s1.intersects(s2) + "\n");

    
    }
}
