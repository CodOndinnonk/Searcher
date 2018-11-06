import Entities.EntityFactory;
import Entities.ReadObject;
import Entities.WordObject;
import Gui.FolderWindow;
import Gui.InfoWindow;
import Gui.InputWindow;
import Utils.FileHelper;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class Main {
    private static final org.apache.log4j.Logger log = Logger.getLogger(Main.class);
    private static ArrayList<ReadObject> readObjectList = new ArrayList<ReadObject>();

    public static final String fileObjectSeparator = "\n@";
    public static final String authorsSeparator = " and ";
    public static final String stringObjectDataSeparator = "\n";

    public static File fileIncome;
    public static String outputPath = "C:\\Temp";
    public static final File fileOutTitle = new File(outputPath + "\\author_titles");
    public static final File fileOutWords = new File(outputPath + "\\titles_words");

    public static String authorToFind;
    public static int topWordsPercent;


    public static void main(String[] args) {
        log.trace("Start program");

        setUserConfiguration();
    }

    /**
     * Check user entered text for data, if have some errors, inform and close program
     *
     * @param str text? untered by user
     */
    private static void checkData(String str) {
        //if nothing entered inform and close
        if (str == null || str.trim().length() == 0) {
            log.error("Entered data is empty. It's necessary for processing.. Program will close.");
            new InfoWindow("Error", "Entered data is empty. It's necessary for processing. Program will close.").showWindow();
            System.exit(0);
        }
    }


    /**
     * Contains main program logic
     */
    private static void processing() {
        //read income data
        String fileStr = FileHelper.readFile(fileIncome);

        if (fileStr == null) {
            log.warn("No data to process");
            new InfoWindow("Warning", "Can't read fileIncome " + fileIncome.getAbsolutePath()).showWindow();
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

        //get titles, associated with author
        ArrayList<String> titles = findAuthor(readObjectList, authorToFind);

        if (titles.size() > 0) {
            //get top words from titles
            ArrayList<WordObject> topWords = getTopWords(titles, 25);

            //save file with titles
            String titlesToSave = "";
            for (String title : titles) {
                titlesToSave += title + "\n";
            }
            FileHelper.writeFile(fileOutTitle, titlesToSave);

            //save file with words
            String wordsToSave = "";
            for (WordObject wordObject : topWords) {
                wordsToSave += wordObject.getWord() + "\n";
            }
            FileHelper.writeFile(fileOutWords, wordsToSave);

            //show info
            new InfoWindow("Information", "Two files associated with author = '" + authorToFind + "' were outputted and located here: " + outputPath).showWindow();

        } else {
            new InfoWindow("Warning", "No authors found by entered key. key = " + authorToFind).showWindow();
        }
    }

    private static void setUserConfiguration() {
        //select income data
        new FolderWindow("Select file with income data", new FolderWindow.FileCallback() {
            @Override
            public void action(File selectedFile) {
                fileIncome = selectedFile;

                //search author
                new InputWindow("Write author for search", null, new InputWindow.ICalledInputWindow() {
                    @Override
                    public void onInputWindowResult(String enteredStr) {
                        checkData(enteredStr);
                        authorToFind = enteredStr;

                        //set top words percent
                        new InputWindow("Write percent of highly frequent words in author titles", "25", new InputWindow.ICalledInputWindow() {
                            @Override
                            public void onInputWindowResult(String enteredStr) {
                                checkData(enteredStr);
                                if (enteredStr.matches("[0-9]+")) {
                                    int enteredNumber = 1;
                                    try {
                                        enteredNumber = Integer.valueOf(enteredStr);
                                        topWordsPercent = enteredNumber;
                                    } catch (Exception e) {
                                        log.error("Error on parsing string to int. " + e.getMessage());
                                        new InfoWindow("Error", "Error on parsing string to int. " + e.getMessage()).showWindow();
                                        System.exit(0);
                                    }
                                } else {
                                    log.error("Entered data is not number. enteredStr = " + enteredStr);
                                    new InfoWindow("Error", "Entered data is not number. It's necessary for processing. Program will close.").showWindow();
                                    System.exit(0);
                                }
                            }
                        }).showWindow();

                        processing();
                    }
                }).showWindow();
            }
        }).showWindow();
    }


    /**
     * Return income list, with separated authors, if have such
     *
     * @param objects list of read object
     * @return -> list of objects with separated authors (can be bigger than income list)
     */
    private static ArrayList<ReadObject> separateAuthors(@NotNull ArrayList<ReadObject> objects) {
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
     * @return -> list of titles associated with author
     * <p>-> NULL, if error
     */
    private static ArrayList<String> findAuthor(@NotNull ArrayList<ReadObject> objects, String author) {
        if (objects == null || objects.size() == 0) {
            log.error("objects is empty.");
            return null;
        }
        if (author == null || author.trim().length() == 0) {
            log.error("author is empty. author = " + author);
            return null;
        }

        ArrayList<String> authorTitlesList = new ArrayList<String>();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getAuthor() != null) {
                if (objects.get(i).getAuthor().trim().toLowerCase().contains(author.trim().toLowerCase())) {
                    //if hasn't such title, add it
                    if (!authorTitlesList.contains(objects.get(i).getTitle())) {
                        authorTitlesList.add(objects.get(i).getTitle());
                    }
                }
            }
        }
        return authorTitlesList;
    }


    /**
     * Perform getting top percent of records by qty
     *
     * @param titlesList list of titles, for getting words
     * @param topPercent percent of records to get, ∈ [1,100]
     * @return -> list with top highly frequent words, in frequency order
     * <p>-> NULL, if error
     */
    private static ArrayList<WordObject> getTopWords(@NotNull ArrayList<String> titlesList, int topPercent) {
        if (titlesList == null || titlesList.size() == 0) {
            log.error("titlesList is empty.");
            return null;
        }
        if (topPercent < 1 || topPercent > 100) {
            log.error("Incorrect percent of top records. it has to be >= 1% and < 100%");
            return null;
        }

        ArrayList<WordObject> words = new ArrayList<WordObject>();

        //get titles and separate by words
        for (String title : titlesList) {
            if (title != null) {
                String[] titleWords = title.split(" ");
                //perform calculation of words
                for (String word : titleWords) {
                    //if letters, not spec symbol

                    if (word.matches(".{0,}[A-z]+.{0,}")) {

                        //if list contains object with word
                        if (words.stream().map(WordObject::getWord).filter(word::equals).findFirst().isPresent()) {
                            words.stream()
                                    .filter(wordObject -> word.equals(wordObject.getWord()))
                                    .findAny()
                                    .orElse(null).incrementQty();
                        } else {
                            words.add(new WordObject(word, 1));
                        }
                    }
                }
            }
        }

        int selectWordsAmt = Math.round(words.size() / 100 * topPercent);

        //set to show at least 1 word
        if (selectWordsAmt < 1) {
            selectWordsAmt = 1;
        }

        //sort list by qty descending
        Collections.sort(words, WordObject.qtyComparatorDescending);

        words = new ArrayList<>(words.subList(0, selectWordsAmt));

        return words;
    }


}

