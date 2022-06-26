/*
 * The GamePanel class contains all the logic, controls, and graphics
 * for the game Snake
 * 
 * @author: Edan Steen
 * @version: 1.0
 */

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class GamePanel extends JPanel {
    
    //Width of the window (px)
    final int WINDOW_SIDELENGTH = 600;
    //width of the individual tiles
    final int TILE_WIDTH = 40;
    //width of the board. Note: a future feature could be updating the tile width to make the board larger
    final int BOARD_SIDELENGTH = WINDOW_SIDELENGTH/TILE_WIDTH;
    //time between frame updates
    final int GAME_SPEED = 100;
    //the maximum length a snake can reach (the area of the board)
    final int MAX_LENGTH = (BOARD_SIDELENGTH*BOARD_SIDELENGTH);

    //The colors of the snake, fruit and background. A future feature could be having these changed by the user.
    final Color SNAKE_COLOR = Color.GREEN;
    final Color FRUIT_COLOR = Color.RED;
    final Color BACKGROUND_COLOR = Color.BLACK;

    //the coordinates of the fruit 
    int fruitX, fruitY;
    //the length of the snake (the snake's head is included in this value)
    int snakeLength; 
    
    //the coordinates of each of the parts of the snake
    int[] snakeX, snakeY;

    //determines if the player is on the start screen
    boolean onStartScreen = true;
    //determines if the player is on the game over screen
    boolean onEndScreen = false;
    //determines if player wants to replay the game 
    boolean replay = false;

    //The directions the snake can move towards
    enum DIRECTION {
        STOP, LEFT, RIGHT, UP, DOWN
    };

    //the current direction of the snake
    DIRECTION dir; 
    //the direction the user wants the snake to go
    DIRECTION queuedDirection; 

    /*
     * Create the panel the game will be played in.
     */
    public GamePanel() {
        //Set the panel to the specified width
        this.setPreferredSize(new Dimension(WINDOW_SIDELENGTH,WINDOW_SIDELENGTH));
        
        //Add the background 
        this.setBackground(BACKGROUND_COLOR);
        this.setOpaque(true);

        //Add the controls to be part of the panel
        this.setFocusable(true);
        this.addKeyListener(new SnakeControl());

        //Put the player on the start screen 
        onStartScreen = true;
    }    

    /*
     * This function resets all of the variables of the game, and then contains all the logic that
     * make the game work. It uses repaint() to update the screen, and will loop until the player's
     * snake hits a wall or itself 
     */
    public void runGame() {
        //Setup the game

        //set game over to false
        boolean gameOver = false;
        replay = false;

        //set length to just the head of the snake
        snakeLength = 1;
        
        //refresh the snake body
        snakeX = new int[MAX_LENGTH]; 
        snakeY = new int[MAX_LENGTH]; 

        //put the snake in the middle
        snakeX[0] = BOARD_SIDELENGTH/2;
        snakeY[0] = BOARD_SIDELENGTH/2;
        
        //put the fruit directly across from snake
        fruitX = snakeX[0] + (BOARD_SIDELENGTH/4);
        fruitY = snakeY[0];

        //keep snake still at the start of the game
        queuedDirection = DIRECTION.STOP;

        //Loop until the game ends
        while (!gameOver) {
            // Render the graphics
            this.repaint();

            //Move up the components of the snake
            for (int i = snakeLength; i > 0; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            
            //update the direction to be the desired direction. this is done to avoid the user clicking to fast and making the snake move backwards
            dir = queuedDirection;

            //displace the head of the snake based on the direction
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
                    //do nothing by default or when the snake is stopped
                    break;
            }

            //check if the snake collided with a wall
            if (snakeX[0] >= BOARD_SIDELENGTH || snakeX[0] < 0 || snakeY[0] >= BOARD_SIDELENGTH || snakeY[0] < 0)
                gameOver = true;

            //check if snake hits itself
            for (int i = 1; i < snakeLength; i ++)
                if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) 
                    gameOver = true;
                    
            //check if fruit is eaten
            if (snakeX[0] == fruitX && snakeY[0] == fruitY) {
                //make the snake longer
                snakeLength++; 

                //Put the fruit in a new position
                boolean validPosition; //is true if the fruit isn't inside the snake
                do {
                    //Assume fruit is in valid position initially
                    validPosition = true;
                    //put the coordinates of the fruit to a random spot on the board
                    fruitY = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));
                    fruitX = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));  

                    //set validPosition to false if fruit is inside the snake
                    for (int i = 0; i < snakeLength;i++) {
                        //check if the fruit spawns in the snake, but ignore if fruit is spawned in head of the snake
                        if  ((fruitX == snakeX[i] && fruitY == snakeY[i])) {
                            validPosition = false;
                        }
                    }
                } while (!validPosition);  
            }

            //delay by the game speed before continuing to avoid destroying the computer hardware
            delay();
        }
     
        //show the end screen
        onEndScreen = true;
        this.repaint();

        //wait for user input as to whether they want to restart the game or not
        do {
            delay(); //delay to not overuse the CPU
        } while(onEndScreen);

        //restart the game if the user decided to
        if (replay == true) 
            runGame(); 

        return; 
    }

    /*
     * Set the direction of the snake
     * 
     * @param DIRECTION d: the direction the snake should head towards
     */
    public void move(DIRECTION d) {
        //dont do anything if the game is still on the start screen
        if (onStartScreen)
            return;

        //check if the desired direction isn't opposite of the current direction (prevent snake from going backwards)
        else if (d == DIRECTION.UP && dir != DIRECTION.DOWN ||
            d == DIRECTION.DOWN && dir != DIRECTION.UP ||
            d == DIRECTION.LEFT && dir != DIRECTION.RIGHT ||
            d == DIRECTION.RIGHT && dir != DIRECTION.LEFT) {
            //set the desired direction to the input
            queuedDirection = d;
        }

        return;
    }

    /*
     * Set a delay equal to the value of GAME_SPEED (in milliseconds)
     */
    public void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(GAME_SPEED);
        } 
        catch (Exception e) {
            //Notify the user that the frame crashed
            System.out.println("Frame crashed. Please refresh if this persists as to avoid overusing your CPU.");
            return;
        }
    }

    /*
     * The paintComponent method of the Graphics class
     * This is used for the graphics of the game.
     * In this case, it does not need to be overriden
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Show the start screen if onStartScreen is true and don't paint anything else
        if (onStartScreen) {
            g.drawImage(
                new ImageIcon("images/startscreen.png").getImage(),0,0,WINDOW_SIDELENGTH,WINDOW_SIDELENGTH,null
            );
            return;
        }

        for (int i = 0; i < BOARD_SIDELENGTH; i++) {
            for (int j = 0; j < BOARD_SIDELENGTH; j++) {
                //Check for fruit
                if (i == fruitY && j == fruitX) {
                    //paint the fruit
                    GameTile tile = new GameTile(TILE_WIDTH, FRUIT_COLOR);
                    tile.paintTile(j,i,g);
                }
                //Check for the snake
                else {
                    for (int k = 0; k < snakeLength; k++) {
                        //is part of snake
                        if (snakeX[k] == j && snakeY[k] == i) {
                            //paint the snake
                            GameTile tile = new GameTile(TILE_WIDTH,SNAKE_COLOR);
                            tile.paintTile(j,i,g);
                            break;
                        }
                    }
                }
                
            }
        }

        //show the game over screen if onEndScreen is true
        if (onEndScreen) {
            g.drawImage(
                new ImageIcon("images/gameoverscreen.png").getImage(),0,0,WINDOW_SIDELENGTH,WINDOW_SIDELENGTH,null
            );
        }

        //draw the score in the top left corner of the panel
        g.setColor(Color.WHITE);
        g.drawString("Score: "+(snakeLength-1), 5, 30); //the head of the snake is excluded from the score (hence snakeLength-1)
    } 

    /*
     * The following nested class is used to get keyboard input from the user in order
     * to play the game
     */
    class SnakeControl extends KeyAdapter {

        public SnakeControl() {
            super();
        }
    
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                //Moving Left
                case 65: // A
                case 97: // a
                case KeyEvent.VK_LEFT: //left arrow key
                    move(GamePanel.DIRECTION.LEFT);
                    break;
                //Moving Right
                case 68: // D
                case 100: // d
                case KeyEvent.VK_RIGHT: //right arrow key
                    move(GamePanel.DIRECTION.RIGHT);
                    break;
                //Moving Up
                case 87: // W
                case 119: // w
                case KeyEvent.VK_UP: //up arrow key
                    move(GamePanel.DIRECTION.UP);
                    break;
                //Moving Down
                case 83: // S
                case 115: //s
                case KeyEvent.VK_DOWN: //down arrow key
                    move(GamePanel.DIRECTION.DOWN);
                    break;
                //Start the game on the Start Screen or replay the game on the end screen
                case 32: //Space
                    //turn off the start screen 
                    if (onStartScreen)
                        onStartScreen = false;
                    //leave gameover screen and set replay to true
                    else if (onEndScreen) {
                        replay = true;
                        onEndScreen = false;
                    }
                    break;
                //Exit the game on the Game Over Screen
                case 27: //escape
                    if (onEndScreen) 
                        onEndScreen= false;
                    break;
                // Do nothing by default.
                default:
                    break;
            }
        }
    }
}
