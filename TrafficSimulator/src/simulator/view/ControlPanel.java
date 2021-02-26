package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;
import simulator.model.events.Event;
import simulator.model.events.NewSetContClassEvent;
import simulator.model.events.NewSetWeatherEvent;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;
import simulator.model.objects.Road;
import simulator.model.objects.Vehicle;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	JFileChooser fileChooser;
	JToolBar toolBar;
	RoadMap rMap;
	JButton open, hoja, weather, run, stop, exit;
	int time;
	boolean _stopped;
	JSpinner ticks;
	
	public ControlPanel(Controller c) throws Exception {
		c.addObserver(this);
		initGUI(c);
	}
	
	private void initGUI(Controller c) throws IOException {
		rMap = new RoadMap();
		toolBar = new JToolBar();
		time = 0;
		_stopped = false;
		setLayout(new BorderLayout());
		
		//Open
		ImageIcon openImage = new ImageIcon(ImageIO.read(new File("resources/icons/open.png")));
		open = new JButton(openImage);
		open.setToolTipText("Open: carga del fichero de eventos");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					select_open(c);
				} catch (FileNotFoundException | NegativeNumber | ContaminationClass | JunctionList
						| NotPendingWaiting e1) {
					onError(e1.getMessage());
					return;
				}
			}
		});
		toolBar.add(open);
		
		//Hoja CO2
		ImageIcon hojaImage = new ImageIcon(ImageIO.read(new File("resources/icons/co2class.png")));
		hoja = new JButton(hojaImage);
		hoja.setToolTipText("CO2: cambio de clase de la contaminacion de un vehiculo");
		hoja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					select_hojaCO2(c);
				} catch (ContaminationClass | JunctionList | NotPendingWaiting | NegativeNumber e1) {
					onError(e1.getMessage());
					return;
				}
			}
		});
		toolBar.add(hoja);
		
		//Weather
		ImageIcon weatherImage = new ImageIcon(ImageIO.read(new File("resources/icons/weather.png")));
		weather = new JButton(weatherImage);
		weather.setToolTipText("Weather: cambio de las condiciones atmosfericas de una carretera");
		weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					select_weather(c);
				} catch (ContaminationClass | JunctionList | NotPendingWaiting | NegativeNumber e1) {
					onError(e1.getMessage());
					return;
				}
			}
		});
		toolBar.add(weather);
		
		//Run
		ImageIcon runImage = new ImageIcon(ImageIO.read(new File("resources/icons/run.png")));
		run = new JButton(runImage);
		run.setToolTipText("Run: comienza la ejecucion");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_run((int)ticks.getValue(), c);
			}
		});
		toolBar.add(run);
		
		//Stop
		ImageIcon stopImage = new ImageIcon(ImageIO.read(new File("resources/icons/stop.png")));
		stop = new JButton(stopImage);
		stop.setToolTipText("Stop: para la ejecucion");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_stop();
			}
		});
		toolBar.add(stop);
		
		//Ticks
		SpinnerModel sm = new SpinnerNumberModel(0, 0, 1000, 1);
		ticks = new JSpinner(sm);
		Dimension d = ticks.getPreferredSize();
		d.width = 80;
		ticks.setPreferredSize(d);
		toolBar.add(new JLabel("Ticks: "));
		toolBar.add(ticks);
		
		toolBar.add(Box.createGlue());
		Box caja = Box.createHorizontalBox();
		caja.add(Box.createHorizontalStrut(700));
		toolBar.add(caja);

		//Exit
		ImageIcon exitImage = new ImageIcon(ImageIO.read(new File("resources/icons/exit.png")));
		exit = new JButton(exitImage);
		exit.setToolTipText("Exit: salir de la aplicacion");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_exit(c);
			}
		});
		toolBar.add(exit);
		
		this.add(toolBar);
		this.setVisible(true);
	}
	
	private void select_open(Controller c) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting, FileNotFoundException  {
		fileChooser = new JFileChooser();
		
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			FileInputStream f = new FileInputStream(fileChooser.getSelectedFile());
			c.reset();
			c.loadEvents(f);
		}
	}
	
	private void select_hojaCO2(Controller c) throws ContaminationClass, JunctionList, NotPendingWaiting, NegativeNumber {
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(this));
		
		List<Dish> dishes = new ArrayList<Dish>();
		for(Vehicle v: rMap.getVehicle()) dishes.add(new Dish(v.getId()));
		
		List<Dish> dishe = new ArrayList<Dish>();
		for (int i = 0; i <= 10; i++) dishe.add(new Dish(Integer.toString(i)));

		int status = dialog.open(dishes, dishe);
		if(status == 1) {
			List<Pair<String,Integer>> cs = new LinkedList<Pair<String,Integer>>();
			cs.add(new Pair<String,Integer>(dialog.getDish().get_name(), Integer.parseInt(dialog.getDish2().get_name())));
			c.addEvent(new NewSetContClassEvent(time + dialog.getTick(), cs));
		}
	}
	
	private void select_weather(Controller c) throws ContaminationClass, JunctionList, NotPendingWaiting, NegativeNumber {
		ChangeWeatherDialog dialog = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		
		List<Dish> dishes = new ArrayList<Dish>();
		for(Road r: rMap.getRoads()) dishes.add(new Dish(r.getId()));
		
		List<Dish> dishe = new ArrayList<Dish>();
		dishe.add(new Dish("SUNNY"));
		dishe.add(new Dish("CLOUDY"));
		dishe.add(new Dish("RAINY"));
		dishe.add(new Dish("WINDY"));
		dishe.add(new Dish("STORM"));

		int status = dialog.open(dishes, dishe);
		if(status == 1) {
			List<Pair<String,Weather>> cs = new LinkedList<Pair<String,Weather>>();
			cs.add(new Pair<String,Weather>(dialog.getDish(), dialog.getDish2()));
			c.addEvent(new NewSetWeatherEvent(time + dialog.getTick(), cs));
		}
	}
	
	private void select_run(int n, Controller _ctrl) {
		if (n > 0 && !_stopped) {
			enableToolBar(false);
			try {
				_ctrl.run(1, null);
				} catch (Exception e) {
					this.onError(e.getMessage());
					_stopped = true;
					return;
				}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					select_run(n - 1, _ctrl);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
		_stopped = false;
	}
	
	private void enableToolBar(boolean b) {
		for(int i = 0; i < 10; i++) if(i != 4) toolBar.getComponent(i).setEnabled(b); 
	}

	private void select_stop() {
		_stopped = true;
	}
	
	private void select_exit(Controller c) {
		ExitDialog dialog = new ExitDialog(SwingUtilities.getWindowAncestor(this));
		dialog.setVisible(true);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		rMap = map;
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		rMap = map;
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		rMap = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		rMap = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		rMap = map;
		this.time = time;
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this, err);
	}
}
