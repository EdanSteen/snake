import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;

public class GamePanel extends JPanel {
    //the sidelength of the board
    // the default width is set to 16
    private int boardSidelength = 16;

    public GamePanel() {
        super();
        setup();
    }    


    public GamePanel(int sidelength) {
        super();
        this.boardSidelength = sidelength;
        setup();
    } 

    /*
     * setup the basic funcions of the panel
     */
    private void setup() {
        GridLayout layout = new GridLayout(boardSidelength,boardSidelength);
        this.setLayout(layout);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
    }

    public void update() {
        //draw/redraw the board
    }
}
