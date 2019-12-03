package airlines;

import java.util.*;

// From the class header, we already know that Airport has some kind of order.
// This is because it implements Comparable<Airport>.
public class Airport implements Comparable<Airport>
{
	private String					name;
	private int						x;				// What do these do? Perhaps they
	private int						y;				// signify geographical coordinates?
	private Set<Airport>			connections;	// all airports with a direct route from this airport
	
	
	public Airport(String name, int x, int y)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		connections = new TreeSet<>();
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public int getX()
	{
		return x;
	}
	
	
	public int getY()
	{
		return y;
	}
	
	// The below method returns a List of Airports.
	// One interesting thing to note is that an Airport instance stores its connections
	// as a TreeSet.
	// However, when we return an Airport instance's connections, we return them as
	// a List of Airports. Why?
	// This is possibly because we don't concern ourselves with accessing individual
	// Airports by index, until we want a full list of an Airport instance's connections.
	// Then, in that situation, we would want to access individual connections of an
	// Airport instance by index, I think.
	public List<Airport> getConnections()
	{
		return new ArrayList<>(connections);
	}
	
	
	// Adds that airport to the list of connections. This is a one-way route, so
	// don't add this airport to that's list of connections.
	public void connectTo(Airport that)
	{
		this.connections.add(that);
	}
	
	
	//
	// Does nothing if this airport is not connected to that.
	// Basically, what we want to do is, remove Airport that from this Airport instance's
	// connections field.
	//
	public void disconnectFrom(Airport that)
	{
		this.connections.remove(that);
	}
	
	
	// What I think we want to do for the equals(Object x) method is, compare instances
	// of the Airport class to each other.
	// Use best practice.
	public boolean equals(Object x)
	{
		Airport that = (Airport)x;	// We want to cast Object x to the Airport class.
		return this.compareTo(that) == 0;
	}
	
	
	// Just compare by airport name.
	public int compareTo(Airport that)
	{
		return this.name.compareTo(that.name);
	}
	
	
	// For this method, we want to return true if this Airport instance has Airport that
	// in this Airport instance's connections field.
	public boolean isConnectedTo(Airport that)
	{
		return this.connections.contains(that);
	}
	
	
	public String toString()
	{
		return "Airport " + name + " @(" + x + "," + y + ")";
	}
}
