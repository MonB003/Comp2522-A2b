import java.util.ArrayList;
import java.awt.Color;

public class Lifeform {
    private int row;
    private int col;
    private boolean hasMoved = false;
    private Color colour;

    public Color getColour() {
        return colour;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean getMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void move(Cell currCell, Cell newCell) {
        // Move Herbivore to new cell
        newCell.setLifeform(this);
        newCell.setCellColour(colour);

        // Update Herbivore's row and col
        this.row = newCell.getRowPos();
        this.col = newCell.getColPos();

        // Remove Herbivore from old cell
        currCell.setLifeform(null);
        currCell.setCellColour(Color.WHITE);
        currCell.setHasLife(false);
    }

    // Calls method to move or pollenate depending on the type of Lifeform
    public void spotToMove(ArrayList<Cell> neighbours, int randLimit, Cell currentCell, Cell[][] world) {
    }


    // Creates an object of the appropriate Lifeform
    public Lifeform createLife(int row, int col) {
        return new Lifeform();
    }


    // Method to reproduce/give birth
    public void giveBirth(Lifeform currLife, ArrayList<Cell> emptyCells) {
        // Find cell for lifeform to birth
        int randomSpot = RandomGenerator.nextNumber(emptyCells.size() - 1);
        Cell randomCell = emptyCells.get(randomSpot);
        Lifeform newLife = currLife.createLife(randomCell.getRowPos(), randomCell.getColPos());
        newLife.setRow(randomCell.getRowPos());
        newLife.setCol(randomCell.getColPos());

        randomCell.setLifeform(newLife);
        randomCell.setCellColour(newLife.getColour());
        randomCell.setHasLife(true);
    }

}
