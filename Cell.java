import java.awt.Color;

public class Cell {
    private Lifeform lifeform; // Holds the Lifeform object
    private Color colour;
    private int rowPos;
    private int colPos;
    private boolean hasLife = false;

    public Cell(int row, int col) {
        rowPos = row;
        colPos = col;

        generateLifeform();
    }

    public Color getColour() {
        return colour;
    }

    public Lifeform getLifeform() {
        return lifeform;
    }

    public int getRowPos() {
        return rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    // Options: green, yellow, red, blue, white
    public void setCellColour(Color colour) {
        this.colour = colour;
    }

    public void setLifeform(Lifeform lifeform) {
        this.lifeform = lifeform;
    }

    public boolean getHasLife() {
        return hasLife;
    }

    public void setHasLife(boolean hasLife) {
        this.hasLife = hasLife;
    }


    // Generates random number which determines the Lifeform
    public void generateLifeform() {
        int randomNumber = RandomGenerator.nextNumber(100);

        if (randomNumber >= 80) {
            lifeform = new Herbivore(rowPos, colPos);
            setCellColour(Color.YELLOW);

        } else if (randomNumber >= 60) {
            lifeform = new Plant();
            setCellColour(Color.GREEN);

        } else if (randomNumber >= 50) {
            lifeform = new Carnivore(rowPos, colPos);
            setCellColour(Color.RED);

        } else if (randomNumber >= 45) {
            lifeform = new Omnivore(rowPos, colPos);
            setCellColour(Color.BLUE);
            
        } else {
            lifeform = null;
            setCellColour(Color.WHITE);
        }
    }

}
