package Utils;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class FileHelper {
    private static final org.apache.log4j.Logger log = Logger.getLogger(FileHelper.class);

    /**
     * Perform reading file by lines
     * @param file file to read
     * @return -> String with file data
     * <p>-> NULL, if file don't exist or any error
     */
    public String readFile(@NotNull File file) {
        if (file == null){
            log.error("File is NULL.");
            return null;
        }

        StringBuilder sb = new StringBuilder();

        if (file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                try {
                    //read file by line
                    String s;
                    while ((s = in.readLine()) != null) {
                        sb.append(s);
                        sb.append("\n");
                    }
                } finally {
                    //close thread
                    in.close();
                }
            } catch (IOException e) {
                log.error("Error on reading file. " + e.toString());
            }
        } else {
            log.warn("File hasn't been found");
            return null;
        }

        if (sb.toString().length() == 0) {
            log.warn("Read file is empty");
        }

        //return created string by lines
        return sb.toString();
    }

    public ArrayList<String> readFileByLines() {
        return null;
    }



    /**
     * Save string to file (OVERRIDE)
     * @param text data to save
     * @return 1 -> wrote successfully
     * <p>-1 -> error
     * <p>0 -> didn't wrote, as text is empty
     */
    public static int writeFile(@NotNull File file,@NotNull String text) {
        if (file == null){
            log.error("File is NULL.");
            return -1;
        }
        if (text == null || text.trim().length() == 0){
            log.error("Text is empty. text = " + text);
            return 0;
        }

        try {
            //create file if don't exist
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //write text to the file
                out.print(text);
                return 1;
            } finally {
                //close file to complete operation
                out.close();
            }
        } catch(IOException e) {
            log.error("Error on writing file. " + e.toString());
            return -1;
        }
    }


}
