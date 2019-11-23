package simpledoku;

import java.util.*;
import java.awt.Toolkit;


public class SimpledokuSolver 
{
	private int							nCellsPerSide;
	// private SimpledokuGrid				solution;
	private ArrayList<SimpledokuGrid>	solutions;
	
	
	public SimpledokuSolver(int nCellsPerSide)
	{
		this.nCellsPerSide = nCellsPerSide;
	}
	
	
	public void solve()
	{
		SimpledokuGrid emptyGrid = new SimpledokuGrid(nCellsPerSide);
		solutions = new ArrayList<>();
		solveRecurse(emptyGrid);
	}
		
	
	private void solveRecurse(SimpledokuGrid grid)
	{		
		System.out.println(grid);
		
		// The example you have seen in class finds all solutions. Here we just want
		// one solution. The 2 lines below force return from the recursion as soon as a
		// solution is found.
		// if (solution != null)
			// return;
		
		// Evaluate the current grid.
		Evaluation eval = grid.evaluate();
		
		if (eval == Evaluation.ABANDON)
		{
			// To abandon the current grid, just return.
			return;
		}
		else if (eval == Evaluation.ACCEPT)
		// {
			// Set solution to be the current grid.
			// solution = grid;
		// }
			solutions.add(grid);
		else
		{
			// Generate all possible next grids, and call solveRecurse on them.
			for (SimpledokuGrid nextGrid: grid.generateNextGrids())
				solveRecurse(nextGrid);
		}
	}

	
	// public SimpledokuGrid getSolution()
	// {
		// return solution;
	// }
	
	
	public static void main(String[] args)
	{
		SimpledokuSolver solver = new SimpledokuSolver(5);
		solver.solve();
		// System.out.println("SOLUTION:\n" + solver.getSolution());
		for (SimpledokuGrid solution : solver.solutions)
			System.out.println(solution);
		System.out.println("Solutions has size " + solver.solutions.size() + ".");
		Toolkit.getDefaultToolkit().beep();
	}
}
