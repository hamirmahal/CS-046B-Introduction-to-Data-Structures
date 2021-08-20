package bubble;

import java.util.*;


public class Statistician 
{
	private final static int N_REPETITIONS = 1000;
	
	private static void getStats(int arrayLength)
	{
		ArrayList<Long> visitCounts = new ArrayList<>();
		ArrayList<Long> swapCounts = new ArrayList<>();
		
		for (int i=0; i<N_REPETITIONS; i++)
		{
			int[] array = BubbleSortTestCaseMaker.buildRandom(arrayLength, arrayLength*100);
			BubbleSorter sorter = new BubbleSorter(array);
			sorter.sort();
			// Assert that the sorter sorted correctly.
			assert sorter.isSorted() == true;
			// Append # visits DONE
			visitCounts.add(sorter.getNVisits());
			// and # swaps to the array lists. DONE
			swapCounts.add(sorter.getNSwaps());
		}
		
		// Compute and print min/average/max number of visits.
		minMaxAverage(visitCounts);
		// Compute and print min/average/max number of swaps.
		minMaxAverage(swapCounts);
	}
	
	public static void minMaxAverage(ArrayList<Long> dataArray)
	{
		long currentMax = dataArray.get(0);
		long currentMin = dataArray.get(0);
		long total = 0;
		
		for (Long dataElement : dataArray)
		{
			if (dataElement < currentMin)
				currentMin = dataElement;
			else if (dataElement > currentMax)
				currentMax = dataElement;
			total += dataElement;
		}
		
		long average = total/dataArray.size();
		
		System.out.println("Min: " + currentMin + ", Average: " + average + ", Max: " + currentMax);
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("1000:");
		getStats(1000);
		
		System.out.println("3000:");
		getStats(3000);
	}
}
