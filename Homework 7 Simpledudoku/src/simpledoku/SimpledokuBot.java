package simpledoku;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;


public class SimpledokuBot
{
	private String									packageName;
	private Class<?> 								gridClass;
	private Constructor<?>							gridIntCtor;
	private Class<?> 								solverClass;
	private Constructor<?>							solverIntCtor;
	
	SimpledokuBot()
	{
		String className = getClass().getName();
		packageName = className.substring(0, className.lastIndexOf('.'));
	}
	

	private Class<?> getClass(String name)
	{
		if (!name.startsWith(packageName))
			name = packageName + "." + name;
		try 
		{
			return Class.forName(name);
		}
		catch (ClassNotFoundException x)
		{
			return null;
		}
	}
	
	
	private void grade()
	{
		gradeGridExists();			// sets gridClass
		gradeGridCopyCtor();
		gradeGridIntCtor();			// sets gridIntCtor
		gradeGridNonZeroRepeats();
		gradeGridIsLegal();
		gradeGridIsFull();
		gradeGridEvaluate();
		
		gradeSolverExists();		// sets solverClass
		gradeSolverCtor();
		gradeSolver();
		
		sop("100 points.");
	}
	
	
	private void fail(String s)
	{
		sop("Zero points: " + s);
		System.exit(0);
	}
	
	
	

	
						
						
						
						
						/**************************************
						 *                                    *
						 *                GRID                *
						 *                                    *
						 **************************************/


	
	private void gradeGridExists()
	{
		gridClass = getClass("SimpledokuGrid");
		if (gridClass == null)
			fail("No SimpledokuGrid class in package simpledoku");
	}
	
	
	private void gradeGridCopyCtor()
	{
		Class<?>[] gridClassList = new Class<?>[] { gridClass };
		try
		{
			gridClass.getDeclaredConstructor(gridClassList);
		}
		catch (NoSuchMethodException x)
		{
			fail("No copy constructor in package simpledoku");
		}		
	}
	
	
	private void gradeGridIntCtor()
	{
		Class<?>[] classList = new Class<?>[] { int.class };
		try
		{
			gridIntCtor = gridClass.getDeclaredConstructor(classList);
		}
		catch (NoSuchMethodException x)
		{
			fail("Zero points: SimpledokuGrid(int) ctor has been deleted. Check the original starter file.");
		}		
	}
	
	
	private void gradeGridNonZeroRepeats()
	{
		try
		{
			Class<?> arrayListClass = Class.forName("java.util.ArrayList");
			Method nzrMeth = gridClass.getDeclaredMethod("containsNonZeroRepeats", arrayListClass);
			nzrMeth.setAccessible(true);
			String[] testVecs =
			{
				"0,1,2,3,4,5", "0,0,0,0", "1,1,1,1", "1,2,3,4,5,6,7,8,9,10,4", "1,2,0,4,0"
			};
			boolean[] expects = { false, false, true, true, false };
			Object uut = constructGrid(20);
			for (int i=0; i<testVecs.length; i++)
			{
				String[] sPieces = testVecs[i].split(",");
				ArrayList<Integer> arg =
					Arrays.stream(sPieces)
					.map(s -> new Integer(s.trim()))
					.collect(Collectors.toCollection(ArrayList::new));
				try
				{
					Boolean zult = (Boolean)nzrMeth.invoke(uut, arg);
					if (zult == null  ||  zult != expects[i])
						fail("containsNonZeroRepeats method returned " + zult + " for " + testVecs[i] + ", expected " + expects[i]);
				}
				catch (Exception x) { }
			}
		}
		catch (ClassNotFoundException x)
		{
			return;
		}
		catch (NoSuchMethodException x)
		{
			fail("No containsNonZeroRepeats(ArrayList) method");
		}
	}
	
	
	private final static String[] LEGAL_GRID_1 =
	{
		"1234",
		"3412",
		"4321",
		"2143"		
	};
	
	
	private final static String[] LEGAL_GRID_2 =
	{
		"123456",
		"231645",
		"546132",
		"412563",
		"654321",
		"365214"
	};
	
	
	private final static String[] LEGAL_GRID_3 =
	{
		"1234",
		"3002",
		"4321",
		"2140"		
	};
	
	
	private final static String[] ILLEGAL_GRID_1 =
	{
		"0000",
		"1001",
		"0000",
		"0000"
	};
	
	
	private final static String[] ILLEGAL_GRID_2 =
	{
		"0000",
		"2001",
		"0000",
		"2000"
	};
	
	
	private final static String[] ILLEGAL_GRID_3 =
	{
		"4000",
		"2001",
		"0040",
		"0000"
	};

	
	
	private final static String[] ILLEGAL_GRID_4 =
	{
		"0004",
		"2041",
		"0000",
		"0000"
	};


	private final static String[] ILLEGAL_GRID_5 =
	{
		"11111",
		"11111",
		"11111",
		"11111",
		"11111"
	};
	
	
	private void gradeGridIsLegal()
	{
		String[][] grids = 
			new String[][] { LEGAL_GRID_1, LEGAL_GRID_2, LEGAL_GRID_3, ILLEGAL_GRID_1, ILLEGAL_GRID_2, ILLEGAL_GRID_3, ILLEGAL_GRID_4  };
		boolean[] expects = new boolean[] { true, true, true, false, false, false, false};
		
		Method isLegalMeth = null;
		try
		{
			isLegalMeth = gridClass.getDeclaredMethod("isLegal");
		}
		catch (NoSuchMethodException x)
		{
			fail("No isLegal() method in SimpledokuGrid class");
		}
		isLegalMeth.setAccessible(true);
		
		for (int i=0; i<grids.length; i++)
		{ 
			Object uut = constructGrid(grids[i]);
			try
			{
				Boolean zult = (Boolean)isLegalMeth.invoke(uut);
				if (zult != expects[i])
				{
					String err = "isLegal() returned " + zult + ", expected " + expects[i] + " for";
					for (String s: grids[i])
						err += "\n" + s;
					fail(err);
				}
			}
			catch (Exception x) { }
		}
	}
	
	
	private void gradeGridIsFull()
	{
		String[][] grids = 
			new String[][] { LEGAL_GRID_2, ILLEGAL_GRID_2 };
		boolean[] expects = new boolean[] { true, false };
		
		Method isFullMeth = null;
		try
		{
			isFullMeth = gridClass.getDeclaredMethod("isFull");
		}
		catch (NoSuchMethodException x)
		{
			fail("No isFull() method in SimpledokuGrid class");
			System.exit(1);
		}
		isFullMeth.setAccessible(true);
		
		for (int i=0; i<grids.length; i++)
		{ 
			Object uut = constructGrid(grids[i]);
			try
			{
				Boolean zult = (Boolean)isFullMeth.invoke(uut);
				if (zult != expects[i])
				{
					String err = "isFull() returned " + zult + ", expected " + expects[i] + " for";
					for (String s: grids[i])
						err += "\n" + s;
					fail(err);
				}
			}
			catch (Exception x) { }
		}
	}
	
	
	private void gradeGridEvaluate()
	{
		String[][] grids = 
			new String[][] { LEGAL_GRID_2, ILLEGAL_GRID_1, LEGAL_GRID_3, ILLEGAL_GRID_5 };
		Evaluation[] expects = { Evaluation.ACCEPT, Evaluation.ABANDON, Evaluation.CONTINUE, Evaluation.ABANDON };
		
		Method evaluateMeth = null;
		try
		{
			evaluateMeth = gridClass.getDeclaredMethod("evaluate");
		}
		catch (NoSuchMethodException x)
		{
			fail("No evaluate() method in SimpledokuGrid class");
			System.exit(1);
		}
		evaluateMeth.setAccessible(true);
		
		for (int i=0; i<grids.length; i++)
		{ 
			Object uut = constructGrid(grids[i]);
			try
			{
				Evaluation zult = (Evaluation)evaluateMeth.invoke(uut);
				if (zult != expects[i])
				{
					String err = "evaluate() returned " + zult + ", expected " + expects[i] + " for";
					for (String s: grids[i])
						err += "\n" + s;
					fail(err);
				}
			}
			catch (Exception x) { }
		}
	}
	
	
							
	
	
	
	
							/**************************************
							 *                                    *
							 *               SOLVER               *
							 *                                    *
							 **************************************/

	
	
	private void gradeSolverExists()
	{
		solverClass = getClass("SimpledokuSolver");
		if (solverClass == null)
			fail("No SimpledokuGrid class in package simpledoku");
	}
	
	
	private void gradeSolverCtor()
	{
		Class<?>[] classList = new Class<?>[] { int.class };
		try
		{
			solverIntCtor = solverClass.getDeclaredConstructor(classList);
		}
		catch (NoSuchMethodException x)
		{
			fail("Zero points: SimpledokuSolver(int) ctor does not exist.");
		}	
	}
	
	
	private void gradeSolver()
	{
		Method solveMeth = null;
		try
		{
			solveMeth = solverClass.getDeclaredMethod("solve");
		}
		catch (NoSuchMethodException x)
		{
			fail("No solve() method in SimpledokuSolver");
		}
		solveMeth.setAccessible(true);
		
		Method getSolutionMeth = null;
		try
		{
			getSolutionMeth = solverClass.getDeclaredMethod("getSolution");
		}
		catch (NoSuchMethodException x)
		{
			fail("No getSolution() method in SimpledokuSolver ... it was in the starter file ... maybe you accidentally deleted it?");
		}
		getSolutionMeth.setAccessible(true);
		
		Field valuesField = null;
		try
		{
			valuesField = gridClass.getDeclaredField("values");
			valuesField.setAccessible(true);
		}
		catch (NoSuchFieldException x)
		{
			fail("SimpledokuGrid has no values variable");
		}
			
		for (int side=4; side<=6; side++)
		{
			Object uut = constructSolver(side);
			try
			{
				solveMeth.invoke(uut);
				Object solution = getSolutionMeth.invoke(uut);
				int[][] vals = (int[][])valuesField.get(solution);
				for (int[] vs: vals)
				{
					boolean full = isFull(vals);
					boolean legal = isLegal(vals);
					if (full && legal)
						continue;
					else if (!full)
					{
						String err = "Solution is not a full grid: \n" + solution;
						fail(err);
					}
					else
					{
						String err = "Solution is not legal: \n" + solution;
						fail(err);
					}
				}
			}
			catch (InvocationTargetException | IllegalAccessException x) { }
		}
	}
	
	
	
	
	

								
								
								/**************************************
								 *                                    *
								 *               UTILS                *
								 *                                    *
								 **************************************/
	
	

	private Object constructGrid(int side)
	{
		try
		{
			return gridIntCtor.newInstance(side);
		}
		catch (Exception x)
		{
			return null;
		}
	}
	
	
	private Object constructGrid(int[][] vals)
	{
		Field valuesField = null;
		try
		{
			valuesField = gridClass.getDeclaredField("values");
			valuesField.setAccessible(true);
		}
		catch (NoSuchFieldException x)
		{
			fail("SimpledokuGrid has no values variable");
		}
		
		Object grid = constructGrid(vals.length);
		try
		{
			valuesField.set(grid, vals);
		}
		catch (IllegalAccessException x) { }
		
		return grid;
	}
	
	
	private Object constructGrid(String[] sarr)
	{
		int[][] vals = new int[sarr.length][sarr.length];
		for (int j=0; j<sarr.length; j++)
		{
			for (int i=0; i<sarr[j].length(); i++)
				vals[j][i] = (int)(sarr[j].charAt(i) - '0');
		}
		return constructGrid(vals);
	}
	
	
	private Object constructSolver(int side)
	{
		try
		{
			return solverIntCtor.newInstance(side);
		}
		catch (Exception x)
		{
			return null;
		}
	}	
	
	
	//
	// Returns true if the input list contains a repeated value that isn't zero.
	// Call this from isLegal().
	//
	private boolean containsNonZeroRepeats(ArrayList<Integer> ints)
	{
		Set<Integer> set = new HashSet<Integer>();
		for (int i: ints)
			if (i != 0  &&  set.contains(i))
				return true;
			else
				set.add(i);
		return false;
	}
	
	
	private boolean isLegal(int[][] values)
	{
		int nCellsPerSide = values.length;

		for (int i=0; i<nCellsPerSide; i++)
			for (int j=0; j<nCellsPerSide; j++)
				if (values[i][j] < 0  ||  values[i][j] > nCellsPerSide)
					return false;
		
		// Rows.
		for (int j=0; j<nCellsPerSide; j++)
		{
			ArrayList<Integer> valsInRow = new ArrayList<Integer>();
			for (int i=0; i<nCellsPerSide; i++)
				valsInRow.add(values[i][j]);
			if (containsNonZeroRepeats(valsInRow))
				return false;
		}

		// Cols.
		for (int i=0; i<nCellsPerSide; i++)
		{
			ArrayList<Integer> valsInCol = new ArrayList<Integer>();
			for (int j=0; j<nCellsPerSide; j++)
				valsInCol.add(values[i][j]);
			if (containsNonZeroRepeats(valsInCol))
				return false;
		}
		
		// Check top-left to bottom-right diagonal. 
		ArrayList<Integer> valsInDiag = new ArrayList<Integer>();
		for (int i=0; i<nCellsPerSide; i++)
			valsInDiag.add(values[i][i]);
		if (containsNonZeroRepeats(valsInDiag))
			return false;
		
		
		// Check top-right to bottom-left diagonal. 
		ArrayList<Integer> valsInOtherDiag = new ArrayList<Integer>();
		for (int i=0; i<nCellsPerSide; i++)
			valsInOtherDiag.add(values[i][nCellsPerSide-i-1]);
		if (containsNonZeroRepeats(valsInOtherDiag))
			return false;
		
		return true;
	}
	
	
	private boolean isFull(int[][] values)
	{
		int nCellsPerSide = values.length;
		
		for (int i=0; i<nCellsPerSide; i++)
			for (int j=0; j<nCellsPerSide; j++)
				if (values[i][j] == 0)
					return false;
		return true;
	}	
	
	
	static void sop(Object x)		{ System.out.println(x); }
	
	
	public static void main(String[] args)
	{
		sop("STARTING");
		
		new SimpledokuBot().grade();
	}
}
