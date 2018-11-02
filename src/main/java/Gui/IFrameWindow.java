package Gui;


public interface IFrameWindow {


    /***
     * Start showing window
     * @return 1 -> performed successfully
     * <p>-1 -> error
     */
    int showWindow();

    /**
     * Closing window (hiding)
     *
     * @return 1 -> performed successfully
     * <p>-1 -> error
     * <p>0 -> implementation is not set, nothing is done
     */
    int closeWindow();

}
