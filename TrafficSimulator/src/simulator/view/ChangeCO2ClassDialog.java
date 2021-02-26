package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class ChangeCO2ClassDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<Dish> _dishes, _dishes2;
	private DefaultComboBoxModel<Dish> _dishesModel, _dishesModel2;
	private JSpinner ticks;
	
	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}
	
	private void initGUI(){

		_status = 0;

		setTitle("Cambiar Clase CO2");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Programa un evento para cambiar la clase CO2 de un vehiculo dado un numero de ticks desde este momento");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);
		
		_dishesModel2 = new DefaultComboBoxModel<>();
		_dishes2 = new JComboBox<>(_dishesModel2);
		
		SpinnerModel sm = new SpinnerNumberModel(1, 0, 1000, 1);
		ticks = new JSpinner(sm);
		Dimension d = ticks.getPreferredSize();
        d.width = 80;
        ticks.setPreferredSize(d);
        
        viewsPanel.add(new JLabel("Vehiculo: "));
		viewsPanel.add(_dishes);
		viewsPanel.add(new JLabel("Clase CO2: "));
		viewsPanel.add(_dishes2);
		viewsPanel.add(new JLabel("Ticks: "));
		viewsPanel.add(ticks);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_dishesModel.getSelectedItem() != null) {
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(List<Dish> dishes, List<Dish> dishe) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		_dishesModel.removeAllElements();
		for (Dish v : dishes)
			_dishesModel.addElement(v);
		
		_dishesModel2.removeAllElements();
		for (Dish v : dishe)
			_dishesModel2.addElement(v);

		// You can chenge this to place the dialog in the middle of the parent window.
		// It can be done using uing getParent().getWidth, this.getWidth(),
		// getParent().getHeight, and this.getHeight(), etc.
		//
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);
		return _status;
	}

	Dish getDish() {
		return  (Dish) _dishesModel.getSelectedItem();
	}
	
	Dish getDish2() {
		return  (Dish) _dishesModel2.getSelectedItem();
	}
	
	int getTick() {
		return (int) ticks.getValue();
	}
}
