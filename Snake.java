/*
 * !!!!!!!! CHANGES TO MAKE !!!!!!!!!!!
 *  Add all moves to a stack that will be executed at each game tick instead of
 *  executing moves on the stop (where they may not trigger if too close together)
 * 
 *  Make the graphics smoother 
 */
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
     
public class Snake {

    final int WINDOW_SIDELENGTH = 800;
    final int ARENA_SIDELENGTH = 16;
    
    public static boolean gameOver = false;
    int x, y, fruitX, fruitY, tailLength; //Note: tailLength is equal to the score
    int maxLength = (ARENA_SIDELENGTH*ARENA_SIDELENGTH)-1;
    int[] tailX = new int[maxLength]; 
    int[] tailY = new int[maxLength]; 

    //The direction the snake is moving towards
    public enum DIRECTION {
        STOP, LEFT, RIGHT, UP, DOWN
    };
    public static DIRECTION dir;

    //Create an queue of keyboard inputs to make controls smoother
    public static DIRECTION[] moveQueue = {DIRECTION.STOP, DIRECTION.STOP};


    public Snake() throws Exception {
        //setup
        JFrame frame = new JFrame("Snake In Java");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel(ARENA_SIDELENGTH);
        frame.setContentPane(panel);
        frame.setSize(WINDOW_SIDELENGTH, WINDOW_SIDELENGTH);
        frame.setResizable(false);

        frame.addKeyListener(new MKeyListener());
        
        // Game setup
        gameOver = false;
        tailLength = 0;
        x = ARENA_SIDELENGTH/2;
        y = ARENA_SIDELENGTH/2;
        fruitX = x + (ARENA_SIDELENGTH/4);
        fruitY = y;
        dir = DIRECTION.STOP;

        while (!gameOver) {
            // Draw
            panel.removeAll();
            for (int i = 0; i < ARENA_SIDELENGTH; i++) {
                for (int j = 0; j < ARENA_SIDELENGTH; j++) {
                    JLabel label = new JLabel();
                    
                    //  Head of the snake
                    if (i == y && j == x) {
                        label.setBackground(Color.GREEN);
                    }
                    //Check for fruit
                    else if (i == fruitY && j == fruitX) {
                        label.setBackground(Color.RED);
                    }
                    //Print the tail
                    else {
                        boolean print = false; //keeps track of whether tail was printed
                        for (int k = 0; k < tailLength; k++) {
                            //is tail
                            if (tailX[k] == j && tailY[k] == i) {
                                label.setBackground(Color.GREEN);
                                print = true;
                            }
                        }
                        //rest of the arena
                        if (!print) {
                            label.setBackground(Color.BLACK);
                        }
                    }
                    label.setOpaque(true);
                    panel.add(label);
                }
            }

            frame.setVisible(true);

            // Logic 
            int prevX = tailX[0];
            int prevY = tailY[0];
            int prev2X, prev2Y;
            tailX[0] = x;
            tailY[0] = y;
            for (int i = 1; i < tailLength; i++) {
                prev2X = tailX[i];
                prev2Y = tailY[i];
                tailX[i] = prevX;
                tailY[i] = prevY;
                prevX = prev2X;
                prevY = prev2Y;
            }

            switch (dir) {
                case LEFT:  
                    x--;
                    break;
                case RIGHT: 
                    x++;
                    break;
                case UP:   
                    y--;
                    break;
                case DOWN: 
                    y++;
                    break;
                case STOP:
                default:
                    break;
            }
            if (x >= ARENA_SIDELENGTH || x < 0 || y >= ARENA_SIDELENGTH || y < 0)
                gameOver = true;

            //check if snake hits itself
            for (int i = 0; i < tailLength; i ++)
                if (tailX[i] == x && tailY[i] == y) 
                    gameOver = true;
                    
            //check if fruit is eaten
            if (x == fruitX && y == fruitY) {
                tailLength++;

                //Respawn the fruit somewhere else

                //prevent fruit from spawning in the same spot
                boolean validPosition = true;
                do {
                    //put the coordinates in a random spot
                    fruitY = (int) Math.round(Math.random()*(ARENA_SIDELENGTH-1));
                    fruitX = (int) Math.round(Math.random()*(ARENA_SIDELENGTH-1));  
                    //reposition the fruit if it is in the same spot as part of the tail
                    for (int i = 0; i < tailLength;i++) {
                        if  (fruitX == tailX[i] && fruitY == tailY[i]) {
                            validPosition = false;
                            break;
                        }
                    }
                }while (!validPosition);
                
                frame.setTitle("Snake in Java - Score: "+tailLength);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } 
            catch (Exception e) {
                System.out.println("there is an error.");
                return;
            }
        }

        frame.setTitle("Game Over. Score: "+tailLength);
        frame.setVisible(true);
        return;
    }

    public static void move(DIRECTION d) {
        dir = d;
        return;
    }

    //Input
    public static void main(String[] args) {
        try {
            new Snake();
        }
        catch (Exception e) {
            System.out.println("Game crashed. Please reload.");
            return;
        }
    }

}

//This class receives keyboard input and gives it to the main class
class MKeyListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 65: // A
            case 97: // a
                if (Snake.dir != Snake.DIRECTION.RIGHT)
                    Snake.move(Snake.DIRECTION.LEFT);
                break; 
            case 68: // D
            case 100: // d
                if (Snake.dir != Snake.DIRECTION.LEFT)
                    Snake.move(Snake.DIRECTION.RIGHT);
                break;
            case 87: // W
            case 119: // w
                if (Snake.dir != Snake.DIRECTION.DOWN)
                    Snake.move(Snake.DIRECTION.UP);
                break;
            case 83: // S
            case 115: //s
                if (Snake.dir != Snake.DIRECTION.UP)
                    Snake.move(Snake.DIRECTION.DOWN);
                break;
            default:
                break;
        }
    }
}