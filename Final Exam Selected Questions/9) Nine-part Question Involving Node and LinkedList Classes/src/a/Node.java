package a;

public class Node implements Comparable<Node> {
	private Integer data;
	private Node 	next;
	
	Node(Integer data)
	{
		this.data = data;
	}
	
	Integer getData()
	{
		return this.data;
	}
	
	Node getNext()
	{
		return this.next;
	}
	
	void setNext(Node next)
	{
		this.next = next;
	}
	
	@Override
	public int hashCode()
	{
		return data + 10;
	}
	
	@Override
	public boolean equals(Object that)
	{
		return this.compareTo((Node)that) == 0;
	}

	@Override
	public int compareTo(Node o) {
		return this.data - o.data;
	}

	public static void main(String[] args) {
		// Does the int compareTo(Node o) method work as intended?
		// Does it properly return an int object, despite the result of 
		// this.data - o.data being of type Integer?
		Node newNode = new Node(98);
		Node otherNewNode = new Node(101);
		
		// Firstly, of what types are the data attributes of newNode and otherNewNode?
		// Are they of type int, or Integer?
		int var = 9;
		// System.out.println(var.getClass());
		// The above line gives a compile-time error because we cannot cast
		// methods on primitives.
		System.out.println(newNode.getData().getClass());	// class java.lang.Integer
		System.out.println(otherNewNode.getData().getClass());//class java.lang.Integer
		
		// Interesting! Even though I initialized newNode and otherNewNode with
		// regular old primitives, their Integer data fields are still of type Integer.
		
		// Okay, so now here's the really interesting part. What is the type of
		// newNode.compareTo(otherNewNode)?
		System.out.println(newNode.compareTo(otherNewNode));
		// System.out.println(newNode.compareTo(otherNewNode).getClass());
		// I think the above line already answers my question! It gives a compile-
		// time error stating that I cannot call getClass() on the primitive type int.
		
		// So, it looks like I don't need to cast to int for compareTo()!
		// It appears as if the JVM does it automatically. Interesting!
	}

}
