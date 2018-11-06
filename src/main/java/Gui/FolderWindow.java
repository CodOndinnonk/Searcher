package Gui;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class FolderWindow implements IFrameWindow {
    public interface FileCallback {
        /**
         * Send selected file to called class
         *
         * @param selectedFile file, selected by used
         */
        void action(File selectedFile);
    }

    private static final Logger log = Logger.getLogger(FolderWindow.class);
    private String title;
    private FileCallback callback;
    private JFrame jFrame;

    public FolderWindow(String title, @NotNull FileCallback callback) {
        if (callback == null) {
            log.error("callback is NULL");
            return;
        }
        this.title = title;
        this.callback = callback;
    }

    @Override
    public int showWindow() {
        jFrame = new JFrame(title);
        Container contentPane = jFrame.getContentPane();


        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(false);
        contentPane.add(fileChooser, BorderLayout.CENTER);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser theFileChooser = (JFileChooser) actionEvent
                        .getSource();
                String command = actionEvent.getActionCommand();
                if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                    File selectedFile = theFileChooser.getSelectedFile();
                    closeWindow();
                    callback.action(selectedFile);
                }
            }
        };
        fileChooser.addActionListener(actionListener);

        jFrame.pack();
        jFrame.setVisible(true);
        return 1;
    }

    @Override
    public int closeWindow() {
        try {
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
            return 1;
        }catch (Exception e){
            log.error("Error during closing. " + e.getMessage() );
            return -1;
        }
    }
}
