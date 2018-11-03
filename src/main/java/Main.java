import Entities.EntityFactory;
import Entities.ReadObject;
import Entities.WordObject;
import Gui.InfoWindow;
import Utils.FileHelper;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;


public class Main {
    private static final org.apache.log4j.Logger log = Logger.getLogger(Main.class);
    private static ArrayList<ReadObject> readObjectList = new ArrayList<ReadObject>();

    //as no gui, set them as variables (for now)
    public static final String fileObjectSeparator = "\n@";
    public static final File fileIncome = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\big.bib");
    //    public static final File fileIncome = new File("D:\\Cloud\\GitHub\\Searcher\\extras\\test.bib");
    public static final File fileOutTitle = new File("D:\\Cloud\\GitHub\\Searcher\\out\\author_titles");
    public static final File fileOutWords = new File("D:\\Cloud\\GitHub\\Searcher\\out\\titles_words");
    public static final String authorsSeparator = " and ";
    public static final String stringObjectDataSeparator = "\n";
    public static final String authorToFind = "J";


    public static void main(String[] args) {
        log.trace("Start program");

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


        ArrayList<String> titles = findAuthor(readObjectList, authorToFind);

        if (titles.size() > 0) {
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

        } else {
            new InfoWindow("Warning", "No authors found by entered key. key = " + authorToFind).showWindow();
        }
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
    private static ArrayList<String> findAuthor(@NotNull ArrayList<ReadObject> objects, String author) {
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

//сделать регулярку на не буквы
                    if (word.matches(".{0,}[A-z]+.{0,}")) {

                        //if list contains object with word
                        if (words.stream().map(WordObject::getWord).filter(word::equals).findFirst().isPresent()) {
                            words.stream()
                                    .filter(wordObject -> word.equals(wordObject.getWord()))
                                    .findAny()
                                    .orElse(null).incrementQty();
//                            words.i(titleWords[j], words.get(titleWords[j]) + 1);
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

