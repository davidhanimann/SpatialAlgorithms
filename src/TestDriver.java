import java.io.*;
import java.util.Scanner;

/**
 * Created by davidhanimann on 22.05.16.
 */
public class TestDriver {

    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("/Users/davidhanimann/Desktop/SpatAlg/project/flickr_uk.csv");
        FileWriter.write("convexHull.csv", fileReader.getConvexHull());
        FileWriter.write("points.csv", fileReader.getP1());



    }
}
