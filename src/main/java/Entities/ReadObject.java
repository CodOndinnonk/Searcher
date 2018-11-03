package Entities;


import Utils.FileHelper;
import org.jetbrains.annotations.NotNull;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class ReadObject implements ReadObjectInterface {
    private static final org.apache.log4j.Logger log = Logger.getLogger(ReadObject.class);


    private String author;
    private String title;

    public ReadObject() {
    }

    public ReadObject(String author, String title) {
        this.author = author;
        this.title = title;
    }


    /**
     * Split object with several authors to different object by each author
     *
     * @param readObject
     * @param authorSeparator key to separate authors in string
     * @return -> list of objects with single author
     * <p>-> NULL, if any error
     */
    public static ArrayList<ReadObject> split(@NotNull ReadObject readObject, @NotNull String authorSeparator) {
        if (readObject == null) {
            log.error("ReadObject is NULL.");
            return null;
        }
        if (authorSeparator == null || authorSeparator.trim().length() == 0) {
            log.error("authorSeparator is empty. authorSeparator = " + authorSeparator);
            return null;
        }

        ArrayList<ReadObject> returnObjects = new ArrayList<ReadObject>();

        if (readObject.getAuthor().contains(authorSeparator)) {
            String[] authors = readObject.getAuthor().split(authorSeparator);
            //if have more than one author
            for (int i = 0; i < authors.length; i++) {
                //make list of object for each author
                returnObjects.add(new ReadObject(authors[i].trim(), readObject.getTitle()));
            }
        } else {
            log.info("No authorSeparator found in author string. authorSeparator = " + authorSeparator);
            returnObjects.add(readObject);
        }

        return returnObjects;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
