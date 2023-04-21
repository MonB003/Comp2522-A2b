import java.awt.Color;
import java.util.ArrayList;

public class Plant extends Lifeform implements HerbEdible, OmniEdible {
    private Color colour = Color.GREEN;

    Plant() { }
    
    
    public Lifeform createLife(int row, int col) {
        return new Plant();
    }

    // Plant doesn't move
    public void move(Cell currCell, Cell newCell) { }


    // Creates a Plant object in a cell
    public void pollenate(Cell[][] world, int row, int col) {
        Cell newCell = new Cell(row, col);
        newCell.setLifeform(new Plant());
        newCell.setCellColour(colour);
        newCell.setHasLife(true);
        world[row][col] = newCell;
    }


    // Checks neighbouring cells to see if this plant can pollinate
    public void spotToMove(ArrayList<Cell> neighbours, int randLimit, Cell currentCell, Cell[][] world) {
        ArrayList<Cell> emptyCells = new ArrayList<>();
        
        int plantNeighbours = 0;
        int emptyNeighbours = 0;

        // Count number of plants and empty cells surrounding this cell
        for (int pos = 0; pos <= randLimit; pos++) {
            Cell neighbourCell = neighbours.get(pos);

            if (neighbourCell.getLifeform() instanceof HerbEdible) {
                plantNeighbours++;
            }

            if (neighbourCell.getLifeform() == null) {
                emptyCells.add(neighbourCell);
                emptyNeighbours++;
            }
        }

        // Check birth (pollenate)
        if (plantNeighbours >= 2 && emptyNeighbours >= 3) {
            giveBirth(this, emptyCells);
        }
    }

}
