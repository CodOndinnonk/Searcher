package Utils;

import org.jetbrains.annotations.NotNull;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class FileHelper {
    private static final org.apache.log4j.Logger log = Logger.getLogger(FileHelper.class);

    /**
     * Perform reading fileIncome by lines
     * @param file fileIncome to read
     * @return -> String with fileIncome data
     * <p>-> NULL, if fileIncome don't exist or any error
     */
    public static String readFile(@NotNull File file) {
        if (file == null){
            log.error("File is NULL.");
            return null;
        }

        StringBuilder sb = new StringBuilder();

        if (file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                try {
                    //read fileIncome by line
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
                log.error("Error on reading fileIncome. " + e.toString());
            }
        } else {
            log.warn("File hasn't been found");
            return null;
        }

        if (sb.toString().length() == 0) {
            log.warn("Read fileIncome is empty");
        }

        //return created string by lines
        return sb.toString();
    }

    public ArrayList<String> readFileByLines() {
        return null;
    }



    /**
     * Save string to fileIncome (OVERRIDE)
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
            //create fileIncome if don't exist
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //write text to the fileIncome
                out.print(text);
                return 1;
            } finally {
                //close fileIncome to complete operation
                out.close();
            }
        } catch(IOException e) {
            log.error("Error on writing fileIncome. " + e.toString());
            return -1;
        }
    }


}
