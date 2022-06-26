/*
 * A very simple class that creates tiles that can be painted in a JPanel using a tile-based coordinate system.
 * 
 * @author: Edan Steen
 * @version: 1.0
 */
import java.awt.Color;
import java.awt.Graphics;

public class GameTile  {

    //color of the tile.
    public Color color;
    //width of the tile
    public int tileWidth;

    /*
     * Create a tile with the specified width
     * 
     * @param int width: the desired width of the tile
     */
    GameTile(int width) {
        this.tileWidth = width;
    }

    /*
     * Create a tile with the specified width and color
     * 
     * @param int width: the desired width of the tile
     * @param Color c: the desired color of the tile
     */
    GameTile(int width, Color c) {
        this.tileWidth = width;
        this.color = c;
    }

    /*
     * Set the tile to a specified color
     * 
     * @param Color c: the color the tile is being set to
     */
    public void setColor(Color c) {
        this.color = c;
        return;
    }

    /*
     * Paint the tile given the coordinates and a graphics object
     * the method automatically places the tile in the proper spot 
     * on the grid based on its width
     * 
     * @param int x: the x coordinate of the tile (in tiles)
     * @param int y: the y coordinate of the tile (in tiles)
     * @param Graphics g: the graphics object used to draw the tile
     */
    public void paintTile(int x, int y, Graphics g) {
        g.setColor(color);
        //make the tile with the specified width and space it so that other tiles fit into their own tile-based coordinates
        g.fillRect(x*tileWidth,y*tileWidth,tileWidth,tileWidth);
        return;
    }
}
