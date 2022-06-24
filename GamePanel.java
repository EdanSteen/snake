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

    final Color SNAKE_COLOR = Color.GREEN;
    final Color FRUIT_COLOR = Color.RED;
    final Color BACKGROUND_COLOR = Color.BLACK;

    //checks if the game is over
    private boolean gameOver = false;
    
    private boolean running = true;

    //the coordinates of the fruit 
    int fruitX, fruitY;
    
    //the length of the snake (the head is included)
    int snakeLength; 
    
    //the maximum length a snake can reach
    int maxLength = (BOARD_SIDELENGTH*BOARD_SIDELENGTH);
    
    //the coordinates of each of the parts of the snake
    int[] snakeX = new int[maxLength]; 
    int[] snakeY = new int[maxLength]; 

    //checks if the player is on the start screen
    private boolean onStartScreen = true;

    //The direction the snake is moving towards
    public enum DIRECTION {
        STOP, LEFT, RIGHT, UP, DOWN
    };
    public DIRECTION dir;


    public GamePanel() {
        this.setPreferredSize(new Dimension(WINDOW_SIDELENGTH,WINDOW_SIDELENGTH));
        this.setBackground(BACKGROUND_COLOR);
        this.setOpaque(true);
        onStartScreen = true;
        running = true;
    }    

    /*
     * This function essentially contains all the logic of snake, and then calls
     * the repaint() method to draw it
     * 
     */
    public void runGame() {
        //Setup the game

        //set game over to false
        this.gameOver = false;

        //set length to just the head
        snakeLength = 1;
        
        //refresh the snake body
        snakeX = new int[maxLength]; 
        snakeY = new int[maxLength]; 

        //put the snake in the middle
        snakeX[0] = BOARD_SIDELENGTH/2;
        snakeY[0] = BOARD_SIDELENGTH/2;
        
        //put the fruit across from snake
        fruitX = snakeX[0] + (BOARD_SIDELENGTH/4);
        fruitY = snakeY[0];

        //keep snake still
        dir = DIRECTION.STOP;

        while (!gameOver) {
            // render the board
            this.repaint();

            // Logic 

            //Move up the components of the snake
            for (int i = snakeLength; i > 0; i--) {
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
                this.gameOver = true;

            //check if snake hits itself
            for (int i = 1; i < snakeLength; i ++)
                if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) 
                    this.gameOver = true;
                    
            //check if fruit is eaten
            if (snakeX[0] == fruitX && snakeY[0] == fruitY) {
                //make the snake longer
                snakeLength++;

                //Respawn the fruit somewhere else
                boolean validPosition = true; //is true if the fruit isn't inside the snake's tail
                do {
                    //put the coordinates in a random spot
                    fruitY = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));
                    fruitX = (int) Math.round(Math.random()*(BOARD_SIDELENGTH-1));  

                    //reposition the fruit if it is in the same spot as part of the tail
                    for (int i = 0; i < snakeLength;i++) {
                        if  (fruitX == snakeX[i] && fruitY == snakeY[i]) {
                            validPosition = false;
                            break;
                        }
                    }
                } while (!validPosition);
            }

            //delay by the game speed before continuing to avoid destroying the computer hardware
            delay();
        }
    
        //gameOver
        do {
            delay();
        } while (running);
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

    /*
     * Set a delay equal to the Game Speed in milliseconds
     * 
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
        //turn of the start screen 
        if (this.onStartScreen)
            this.onStartScreen = false;
        
        /*
        //restart the game
        else if (this.gameOver) {
            this.running = false;
            this.runGame();
        }  */
    }

    public void exit() {
        if (this.gameOver) 
            this.running = false;
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
        else if (this.gameOver && running) {
            g.drawImage(
                new ImageIcon("tempgameover.png").getImage(), 0, 0, WINDOW_SIDELENGTH,WINDOW_SIDELENGTH, null
            );
            return;
        }


        for (int i = 0; i < BOARD_SIDELENGTH; i++) {
            for (int j = 0; j < BOARD_SIDELENGTH; j++) {
                GameTile tile = new GameTile(TILE_WIDTH);
                

                //Check for fruit
                if (i == fruitY && j == fruitX) {
                    tile.setColor(FRUIT_COLOR);
                }
                //Check for the snake
                else {
                    boolean isSnake = false; //keeps track of whether the tile is part of the snake
                    for (int k = 0; k < snakeLength; k++) {
                        //is part of snake
                        if (snakeX[k] == j && snakeY[k] == i) {
                            tile.setColor(SNAKE_COLOR);
                            isSnake = true;
                            break;
                        }
                    }
                    //if this tile isn't part of the snake, don't add it
                    if (!isSnake) {
                        continue;
                    }
                }
                tile.paintTile(j,i,g);
            }
        }
    } 

}

