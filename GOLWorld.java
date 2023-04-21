import java.util.ArrayList;

public class GOLWorld {
    private int cellRows;
    private int cellCols;
    private Cell[][] world;
    private Cell[][] snapShot;

    private GameFrame gameFrame;

    public GOLWorld(int rows, int cols) {
        cellRows = rows;
        cellCols = cols;
        world = new Cell[rows][cols];
    }

    // Initializes each cell in the world
    public void init() {
        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                world[r][c] = new Cell(r, c);
            }
        }
    }

    public Cell[][] getWorldCells() {
        return world;
    }

    public Cell getACell(int row, int col) {
        return world[row][col];
    }

    public int getCellRows() {
        return cellRows;
    }

    public int getCellCols() {
        return cellCols;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    // Called in GameFrame MouseInput click event when window is clicked
    // This moves all the Lifeforms
    public void windowClicked() {
        snapShot = world;

        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                movePlants(snapShot[r][c]);
            }
        }

        snapShot = world;

        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                moveHerbivores(snapShot[r][c]);
            }
        }

        snapShot = world;

        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                moveOmnivores(snapShot[r][c]);
            }
        }

        snapShot = world;

        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                moveCarnivores(snapShot[r][c]);
            }
        }

        // Update board colours
        coloursAfterAction();
    }

    // Changes GameFrame board colours after window is clicked
    public void coloursAfterAction() {
        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                gameFrame.setCellPanelColour(r, c);

                if (world[r][c].getLifeform() instanceof Animal) {
                    resetAnimalMoved(world[r][c].getLifeform());
                }
            }
        }
    }

    // Sets Animal objects hasMoved value to false
    public void resetAnimalMoved(Lifeform lifeform) {
        if (lifeform instanceof Animal) {
            Animal animal = (Animal) lifeform;
            animal.setMoved(false);
        }
    }

    // When each object moves after window is clicked
    public void movePlants(Cell c) {
        Lifeform lifeform = c.getLifeform();

        if (lifeform instanceof HerbEdible) {
            checkPosition(c);
        }
    }

    public void moveHerbivores(Cell c) {
        Lifeform lifeform = c.getLifeform();

        if (lifeform instanceof Herbivore && !lifeform.getMoved()) {
            Animal animal = (Animal) lifeform;
            animal.livesLeft();

            // If animal has no more lives
            if (animal.getCurrentLives() == 0) {
                animal.die(world);
            } else {
                checkPosition(c);
            }
        } 
    }

    public void moveOmnivores(Cell c) {
        Lifeform lifeform = c.getLifeform();

        if (lifeform instanceof Omnivore && !lifeform.getMoved()) {
            Animal animal = (Animal) lifeform;
            animal.livesLeft();

            // If animal has no more lives
            if (animal.getCurrentLives() == 0) {
                animal.die(world);
            } else {
                checkPosition(c);
            }
        } 
    }

    public void moveCarnivores(Cell c) {
        Lifeform lifeform = c.getLifeform();

        if (lifeform instanceof Carnivore && !lifeform.getMoved()) {
            Animal animal = (Animal) lifeform;
            animal.livesLeft();

            // If animal has no more lives
            if (animal.getCurrentLives() == 0) {
                animal.die(world);
            } else {
                checkPosition(c);
            }
        } 
    }


    // Checks the position of the cell and adds that cell's neighbours to an ArrayList
    public void checkPosition(Cell cell) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        int randLimit;

        if (cell.getRowPos() == 0 && cell.getColPos() == 0) { // Top left corner [3 neighbours]
            neighbours = addCornerCells(cell);
            randLimit = 2;

        } else if (cell.getRowPos() == 0 && cell.getColPos() == getCellCols() - 1) { // Top right corner [3 neighbours]
            neighbours = addCornerCells(cell);
            randLimit = 2;

        } else if (cell.getRowPos() == 0 && cell.getColPos() != 0 && cell.getColPos() != getCellCols() - 1) { // Row 0
                                                                                                              // [5 neighbours]
            neighbours = addEdgeCells(cell);
            randLimit = 4;

        } else if (cell.getRowPos() != 0 && cell.getRowPos() != getCellRows() - 1 && cell.getColPos() == 0) { // Col 0
                                                                                                              // [5 neighbours]
            neighbours = addEdgeCells(cell);
            randLimit = 4;

        } else if (cell.getRowPos() == getCellRows() - 1 && cell.getColPos() == 0) { // Bottom left corner [3
                                                                                     // neighbours]
            neighbours = addCornerCells(cell);
            randLimit = 2;

        } else if (cell.getRowPos() == getCellRows() - 1 && cell.getColPos() == getCellCols() - 1) { // Bottom right
                                                                                                     // corner [3
                                                                                                     // neighbours]
            neighbours = addCornerCells(cell);
            randLimit = 2;

        } else if (cell.getRowPos() == getCellRows() - 1 && cell.getColPos() != 0
                && cell.getColPos() != getCellCols() - 1) { // Last Row (rows-1) [5 neighbours]
            neighbours = addEdgeCells(cell);
            randLimit = 4;

        } else if (cell.getRowPos() != 0 && cell.getRowPos() != getCellRows() - 1
                && cell.getColPos() == getCellCols() - 1) { // Last Col (cols-1) [5 neighbours]
            neighbours = addEdgeCells(cell);
            randLimit = 4;

        } else { // Middle section, has neighbours on all sides [8 neighbours]
            neighbours = addNeighbourCells(cell);
            randLimit = 7;
        }

        Lifeform lifeform = cell.getLifeform();
        // Calls appropriate overidden method based on if it's a herbivore or plant
        lifeform.spotToMove(neighbours, randLimit, cell, world);
    }

    public ArrayList<Cell> addNeighbourCells(Cell currentCell) {
        // Stores all the neighbour cells surrounding this cell
        ArrayList<Cell> neighbours = new ArrayList<>();

        int rowPos = currentCell.getRowPos();
        int colPos = currentCell.getColPos();
        neighbours.add(snapShot[rowPos - 1][colPos - 1]);
        neighbours.add(snapShot[rowPos - 1][colPos]);
        neighbours.add(snapShot[rowPos - 1][colPos + 1]);
        neighbours.add(snapShot[rowPos][colPos - 1]);
        neighbours.add(snapShot[rowPos][colPos + 1]);
        neighbours.add(snapShot[rowPos + 1][colPos - 1]);
        neighbours.add(snapShot[rowPos + 1][colPos]);
        neighbours.add(snapShot[rowPos + 1][colPos + 1]);

        return neighbours;
    }

    public ArrayList<Cell> addCornerCells(Cell currentCell) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        int rowPos = currentCell.getRowPos();
        int colPos = currentCell.getColPos();

        // Adds neighbour cells based on the position of the current cell
        if (rowPos == 0 && colPos == 0) { // Top left
            neighbours.add(snapShot[rowPos][colPos + 1]);
            neighbours.add(snapShot[rowPos + 1][colPos + 1]);
            neighbours.add(snapShot[rowPos + 1][colPos]);

        } else if (rowPos == 0 && colPos == getCellCols() - 1) { // Top right
            neighbours.add(snapShot[rowPos][colPos - 1]);
            neighbours.add(snapShot[rowPos + 1][colPos - 1]);
            neighbours.add(snapShot[rowPos + 1][colPos]);

        } else if (rowPos == getCellRows() - 1 && colPos == 0) { // Bottom left
            neighbours.add(snapShot[rowPos - 1][colPos]);
            neighbours.add(snapShot[rowPos - 1][colPos + 1]);
            neighbours.add(snapShot[rowPos][colPos + 1]);

        } else if (rowPos == getCellRows() - 1 && colPos == getCellCols() - 1) { // Bottom right
            neighbours.add(snapShot[rowPos - 1][colPos - 1]);
            neighbours.add(snapShot[rowPos - 1][colPos]);
            neighbours.add(snapShot[rowPos][colPos - 1]);
        }

        return neighbours;
    }

    public ArrayList<Cell> addEdgeCells(Cell currentCell) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        int rowPos = currentCell.getRowPos();
        int colPos = currentCell.getColPos();

        if (rowPos == 0 && colPos != 0 && colPos != getCellCols() - 1) { // Row 0
            neighbours.add(snapShot[rowPos][colPos - 1]);
            neighbours.add(snapShot[rowPos][colPos + 1]);
            neighbours.add(snapShot[rowPos + 1][colPos - 1]);
            neighbours.add(snapShot[rowPos + 1][colPos]);
            neighbours.add(snapShot[rowPos + 1][colPos + 1]);

        } else if (rowPos != 0 && rowPos != getCellRows() - 1 && colPos == 0) { // Col 0
            neighbours.add(snapShot[rowPos - 1][colPos]);
            neighbours.add(snapShot[rowPos - 1][colPos + 1]);
            neighbours.add(snapShot[rowPos][colPos + 1]);
            neighbours.add(snapShot[rowPos + 1][colPos]);
            neighbours.add(snapShot[rowPos + 1][colPos + 1]);

        } else if (rowPos == getCellRows() - 1 && colPos != 0 && colPos != getCellCols() - 1) { // Last Row (rows-1)
            neighbours.add(snapShot[rowPos - 1][colPos - 1]);
            neighbours.add(snapShot[rowPos - 1][colPos]);
            neighbours.add(snapShot[rowPos - 1][colPos + 1]);
            neighbours.add(snapShot[rowPos][colPos - 1]);
            neighbours.add(snapShot[rowPos][colPos + 1]);

        } else if (rowPos != 0 && rowPos != getCellRows() - 1 && colPos == getCellCols() - 1) { // Last Col (cols-1)
            neighbours.add(snapShot[rowPos - 1][colPos - 1]);
            neighbours.add(snapShot[rowPos - 1][colPos]);
            neighbours.add(snapShot[rowPos][colPos - 1]);
            neighbours.add(snapShot[rowPos + 1][colPos - 1]);
            neighbours.add(snapShot[rowPos + 1][colPos]);
        }

        return neighbours;
    }

}
