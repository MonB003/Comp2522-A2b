import java.awt.Color;
import java.util.ArrayList;

public class Herbivore extends Animal implements CarnEdible, OmniEdible {
    private Color colour = Color.YELLOW;
    private int rowPos;
    private int colPos;

    Herbivore(int row, int col) {
        rowPos = row;
        colPos = col;
    }
    

    public Lifeform createLife(int row, int col) {
        return new Herbivore(row, col);
    }

    // Sets Lifeform into new cell, removes Lifeform from old cell
    public void move(Cell currCell, Cell newCell) {
        // Move to new cell
        newCell.setLifeform(this);
        newCell.setCellColour(colour);

        // Update it's row and col
        this.rowPos = newCell.getRowPos();
        this.colPos = newCell.getColPos();

        // Remove it from old cell
        currCell.setLifeform(null);
        currCell.setCellColour(Color.WHITE);
        currCell.setHasLife(false);
    }

    @Override
    void eat(Cell currentCell, Cell plantCell) {
        move(currentCell, plantCell);
        resetCurrentLives();
    }

    @Override
    void die(Cell[][] world) {
        // If it ran out of lives, clear the cell
        Cell newCell = new Cell(rowPos, colPos);
        newCell.setLifeform(null);
        newCell.setCellColour(Color.WHITE);
        newCell.setHasLife(false);
        world[rowPos][colPos] = newCell;
    }

    // Generates a random number which determines the neighbour cell to move to
    public void spotToMove(ArrayList<Cell> neighbours, int randLimit, Cell currentCell, Cell[][] world) {
        boolean hasMoved = false;

        while (!hasMoved) {
            int randomSpot = RandomGenerator.nextNumber(randLimit);
            Cell neighbourCell = neighbours.get(randomSpot);
            Lifeform life = neighbours.get(randomSpot).getLifeform();

            if (life == null && !neighbourCell.getHasLife()) { // If cell is empty
                move(currentCell, neighbourCell);
                hasMoved = true;
                setMoved(true);
                currentCell.setHasLife(true);

            } else if (life instanceof HerbEdible && !neighbourCell.getHasLife()) { // If lifeform is edible
                eat(currentCell, neighbourCell);
                hasMoved = true;
                setMoved(true);
                currentCell.setHasLife(true);

            } else {
                setMoved(true);
                hasMoved = true;
                move(currentCell, neighbourCell);
            }
        }

        checkBirth(neighbours);
    }

    public void checkBirth(ArrayList<Cell> neighbours) {
        ArrayList<Cell> emptyCells = new ArrayList<>();

        int thisNeighbours = 0;
        int emptyNeighbours = 0;
        int foodCount = 0;

        for (Cell cell : neighbours) {
            // If they are the same type
            if (cell.getLifeform() instanceof Herbivore) {
                thisNeighbours++;
            } else if (cell.getLifeform() == null) {
                emptyNeighbours++;
                emptyCells.add(cell);
            } else if (cell.getLifeform() instanceof HerbEdible) {
                foodCount++;
            }
        }

        if (thisNeighbours >= 1 && emptyNeighbours >= 2 && foodCount >= 2) {
            giveBirth(this, emptyCells);
        }        
    }

}
