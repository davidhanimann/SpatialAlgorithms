import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by davidhanimann on 23.05.16.
 */
public class FileWriter {

    public static void write(String filename, PointGroup pointGroup) throws FileNotFoundException {

        File f = new File(filename);
        PrintWriter writer = new PrintWriter(filename);
        for (int i = 0; i < pointGroup.size(); i++) {
            writer.println(pointGroup.get(i).getX() + "," + pointGroup.get(i).getY());
        }
        writer.flush();
        writer.close();
    }

}
