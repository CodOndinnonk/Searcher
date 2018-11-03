import Entities.EntityFactory;
import Entities.ReadObject;
import Gui.InfoWindow;
import Utils.FileHelper;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;


public class Main {
    private static final org.apache.log4j.Logger log = Logger.getLogger(Main.class);
    private static ArrayList<ReadObject> readObjectList = new ArrayList<ReadObject>();

    //as no gui, set them as variables (for now)
    public static final String fileObjectSeparator = "\n@";
    public static final File file = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\big.bib");
    //    public static final File file = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\test.bib");
    public static final String authorsSeparator = " and ";
    public static final String stringObjectDataSeparator = "\n";
    public static final String authorToFind = "J";


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
                readObjectList.add(EntityFactory.create(stringObjects[i], stringObjectDataSeparator));
            }
        }

        //overwrite list of read entities with splitted list
        readObjectList = separateAuthors(readObjectList);


        ArrayList<String> titles = findAuthor(readObjectList, authorToFind);

        getTopWords(titles, 25);

    }

    /**
     * Return income list, with separated authors, if have such
     *
     * @param objects list of read object
     */
    private static ArrayList<ReadObject> separateAuthors(ArrayList<ReadObject> objects) {
        ArrayList<ReadObject> separatedAuthorsList = new ArrayList<ReadObject>();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getAuthor() != null && objects.get(i).getAuthor().contains(authorsSeparator)) {
                separatedAuthorsList.addAll(ReadObject.split(objects.get(i), authorsSeparator));
            } else {
                separatedAuthorsList.add(objects.get(i));
            }
        }
        return separatedAuthorsList;
    }

    /**
     * Perform search of authors posts from read object
     *
     * @param objects list of objects  to search in
     * @param author  text to find
     * @return list of titles associated with author
     */
    private static ArrayList<String> findAuthor(ArrayList<ReadObject> objects, String author) {
        ArrayList<String> authorTitlesList = new ArrayList<String>();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getAuthor() != null) {
                if (objects.get(i).getAuthor().trim().toLowerCase().contains(author.trim().toLowerCase())) {
                    authorTitlesList.add(objects.get(i).getTitle());
                }
            }
        }
        return authorTitlesList;
    }


    private static ArrayList<String> getTopWords(@NotNull ArrayList<String> titlesList, int topPercent) {
        if (titlesList == null || titlesList.size() == 0) {
            log.error("titlesList is empty.");
            return null;
        }
        if (topPercent < 1 || topPercent > 100) {
            log.error("Incorrect percent of top records. it has to be >= 1% and < 100%");
            return null;
        }

        HashMap<String, Long> words = new HashMap<String, Long>();

        //todo заменить на foreach
        //get titles and separate by words
        for (int i = 0; i < titlesList.size(); i++) {
            if (titlesList.get(i) != null) {
                String[] titleWords = titlesList.get(i).split(" ");
                //perform calculation of words
                for (int j = 0; j < titleWords.length; j++) {
                    //if letters, not spec symbol
//сделать регулярку на не буквы
                    if (titleWords[j].toString().p)
                    if (words.containsKey(titleWords[j])) {
                        words.put(titleWords[j], words.get(titleWords[j]) + 1);
                    } else {
                        words.put(titleWords[j], Long.valueOf(1));
                    }
                }
            }
        }

        int selectWordsAmt = words.size() / 100 * topPercent;


        return null;
    }


    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {

        //Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        //Sort list with Collections.sort(), provide a custom Comparator
        //Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        //Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }


}

