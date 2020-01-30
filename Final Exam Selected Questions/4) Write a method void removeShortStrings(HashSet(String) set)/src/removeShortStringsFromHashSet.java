/**
 * 
 */
import java.util.Arrays;
import java.util.HashSet;	// I need this import statement to use the HashSet class.
/**
 * @author hamir
 *
 */
public class removeShortStringsFromHashSet {
	/**
	 * I made this method private because this is, for the moment, a standalone class
	 * that doesn't need to interact with anything else. It's only for testing purposes
	 * at the moment.
	 * 
	 * If you would like to use this method from outside of this class, feel free
	 * to change "private" to "public" and you should be able to call this method
	 * from outside of the removeShortStringsFromHashSet class.
	 * @param set This is the HashSet<String> object from which this method will
	 * remove all String instances with a length shorter than 100.
	 */
	private static void removeShortStrings(HashSet<String> set) {
		// The first thing we have to do is create an instance of the HashSet class
		// that will contain all the String objects we want to remove from the main
		// HashSet class instance, i.e., the HashSet instance passed in as an
		// argument to this method.
		HashSet<String> removeTheseStringsFromMainHashSet = new HashSet<>();
		for (String string : set)
			if (string.length() < 100)
				removeTheseStringsFromMainHashSet.add(string);
		
		set.removeAll(removeTheseStringsFromMainHashSet);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// To test the removeShortStrings(HashSet<String> set) method,
		// we want to create a whole bunch of String instances.
		// Some of these String instances should have a length of at least 100.
		// Others should have a length of less than 100.
		// After calling the aforementioned method on a HashSet<String> set
		// containing all of the aforementioned Strings, only the String objects with
		// length greater than or equal to 100 should remain.
		// Okay, let's test this out!
		HashSet<String> tester = new HashSet<>();
		for (int i = 1; i <= 100; i++) {
			char[] temporaryArray = new char[100];
			Arrays.fill(temporaryArray, (char)i);
			tester.add(new String(temporaryArray));
		}
		// HashSet<String> tester should now have 100 String objects.
		
		// We now add a couple of shorter String objects to HashSet<String> tester
		// that the removeShortStrings(HashSet<String> set) method should remove.
		// After calling that method, the size of HashSet<String> tester should be 100.
		tester.add("Hello");
		tester.add("How are you?");
		tester.add("How's it going?");
		// Just as a check, tester.size() should be 103.
		System.out.println(tester.size());	// Prints "103", without quotation marks.
		removeShortStrings(tester);
		// The next line should print out "100", without quotation marks.
		System.out.println(tester.size());	// Prints "100", without quotation marks.
		
		// Success!
		
		// If you are curious, the reason I picked this exercise to write up after
		// the final exam is because I genuinely thought the question is interesting,
		// and I think there is practical benefit to having a program that solves
		// this problem on hand, and part of the reason I wanted to program this
		// method into an Eclipse integrated development environment (IDE) is so
		// I have an easily accessible solution I can use at a moment's notice.
		
		// Hope you enjoyed this!
	}

}
