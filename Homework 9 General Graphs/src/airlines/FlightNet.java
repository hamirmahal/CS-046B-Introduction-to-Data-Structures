package airlines;

import java.util.HashSet;
import java.util.NoSuchElementException;	// Needed for disconnect(Airport, Airport)

public class FlightNet extends HashSet<Airport>
{
	/**
	 * Checks to see if an Airport instance's name is equal to the String name passed in
	 * as a parameter for each of the Airport instances in this HashSet of Airport objects
	 * If an instance's name is equal, that name is not available and nameIsAvailable
	 * immediately exits, returning false.
	 * If this method iterates through this entire HashSet of Airport objects
	 * without executing that if statement, the String name passed in as a parameter
	 * must be available, so nameIsAvailable returns true.
	 * @param name The name we are checking to see is available
	 * @return boolean indicating whether or not the String name passed in as
	 * a parameter to the method is available
	 */
	public boolean nameIsAvailable(String name)
	{
		for(Airport checkMyName : this)
			if (checkMyName.getName().equals(name))
				return false;
		
		return true;
	}
	
	/**
	 * Assumes Airport instances a1 and a2 already exist. Connects Airport a1 to Airport
	 * a2 using the connectTo(Airport that) method of the Airport class, which this class
	 * aggregates.
	 * @param a1 The Airport instance we connect to Airport instance a2
	 * @param a2 The Airport instance we connect to Airport instance a1
	 */
	public void connect(Airport a1, Airport a2)
	{
		this.add(a1);		// Add Airport a1 to this FlightNet instance
		this.add(a2);		// Add Airport a2 to this FlightNet instance
		
		a1.connectTo(a2);	// Why does a1.getConnections().add(a2); not work?
		a2.connectTo(a1);	// Why does a2.getConnections().add(a1); not work?
	}
	
	/**
	 * Removes Airport a2 from the private connections field of Airport a1.
	 * Also removes Airport a1 from the private connections field of Airport a2.
	 * @param a1 The Airport instance to remove from the connections field of Airport a2
	 * @param a2 The Airport instance to remove from the connections field of Airport a1
	 * @throws NoSuchElementException Throws NoSuchElementException if Airport a2 is not
	 * present in the private connections field of Airport a1, and throws
	 * NoSuchElementException if Airport a1 is not in the private connections field of
	 * Airport a2.
	 */
	public void disconnect(Airport a1, Airport a2) throws NoSuchElementException
	{
		if (!(a1.getConnections().remove(a2)))
			throw new NoSuchElementException("Cannot remove " + a2
											+ " from a1.getConnections()"
											+ " because " + a2 + " was not present in "
											+ "a1.getConnections() before calling "
											+ "disconnect(Airport a1, Airport a2)");
		if (!(a2.getConnections().remove(a1)))
			throw new NoSuchElementException("Cannot remove " + a1
											+ " from a2.getConnections()"
											+ " because " + a1 + " was not present in "
											+ "a2.getConnections() before calling "
											+ "disconnect(Airport a1, Airport a2)");
	}
	
	/**
	 * Removes Airport removeMe from this FlightNet instance, and removes it from
	 * the private connections field of any and all Airport instances aggregated by
	 * this FlightNet object.
	 * @param removeMe The Airport instance to remove from this FlightNet object, and
	 * disconnect from any and all Airport instances in this FlightNet instance.
	 * @throws NoSuchElementException Throws NoSuchElementException if Airport removeMe
	 * is not in this FlightNet object before calling
	 * removeAndDisconnect(Airport removeMe)
	 */
	public void removeAndDisconnect(Airport removeMe) throws NoSuchElementException
	{
		if(!this.remove(removeMe))
			throw new NoSuchElementException("Cannot remove " + removeMe + " because it "
											+ " was not present in this FlightNet object"
											+ " before calling "
											+ "removeAndDisconnect(Airport removeMe)");
		
		for (Airport removeRemoveMeFromMyConnections : this)
			removeRemoveMeFromMyConnections.getConnections().remove(removeMe);
	}
	
	/**
	 * Checks all Airport objects in this instance of the FlightNet class to find
	 * the first Airport instance whose (x,y) location is within maximumDistance of
	 * the (x,y) coordinates passed in as parameters.
	 * @param x The x component of the (x,y) coordinate of which
	 * the returned Airport object should be within maximumDistance
	 * @param y The y component of the (x,y) coordinate of which
	 * the returned Airport object should be within maximumDistance
	 * @param maximumDistance The farthest the returned Airport object can be from
	 * the (x,y) coordinates passed in as parameters
	 * @return withinMaximumDistance, which will reference the first Airport instance in
	 * this FlightNet instance with maximumDistance within the (x,y) coordinates passed
	 * in as parameters if one exists, or will be null if unable to find an Airport
	 * instance in this FlightNet instance with (x,y) location within maximumDistance of
	 * the (x,y) coordinates passed in as parameters
	 */
	public Airport getAirportNearXY(int x, int y, int maximumDistance)
	{
		Airport withinMaximumDistance = null;
		
		for (Airport airport : this)
			if (Math.hypot(x - airport.getX(), y - airport.getY()) <= maximumDistance)
				withinMaximumDistance = airport;
		
		return withinMaximumDistance;
	}
	
	public static void main(String[] args)
	{
		FlightNet fn = new FlightNet();
		
		Airport sfc = new Airport("SFC", 50, 150);
		Airport lax = new Airport("LAX", 150, 50);
		
		// Time to get the disconnect(Airport a1, Airport a2) method to throw an exception
		// fn.disconnect(sfc, lax);
		
		// Now, let's try to have removeAndDisconnect(Airport removeMe) throw an exception
		// fn.removeAndDisconnect(sfc);
		
		// Check to see if nameIsAvailable(String name) method works.
		fn.connect(sfc, lax);
		
		// These should print "false", without quotation marks!
		System.out.println(fn.nameIsAvailable("SFC"));
		System.out.println(fn.nameIsAvailable("LAX"));
	}
}
