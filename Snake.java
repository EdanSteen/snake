/*
 * Snake created in Java.
 * This class requires GamePanel.java, GameTile.java, and startScreen.png to
 * be in the same directory in order to funciton properly. 
 * 
 * @author: Edan Steen
 * @version: 1.0
 */
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class Snake {

    //This variable detects if the user has started the game yet
    public static boolean onStartScreen = true;

    public Snake() throws Exception {
        //setup
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel panel = new GamePanel();
        frame.setContentPane(panel);
        frame.addKeyListener(new SnakeControls(panel));
        frame.pack();
        frame.setLocationRelativeTo(null); //put the frame in the middle of the screen
        frame.setVisible(true);

        //run the game 
        panel.runGame();
        
        frame.dispose();
        return;
    }

    //Input
    public static void main(String[] args) {
        try {
            new Snake();
        }
        catch (Exception e) {
            System.out.println("Game crashed.");
            return;
        }
    }
}

class SnakeControls extends KeyAdapter {
    GamePanel panel;
    public SnakeControls(GamePanel p) {
        super();
        panel = p;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 65: // A
            case 97: // a
                panel.move(GamePanel.DIRECTION.LEFT);
                break; 
            case 68: // D
            case 100: // d
                panel.move(GamePanel.DIRECTION.RIGHT);
                break;
            case 87: // W
            case 119: // w
                panel.move(GamePanel.DIRECTION.UP);
                break;
            case 83: // S
            case 115: //s
                panel.move(GamePanel.DIRECTION.DOWN);
                break;
            case 32: //Space
                panel.triggerScreenEvent();
                break;
            case 27: //escape
                panel.exit();
                break;
            default:
                break;
        }
    }
}