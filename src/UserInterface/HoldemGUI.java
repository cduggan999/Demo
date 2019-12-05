package UserInterface;

import gameFlow.Card;
import gameFlow.Player;
import gameFlow.PokerPlatform;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HoldemGUI implements ActionListener {
	private JButton cont; //continue button
	private JButton fold;
	private JButton call;
	private JButton raise;
	private JButton newGame;
	private JButton quit;
	private RaiseSlider raiseSlider;
	
	boolean isFoldPressed;
	boolean isCallPressed;
	boolean raisePressed;
	boolean isContPressed;
	boolean isNewGamePressed;
	boolean isQuitPressed;
	
	static boolean start = false;
	private static HoldemGUI gui = new HoldemGUI();
	private HoldemCanvas canvas = new HoldemCanvas();
	private JTextArea handSummaryBox = new JTextArea(5, 20);
	private RaiseBox raiseField;
	String newline = "\n";
	int raiseAmount;
	private int sliderMin =0;
	private int sliderMax =100;
	private int sliderInit;
	
	public static void main(String[] args) {
		PokerPlatform pokerPlatform = new PokerPlatform(gui);
		do{
			pokerPlatform.Initilize();
			start = pokerPlatform.startGame();
		}while(start == true);
		pokerPlatform.startGame();
	}
	public void initilizeGUI(){
		JFrame frame = new JFrame();
		MyPanel buttonPanel = new MyPanel();
		MyPanel menuPanel = new MyPanel();
		handSummaryBox.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(handSummaryBox,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fold = new JButton("Fold");
		fold.addActionListener(this);
		buttonPanel.add(fold);
		call = new JButton("Call");
		call.addActionListener(this);
		buttonPanel.add(call);
		raise = new JButton("Raise");
		raise.addActionListener(this);
		buttonPanel.add(raise);
		raiseField = new RaiseBox(5, raise);
		raiseField.addActionListener(this);
		buttonPanel.add(raiseField);
		cont = new JButton("Click to CONTINUE!");
		cont.addActionListener(this);
		buttonPanel.add(cont);
		cont.setVisible(false);
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		buttonPanel.add(newGame);
		newGame.setVisible(false);
		quit = new JButton("Quit");
		newGame.addActionListener(this);
		buttonPanel.add(quit);
		quit.setVisible(false);
		//Create the slider
		raiseSlider = new RaiseSlider(sliderMin, sliderMax, sliderInit, raise, raiseField);
		buttonPanel.add(raiseSlider);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollPane, BorderLayout.EAST);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(menuPanel, BorderLayout.NORTH);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.setSize(1280, 760);
		frame.setVisible(true);
	}
	public void actionPerformed(ActionEvent event){
		if (event.getSource()== fold){
			isFoldPressed = true;
			System.out.print("Fold pressed - HoldemGUI");
		}
		if (event.getSource()== call){
			isCallPressed = true;
		}
		if (event.getSource()== raise){
			raisePressed = true;
		}
		if (event.getSource()== cont){
			isContPressed = true;
		}
		if (event.getSource()== raiseField.getText()){
			raiseAmount = Integer.parseInt(raiseField.getText());
		}
		if (event.getSource()== newGame){
			isNewGamePressed = true;}
		if (event.getSource()== quit){
			isQuitPressed = true;
		}
	}
	//setHole calds
	public void setHoleCards(Card c1, Card c2){
		canvas.setHoleCards(c1, c2);
	}
	//setHole calds
	public void setPlayersHoleCards(ArrayList<Player> playerList){
		canvas.updateHoldemCanvas(playerList);
	}
	public void displayFlop(String fc1, String fc2, String fc3){
		canvas.displayFlop(fc1, fc2, fc3);
	}
	public void displayTurn(String tC){
		canvas.displayTurn(tC);
	}
	public void displayRiver(String rC){
		canvas.displayRiver(rC);
	}
	public void setCallButton(int callAmount){
		if(callAmount ==0){
			call.setText("Check");
		}
		else{
			call.setText("Call (" + callAmount + ")");
		}
	}
	public void setRaiseButton(int raiseAmount){
		raise.setText("Raise (" + raiseAmount + ")");
	}
	public void resetButtons(){
		isFoldPressed = false;
		isCallPressed = false;
		raisePressed = false;
		isContPressed = false;
	}
	public void resetNewGame(){
		isNewGamePressed = false;
		isQuitPressed = false;
		setNewGameVisibility(false);
		setQuitVisibility(false);
	}
	public boolean isCallPressed(){
		return isCallPressed;
	}
	public boolean isFoldPressed(){
		return isFoldPressed;
	}
	public boolean isRaisePressed(){
		return raisePressed;
	}
	public boolean isContinuePressed(){
		return isContPressed;
	}
	public boolean isNewGamePressed(){
		return isNewGamePressed;
	}
	public boolean isQuitPressed(){
		return isQuitPressed;
	}
	public void updateSummaryBox(String text){
		handSummaryBox.append(" " + text + newline);
	}
	public void setFoldVisibility(boolean bool){
		fold.setVisible(bool);
	}
	public void setCallVisibility(boolean bool){
		call.setVisible(bool);
	}
	public void setRaiseVisibility(boolean bool){
		raise.setVisible(bool);
		raiseField.setVisible(bool);
		raiseSlider.setVisible(bool);
	}
	public void setContinueVisibility(boolean bool){
		cont.setVisible(bool);
	}
	public void setNewGameVisibility(boolean bool){
		newGame.setVisible(bool);
	}
	public void setQuitVisibility(boolean bool){
		quit.setVisible(bool);
	}
	public int getRaiseAmount(){
		return raiseField.getRaiseAmount();
	}
	public boolean getBoolStart(){
		return start;
	}
	public void setHandNumber(int handNo){
		canvas.setHandNumber(handNo);
	}
	public void setPot(int newPot){
		canvas.setPot(newPot);
	}
	public void setDealerPos(int dealer, int sB, int bB){
		canvas.setDealerPos(dealer, sB, bB);
	}
	public void setBlindAmounts(int sB, int bLevel, int bII){
		canvas.setBlindAmounts(sB, bLevel, bII);
	}
	public void resetRaiseText(){
		raiseField.replaceSelection("");
	}
	public void setMinMaxRaise(int min, int max){
		raiseField.setMinRaise(min);
		raiseField.setMaxRaise(max);
		raiseSlider.setMinRaise(min);
		raiseSlider.setMaxRaise(max);
	}
	//Used to display the players hands at showdown
	public void showCards(ArrayList<Player> playerList, int playerNo){
		canvas.showCards(playerList, playerNo);
	}
}