/*
 * The GamePanel class contains all the logic of the game and
 * draws it to the screen
 * 
 * @author: Edan Steen
 * @version: 1.0
 */
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel {
    
    //Width of the window (px)
    final int WINDOW_SIDELENGTH = 600;
    //width of the individual tiles
    final int TILE_WIDTH = 20;
    //width of the board
    final int BOARD_SIDELENGTH = WINDOW_SIDELENGTH/TILE_WIDTH;
    //time between screen refresh (ms)
    final int GAME_SPEED = 100;

    final Color SNAKE_COLOR = Color.GREEN;
    final Color FRUIT_COLOR = Color.RED;
    final Color BACKGROUND_COLOR = Color.BLACK;

    public static boolean gameOver = false;
    //the coordinates of the fruit 
    int fruitX, fruitY;
    //the length of the tile +1 (the head is included)
    int tailLength; 
    //the maximum length a snake can reach
    int maxLength = (BOARD_SIDELENGTH*BOARD_SIDELENGTH);
    //the coordinates of each of the parts of the snake
    int[] snakeX = new int[maxLength]; 
    int[] snakeY = new int[maxLength]; 

    //The direction the snake is moving towards
    public enum DIRECTION {
        STOP, LEFT, RIGHT, UP, DOWN
    };
    public DIRECTION dir;


    public GamePanel() {
        this.setPreferredSize(new Dimension(WINDOW_SIDELENGTH,WINDOW_SIDELENGTH));
        this.setBackground(BACKGROUND_COLOR);
        this.setOpaque(true);
        setupGame();
    }    


    public void setupGame() {
        gameOver = false;
        tailLength = 1;
        snakeX[0] = BOARD_SIDELENGTH/2;
        snakeY[0] = BOARD_SIDELENGTH/2;
        fruitX = snakeX[0] + (BOARD_SIDELENGTH/4);
        fruitY = snakeY[0];
        dir = DIRECTION.STOP;
    }

    /*
     * This function essentially contains all the logic of snake, and then calls
     * the repaint() method to draw it
     */
    public void runGame() {

        while (!gameOver) {
            // render the board
            this.repaint();

            // Logic 

            //Move up the components of the snake
            for (int i = tailLength; i > 0; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            
            //displace the x value based on the direction of the head
            switch (dir) {
                case LEFT:  
                    snakeX[0]--;
                    break;
                case RIGHT: 
                    snakeX[0]++;
                    break;
                case UP:   
                    snakeY[0]--;
                    break;
                case DOWN: 
                    snakeY[0]++;
                    break;
                case STOP:
                default:
                    break;
            }

            //check if the snake collided with a wall
            if (snakeX[0] >= BOARD_SIDELENGTH || snakeX[0] < 0 || snakeY[0] >= BOARD_SIDELENGTH || snakeY[0] < 0)
                gameOver = true;

            //check if snake hits itself
            for (int i = 1; i < tailLength; i ++)
                if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) 
                    gameOver = true;
                    
            //check if fruit is eaten
            if (snakeX[0] == fruitX && snakeY[0] == fruitY) {
                tailLength++;

                //Respawn the fruit somewhere else
                boolean validPosition = true; //is true if the fruit isn't inside the snake's tail
                do {
                    //put the coordinates in a random spot
                    fruitY = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));
                    fruitX = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));  

                    //reposition the fruit if it is in the same spot as part of the tail
                    for (int i = 0; i < tailLength;i++) {
                        if  (fruitX == snakeX[i] && fruitY == snakeY[i]) {
                            validPosition = false;
                            break;
                        }
                    }
                }while (!validPosition);
                
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } 
            catch (Exception e) {
                System.out.println("there is an error.");
                return;
            }
        }
    
        //gameOver
    }

    /*
     * set the direction of the snake
     * 
     * @param DIRECTION d: the direction the snake should head towards
     */
    public void move(DIRECTION d) {
        //prevent user from moving backwards and ending the game on the spot.
        switch(d) {
            case UP:
                if (dir == DIRECTION.DOWN)
                    return;
                break;
            case DOWN:
                if (dir == DIRECTION.UP)
                    return;
                break;
            case LEFT:
                if (dir == DIRECTION.RIGHT)
                    return;
                break;
            case RIGHT:
                if (dir == DIRECTION.LEFT)
                    return;
            case STOP:
            default:
                break;
        }
        this.dir = d;
        return;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < BOARD_SIDELENGTH; i++) {
            for (int j = 0; j < BOARD_SIDELENGTH; j++) {
                GameTile tile = new GameTile(TILE_WIDTH);
                
                /*  Head of the snake
                if (i == y && j == x) {
                    tile.setColor(SNAKE_COLOR);
                } 
                //Check for fruit
                else*/ if (i == fruitY && j == fruitX) {
                    tile.setColor(FRUIT_COLOR);
                }
                //Print the tail
                else {
                    boolean tailPrinted = false; //keeps track of whether tail was printed
                    for (int k = 0; k < tailLength; k++) {
                        //is tail
                        if (snakeX[k] == j && snakeY[k] == i) {
                            tile.setColor(SNAKE_COLOR);
                            tailPrinted = true;
                            break;
                        }
                    }
                    //dont print this tile and continue
                    if (!tailPrinted) {
                        continue;
                    }
                }
                tile.paintTile(j,i,g);
            }
        }
    } 

}

