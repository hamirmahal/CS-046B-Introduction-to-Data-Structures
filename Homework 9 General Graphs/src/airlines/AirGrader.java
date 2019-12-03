package airlines;

import java.lang.reflect.*;
import java.util.*;



public class AirGrader 
{
	private final static Airport		AAA 	= new Airport("AAA", 10, 20);
	private final static Airport		BBB1 	= new Airport("BBB", 10, 20);
	private final static Airport		BBB2 	= new Airport("BBB", 10, 20);
	private final static Airport		BBB3 	= new Airport("BBB", 99, 88);
	private final static Airport		CCC 	= new Airport("CCC", 10, 20);
	private final static Airport		DDD 	= new Airport("DDD", 10, 20);
	private final static Airport		MMM 	= new Airport("MMM", 10, 20);
	private final static Airport		NNN 	= new Airport("NNN", 10, 20);
	
	
	private static void checkAirport()
	{
		// Class.
		Class<?> airportClass = null;
		try
		{
			airportClass = Class.forName("airlines.Airport");
		}
		catch (ClassNotFoundException x)
		{
			abortZeroPoints("No airlines.Airport class");
		}
		
		// Implements Comparable<Airport>
		Class<?>[] interfaces = airportClass.getInterfaces();
		boolean hasComparable = false;
		for (Class<?> c: interfaces)
		{
			if (c.getName().equals("java.lang.Comparable"))
			{
				hasComparable = true;
				break;
			}
		}
		if (!hasComparable)
			abortZeroPoints("Airport doesn't implement Comparable<Airport>");
		
		// equals() calls compareTo()
		SubAirport sub1 = new SubAirport("XXX", 0, 0);
		SubAirport sub2 = new SubAirport("ZZZ", 0, 0);
		sub1.equals(sub2);
		if (!sub1.compareToWasCalled)
			abortZeroPoints("In Airport, equals() doesn't call compareTo => Not best practice");
		
		// equals()
		checkEquals(AAA, BBB1, false);
		checkEquals(BBB1, BBB2, true);
		checkEquals(BBB1, BBB3, true);
		
		// compareTo()
		checkCompareTo(AAA, BBB1, -1);
		checkCompareTo(BBB1, BBB2, 0);
		checkCompareTo(BBB1, BBB3, 0);
		
		// getConnections() should return sorted airports.
		Airport that = new Airport("ZZZ", 1, 0);
		Airport[] addOrder = { CCC, DDD, BBB1, AAA };
		Airport[] expectOrder = { AAA, BBB1, CCC, DDD };		
		for (Airport a: addOrder)
			that.connectTo(a);
		List<Airport> conns = that.getConnections();
		String errPiece = "Added in this order:";
		for (Airport a: addOrder)
			errPiece += "\n   " + a;
		errPiece += "\ngetConnections() returned:";
		for (Airport a: conns)
			errPiece += "\n   " + a;
		errPiece += "\nexpected:";
		for (Airport a: expectOrder)
			errPiece += "\n   " + a;
	
		if (conns.size() != 4)
			abortZeroPoints("Wrong number of airports:\n" + errPiece);
		for (int i=0; i<4; i++)
			if (!conns.get(i).equals(expectOrder[i]))
				abortZeroPoints("getConnections returned airports in wrong order\n" + errPiece);
		
		// isConnectedTo.
		that = new Airport("XYZ", 0, 0);
		that.connectTo(CCC);
		that.connectTo(DDD);
		Airport[] connOrder = { CCC, DDD };
		Airport[] checks = { CCC, DDD, MMM, NNN };
		boolean[] bExpect = { true, true, false, false };
		for (Airport a: connOrder)
			that.connectTo(a);
		boolean trouble = false;
		for (int i=0; i<4; i++)
			if (that.isConnectedTo(checks[i]) != bExpect[i])
				trouble = true;
		if (trouble)
		{
			String err = "Connected an airport to " + CCC + " and " + DDD;
			for (int i=0; i<4; i++)
				err += "\n  isConnectedTo(" + checks[i] + ") returned " + that.isConnectedTo(checks[i]) + ", expected " + bExpect[i];
			abortZeroPoints(err);
		}
	}
	
	
	private static void checkEquals(Airport a1, Airport a2, boolean expect)
	{
		boolean result = a1.equals(a2);
		if (result != expect)
			abortZeroPoints("equals() on " + a1 + " and " + a2 + " returned " + result + ", should be " + expect);
		
		result = a2.equals(a1);
		if (result != expect)
			abortZeroPoints("equals() on " + a2 + " and " + a1 + " returned " + result + ", should be " + expect);
	}
	
	
	private static void checkCompareTo(Airport a1, Airport a2, int expectSignum)
	{
		int result = a1.compareTo(a2);
		if (Math.signum(result) != expectSignum)
			abortZeroPoints("compareTo() on " + a1 + " and " + a2 + " returned " + result + ", should be " + signumToString(expectSignum));
		
		result = a2.compareTo(a1);
		expectSignum *= -1;
		if (Math.signum(result) != expectSignum)
			abortZeroPoints("compareTo() on " + a2 + " and " + a1 + " returned " + result + ", should be " + signumToString(expectSignum));

	}
	
	
	private static String signumToString(int n)
	{
		if (n == 0)
			return "0";
		else if (n < 0)
			return "<0";
		else
			return ">0";
	}
	
	
	private static class SubAirport extends Airport
	{
		boolean 		compareToWasCalled;
		
		SubAirport(String name, int x, int y)
		{
			super(name, x, y);
		}
		
		
		public int compareTo(Airport that)
		{
			compareToWasCalled = true;
			return 0;
		}
	}
	
	
	private static void checkFlightNet()
	{
		// Extends HashSet?
		Class fnClass = null;
		try
		{
			fnClass = Class.forName("airlines.FlightNet");
		}
		catch (ClassNotFoundException x)
		{
			abortZeroPoints("No airlines.FlightNet class");
		}
		Class supeClass = fnClass.getSuperclass();
		try
		{
			Class hsClass = Class.forName("java.util.HashSet");
			if (hsClass != supeClass)
				abortZeroPoints("FlightNet doesn't extend HashSet");
		}
		catch (ClassNotFoundException x) { }			// Shouldn't happen, ignore if it does

		// Add
		FlightNet that = new FlightNet();
		Airport[] adds = { AAA, BBB1, CCC, DDD };
		String addsErr = "Added to a FlightNet:";
		for (Airport a: adds)
		{
			that.add(a);
			addsErr += "\n  " + a;
		}
		
		// Name availability
		for (Airport a: adds)
		{
			if (that.nameIsAvailable(a.getName()))
			{
				String err = addsErr + "\nThen nameIsAvailable(" + a.getName() + ") returned true";
				abortZeroPoints(err);
			}
		}
		if (!that.nameIsAvailable("SFO"))
		{
			String err = addsErr + "\nThen nameIsAvailable(\"SFO\") returned false";
			abortZeroPoints(err);
		}
		
		// Connection
		that = new FlightNet();
		Airport sfo = new Airport("SFO", 0, 0);
		Airport jfk = new Airport("JFK", 100, 100);
		that.add(sfo);
		that.add(jfk);
		that.connect(sfo, jfk);
		if (sfo.isConnectedTo(jfk) == false  ||  jfk.isConnectedTo(sfo) == false)
		{
			String err = "Created a FlightNet fn, then fn.connect(sfo, jfk);\n";
			err += "Then sfo.isConnectedTo(jfk) returned " + sfo.isConnectedTo(jfk) + " and ";
			err += "jfk.isConnectedTo(sfo) returned " + jfk.isConnectedTo(sfo) + "\nBoth should return true";
			abortZeroPoints(err);
		}
		Airport sea = new Airport("SEA", 200, 200);
		that.add(sea);
		that.connect(sfo, sea);
		if (sea.isConnectedTo(jfk))
		{
			String err = "Created a FlightNet fn, then fn.connect(sfo, jfk); fn.connect(sfo, sea);\n";
			err += "Then sea.isConnectedTo(jfk) returned true";
			abortZeroPoints(err);
		}
		if (jfk.isConnectedTo(sea))
		{
			String err = "Created a FlightNet fn, then fn.connect(sfo, jfk); fn.connect(sfo, sea);\n";
			err += "Then jfk.isConnectedTo(sea) returned true";
			abortZeroPoints(err);
		}
		
		// getAirportNearXY
		Airport nearest = that.getAirportNearXY(110, 100, 12);
		if (nearest != jfk)
		{
			String err = "FlightNet fn contains SFO at (0, 0), JFK at (100, 100), and SEA at (200, 200)\n";
			err += "Then that.getAirportNearXY(110, 100, 12) returned " + nearest + ", expected jfk";
			abortZeroPoints(err);
		}
		nearest = that.getAirportNearXY(123456, 123456, 100);
		if (nearest != null)
		{
			String err = "FlightNet fn contains SFO at (0, 0) and JFK at (100, 100)\n";
			err += "Then that.getAirportNearXY(123456, 123456, 100) returned " + nearest + ", expected null";
			abortZeroPoints(err);
		}
		
		// removeAndDisconnect.
		that.removeAndDisconnect(jfk);
		String err = "Created a FlightNet fn, then fn.connect(sfo, jfk); fn.connect(sfo, sea); fn.removeAndDisconnect(jfk);\n";
		if (that.contains(jfk))
		{
			err += "Then fn still contains jfk";
			abortZeroPoints(err);
		}
		if (!that.contains(sfo))
		{
			err += "Then fn no longer contains sfo";
			abortZeroPoints(err);
		}
		if (!that.contains(sea))
		{
			err += "Then fn no longer contains sea";
			abortZeroPoints(err);
		}
		
		// Has instance vars?
		Field[] fields = fnClass.getDeclaredFields();
		if (fields.length > 0)
			abortZeroPoints("FlightNet class has " + fields.length + " instance variable(s)");
	}
	
	
	private static void abortZeroPoints(String msg)
	{
		sop("Zero points: \n" + msg);
		System.exit(1);
	}
	
	
	static void sop(Object x)
	{
		System.out.println(x);
	}
	
	
	public static void main(String[] args)
	{
		sop("START");
		
		checkAirport();
		checkFlightNet();
		sop("Full marks for correct code.");

		sop("DONE");
	}
}
