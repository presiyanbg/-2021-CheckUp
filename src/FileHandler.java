import java.io.*;
import java.util.*;

public class FileHandler {

    // Load file
    public File loadFile(String fileName)  {
        try {
            return new File(fileName);
        }  catch (Exception e ) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }


    // Read (SINGLE) String From File
    public String readStringFromFile(String fileName) {
        try {
            File file = this.loadFile(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                return myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return null;
    }

    // Write String to File
    public void writeStringToFile(String fileName, String dataIn) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(dataIn);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

