package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.VehicleStatus;
import simulator.model.Weather;
import simulator.model.events.Event;
import simulator.model.objects.Road;
import simulator.model.objects.Vehicle;

public class MapByRoad extends JComponent implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	private static final int _JRADIUS = 10;
	
	private int x1, x2, y, i, xCoche;
	private RoadMap map;
	private Graphics2D g;
	
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _ROAD_ID_COLOR = Color.BLACK;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	
	private ArrayList<Image> iconos;
	private Image car;
	//FALTA WEATHER
	
	public MapByRoad(Controller _ctrl) {
		initGUI();
		setPreferredSize (new Dimension (300, 200));
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		iconos = loadImages();
		
	}
	
	public void paintComponent(Graphics graphics) {
		i = 0;
		x1 = 50;
		x2 = this.getWidth() - 100;
		
		super.paintComponent(graphics);
		g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getJunction().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap();
		}
	}
	
	private void drawMap() {
		int A, B;
		for(Road r: map.getRoads()) {
			A = r.getTotalCO2(); B = r.getCO2Limit();
			y = (i + 1) * 50;
			g.setColor(_ROAD_ID_COLOR);
			g.drawString(r.toString(), x1 - 35, y + 5);
			g.setColor(_ROAD_ID_COLOR);
			g.drawLine(x1, y, x2, y);
			
			drawJunctions(r);
			drawVehicles(r);
			g.drawImage(weather(r.getClima()), x2 + 10, y - 20, 32, 32, this);
			int c = (int) Math.floor(Math.min((double) A/(1.0 + (double) B),1.0) / 0.19);
			g.drawImage(iconos.get(c), x2 + 50, y - 20, 32, 32, this);
			i++;
		}
		i = 0;
	}
	
	private void drawVehicles(Road r) {
		int A, B;
		for (Vehicle v : r.getVehiculos()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				A = v.getLocation(); B = r.getLength();
				xCoche = x1 + (int) ((x2 - x1) * ((double) A / (double) B));
			
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
			
				g.drawImage(car, xCoche, y - 10, 16, 16, this);
				g.drawString(v.getId(), xCoche, y - 10);
			}
		}
	}
	
	private void drawJunctions(Road r) {
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSource().getId(), x1, y - 5);

		int green = r.getDest().getGreenLightIndex();
		if(r.getDest().getInRoads().size() == 1 || r.getDest().getInRoads().get(green).getId().equals(r.getId()))
			 g.setColor(_GREEN_LIGHT_COLOR);
		else g.setColor(_RED_LIGHT_COLOR);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getDest().getId(), x2, y - 5);
	}

	private ArrayList<Image> loadImages() {
		ArrayList<Image> iconos = new ArrayList<Image>();
		try {
			car = ImageIO.read(new File("resources/icons/car.png"));
			for(i = 0; i < 6; i++) iconos.add(ImageIO.read(new File("resources/icons/cont_" + i + ".png")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return iconos;
	}
	
	private Image weather(Weather w) {
		try {
			switch (w) {
			case SUNNY:
				return ImageIO.read(new File("resources/icons/sun.png"));
			case CLOUDY:
				return ImageIO.read(new File("resources/icons/cloud.png"));
			case RAINY:
				return ImageIO.read(new File("resources/icons/rain.png"));
			case WINDY:
				return ImageIO.read(new File("resources/icons/wind.png"));
			case STORM:
				return ImageIO.read(new File("resources/icons/storm.png"));
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.map = map;
		repaint();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.map = map;
		repaint();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.map = map;
		repaint();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.map = map;
		repaint();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.map = map;
		repaint();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
