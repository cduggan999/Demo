package UserInterface;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RaiseSlider extends JSlider implements ChangeListener{
	private int sliderMin;
	private int sliderMax;
	private int raiseAmount;
	private JButton raise;
	private RaiseBox raiseBox;
	public RaiseSlider(int sliderMin, int sliderMax, int sliderInit,
			JButton raise, RaiseBox raiseBox) {
		super(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderInit);
		this.raise = raise;
		this.raiseBox = raiseBox;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setPaintTicks(true);
		setPaintLabels(true);
		addChangeListener(this);
		setPaintTicks(true);
		Font font = new Font("Serif", Font.ITALIC, 15);
		setFont(font);
		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( sliderMin ), new JLabel ( "Min " ) );
		labelTable.put( new Integer( sliderMax ), new JLabel ( "Max " ) );
		setLabelTable( labelTable );
		setPaintLabels(true);
	}
	public void setMinRaise(int min) {
		this.sliderMin = min;
		//raiseAmount = min;
	}
	public int getMinRaise(){
		return sliderMin;
	}
	public void setMaxRaise(int max) {
		sliderMax = max;
	}
	public void stateChanged(ChangeEvent e) {
		RaiseSlider source = (RaiseSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			int raisePercentage = (int)source.getValue();
			raiseAmount = (sliderMax/100)*raisePercentage;
			raiseBox.setText(""+raiseAmount);
		}
	}
}