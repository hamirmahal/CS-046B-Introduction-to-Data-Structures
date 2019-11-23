package simpledoku;

import java.util.*;


public class SimpledokuGrid 
{
	private int				nCellsPerSide;
	private int[][]			values;
	
	
	public SimpledokuGrid(int nCellsPerSide)
	{
		this.nCellsPerSide = nCellsPerSide;
		values = new int[nCellsPerSide][nCellsPerSide];
	}
	
	
	// This is called a "copy constructor". A copy ctor has 1 arg, which is the same type as
	// the current class. The ctor should make the new instance look just like the "source"
	// instance. CAUTION: The source and the current grid should each have their own "values"
	// array. So don't say "this.values = source.values". You have to create a new values
	// array, and copy numbers from source.values into the new array, one at a time.
	public SimpledokuGrid(SimpledokuGrid source)
	{
		this.nCellsPerSide = source.nCellsPerSide;
		this.values = new int[nCellsPerSide][nCellsPerSide];
		
		for (int i = 0; i < source.values.length; i++)
			for (int j = 0; j < source.values[i].length; j++)
				this.values[i][j] = source.values[i][j];
	}	
	
	
	//
	// Returns true if the input list contains a repeated value that isn't zero.
	// Call this from isLegal().
	//
	private boolean containsNonZeroRepeats(ArrayList<Integer> ints)
	{
		/*// I believe this next algorithm takes O(n) + O(n) time if the input
		// ArrayList contains 0. Otherwise, I believe it will take O(n) + O(1) time.
		HashSet uniqueElements = new HashSet(ints);
		
		// We subtract the size of uniqueElements by 1 to account for zero...
		if (uniqueElements.contains(0))
			return uniqueElements.size() - 1 !=
					ints.size() - Collections.frequency(ints, 0);
		
		// If the number of unique elements in ints does not equal the size of ints,
		// when ints contains nonzero elements, 
		// that means there are duplicates, so we return true.
		return uniqueElements.size() != ints.size();*/
		
		// I believe this next algorithm is the most efficient, because it only takes
		// O(n) time, even in the worst case.
		HashSet helper = new HashSet();
		int timesAdded = 0;
		
		for (int i = 0; i < ints.size(); i++)
			if (ints.get(i) != 0)
			{
				helper.add(ints.get(i));
				timesAdded++;
			}
		
		// Because we only add to helper when we add a nonzero element,
		// if helper's size does not equal the amount of times we added to helper,
		// that means there are duplicates that are not zero.
		return helper.size() != timesAdded;
		
		/*// This will have a O(n^2) running time...
		// Specifically, it will have n(n + 1)/2 operations
		for (int i = 0; i < ints.size(); i++)
			for (int j = i + 1; j < ints.size(); j++)
				if (ints.get(i).equals(ints.get(j)) && ints.get(i) != 0)
					return true;
		
		return false;*/
	}
	
	
	public boolean isLegal()
	{	
		ArrayList<Integer> parameterForCNZR = new ArrayList<Integer>();
		
		// Check all rows. For each row, put the corresponding numbers from
		// the values[][] array into an ArrayList<Integer>. Then pass the array
		// list to containsNonZeroRepeats(). If containsNonZeroRepeats() return true,
		// then this grid isn't legal, so return false.
		for (int i = 0; i < this.values.length; i++)
		{
			for (int j = 0; j < this.values[i].length; j++)
				parameterForCNZR.add(values[i][j]);
		
			if (this.containsNonZeroRepeats(parameterForCNZR))
				return false;
			
			parameterForCNZR.clear();
		}
		// Check all columns. For each column, put the corresponding numbers from
		// the values[][] array into an ArrayList<Integer>. Then pass the array
		// list to containsNonZeroRepeats(). If containsNonZeroRepeats() return true,
		// then this grid isn't legal, so return false.
		for (int j = 0; j < values.length; j++)
		{
			for (int i = 0; i < values.length; i++)
				parameterForCNZR.add(values[i][j]);
			
			if (this.containsNonZeroRepeats(parameterForCNZR))
				return false;
			
			parameterForCNZR.clear();
		}
		// Check top-left to bottom-right diagonal. 
		for (int i = 0; i < values.length; i++)
			parameterForCNZR.add(values[i][i]);
		
		if (this.containsNonZeroRepeats(parameterForCNZR))
			return false;
		
		parameterForCNZR.clear();
		// Check top-right to bottom-left diagonal. 
		for (int i = 0; i < values.length; i++)
			parameterForCNZR.add(values[i][values.length - 1 - i]);

		if (this.containsNonZeroRepeats(parameterForCNZR))
			return false;
		
		parameterForCNZR.clear();
		// If we haven't returned yet, this grid must be legal.
		return true;
	}
	
	
	// Returns true if all members of the values[][] array are non-zero.
	public boolean isFull()
	{
		for (int i = 0; i < this.values.length; i++)
			for (int j = 0; j < this.values[i].length; j++)
				if (values[i][j] == 0)
					return false;
		
		return true;
	}	
	
	
	
	// Returns the Evaluation corresponding to the state of this grid. The return will be
	// Evaluation.ABANDON, Evaluation.ACCEPT, or Evaluation.CONTINUE. To determine what to
	// return, call isLegal() and isFull().
	public Evaluation evaluate()
	{
		if (!isLegal())
			return Evaluation.ABANDON;
		else if (isFull())
			return Evaluation.ACCEPT;
		else
			return Evaluation.CONTINUE;
	}
	
	
	// Returns a size=2 array of ints that index the next empty cell in the values[][] array.
	// The 2 ints in the returned array are the first and second indices into the values[][] array.
	// Returns null if this grid is full.
	private int[] getIndicesOfNextEmptyCell()
	{
		int[] xy = new int[2];
		
		for (xy[0]=0; xy[0]<nCellsPerSide; xy[0]++)
			for (xy[1]=0; xy[1]<nCellsPerSide; xy[1]++)
				if (values[xy[0]][xy[1]] == 0)
					return xy;
		
		return null;
	}
	
	
	//
	// COMPLETE THIS
	//
	//
	// Finds an empty member of values[][]. Returns an array list containing nCellsPerSide grids that look like the 
	// current grid, except the empty member contains 1, 2, 3 .... nCellsPerSide. 
	// 
	//
	// Example: if this grid = 12300
	//                         00000
	//                         00000
	//                         00000
	//                         00000
	//
	// Then the returned array list contains:
	//
	//      12310        12320        12330        12340        12350
	//      00000        00000        00000        00000        00000
	//      00000        00000        00000        00000        00000
	//      00000        00000        00000        00000        00000
	//      00000        00000        00000        00000        00000
	//
	ArrayList<SimpledokuGrid> generateNextGrids()
	{		
		int[] indicesOfNext = getIndicesOfNextEmptyCell();
		ArrayList<SimpledokuGrid> nextGrids = new ArrayList<SimpledokuGrid>();
		// Generate some grids and put them in nextGrids. Be sure that every
		// grid is a separate object.
		for (int i = 1; i <= nCellsPerSide; i++)
		{
			SimpledokuGrid newGrid = new SimpledokuGrid(this);
			newGrid.values[indicesOfNext[0]][indicesOfNext[1]] = i;
			
			// For this next part, we should only add newGrid if it's legal. I want to 
			// see if this change significantly decreases the running time.
			if (newGrid.isLegal())
				nextGrids.add(newGrid);
		}
		return nextGrids;
	}
	
	
	// Use this for debugging if it's helpful.
	public String toString()
	{
		String s = "";
		for (int j=0; j<nCellsPerSide; j++)
		{
			for (int i=0; i<nCellsPerSide; i++)
			{
				if (values[j][i] == 0)
					s += ".";
				else
					s += ("" + values[j][i]);
			}
			s += "\n";
		}
		return s;
	}
}
