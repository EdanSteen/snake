/*
 * Snake created in Java.
 * The game involves a snake, controlled by the player using WASD or arrow keys,
 * that eats fruit randomly spawned in the arena to 
 * This class requires GamePanel.java, GameTile.java, startScreen.png and
 * gameoverscreen.png to be in the same directory in order to funciton as intended. 
 * 
 * @author: Edan Steen
 * @version: 1.0
 */
import javax.swing.JFrame;

public class Snake {

    public Snake() throws Exception {
        //Create the window
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //create the main panel the game is in
        GamePanel panel = new GamePanel();
        frame.setContentPane(panel);

        //make sure frame is sized around the preferred size of the panel
        frame.pack();

        //put the frame in the middle of the screen
        frame.setLocationRelativeTo(null); 

        frame.setVisible(true);

        //run the game 
        panel.runGame();

        //automatically close the window once game is done
        frame.dispose();

        return;
    }

    /*
     * The main method is used simply to attempt to run the game. 
     * If a crash occurs, it will return an error message.
     */
    public static void main(String[] args) {
        //Attempt to run the game, print a crash message if failed
        try {
            new Snake();
        }
        catch (Exception e) {
            System.out.println("Game crashed. Please try refreshing.");
            return;
        }
    }
}