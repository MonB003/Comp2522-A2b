import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {

   private JPanel panels[][];    // Array of JPanels to display layout
   private Container container;
   private GridLayout grid;

   private GOLWorld world;
   private int turns = 0;        // Keeps track of turns

   public GameFrame(GOLWorld golWorld) {
      world = golWorld;
      world.setGameFrame(this);

      // Set up layout
      grid = new GridLayout(world.getCellRows(), world.getCellCols(), 0, 0);
      container = getContentPane();
      container.setLayout(grid);

      // Create and add panels to window
      panels = new JPanel[world.getCellRows()][world.getCellCols()];

      for (int r = 0; r < world.getCellRows(); r++) {
         for (int c = 0; c < world.getCellCols(); c++) {
            panels[r][c] = new JPanel();
            panels[r][c].setBackground(world.getACell(r, c).getColour());
            panels[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(panels[r][c]);
         }
      }

      this.setTitle("Game of Life");
      addMouseListener(new MouseInput()); 
   }


   public int getTurns() {
      return turns;
   }

   public void setCellPanelColour(int row, int col) {
      panels[row][col].setBackground(world.getACell(row, col).getColour());
   }


   private class MouseInput extends MouseAdapter {
      // Responds when a mouse clicks the screen
      public void mouseClicked(MouseEvent e) {
         turns++;
         world.windowClicked();
      }
   }

}
