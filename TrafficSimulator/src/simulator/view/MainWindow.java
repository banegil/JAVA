package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.objects.Road;
import simulator.model.objects.Vehicle;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	JPanel p;
	RoadMap rMap;
	private JComboBox<Dish> _dishes, _dishes2;
	private DefaultComboBoxModel<Dish> _dishesModel;
	public MainWindow(Controller ctrl) throws Exception {
		super("Traffic Simulator");
		_ctrl = ctrl;
		rMap = new RoadMap();
		initGUI();
	}
	
	private void initGUI() throws Exception {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);

		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel vechiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vechicles");
		vechiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vechiclesView);
		
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);
		
		List<Dish> dishes = new ArrayList<Dish>();
		for(Road r: rMap.getRoads()) dishes.add(new Dish(r.getId()));
		
		_dishesModel.removeAllElements();
		for (Dish v : dishes)
		_dishesModel.addElement(v);

		tablesPanel.add(_dishes);
		
		JPanel ok = new JPanel();
		JButton okButton = new JButton("OK");
		ok.add(okButton);
		tablesPanel.add(ok);
		
		JPanel carreterasEntrantes = createViewPanel(new JTable(new InRoadTableModel(_ctrl)), "Carreteras Entrantes");
		carreterasEntrantes.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(carreterasEntrantes);

		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		JPanel mapByRoadView = createViewPanel((JComponent)new MapByRoad(_ctrl), "Map");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
}
