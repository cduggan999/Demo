package UserInterface;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Graphics;

public class MyPanel extends JPanel {
	public MyPanel() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground( new Color(220,200,180) );
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}