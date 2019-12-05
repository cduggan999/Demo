package UserInterface;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class RaiseBox extends JTextField implements ActionListener, DocumentListener{
	static int count = 0;
	private int maxRaise;
	private int minRaise;
	private int raiseAmount;
	private JButton raise;
	public RaiseBox(int l, JButton raise) {
		super(l);
		addActionListener(this);
		Document doc = this.getDocument();
		System.out.println("The document object: "+doc);
		doc.addDocumentListener(this);
		this.raise = raise;
	}
	public void actionPerformed(ActionEvent e) {
		if(Integer.parseInt(getText()) < minRaise){
			raiseAmount = minRaise;
		}
		else if(Integer.parseInt(getText()) > maxRaise){
			raiseAmount = maxRaise;
		}
		else{
			raiseAmount = Integer.parseInt(getText());
		}
		raise.setText("Raise (" + raiseAmount + ")");
	}
	public void insertUpdate(DocumentEvent e) {
		if(Integer.parseInt(getText()) >= minRaise
				&& Integer.parseInt(getText()) <= maxRaise){
			raise.setText("Raise (" + getText() + ")");
			raiseAmount = Integer.parseInt(getText());
		}
		else if(Integer.parseInt(getText()) < minRaise){
			raise.setText("Raise (" + minRaise + ")");
			raiseAmount = minRaise;
		}
		else{
			raise.setText("Raise (" + maxRaise + ")");
			raiseAmount = maxRaise;
		}
	}
	public void removeUpdate(DocumentEvent e) {
		if(Integer.parseInt(getText()) >= minRaise
				&& Integer.parseInt(getText()) <= maxRaise){
			raise.setText("Raise (" + getText() + ")");
			raiseAmount = Integer.parseInt(getText());
		}
		else if(Integer.parseInt(getText()) < minRaise){
			raise.setText("Raise (" + minRaise + ")");
			raiseAmount = minRaise;
		}
		else{
			raise.setText("Raise (" + maxRaise + ")");
			raiseAmount = maxRaise;
		}
	}
	public void changedUpdate(DocumentEvent e) {
	}
	public void setMinRaise(int min) {
		minRaise = min;
		raiseAmount = min;
	}
	public int getMinRaise() {
		return minRaise;
	}
	public void setMaxRaise(int max) {
		maxRaise = max;
	}
	public int getMaxRaise() {
		return maxRaise;
	}
	public int getRaiseAmount(){
		return raiseAmount;
	}
}