/*
 * The GamePanel class contains all the logic of the game Snake and
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
import javax.swing.ImageIcon;

public class GamePanel extends JPanel {
    
    //Width of the window (px)
    final int WINDOW_SIDELENGTH = 600;
    //width of the individual tiles
    final int TILE_WIDTH = 40;
    //width of the board
    final int BOARD_SIDELENGTH = WINDOW_SIDELENGTH/TILE_WIDTH;
    //time between screen repaint (ms)
    final int GAME_SPEED = 100;

    //The colors of the snake, fruit and background
    //In the future, these could be changed by the player
    final Color SNAKE_COLOR = Color.GREEN;
    final Color FRUIT_COLOR = Color.RED;
    final Color BACKGROUND_COLOR = Color.BLACK;

    //the coordinates of the fruit 
    int fruitX, fruitY;
    
    //the length of the snake (the snake's head is included)
    int snakeLength; 
    
    //the maximum length a snake can reach
    final int MAX_LENGTH = (BOARD_SIDELENGTH*BOARD_SIDELENGTH);
    
    //the coordinates of each of the parts of the snake
    int[] snakeX = new int[MAX_LENGTH]; 
    int[] snakeY = new int[MAX_LENGTH]; 

    //checks if the player is on the start screen
    private boolean onStartScreen = true;
    //Is shown when the game is over
    private boolean onEndScreen = false;
    //determines if player wants to replay the game or not
    private boolean replay = false;

    //The direction the snake is moving towards
    public enum DIRECTION {
        STOP, LEFT, RIGHT, UP, DOWN
    };
    private DIRECTION dir;


    public GamePanel() {
        this.setPreferredSize(new Dimension(WINDOW_SIDELENGTH,WINDOW_SIDELENGTH));
        this.setBackground(BACKGROUND_COLOR);
        this.setOpaque(true);
        onStartScreen = true;
    }    

    /*
     * This function essentially contains all the logic of snake, and then calls
     * the repaint() method to draw it
     * 
     */
    public void runGame() {
        //Setup the game

        //set game over to false
        boolean gameOver = false;
        replay = false;

        //set length to just the head
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

        //keep snake still at the starts
        dir = DIRECTION.STOP;

        //Loop until the game ends
        while (!gameOver) {
            // Render the graphics
            this.repaint();

            if (onEndScreen) {
                delay();
                return;
            }
                
            // Logic 

            //Move up the components of the snake
            for (int i = snakeLength; i > 0; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            
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
                    //set value to true to avoid infinite loop
                    validPosition = true;
                    //put the coordinates to a random spot on the board
                    fruitY = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));
                    fruitX = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));  

                    //reposition the fruit if it is inside part of the snake
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
     
        //gameOver
        onEndScreen = true;
        this.repaint();
        do {
            delay();
        } while(onEndScreen);
        if (replay == true) 
            runGame();

        return; 
    }

    /*
     * set the direction of the snake
     * 
     * @param DIRECTION d: the direction the snake should head towards
     */
    public void move(DIRECTION d) {
        //dont do anything if the game is still on the start screen
        if (onStartScreen)
            return;

        //prevent user from trying moving backwards into the snake

        //check if the desired direction isn't opposite of the current direction
        if ((d == DIRECTION.UP && dir != DIRECTION.DOWN) ||
            (d == DIRECTION.DOWN && dir != DIRECTION.UP) ||
            (d == DIRECTION.LEFT && dir != DIRECTION.RIGHT) ||
            (d == DIRECTION.RIGHT && dir != DIRECTION.LEFT)) {
                //set the direction to the input
                this.dir = d;
        }

        return;
    }

    /*
     * Set a delay equal to the Game Speed (in milliseconds)
     */
    public void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(GAME_SPEED);
        } 
        catch (Exception e) {
            System.out.println("Game crashed for unexpected reason. Please refresh.");
            return;
        }
    }

    /*
     * Triggered by the space bar when the Start Screen and 
     * Game Over Screens are visible
     */
    public void triggerScreenEvent() {
        //turn off the start screen 
        if (this.onStartScreen)
            this.onStartScreen = false;
        else if (this.onEndScreen) {
            this.replay = true;
            onEndScreen = false;
        }
    }

    /*
     * exit the game over screen
     */
    public void exit() {
        if (onEndScreen) 
            onEndScreen= false;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Show the start screen
        if (onStartScreen) {
            g.drawImage(
                new ImageIcon("startscreen.png").getImage(), 0, 0, WINDOW_SIDELENGTH,WINDOW_SIDELENGTH, null
            );
            return;
        }
        //show the game over screen
        if (onEndScreen) {
            g.drawImage(
                new ImageIcon("gameoverscreen.png").getImage(), 0, 0, WINDOW_SIDELENGTH,WINDOW_SIDELENGTH, null
            );
            
            //Draw the score in the corner over the game over screen
            g.setColor(Color.WHITE);
            g.drawString("Score: "+(snakeLength-1), 5, 30);

            return;
        }

        for (int i = 0; i < BOARD_SIDELENGTH; i++) {
            for (int j = 0; j < BOARD_SIDELENGTH; j++) {
                //Check for fruit
                if (i == fruitY && j == fruitX) {
                    //paint the fruit
                    GameTile tile = new GameTile(TILE_WIDTH);
                    tile.setColor(FRUIT_COLOR);
                    tile.paintTile(j,i,g);
                }
                //Check for the snake
                else {
                    for (int k = 0; k < snakeLength; k++) {
                        //is part of snake
                        if (snakeX[k] == j && snakeY[k] == i) {
                            //paint the snake
                            GameTile tile = new GameTile(TILE_WIDTH);
                            tile.setColor(SNAKE_COLOR);
                            tile.paintTile(j,i,g);
                            break;
                        }
                    }
                }
                
            }
        }

        //draw the score in the corner of the panel
        g.setColor(Color.WHITE);
        g.drawString("Score: "+(snakeLength-1), 5, 30);
    } 
}

