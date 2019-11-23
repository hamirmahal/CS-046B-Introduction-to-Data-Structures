package linked;

import java.util.*;


public class CharLinkedList 
{
	private CharNode		head;	// Empty if head and
	private CharNode		tail;	// tail are null
	
	
	public CharLinkedList()		{ }
	
	/**
	 * If we find a node with the data for which we are looking, this method returns it.
	 * 
	 * If this method is not able to find a node with the data for which we
	 * are looking, it still returns nodeWithDataForWhichWeAreLooking, but its value
	 * should be "null", without quotation marks, which is what the assignment sheet
	 * says this method should output if find(char ch) cannot find a node with the data
	 * for which we are looking.
	 * 
	 * This method does not need a pre- and postcondition integrity check because it only
	 * queries this linked list: it does not modify this linked list in any way.
	 * 
	 * @param ch The node we are looking for should have char ch as its data.
	 * @return This is the node with the data for which we are looking.
	 */
	public CharNode find(char ch)
	{	
		CharNode nodeWithDataForWhichWeAreLooking;
		
		// We start off with this linked list's head.
		nodeWithDataForWhichWeAreLooking = this.head;
		
		while (nodeWithDataForWhichWeAreLooking != null)
		{
			if (ch == nodeWithDataForWhichWeAreLooking.getData())
				return nodeWithDataForWhichWeAreLooking;
			nodeWithDataForWhichWeAreLooking = nodeWithDataForWhichWeAreLooking.getNext();
		}
		
		return nodeWithDataForWhichWeAreLooking;
	}
	
	/**
	 * 
	 * @param ch This method should duplicate the CharNode in this linked list that has char ch
	 * as its data. If no such CharNode exists in this linked list, this method throws a new
	 * instance of an IllegalArgumentException with the helpful error message, "There is no node
	 * in this linked list with " + ch + " as its data."
	 * 
	 * I copied and pasted Professor Heller's pre- and postcondition integrity checks that he
	 * included in the insertAtHead(char ch) method, so that I can acquire more practice
	 * with using assertions in Eclipse and Java.
	 */
	public void duplicate(char ch)
	{
		assert hasIntegrity();		// Precondition
		
		// First, we find the first node in this linked list with char ch as its data.
		// If there is no such node, that is, find(char ch) returns null, we throw
		// an IllegalArgumentException.
		CharNode nodeToDuplicate = this.find(ch);
		
		if (nodeToDuplicate == null)
			throw new IllegalArgumentException("The character " + "'" + ch + "'"
																+ " is not present in "
																+ "the String " + "\"" + this
																+ "\"" + "," + "\n "
																+ "so we cannot duplicate it.");
		
		// If we get this far in the method, that means we found a CharNode in this linked list
		// with char data equal to the char data passed in as an argument to this method.
		
		// So, the first thing we do is make a duplicate copy of it.
		CharNode duplicateOfNodeToDuplicate = new CharNode(nodeToDuplicate.getData());
		
		// If the CharNode we duplicated was the tail of this linked list, then the
		// duplicate copy CharNode we just created is now the new tail of this linked list.
		if (this.tail == nodeToDuplicate)
			this.tail = duplicateOfNodeToDuplicate;
		
		// Next, we insert that copy immediately after the original CharNode with char data
		// equal to the char data passed in as an argument to this method.
		
		// We need to keep track of what the original CharNode's next reference refers to
		// before we overwrite its next attribute with a reference to the duplicate that we
		// created in this method.
		CharNode originalNextAttributeOfNodeWeDuplicated = nodeToDuplicate.getNext();
		
		// Now, we overwrite the next attribute of the original node that we duplicated
		// so that it references the duplicate copy that we created.
		nodeToDuplicate.setNext(duplicateOfNodeToDuplicate);
		
		// The final step is to make sure the duplicate CharNode instance we constructed
		// has its next attribute reference whatever the next attribute of the original node
		// that we duplicated referenced.
		duplicateOfNodeToDuplicate.setNext(originalNextAttributeOfNodeWeDuplicated);
		
		// Oh! One more thing. If 
		
		// ... And we're done!
		
		assert hasIntegrity();		// Postcondition
	}
	
	// If we insert a String s, we insert the last character of s first, and then the
	// second-to-last character, and then the third-to-last character, and so on and
	// so forth until, eventually, we reach the first character of s.
	public CharLinkedList(String s)
	{
		// We're passing in a character as an argument to insertAtHead(char ch).
		// This is fine, because as you can tell by the method signature,
		// insertAtHead takes a char ch as its argument.
		for (int i=s.length()-1; i>=0; i--)
			insertAtHead(s.charAt(i));
		// At the end of this for loop, the character components of s
		// will appear in this linked list in order.
	}
	
	
	public void insertAtHead(char ch)
	{
		assert hasIntegrity();		// Precondition
		
		CharNode node = new CharNode(ch);
		
		// The next line says that the next attribute of the new CharNode that we just
		// created will reference the head of this linked list. So, in other words...
		// Does that mean we made the new CharNode we just created the tail of this linked
		// list?
		node.setNext(head);
		// Ah! Okay, I get it now. First we make the new CharNode, node.
		// Then, since we are inserting node at the head of this linked list,
		// We want its next attribute to reference the old head of this linked list,
		// which this linked list can reference with the private CharNode head variable.
		
		// This next line says that this linked list's head attribute now points to the
		// new CharNode that we just created. So, the new CharNode that we just made
		// is now both the head, and de facto tail of this linked list?
		// Shouldn't it only do this if the linked list into which we are inserting is
		// empty?
		head = node;
		// All we're doing here is updating the head of this linked list.
		// Since we're inserting the new CharNode, we just created at the head of this
		// linked list, as stated in the name of this method, we're just updating this
		// linked list's head attribute to reference the new CharNode we created.
		
		// If this linked list doesn't have a tail already...
		if (tail == null)
			tail = node;			// Corner case: inserting into empty node
		// Should the above comment say, "Corner case: inserting into empty list"?
		// ... then set the tail of this linked list to be the new CharNode that we just
		// created.
		
		assert hasIntegrity();		// Postcondition
	}
	
	/**
	 * This method works by returning an empty string if this linked list is empty.
	 * Specifically, it returns an empty list if the head attribute of this linked list
	 * is null, which should only be the case when this linked list is empty.
	 * 
	 * On the other hand, if this linked list is nonempty, this method returns
	 * a sequence of characters. Specifically, if we construct an instance of
	 * CharLinkedList with the constructor that takes a String argument, calling toString()
	 * on that instance should return the exact String that we passed into the constructor.
	 */
	public String toString()
	{
		String s = "";
		CharNode node = head;
		while (node != null)
		{
			s += node.getData();
			node = node.getNext();
		}
		return s;
	}
	
	
	//
	// Returns true if this list has emptiness integrity, has tail integrity, has no loops,  
	// and tail is reachable from head.
	//
	// Caution: this checks for most but not all common integrity problems. 
	//
	boolean hasIntegrity()
	{
		// Check emptiness. If either head or tail is null, the other must
		// also be null. Different logic from what you saw in lecture. Returns
		// immediately if this list is empty.
		if (head == null  ||  tail == null)
			return head == null  &&  tail == null;
		// If the tail is null, for instance, say, when this linked list is empty,
		// the head also had better be null. That's the only way for this linked list
		// to have integrity when it is empty if this linked list's tail attribute is null.
		
		// Check tail integrity (tail.next must be null).
		if (tail.getNext() != null)
			return false;
		// Can we just say, "return tail.getNext() == null"?
		// Ah... no we can't! Because we don't want this conditional to return true if
		// tail.getNext() == null just yet, since we still have a couple other conditions to
		// check. If this were the last condition we were checking, then we could get away
		// with the line I suggested above!
		
		// I believe CharNode.java cannot have an equals method that checks for deep equality
		// if the following integrity check for loops is to work correctly.
		// Check for loops.
		Set<CharNode> visitedNodes = new HashSet<>();
		CharNode node = head;
		while (node != null)
		{
			if (visitedNodes.contains(node))
				return false;		// Current node has been visited before, we must have a loop
			visitedNodes.add(node); // First visit to this node
			node = node.getNext();
		}
		
		// For the next check, we start from this linked list's head, and iterate through
		// every node in this linked list until we either reach the tail or there are no more
		// nodes through which the below while loop can iterate.
		// Then, we check if the last node we checked is the tail, and return the result!
		// Make sure tail is reachable from head.
		node = head;
		while (node != null && node != tail)
			node = node.getNext();
		return node == tail;
	}
	
	public static void main(String[] args)
	{	
		// I want to test the find(char ch) method I just wrote.
		CharLinkedList findMethodTester = new CharLinkedList("Really awesome String!");
		
		// I want to find the node with char 'e' as its data.
		// This should return a reference to the second node in
		// the linked list findMethodTester, since the assignment instructions said to
		// write find(char ch) in such a way that it returns the first node in a linked list
		// whose data is equal to the char argument we pass into the method.
		CharNode thisShouldFindE = findMethodTester.find('e');	// This should work!
		
		System.out.println(findMethodTester.find('O'));			// This should return null!
		
		// The above statement actually returns null! It feels good knowing that works.
		
		// I also want to test the toString() method of CharLinkedList.java.
		// If we construct an instance of CharLinkedList with a String, I believe calling
		// toString() on that instance will return the exact String we passed into the
		// constructor.
		System.out.println(findMethodTester);	// This should print "Really awesome String!"
		
		// There is one more thing I want to test with the toString() method.
		// If we call toString() on an instance of CharLinkedList with head attribute
		// equal to null, toString() should return the empty String "". Is this what happens?
		// We test this out by first creating an instance of CharLinkedList using the no-args
		// constructor of CharLinkedList, which does not initialize the instance's head
		// attribute. This means the toString() method should return the empty string "".
		CharLinkedList emptyLinkedListForToStringTest = new CharLinkedList();
		
		// The following statement should print out "StartEnd".
		System.out.println("Start" + emptyLinkedListForToStringTest + "End");
		
		// It actually prints out "StartEnd"! Wow, that's awesome.
		
		// The last thing I want to test is the public void duplicate(char ch) method that
		// I just wrote.
		
		// If I create a new CharLinkedList instance, passing in "I fed my dog" as an argument
		// to the constructor that takes a String parameter, and then I call duplicate('e'),
		// on that instance, that instance should return "I feed my dog" when I call toString()
		// on it. In addition, if I call duplicate('z') on that instance, it should throw a new
		// IllegalArgumentException. Let's try both of those things out!
		CharLinkedList testToDuplicateE = new CharLinkedList("I fed my dog");
		testToDuplicateE.duplicate('e');
		System.out.println(testToDuplicateE);	// This should print out "I feed my dog"!
		
		// It worked! Now, to try duplicating z in a new instance of CharLinkedList with the
		// exact same String passed in as an argument to its constructor that takes a String
		// parameter.
		CharLinkedList testToDuplicateZ = new CharLinkedList("I fed my dog");
		
		// This works! It throws a new instance of IllegalArgumentException exactly as expected.
		// testToDuplicateZ.duplicate('z');
		
		// I just want to try it one more time, except with a try and catch block.
		try {
			testToDuplicateZ.duplicate('z');
		}
		catch (IllegalArgumentException e)
		{
			// For clarification, IllegalArgument Exception is a kind of runtime exception
			// and I should not try to catch it. I only include a try and catch block here
			// for my own practice. Hope this was informative!
			System.out.println("I don't think I'm supposed to try to catch " +
								"IllegalArgumentExceptions...");
		}
	}
}
