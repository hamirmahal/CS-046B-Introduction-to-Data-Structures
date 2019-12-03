package airlines;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;


public class RoutesPanel extends JPanel
{
	private final static Font		FONT						= new Font("SansSerif", Font.PLAIN, 12);
	private final static int		CLICK_RADIUS				= 11;
	private final static Color		UNARMED_COLOR 				= Color.BLACK;
	private final static Color		ARMED_FOR_CONNECTION_COLOR 	= new Color(0, 200, 0);
	private final static Color		ARMED_FOR_DELETION_COLOR 	= Color.RED;
	private final static File		MAP_FILE					= new File("pix/usmap.jpg");
	private final static Color		WHITEWASH_COLOR				= new Color(255, 255, 255, 200);
	private final static Stroke		ROUTE_STROKE				= new BasicStroke(2);
	
	
	private FlightNet				net;
	private Airport					armedAirport;
	private BufferedImage			usMapImage;
	
	
	RoutesPanel()
	{
		try
		{
			usMapImage = ImageIO.read(MAP_FILE);
		}
		catch (IOException x)
		{
			sop("Can't read map image file " + MAP_FILE.getAbsolutePath());
			System.exit(1);
		}
		
		net = new FlightNet();
		
		setPreferredSize(new Dimension(usMapImage.getWidth()/2, usMapImage.getHeight()/2));
		
		MLis lis = new MLis();
		addMouseListener(lis);
		addMouseMotionListener(lis);
	}
	
	
	private class MLis extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			Airport clickedAirport = net.getAirportNearXY(e.getX(), e.getY(), CLICK_RADIUS);
			
			// First of 1 or 2 clicks.
			if (armedAirport == null)
			{
				if (clickedAirport == null)
				{
					// Click in empty space to create new airport.
					armedAirport = null;
					NameDia dia = new NameDia(e.getX(), e.getY());
					dia.setVisible(true);
				}
				else
				{
					// Click on existing airport to arm it.
					armedAirport = clickedAirport;
				}
			}
			
			// Second of 2 clicks
			else
			{
				if (clickedAirport == null)
				{
					// 2nd click in empty space to cancel operation.
					armedAirport = null;
				}
				else if (clickedAirport == armedAirport)
				{
					// 2nd click on armed airport to delete it.
					net.removeAndDisconnect(armedAirport);
					armedAirport = null;
				}
				else if (clickedAirport.isConnectedTo(armedAirport))
				{
					// 2nd click on connected airport to delete the connection.
					net.disconnect(armedAirport, clickedAirport);
					armedAirport = null;
				}
				else
				{
					// 2nd click on unconnected airport to connect it.
					net.connect(armedAirport, clickedAirport);
					armedAirport = null;
				}
			}
			
			repaint();
		}
	}  // MLis
	
	
	private class NameDia extends JDialog implements ActionListener
	{
		private int				x;
		private int				y;
		private JTextField		tf;
		private JButton			okBtn;
		private JButton			cancelBtn;
		
		NameDia(int x, int y)
		{
			this.x = x;
			this.y = y;
			
			JPanel pan = new JPanel();
			pan.add(new JLabel("3-letter name: "));
			tf = new JTextField(4);
			tf.addActionListener(this);
			pan.add(tf);
			add(pan, BorderLayout.NORTH);
			
			pan = new JPanel();
			okBtn = new JButton("Ok");
			okBtn.addActionListener(this);
			pan.add(okBtn);
			cancelBtn = new JButton("Cancel");
			cancelBtn.addActionListener(this);
			pan.add(cancelBtn);
			add(pan, BorderLayout.SOUTH);
			
			pack();			
			setModal(true);
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == cancelBtn)
			{
				setVisible(false);
			}
			else
			{
				String name = tf.getText().trim();
				if (name.length() != 3)
				{
					JOptionPane.showMessageDialog(this, "Name must be 3 letters");
					return;
				}
				if (!net.nameIsAvailable(name))
				{
					JOptionPane.showMessageDialog(this, "Name is in use");
					return;
				}
				setVisible(false);
				net.add(new Airport(name, x, y));
				repaint();
			}
		}
	}  // NameDia
	
	
	public void paintComponent(Graphics g)
	{
		// Background.
		g.drawImage(usMapImage, 0, 0, getWidth(), getHeight(), this);
		
		// Whitewash.
		g.setColor(WHITEWASH_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Existing routes in black. If an airport is armed, some will get overdrawn.
		g.setColor(UNARMED_COLOR);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(ROUTE_STROKE);
		for (Airport a: net)
			for (Airport dest: a.getConnections())
				g.drawLine(a.getX(), a.getY(), dest.getX(), dest.getY());
		
		// Routes that are armed for deletion.
		if (armedAirport != null)
		{
			g.setColor(ARMED_FOR_DELETION_COLOR);
			for (Airport dest: armedAirport.getConnections())
				g.drawLine(armedAirport.getX(), armedAirport.getY(), dest.getX(), dest.getY());
		}
		
		// Routes that are armed for connection.
		if (armedAirport != null)
		{
			g.setColor(ARMED_FOR_CONNECTION_COLOR);
			for (Airport dest: net)
			{
				if (dest == armedAirport)
					continue;
				if (armedAirport.getConnections().contains(dest))
					continue;
				g.drawLine(armedAirport.getX(), armedAirport.getY(), dest.getX(), dest.getY());
			}
		}

		// Airports
		g.setFont(FONT);
		for (Airport a: net)
			if (a != armedAirport)
				paintAirport(g, a);
		if (armedAirport != null)
			paintAirport(g, armedAirport);
	}
	
	
	private void paintAirport(Graphics g, Airport a)
	{	
		int x = a.getX();
		int y = a.getY();
		boolean armed = a == armedAirport;
		g.setColor(armed ? ARMED_FOR_DELETION_COLOR : UNARMED_COLOR);
		g.fillOval(x-2, y-2, 5, 5);
		g.drawOval(x-5, y-5, 11, 11);
		int sw = g.getFontMetrics().stringWidth(a.getName());
		g.drawString(a.getName(), x-sw/2, y+18);
	}
	
	
	static void sop(Object x) 		{ System.out.println(x); }
	
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new RoutesPanel());
		frame.pack();
		frame.setLocation(100, 100);
		frame.setVisible(true);
	}
}
