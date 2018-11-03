package Gui;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class InputWindow implements IFrameWindow {
    public interface ICalledInputWindow {

        /**
         * Callback with entered data
         *
         * @param enteredStr text, entered by user
         */
        void onInputWindowResult(String enteredStr);
    }

    private static final Logger log = Logger.getLogger(InputWindow.class);
    private JOptionPane jOptionPane = new JOptionPane();
    private String massage;
    private String defaultValue;
    private String enteredInfo;
    private ICalledInputWindow calledClass;


    /***
     * Constructor
     * @param massage task info
     * @param defaultValue default text for input field
     * @param callback callback to handle result
     */
    public InputWindow(@NotNull String massage, @Nullable String defaultValue, @NotNull ICalledInputWindow callback) {
        if (massage == null || massage.trim().length() == 0) {
            log.error("massage is empty. massage = " + massage);
            return;
        }
        if (callback == null) {
            log.error("callback is NULL.");
            return;
        }

        this.massage = massage;
        this.defaultValue = defaultValue;
        this.calledClass = callback;
    }

    @Override
    public int showWindow() {
        try {
            if ((massage.length() == 0) || (massage.length() == 0)) {
                log.warn("Window wasn't configured. Massage =" + massage.toString() + ", DefaultValue=" + defaultValue.toString());
                return -1;
            } else {
                enteredInfo = jOptionPane.showInputDialog(null, massage, defaultValue);

                if (enteredInfo.toString().length() != 0) {

                    calledClass.onInputWindowResult(enteredInfo.toString());
                    return 1;
                } else {
                    log.warn("Entered text is empty.");
                    return -1;
                }
            }
        } catch (Exception e) {
            log.error("Dialog window is canceled. " + e.getMessage());
            return -1;
        }
    }

    @Override
    public int closeWindow() {
        return 0;
    }


}