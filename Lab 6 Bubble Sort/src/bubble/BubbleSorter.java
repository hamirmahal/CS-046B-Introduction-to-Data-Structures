package bubble;

public class BubbleSorter 
{
	private int[]		a;
	private long		nVisits;
	private long		nSwaps;
	
	
	public BubbleSorter(int[] a)
	{
		this.a = a; 
	}
	
	
	public void sort()
	{
		for (int i = 0; i < a.length; i++)
			for (int j = a.length; j > i + 1; j--)
			{
				// Increment number of visits by 2. DONE
				nVisits += 2;
				// if 2 elements you're visiting need to be swapped
				if (a[j-2] > a[j-1])
				{
					// Swap the elements and
					int temp = a[j-2];
					a[j-2] = a[j-1];
					a[j-1] = temp;
					// increment nSwaps. DONE
					nSwaps++;
				}
			}
	}
	
	
	public String toString()
	{
		String s = nVisits + " visits, " + nSwaps + " swaps\n{";
		for (int n: a)
			s += " " + n;
		s += " }";
		return s;
	}
	
	
	public boolean isSorted()
	{
		for (int i = 0; i < this.a.length - 1; i++)
			if (this.a[i] > this.a[i + 1])
				return false;
		
		return true;
	}
	
	
	public long getNVisits()
	{
		return this.nVisits;
	}
	
	
	public long getNSwaps()
	{
		return this.nSwaps;
	}
	
	
	public int[] getArray()
	{
		return a;
	}
	
	
	public static void main(String[] args)
	{
		int[] a = BubbleSortTestCaseMaker.buildRandom(100, 1000);
		
		BubbleSorter sorter = new BubbleSorter(a);
		sorter.sort();
		System.out.println(sorter);
		System.out.println(sorter.isSorted()  ?  "SORTED"  :  "NOT SORTED");
	}
}
