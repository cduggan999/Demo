package UserInterface;

import gameFlow.Card;
import gameFlow.Player;
import gameFlow.Player.BettingStatus;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class HoldemCanvas extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private String holeCard1, holeCard2, p1Card1, p1Card2, p2Card1, p2Card2;
	private String p3Card1, p3Card2, p4Card1, p4Card2, p5Card1, p5Card2;
	private String p6Card1, p6Card2, p7Card1, p7Card2, p8Card1, p8Card2;
	private String flopCard1, flopCard2, flopCard3, turnCard, riverCard;
	//players names which is sorted according to their table position
	private String name0=" ", name1=" ", name2=" ", name3=" ", name4=" ", name5=" ", name6=" ", name7=" ";
			private String name8 =" ";
	//players action e.g. call/fold which is sorted according to their table position
	private String p0Action=" ", p1Action=" ", p2Action=" ", p3Action=" ", p4Action=" ",
			p5Action=" ";
	private String p6Action=" ", p7Action=" ", p8Action=" ";
	private int chips0, chips1, chips2, chips3, chips4, chips5, chips6,chips7,chips8;
	private int potAmount;
	private int handNumber = 1;
	private int sBlind, bBlind, blindLevel;
	private int blindsInceaseIn; // number of rounds left until blinds increase
	private Font bigFont; // Font that will be used to display pot winners.
	private Font smallFont; // Font that will be used for names, chips.
	private Font biggerFont;
	private int smallBlind_X_Coordinadte;
	private int smallBlind_Y_Coordinadte;
	private int bigBlind_X_Coordinadte;
	private int bigBlind_Y_Coordinadte;
	private int dealer_X_Coordinadte;
	private int dealer_Y_Coordinadte;
	ArrayList<Player> playerList;
	// Constructor. Creates fonts and starts the first game.
	HoldemCanvas() {
		setBackground( new Color(0,120,0) );
		smallFont = new Font("SansSerif", Font.PLAIN, 12);
		bigFont = new Font("Serif", Font.BOLD, 14);
		biggerFont = new Font("Serif", Font.BOLD, 18);
	}
	
	public void actionPerformed(ActionEvent evt) {
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Draws the rectangles which marks where the cards will be placed
		g.setColor(Color.GREEN);
		g.drawRect(380,490,148,96);
		g.drawRect(175,490,148,96);
		g.drawRect(20,270,148,96);
		g.drawRect(70,10,148,96);
		g.drawRect(275,10,148,96);
		g.drawRect(480,10,148,96);
		g.drawRect(685,10,148,96);
		g.drawRect(725,270,148,96);
		g.drawRect(585,490,148,96);
		g.setFont(bigFont);
		g.setColor(Color.cyan);
		Image hc1 = new ImageIcon("images/"+holeCard1+".gif").getImage();
		Image hc2 = new ImageIcon("images/"+holeCard2+".gif").getImage();
		g.drawImage(hc1, 380, 490, this);
		g.drawImage(hc2, 455, 490, this);
		g.drawString(name0, 410, 600);
		g.drawString("(" + chips0 + ")", 410, 615);
		Image p1c1 = new ImageIcon("images/"+p1Card1+".gif").getImage();
		Image p1c2 = new ImageIcon("images/"+p1Card2+".gif").getImage();
		g.drawImage(p1c1, 175, 490, this);
		g.drawImage(p1c2, 250, 490, this);
		g.drawString(name1, 205, 600);
		g.drawString("(" + chips1 + ")", 205, 615);
		Image p2c1 = new ImageIcon("images/"+p2Card1+".gif").getImage();
		Image p2c2 = new ImageIcon("images/"+p2Card2+".gif").getImage();
		g.drawImage(p2c1, 20, 270, this);
		g.drawImage(p2c2, 95, 270, this);
		g.drawString(name2, 50, 380);
		g.drawString("(" + chips2 + ")", 50, 395);
		Image p3c1 = new ImageIcon("images/"+p3Card1+".gif").getImage();
		Image p3c2 = new ImageIcon("images/"+p3Card2+".gif").getImage();
		g.drawImage(p3c1, 70, 10, this);
		g.drawImage(p3c2, 145, 10, this);
		g.drawString(name3, 100, 120);
		g.drawString("(" + chips3 + ")", 100, 135);
		Image p4c1 = new ImageIcon("images/"+p4Card1+".gif").getImage();
		Image p4c2 = new ImageIcon("images/"+p4Card2+".gif").getImage();
		g.drawImage(p4c1, 275, 10, this);
		g.drawImage(p4c2, 350, 10, this);
		g.drawString(name4, 305, 120);
		g.drawString("(" + chips4 + ")", 305, 135);
		Image p5c1 = new ImageIcon("images/"+p5Card1+".gif").getImage();
		Image p5c2 = new ImageIcon("images/"+p5Card2+".gif").getImage();
		g.drawImage(p5c1, 480, 10, this);
		g.drawImage(p5c2, 555, 10, this);
		g.drawString(name5, 510, 120);
		g.drawString("(" + chips5 + ")", 510, 135);
		Image p6c1 = new ImageIcon("images/"+p6Card1+".gif").getImage();
		Image p6c2 = new ImageIcon("images/"+p6Card2+".gif").getImage();
		g.drawImage(p6c1, 685, 10, this);
		g.drawImage(p6c2, 760, 10, this);
		g.drawString(name6, 715, 120);
		g.drawString("(" + chips6 + ")", 715, 135);
		Image p7c1 = new ImageIcon("images/"+p7Card1+".gif").getImage();
		Image p7c2 = new ImageIcon("images/"+p7Card2+".gif").getImage();
		g.drawImage(p7c1, 725, 270, this);
		g.drawImage(p7c2, 800, 270, this);
		g.drawString(name7, 755, 380);
		g.drawString("(" + chips7 + ")", 755, 395);
		Image p8c1 = new ImageIcon("images/"+p8Card1+".gif").getImage();
		Image p8c2 = new ImageIcon("images/"+p8Card2+".gif").getImage();
		g.drawImage(p8c1, 585, 490, this);
		g.drawImage(p8c2, 660, 490, this);
		g.drawString(name8, 615, 600);
		g.drawString("(" + chips8 + ")", 615, 615);
		//Flop
		Image fC1 = new ImageIcon("images/"+flopCard1+".gif").getImage();
		Image fC2 = new ImageIcon("images/"+flopCard2+".gif").getImage();
		Image fC3 = new ImageIcon("images/"+flopCard3+".gif").getImage();
		g.drawImage(fC1, 250, 240, this);
		g.drawImage(fC2, 325, 240, this);
		g.drawImage(fC3, 400, 240, this);
		//Turn
		Image tC = new ImageIcon("images/"+turnCard+".gif").getImage();
		g.drawImage(tC, 475, 240, this);
		//River
		Image rC = new ImageIcon("images/"+riverCard+".gif").getImage();
		g.drawImage(rC, 550, 240, this);
		g.setFont(biggerFont);
		g.setColor(Color.yellow);
		g.drawString("Pot: " + potAmount, 325, 365);
		//SmallBlind
		g.fillOval(smallBlind_X_Coordinadte, smallBlind_Y_Coordinadte, 25, 25);
		g.setFont(bigFont);
		g.setColor(Color.BLACK);
		g.drawString("SB",smallBlind_X_Coordinadte+4, smallBlind_Y_Coordinadte+18);
		g.drawOval(smallBlind_X_Coordinadte, smallBlind_Y_Coordinadte, 25, 25);
		//BigBlind
		g.setColor(Color.ORANGE);
		g.fillOval(bigBlind_X_Coordinadte, bigBlind_Y_Coordinadte, 25, 25);
		g.setColor(Color.BLACK);
		g.drawOval(bigBlind_X_Coordinadte, bigBlind_Y_Coordinadte, 25, 25);
		g.drawString("BB",bigBlind_X_Coordinadte+4, bigBlind_Y_Coordinadte+18);
		//BigBlind
		g.setColor(Color.WHITE);
		g.fillOval(dealer_X_Coordinadte, dealer_Y_Coordinadte, 25, 25);
		g.setColor(Color.BLACK);
		g.drawOval(dealer_X_Coordinadte, dealer_Y_Coordinadte, 25, 25);
		g.drawString("D",dealer_X_Coordinadte+8, dealer_Y_Coordinadte+18);
		//Game Info
		g.drawString("Hand Number: " + handNumber,880, 20);
		g.drawString("Blind Level: " + blindLevel,825, 590);
		g.drawString("Blinds: " + sBlind + "/" + bBlind,825, 608);
		g.setFont(smallFont);
		g.drawString("Blinds will increase after " + blindsInceaseIn + " hands",825, 626);
		//Players Actions
		g.setFont(biggerFont);
		g.setColor(Color.RED);
		g.drawString(p0Action, 410, 635);
		g.drawString(p1Action, 205, 635);
		g.drawString(p2Action, 50, 415);
		g.drawString(p3Action, 100, 155);
		g.drawString(p4Action, 305, 155);
		g.drawString(p5Action, 510, 155);
		g.drawString(p6Action, 715, 155);
		g.drawString(p7Action, 755, 415);
		g.drawString(p8Action, 615, 635);
	}
	public void setHoleCards(Card c1, Card c2){
		holeCard1 = c1.cardAsString();
		holeCard2 = c2.cardAsString();
	}
	public void updateHoldemCanvas(ArrayList<Player> playerList){
		//boolean set to true if player exists at said position
		boolean boolP0 = false, boolP1 = false, boolP2 = false, boolP3 = false, boolP4 =
				false;
		boolean boolP5 = false, boolP6 = false, boolP7 = false, boolP8 = false;
		for(int i = 0; i< playerList.size(); i++){
			if(playerList.get(i).getTablePosition()==0){
				name0 = playerList.get(i).getName();
				chips0 = playerList.get(i).getChips();
				boolP0 = true;
				p0Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					holeCard1 =
							playerList.get(0).getPlayerHand().getCardOriginalOrder(0).cardAsString();
					holeCard2 =
							playerList.get(0).getPlayerHand().getCardOriginalOrder(1).cardAsString();
				}
				else{
					holeCard1 = " ";
					holeCard2 = " ";
				}
			}
			else{
				if(boolP0 == false){
					name0 = " ";
					chips0 = 0;
					holeCard1 = " ";
					holeCard2 = " ";
					p0Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==1){
				name1 = playerList.get(i).getName();
				chips1 = playerList.get(i).getChips();
				boolP1 = true;
				p1Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p1Card1 = "b";
					p1Card2 = "b";
				}
				else{
					p1Card1 = " ";
					p1Card2 = " ";
				}
			}
			else{
				if(boolP1 == false){
					name1 = " ";
					chips1 = 0;
					p1Card1 = " ";
					p1Card2 = " ";
					p1Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==2){
				name2 = playerList.get(i).getName();
				chips2 = playerList.get(i).getChips();
				boolP2 = true;
				p2Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p2Card1 = "b";
					p2Card2 = "b";
				}
				else{
					p2Card1 = null;
					p2Card2 = null;
				}
			}
			else{
				if(boolP2 == false){
					name2 = " ";
					chips2 = 0;
					p2Card1 = " ";
					p2Card2 = " ";
					p2Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==3){
				name3 = playerList.get(i).getName();
				chips3 = playerList.get(i).getChips();
				boolP3 = true;
				p3Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p3Card1 = "b";
					p3Card2 = "b";
				}
				else{
					p3Card1 = null;
					p3Card2 = null;
				}
			}
			else{
				if(boolP3 == false){
					name3 = " ";
					chips3 = 0;
					p3Card1 = " ";
					p3Card2 = " ";
					p3Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==4){
				name4 = playerList.get(i).getName();
				chips4 = playerList.get(i).getChips();
				boolP4 = true;
				p4Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p4Card1 = "b";
					p4Card2 = "b";
				}
				else{
					p4Card1 = null;
					p4Card2 = null;
				}
			}
			else{
				if(boolP4 == false){
					name4 = " ";
					chips4 = 0;
					p4Card1 = " ";
					p4Card2 = " ";
					p4Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==5){
				name5 = playerList.get(i).getName();
				chips5 = playerList.get(i).getChips();
				boolP5 = true;
				p5Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p5Card1 = "b";
					p5Card2 = "b";
				}
				else{
					p5Card1 = null;
					p5Card2 = null;
				}
			}
			else{
				if(boolP5 == false){
					name5 = " ";
					chips5 = 0;
					p5Card1 = " ";
					p5Card2 = " ";
					p5Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==6){
				name6 = playerList.get(i).getName();
				chips6 = playerList.get(i).getChips();
				boolP6 = true;
				p6Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p6Card1 = "b";
					p6Card2 = "b";
				}
				else{
					p6Card1 = null;
					p6Card2 = null;
				}
			}
			else{
				if(boolP6 == false){
					name6 = " ";
					chips6 = 0;
					p6Card1 = " ";
					p6Card2 = " ";
					p6Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==7){
				name7 = playerList.get(i).getName();
				chips7 = playerList.get(i).getChips();
				boolP7 = true;
				p7Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p7Card1 = "b";
					p7Card2 = "b";
				}
				else{
					p7Card1 = null;
					p7Card2 = null;
				}
			}
			else{
				if(boolP7 == false){
					name7 = " ";
					chips7 = 0;
					p7Card1 = " ";
					p7Card2 = " ";
					p7Action = " ";
				}
			}
			if(playerList.get(i).getTablePosition()==8){
				name8 = playerList.get(i).getName();
				chips8 = playerList.get(i).getChips();
				boolP8 = true;
				p8Action = getBetAsString(playerList.get(i));
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					p8Card1 = "b";
					p8Card2 = "b";
				}
				else{
					p8Card1 = null;
					p8Card2 = null;
				}
			}
			else{
				if(boolP8 == false){
					name8 = " ";
					chips8 = 0;
					p8Card1 = " ";
					p8Card2 = " ";
					p8Action = " ";
				}
			}
		}
		repaint();
	}
	//Used to display the players hands at showdown
	public void showCards(ArrayList<Player> playerList, int playerNo){
		int tablePos = playerList.get(playerNo).getTablePosition();
		switch (tablePos) {
		case 0: holeCard1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		holeCard2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 1: p1Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p1Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 2: p2Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p2Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 3: p3Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p3Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 4: p4Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p4Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 5: p5Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p5Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 6: p6Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p6Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 7: p7Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p7Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		case 8: p8Card1 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(0).cardAsString();
		p8Card2 = playerList.get(playerNo).getPlayerHand().
				getCardOriginalOrder(1).cardAsString();
		break;
		}
	}
	public void displayFlop(String fc1, String fc2, String fc3){
		flopCard1 = fc1;
		flopCard2 = fc2;
		flopCard3 = fc3;
	}
	public void displayTurn(String tC){
		turnCard=tC;
	}
	public void displayRiver(String rC){
		riverCard=rC;
	}
	public void setPot(int newPot){
		potAmount = newPot;
	}
	public void setDealerPos(int dealer, int sB, int bB){
		dealer_X_Coordinadte=get_X_Coordinate(dealer);
		dealer_Y_Coordinadte=get_Y_Coordinate(dealer);
		smallBlind_X_Coordinadte = get_X_Coordinate(sB);
		smallBlind_Y_Coordinadte = get_Y_Coordinate(sB);
		bigBlind_X_Coordinadte = get_X_Coordinate(bB);
		bigBlind_Y_Coordinadte = get_Y_Coordinate(bB);
	}
	private int get_X_Coordinate(int tablePos){
		int x;
		switch (tablePos) {
		case 0: x = 440;
		break;
		case 1: x = 235;
		break;
		case 2: x=170;
		break;
		case 3: x=190;
		break;
		case 4: x=335;
		break;
		case 5: x=540;
		break;
		case 6: x=698;
		break;
		case 7: x=698;
		break;
		case 8: x = 645;
		break;
		default: x=0;
		break;
		}
		return x;
	}
	private int get_Y_Coordinate(int tablePos){
		int y;
		switch (tablePos) {
		case 0: y = 460;
		break;
		case 1: y = 460;
		break;
		case 2: y=305;
		break;
		case 3: y=140;
		break;
		case 4: y=140;
		break;
		case 5: y=140;
		break;
		case 6: y=140;
		break;
		case 7: y=305;
		break;
		case 8: y = 460;
		break;
		default: y=0;
		break;
		}
		return y;
	}
	public void setBlindAmounts(int sB, int bLevel, int bII){
		sBlind = sB;
		bBlind = sBlind*2;
		blindLevel = bLevel;
		blindsInceaseIn = bII;
	}
	public void setHandNumber(int handNo){
		handNumber = handNo;
	}
	//Returns the string to be displayed in the GUI for the players bet
	private String getBetAsString(Player player){
		String betAsString;
		if(player.getBettingStatus() == BettingStatus.Checked || player.getBettingStatus()
				== BettingStatus.Folded){
			betAsString = player.getBettingStatus().toString();
		}
		else if(player.getBettingStatus() == BettingStatus.Called){
			betAsString = player.getBettingStatus().toString() +" " +
					player.getLastBet();
		}
		else if(player.getBettingStatus() == BettingStatus.Raised){
			betAsString = "Raised to " + player.getPotContribution();
		}
		else if(player.getBettingStatus() == BettingStatus.Allin){
			betAsString = player.getBettingStatus().toString() +" " +
					player.getPotContribution();
		}
		else{
			betAsString = " ";
		}
		return betAsString;
	}
}