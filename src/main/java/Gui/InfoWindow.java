package Gui;


import org.apache.log4j.Logger;

import javax.swing.*;

public class InfoWindow implements IFrameWindow {
    private static final Logger log = Logger.getLogger(InfoWindow.class);
    private JOptionPane jOptionPane = new JOptionPane();
    private String massage;
    private String title;


    /***
     * Constructor
     * @param title window title
     * @param massage text of information
     */
    public InfoWindow(String title, String massage) {
        this.title = title;
        this.massage = massage;
    }


    public int showWindow() {
        try {
            if ((title.length() == 0) || (massage.toString().length() == 0)) {
                log.warn("Window wasn't configured. Title=" + title.toString() + ", Massage=" + massage.toString());
                return -1;
            } else {
                // if all is correct
                int answer = jOptionPane.showConfirmDialog(null, massage, title, JOptionPane.WARNING_MESSAGE);
                // ok-> 0, cancel-> 2, X(close)-> -1
                if (answer == -1 || answer == 0 || answer == 2) {
                    return 1;
                } else {
                    log.error("Stop program, as error");
                    return -1;
                }
            }
        } catch (Exception e) {
            log.error("Error on showing window. " + e.toString());
            return -1;
        }
    }

    public int closeWindow() {
        return 0;
    }


}