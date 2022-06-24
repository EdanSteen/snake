import javax.swing.JLabel;
import java.awt.Color;

public class GameTile extends JLabel {
    public enum TILES {
        HEAD, TAIL, FRUIT, EMPTY
    };
    public TILES type;

    Color color;

    GameTile() {
        super();
    }
    
    GameTile(TILES tileType) {
        super();
        this.type = tileType;
    }

    public void updateTile() {
        switch (this.type) {
            case HEAD:
            case TAIL:
                this.setColor(Color.GREEN);
                break;
            case FRUIT:
                this.setColor(Color.RED);
                break;
            case EMPTY:
                this.setColor(Color.BLACK);
                break;
            default:
                break;
        }
    }

    private void setColor(Color c) {
        this.color = c;
        return;
    }

    public Color getColor() {
        return this.color;
    }
}
