package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.events.Event;

public class StatusBar extends JPanel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	private JLabel tiempo, evento;
	
	public StatusBar(Controller c) {
		c.addObserver(this);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		InitGUI();
	}
	
	private void InitGUI() {
		Box caja = Box.createHorizontalBox();
		
		this.tiempo = new JLabel(" Tiempo: 0");
		this.tiempo.setSize(new Dimension(9,5));
		this.tiempo.setVisible(true);
		caja.add(this.tiempo);
		caja.add(Box.createHorizontalStrut(90));
		
		this.evento = new JLabel("Bienvenido");
		this.evento.setVisible(true);
		this.evento.setSize(new Dimension(9,5));
		this.evento.setVisible(true);
		caja.add(this.evento);
		
		this.add(caja);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 0));
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		tiempo.setText("Tiempo: " + time);
		evento.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		tiempo.setText("Tiempo: " + time);
		evento.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		tiempo.setText("Tiempo: " + time);
		evento.setText("Nuevo Evento: " + e.toString());

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		tiempo.setText("Tiempo: 0");
		evento.setText("Reset");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this, err);
	}

}
