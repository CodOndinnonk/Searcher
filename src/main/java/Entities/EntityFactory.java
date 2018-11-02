package Entities;

import Utils.SearcherConfig;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class EntityFactory {
    private static final org.apache.log4j.Logger log = Logger.getLogger(EntityFactory.class);


    /**
     * Perform creation of ReadObject base on string
     *
     * @param str text with data
     * @return -> filled object
     * <p>-> NULL, if error
     */
    public static ReadObject create(@NotNull String str, @NotNull String dataSeparator) {
        if (str == null || str.trim().length() == 0) {
            log.error("str is empty. str = " + str);
            return null;
        }
        if (dataSeparator == null || dataSeparator.trim().length() == 0) {
            log.error("dataSeparator is empty, it has to be set. dataSeparator = " + dataSeparator);
            return null;
        }

        //make all lowercase
        str = str.toLowerCase();
        dataSeparator = dataSeparator.toLowerCase();

        if (!str.contains(dataSeparator)) {
            log.error("Can't find dataSeparator it text. dataSeparator = " + dataSeparator);
            return null;
        }

        ReadObject outObject = new ReadObject();

        String[] data = str.split(dataSeparator);
        for (int i = 0; i < data.length; i++) {
            String test = data[i].trim();
            //set author
            if (data[i].trim().contains(SearcherConfig.AUTHOR_KEY)) {
                //if overwrite author
                if (outObject.getAuthor() != null) {
                    log.warn("Author has been already set, it will be overwrite. Old = " + outObject.getAuthor() + " New = " + data[i]);
                }

                //get value after key
                String author = data[i].trim().split(SearcherConfig.AUTHOR_KEY)[1].trim();
                //clean string
                author = author.replace("{", "");
                author = author.replace("}", "");
                author = author.replace("=", "");
                author = author.trim();

                outObject.setAuthor(author);
            }
            //setTitle
            if (data[i].trim().contains(SearcherConfig.TITLE_KEY)) {
                //if overwrite title
                if (outObject.getTitle() != null) {
                    log.warn("Title has been already set, it will be overwrite. Old = " + outObject.getTitle() + " New = " + data[i]);
                }

                //get value after key
                String title = data[i].trim().split(SearcherConfig.TITLE_KEY)[1].trim();
                //clean string
                title = title.replace("{", "");
                title = title.replace("}", "");
                title = title.replace("=", "");
                title = title.trim();

                outObject.setTitle(title);
            }
        }

        if (outObject == null) {
            log.error("Can't create object from provided string. str = " + str);
        } else {
            if (outObject.getAuthor() == null || outObject.getTitle() == null) {
                log.error("Some data is empty. Author = " + outObject.getAuthor() + " Title = " + outObject.getTitle());
            }
        }


        //parse income object
        return outObject;
    }


}
