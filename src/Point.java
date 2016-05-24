/**
 * Created by davidhanimann on 23.05.16.
 */
public class Point {
    private double x;
    private double y;
    private boolean cartesian;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.cartesian = true;
    }

    public Point(double x, double y, boolean cartesian) {
        this.x = x;
        this.y = y;
        this.cartesian = cartesian;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public boolean getCartesian() {
        return this.cartesian;
    }

    public void status() {
        System.out.println(this.x + "," + this.y + "," + (this.cartesian?"Cartesian":"Spherical"));
    }

    public double distance(Point q) {
        if(this.cartesian != q.getCartesian()) {
            System.err.println("Can\'t calculate a distance since points have different coordinate systems");
            return 0.0D / 0.0;
        } else {
            return this.cartesian?this.eucDistance(q):this.havDistance(q);
        }
    }

    private double eucDistance(Point q) {
        double deltaX = q.getX() - this.x;
        double deltaY = q.getY() - this.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance;
    }

    private double havDistance(Point q) {
        double r = 6371000.0D;
        double lat1 = Math.toRadians(this.y);
        double lat2 = Math.toRadians(q.getY());
        double lon1 = Math.toRadians(this.x);
        double lon2 = Math.toRadians(q.getX());
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double havLat = Math.sin(deltaLat / 2.0D) * Math.sin(deltaLat / 2.0D);
        double havLon = Math.sin(deltaLon / 2.0D) * Math.sin(deltaLon / 2.0D);
        double a = havLat + Math.cos(lat1) * Math.cos(lat2) * havLon;
        double c = 2.0D * Math.asin(Math.min(1.0D, Math.sqrt(a)));
        double distance = c * r;
        return distance;
    }

    public Point min(Point p) {
        return this.getX() < p.getX()?(this.getY() < p.getY()?new Point(this.getX(), this.getY()):new Point(this.getX(), p.getY())):(this.getY() < p.getY()?new Point(p.getX(), this.getY()):new Point(p.getX(), p.getY()));
    }

    public Point max(Point p) {
        return this.getX() > p.getX()?(this.getY() > p.getY()?new Point(this.getX(), this.getY()):new Point(this.getX(), p.getY())):(this.getY() > p.getY()?new Point(p.getX(), this.getY()):new Point(p.getX(), p.getY()));
    }

    public double cosine(Point p) {
        double adj = p.getX() - this.x;
        double hyp = this.distance(p);
        return adj / hyp;
    }

    public double determinant(Point p, Point q) {
        double ax = this.getX();
        double ay = this.getY();
        double px = p.getX();
        double py = p.getY();
        double qx = q.getX();
        double qy = q.getY();
        double determinant = ax * py - px * ay + qx * ay - ax * qy + px * qy - qx * py;
        return determinant;
    }

    public int hashCode() {
        boolean PRIME = true;
        byte result = 1;
        int result1 = 31 * result + (int)(this.x * this.y);
        return result1;
    }

    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(!(obj instanceof Point)) {
            return false;
        } else {
            Point other = (Point)obj;
            return this.x != other.x?false:this.y == other.y;
        }
    }
}
