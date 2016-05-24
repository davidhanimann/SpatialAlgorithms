/**
 * Created by davidhanimann on 23.05.16.
 */
public class Ellipse {
    private double centerX;
    private double centerY;
    private double stdX;
    private double stdY;
    private double rotation;

    public Ellipse(double centerX, double centerY, double stdX, double stdY, double rotation) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.stdX = stdX;
        this.stdY = stdY;
        this.rotation = rotation;
    }


    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getStdX() {
        return stdX;
    }

    public void setStdX(double stdX) {
        this.stdX = stdX;
    }

    public double getStdY() {
        return stdY;
    }

    public void setStdY(double stdY) {
        this.stdY = stdY;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
