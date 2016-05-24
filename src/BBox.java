
/**
 * A class which defines a Bounding Box
 * 
 * @author Ross Purves 
 * @version 17.04.2016
 */
public class BBox
{
    // instance variables - the lower left and upper right Points
    // which define our Bounding Box
    private Point ll, ur; 

    /**
     * Constructor for objects of class which takes a Segment as argument
     * 
     * @param The Segment defining the bounding box
     */
    public BBox(Segment s)
    {
        Point min = s.getStart().min(s.getEnd());
        Point max = s.getStart().max(s.getEnd());
        if (!checkValidCoordinates(min, max)){
            min.status();
            max.status();
            throw new IllegalArgumentException("Coordinates of segments are identical or lie on a horizontal or vertical line");            
        }
        this.ll = min;
        this.ur = max;
    }

     /**
     * Constructor for objects of class which takes a PointGroup as argument
     * 
     * @param The PointGroup defining the bounding box
     */
    public BBox(PointGroup pg)
    {
        Point min = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        Point max = new Point(-1 * Double.MAX_VALUE, -1 * Double.MAX_VALUE);
        
        
        for (Point p: pg.getPoints()){
            min = p.min(min);
            max = p.max(max);
        }
        if (!checkValidCoordinates(min, max)){
            min.status();
            max.status();
            throw new IllegalArgumentException("Coordinates of BBox are identical or lie on a horizontal or vertical line");            
        }
        this.ll = min;
        this.ur = max;
    }

    /**
     * Constructor for objects of class which takes a Polygon as argument
     * 
     * @param The Polygon defining the bounding box
     */

    public BBox (Polygon poly){
        this(poly.toPointGroup());  
    }
    /**
     * A private method which checks for valid coordinates
     * Coordinates are not valid if they are equal or 
     * lie on a horizontal or vertical line
     * 
     * @param   The coordinates of the lower left hand corner of the bounding box 
     * @param   The coordinates of the upper right hand corner of the bounding box
     */
    private boolean checkValidCoordinates(Point min, Point max){
        if (min.equals(max)) // identical coordinates
            return false;
        else if (min.getX() == max.getX()) // vertical segment
            return false;
        else if (min.getY() == max.getY()) // horizontal segment
            return false;
            
        return true;
        }
     /**
     * Checks whether a given Point lies in this bounding box - points lying on the border are treated as outside
     * 
     * @param The Point to be tested
     * 
     * @return The result of the test
     */
    public boolean pointIn(Point p){
        double x = p.getX();
        double y = p.getY();
        if ((x > ll.getX()) && (x < ur.getX()) && (y > ll.getY()) && (y < ur.getY())) // Note here a Point must be wholly in a bounding box, not lying on its border
            return true;
        return false;
    }    
    /**
     * @return The Point defining the lower left corner of this box
     */
    public Point getLL(){
        return this.ll;
    }

    /**
     * @return The Point defining the upper right corner of this box
     */
    public Point getUR(){
        return this.ur;
    }


}
