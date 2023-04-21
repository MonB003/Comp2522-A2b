public abstract class Animal extends Lifeform {

    private int currentLives = 5;

    abstract void eat(Cell currentCell, Cell plantCell);

    abstract void die(Cell[][] world);

    public int getCurrentLives() {
        return currentLives;
    }

    // When plant is eaten
    public void resetCurrentLives() {
        currentLives = 5;
    }

    // When window is clicked
    public void livesLeft() {  
        currentLives--;
    }

}
