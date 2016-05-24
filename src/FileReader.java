import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by davidhanimann on 23.05.16.
 */
public class FileReader {

    private Point centroid;
    private PointGroup convexHull;
    private Ellipse stdEllipse;
    private PointGroup points = new PointGroup();
    PointGroup p1 = new PointGroup();



    public FileReader(String path) throws IOException {
    /*
    This solution will iterate through all the lines in the file – allowing for processing of each line – without
    keeping references to them – and in conclusion, without keeping them in memory.
     */

         FileInputStream inputStream = null;
         Scanner sc = null;

        double averageX = 0;
        double averageY = 0;
        double stdX = 0;
        double stdY = 0;

        PointGroup temp = new PointGroup();



        int i = 0;


        try {

            inputStream = new FileInputStream(new File(path));
            sc = new Scanner(inputStream, "UTF-8");

            while (sc.hasNextLine() && i < 50000) {

                i++;

                String line = sc.nextLine();
                String[] words = line.split(",");
                double x = Double.parseDouble(words[4]);
                double y = Double.parseDouble(words[5]);


                double deltaX = x - averageX;
                double deltaY = y - averageY;
                averageX += (deltaX)/i;
                averageY += (deltaY)/i;

                stdX += Math.sqrt(deltaX*(x-averageX));
                stdY += Math.sqrt(deltaY*(y-averageY));

                temp.add(new Point(x, y));
                points.add(new Point(x,y));


                // After 10000 points calculate convex hull and reset temp to convex hull points
                if (i % 10000 == 0 || !sc.hasNextLine()) {

                    temp = temp.grahamScan().toPointGroup();
                    temp.remove(0);

                }


            }

            p1 = points.grahamScan().toPointGroup();


            System.out.println(temp.size());
            stdX /= (i-1);
            stdY /= (i-1);

            System.out.println(averageX);
            System.out.println(averageY);
            System.out.println(stdX);
            System.out.println(stdY);

            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        centroid =  new Point(averageX, averageY);
        convexHull = temp;

    }

    public Point getCentroid() {
        return centroid;
    }

    public PointGroup getConvexHull() {
        return convexHull;
    }

    public Ellipse getStdEllipse() {
        return stdEllipse;
    }

    public PointGroup getPoints() {
        return points;
    }

    public PointGroup getP1() {
        return p1;
    }
}
