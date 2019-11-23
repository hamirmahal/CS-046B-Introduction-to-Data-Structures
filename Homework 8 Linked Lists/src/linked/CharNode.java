package linked;

class CharNode
{
	private char		ch;
	private CharNode	next;
	
	
	CharNode(char ch)
	{
		this.ch = ch; 
	}
	
	
	CharNode getNext()
	{
		return next;
	}
	
	
	void setNext(CharNode next)
	{
		this.next = next;
	}
	
	
	char getData()
	{
		return ch;
	}
	
	// IMPORTANT: Note that this class does not have an equals method that checks for
	// deep equality! This is actually exactly what we want in this instance because this allows
	// the integrity check for loops to work properly.
}