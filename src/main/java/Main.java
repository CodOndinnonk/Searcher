import Entities.EntityFactory;
import Entities.ReadObject;
import Gui.InfoWindow;
import Utils.FileHelper;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;


public class Main {
    private static final org.apache.log4j.Logger log = Logger.getLogger(Main.class);
    private static ArrayList<ReadObject> readObjectList = new ArrayList<ReadObject>();

    //as no gui, set them as variables (for now)
    public static final String fileObjectSeparator = "@";
        public static final File file = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\big.bib");
//    public static final File file = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\test.bib");
    public static final String authorsSeparator = "and";




    public static void main(String[] args) {
        log.trace("Start program");

        String fileStr = new FileHelper().readFile(file);

        if (fileStr == null) {
            log.warn("No data to process");
            new InfoWindow("Warning", "Can't read file " + file.getAbsolutePath()).showWindow();
            return;
        }

        //fill list of object
        String[] stringObjects = fileStr.split(fileObjectSeparator);
        for (int i = 0; i < stringObjects.length; i++) {
            //if line contains object data (not empty)
            if (stringObjects[i].trim().length() > 0) {
                readObjectList.add(EntityFactory.create(stringObjects[i], ",\n"));
            }
        }

        separateAuthors(readObjectList);

    }

//    /**
//     * Perform creation of ReadObject base on string
//     *
//     * @param str text with data
//     * @return -> filled object
//     * <p>-> NULL, if error
//     */
//    private static ReadObject convertStringToObject(@NotNull String str) {
//        if (str == null || str.trim().length() == 0) {
//            log.error("str is empty. str = " + str);
//            return null;
//        }
//
//        ReadObject outObject = EntityFactory.create(str);
//
//        if (outObject == null) {
//            log.error("Can't create object from provided string. str = " + str);
//        }
//
//        return outObject;
//    }

    /**
     * Return income list, with separated authors, if have such
     *
     * @param objects list of read object
     */
    private static ArrayList<ReadObject> separateAuthors(ArrayList<ReadObject> objects) {
        ArrayList<ReadObject> separatedAuthorsList = new ArrayList<ReadObject>();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getAuthor().contains(authorsSeparator)) {
                separatedAuthorsList.addAll(ReadObject.split(objects.get(i),authorsSeparator));
                int test=0;
        } else {
            separatedAuthorsList.add(objects.get(i));
        }
        }
    return separatedAuthorsList;
    }
}

