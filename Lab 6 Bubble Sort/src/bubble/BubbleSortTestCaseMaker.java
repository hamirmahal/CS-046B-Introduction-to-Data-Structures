package bubble;

public class BubbleSortTestCaseMaker 
{
	private final static int[]		TINY =
	{
		1000, 1, 2, 3
	};
	
	public static int[] getTiny()
	{
		return TINY;
	}
	
	
	private final static int[]		ALREADY_SORTED =
	{
		1, 2, 3, 4, 5, 6, 7, 8, 9, 10
	};
	
	public static int[] getAlreadySorted()
	{
		return ALREADY_SORTED;
	}
	
	
	private final static int[]		BACKWARD =
	{
		10, 9, 8, 7, 6, 5, 4, 3, 2, 1
	};
	
	public static int[] getBackward()
	{
		return BACKWARD;
	}
	
	
	public static int[] buildRandom(int length, int maxValue)
	{
		int[] array = new int[length];
		for (int i=0; i<length; i++)
			array[i] = (int)(Math.random()*(maxValue + 1));
		return array;
	}
	
	
	public static void main(String[] args)
	{
		for (int i: buildRandom(10, 100))
			System.out.println(i);
	}
}
