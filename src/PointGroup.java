import java.util.*;
/**
 * Write a description of class PointGroup here.
 * 
 * @author Ross Purves 
 * @version 17.04.2016
 */
public class PointGroup
{
    // We are going to store all Points in an ArrayList: this implies that duplicates are allowed and Points are stored in order of entry
    private ArrayList<Point> points;

    /**
     * Constructor for objects of class PointGroup - creates an empty ArrayList
     */
    public PointGroup()
    {
        this.points = new ArrayList<Point>();
    }

    /**
     * Constructor for objects of class PointGroup - takes an existing ArrayList as an argument
     *
     * @param An ArrayList of Points
     */
    public PointGroup(ArrayList<Point> points)
    {
        this.points = new ArrayList<Point>(points); // Note here we take a copy of the ArrayList - this is important as we don't want to modify the orginal
    }

    /**
     * A method which adds a point to the list
     * 
     * @param  p   the Point to be added
     * @return     the number of Points in the list
     */
    public int add(Point p)
    {
        this.points.add(p);
        return this.points.size();
    }

    /**
     * A status method which uses a method of the Point class
     * and shows how we can iterate through all the Points stored
     * 
     */
    public void status(){
        for (Point p: this.points){
            p.status();
        }
    }

    /**
     * The number of points in the list
     * 
     * @return     the number of Points in the list
     */
    public int size()
    {
        return this.points.size();
    }

    /**
     * This method creates a distance matrix for pairs of points 
     * The matrix is one dimensional, but the number of points can be used to build a matrix
     * It contains (n*n) values
     * 
     * @return the distance matrix as a 1D array
     */
    public double[] distanceMatrix(){
        double [] distances = new double[this.size() * this.size()];
        int i=0;
        for (Point p: this.points){
            for (Point q: this.points){
                distances[i] = p.distance(q);
                i++;

            }

        }
        return distances;
    }

    /**
     * A method which calculates the difference in distances between Points in PointGroups of identical length
     * Underlying assumption is that Points in both PointGroups are the same, but have different coordinate systems
     * 
     * @param The PointGroup to be compared
     * 
     * @return A 1D array of all the distances. Since the distance matrix is square (nxn) it is easy to turn this array of length nxn into a nxn matrix
     * 
     */
    public double[] meanDifference(PointGroup pgq){
        if (this.points.size() != pgq.size()){
            System.err.println("PointGroups must be identical if we wish to compare distances");
            System.err.println("Method designed to compare effects of projections where Points are assumed to be identical");
            double result[] = {Double.NaN, Double.NaN};
            return result;      
        }

        double mean = 0;
        double s = 0;

        // Since we know that the first value in this symmetrical matrix will be 0, we can initialise it 
        // and reuse the value as it won't change anything
        int k=0;
        for (int i=0; i < this.size(); i++){
            for (int j=0; j < this.size(); j++){
                if (k>=1){
                    Point p1 = this.points.get(i);
                    Point q1 = this.points.get(j);
                    Point p2 = pgq.get(i);
                    Point q2 = pgq.get(j);
                    double dist1 = p1.distance(q1);
                    double dist2 = p2.distance(q2);
                    double difference = dist1 - dist2;
                    double oldMean = mean;
                    mean = oldMean + (difference - oldMean)/(k+1);
                    s = s + (difference-oldMean) * (difference - mean);             
                }
                k++;
            }

        }
        double s2 = s/(k-1);
        double sd = Math.sqrt(s2);
        double results[] = {mean, sd}; // We give the results back as an array     

        return results;

    }

    /**
     * A gettter method for a Point at a given location
     * 
     * @param The index of the Point
     * @return The Point at index i
     */
    public Point get(int i){
        return this.points.get(i);
    }

    /**
     * A gettter method for the complete ArrayList of Points
     * 
     * 
     * @return An ArrayList of Points
     */
    public ArrayList<Point> getPoints(){
        return this.points;
    }

    public Point min(){
        Point min = this.get(0);
        for (int i=1; i < this.points.size(); i++){
            Point p = this.get(i);
            if (p.getY() < min.getY())
                min = p;
            else if(p.getY() == min.getY())
                if (p.getX() < min.getX())
                    min = p;
        }
        return min;
    }

    public PointGroup sortByAngle(){
        ArrayList newPoints = new ArrayList<Point>(this.points);
        final Point min = this.min();
        // This code sorts all the Points, around the value of the Point with the min coordinates
        // It uses a so-called anonymous comparator which defines how we order two points, p and q
        // If you want to do something similar, you should simply make sure you copy the code exactly        
        Collections.sort(newPoints, new Comparator<Point>(){
                public int compare(Point p, Point q){
                    double a1 = min.cosine(p);
                    double a2 = min.cosine(q);
                    if (a1 > a2){
                        return -1;
                    }
                    else if (a1 < a2){
                        return 1;
                    }
                    else{ // If the angle is the same, we sort by distance
                        if (min.distance(p) > min.distance(q))
                            return -1;
                        else if (min.distance(p) < min.distance(q))
                            return 1;
                        else 
                            return 0;
                    }
                }
            }
        );
        newPoints.add(0, min); // The pivot Point is stored in last Place - we add it first...
        
        return new PointGroup(newPoints);
    }

    /**
     * An implementation of the Graham scan algorithm which returns the convex hull of a set of Points
     * Based on the solution from Sedgewick and Wayne (http://algs4.cs.princeton.edu/99hull/GrahamScan.java.html
     * 
     * @return The convex hull as a Polygon
     */
    public Polygon grahamScan(){
        // First we find the minimum y (and x if ties) - this anchors the hull        
        Point a = this.min();
        // Then we sort the Points by angle - this guarantees that we have a simple polygon with no self-intersection
        PointGroup sorted = this.sortByAngle();
        // The candidate points are those from the sorted list
        ArrayList<Point> candidates = sorted.getPoints();
        // We use a Stack to add and process Points on the hull
        Stack<Point> hull = new Stack<Point>();

        hull.push(a); // Add the first Point to the hull

        // This code is adapted from the solution be Sedgewick and Wayne (http://algs4.cs.princeton.edu/99hull/GrahamScan.java.html)
        // find index k1 of first point not equal to a
        int k1;
        // First we look for the first Point which is not the same as our anchor (this is effectively the special case where all 
        // Points are identical
        for (k1 = 1; k1 < candidates.size(); k1++)
            if (!a.equals(candidates.get(k1)))
                break;
        if (k1 == candidates.size()) 
            return null;        // if k1 reaches N, then all the points are the same, and there is no hull

        // find index k2 of first point not collinear with a and candidates[k1]
        int k2;
        for (k2 = k1 + 1; k2 < candidates.size(); k2++)
            if (a.determinant(candidates.get(k1), candidates.get(k2)) != 0) 
                break; // We have found a point which doesn't lie on a straight line
        hull.push(candidates.get(k2-1));    // This is the 2nd Point which we add as a candidate to the hull

        // Graham scan - we are looking for left turns (positive determinant)
        for (int i = k2; i < candidates.size(); i++) {
            Point p = hull.pop(); // The middle Point of our current segment
            while (hull.peek().determinant(p, candidates.get(i)) <= 0) {
                p = hull.pop(); // We found a right turn, so we delete the last Point we added to the stack - it's inside the hull
            }
            hull.push(p); // When we exit we have a left turn, so p appears to lie on the hull...
            hull.push(candidates.get(i)); // ...and we will test this Point now
        }

        PointGroup h = new PointGroup(new ArrayList<Point>(hull)); // Use the Stack to create a new PointGroup
        return new Polygon(h); // And finally return this as a Polygon

    }

    public void remove(int index) {
        points.remove(index);
    }



}
