import java.util.*;
/**
 * Write a description of class Polygon here.
 * 
 * @author Ross Purves
 * @version 05.04.2016
 */
public class Polygon
{
    // instance variables - replace the example below with your own
    ArrayList<Point> points;

    /**
     * Constructor for objects of class Polygon
     * 
     * 
     * @param An ordered set of Points represented as a PointGroup
     */

    public Polygon(PointGroup pg)
    {
        this.points = new ArrayList<Point>();
        // Check for duplicates
        for (Point p: pg.getPoints()){
            if (!this.points.contains(p))
                this.points.add(p);
        }

        //Check Polygon is closed - this is the only duplicate point allowed
        if (!this.points.get(0).equals(this.points.get(this.points.size()-1)))
            this.points.add(this.points.get(0));

        // We should also check for self intersection    

    }

    /**
     * A simple status method which reports on the Points in the Polygon
     */
    public void status()
    {
        System.out.println("This polygon has " + this.size() + " points.");
        for (Point p: this.points){
            p.status();
        }
    }

    /**
     * @return The number of points in the polygon
     */
    public int size(){
        return this.points.size();
    }

    /**
     * Calculate the area but keep the sign - this we need to calculate centroid
     * correctly (basically deals with the issue of clockwise/ anti-clockwise sets of points
     * 
     * @return the signed polygon area
     */
    private double rawArea(){
        double area = 0.0;
        // Calculating the area basically requires us to iterate through all the points
        // Since we need point i and point i+1 we need to get use the arrayList indices
        // and not a for each method, and we stop when we reach n-1

        for (int i=0; i< this.points.size()-1; i++){
            double xi = this.points.get(i).getX();
            double yi = this.points.get(i).getY();
            double xii = this.points.get(i+1).getX();
            double yii = this.points.get(i+1).getY();
            area = area + ((xii*yi) -(xi*yii));
        }
        area = 0.5 * area; // Make sure the value is positive
        return area;
    }

    /**
     * @return The area of a polygon in the units of the orginal data
     */
    public double area(){
        return Math.abs(this.rawArea());
    }

    /**
     * @return A point at the centroid of the polygon 
     */
    public Point centroid(){
        double x = 0;
        double y = 0;
        for (int i=0; i< this.points.size()-1; i++){
            double xi = this.points.get(i).getX();
            double yi = this.points.get(i).getY();
            double xii = this.points.get(i+1).getX();
            double yii = this.points.get(i+1).getY();
            double product = (xii*yi) -(xi*yii);
            x = x + ((xi + xii) * product); // The brackets are not essential 
            y = y + ((yi + yii) * product); // Java knows about precedence, but now you know what I intended
        }
        double area = this.rawArea();
        x = x/(6.0*area);
        y = y/(6.0*area);
        return new Point(x,y, this.points.get(0).getCartesian()); // We can argue about calculating centroids with non-cartesian points
    }

    /**
     * A method which allows us to create a PointGroup based on a Polygon
     * 
     * @return The PointGroup consisting of this set of Points
     */
    public PointGroup toPointGroup(){
        return new PointGroup(this.points);
    }

    /**
     * A method to test whether a given Point lies in this Polygon
     * 
     * @param The Point to be tested
     * 
     * @return True if the Point lies in the Polygon - note that cases on the boundary are not always well defined
     */
    public boolean pointIn(Point p){
        BBox bbox = new BBox(this);
        if (!bbox.pointIn(p)) // If the Point isn't in the bounding box, it isn't in the polygon
            return false;

        //Now create the ray
        double xInf = bbox.getUR().getX() + 1;
        double yInf = p.getY();
        Segment ray = new Segment(p, new Point(xInf, yInf));

        int intersections = 0;
        Point start = this.points.get(0);
        //This loop works for Points which are inside the polygon and where the ray doesn't intersect vertices or lie along
        //segments - i.e. it doesn't deal with our special cases
        /*
        for (int i=1; i < this.points.size(); i++){
        Point end = this.points.get(i);
        Segment s = new Segment(start, end);
        start = end;
        if (ray.intersects(s))
        intersections++;
        } 
         */
        // This loop also deals with the special cases
        for (int i=1; i < this.points.size(); i++){
            Point end = this.points.get(i);
            if (start.getY() != end.getY()){ // We don't create segments which are horizontal, as we don't count them in any case
                Segment s = new Segment(start, end);
                Point min = start.min(end); // We need this to work out the minY of the segment
                if (p.getY() != min.getY()) // If the ray goes through the minY of the segment we don't count the intersection
                    if (ray.intersects(s))
                        intersections++;
            }
            start = end;

        } 
        return intersections%2==0? false: true;  // false if odd, true if even   
    }

}
