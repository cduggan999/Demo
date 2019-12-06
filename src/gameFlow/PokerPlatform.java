package gameFlow;

import gameFlow.Player.BettingStatus;
import java.util.ArrayList;
import UserInterface.HoldemGUI;
import UserInterface.Human;
import aI.*;

public class PokerPlatform {
	HoldemGUI gui;
	static ArrayList<Player> playerArray;
	static Game myGame;
	int chips =2000;
	public PokerPlatform(HoldemGUI gui){
		this.gui= new HoldemGUI();
	}
	public void Initilize(){
		//gui = new HoldemGUI(PokerPlatform.this);
		Human player1 = new Human("Human_Player", 2000);
		CallStation player2 = new CallStation("Call_Station", chips);
		LooseAggressive player3 = new LooseAggressive("Loose_Aggressive", chips);
		TightAggressive player4 = new TightAggressive("Tight_Aggressive", chips);	
		TightPassive player5 = new TightPassive("Tight_Passive", chips);
		MediumPassive player6 = new MediumPassive("Medium_Passive ", chips);
		//LoosePassive player7 = new LoosePassive("Loose_Passive", chips);
	//	Medium player8 = new Medium("Medium ", chips);
	//	Adaptive player9 = new Adaptive("Adaptive ", chips);
//		MediumAggressive player10 = new MediumAggressive("Med_Aggressive ", chips);
		LooseAggressive player7 = new LooseAggressive("Loose_Aggressive", chips);
		TightAggressive player8 = new TightAggressive("Tight_Aggressive", chips);	
		TightPassive player9 = new TightPassive("Tight_Passive", chips);
		MediumPassive player10 = new MediumPassive("Medium_Passive ", chips);
		
		playerArray = new ArrayList<Player>();
		playerArray.add(player1);
		playerArray.add(player2);
		playerArray.add(player3);
		playerArray.add(player4);
		playerArray.add(player5);
		playerArray.add(player6);
		playerArray.add(player7); 
		playerArray.add(player8); 
		playerArray.add(player9);
		playerArray.add(player10);
		 
		myGame = new Game(playerArray, gui);
	}
	public boolean startGame(){
		//Do this whilst there is more than 1 player with more than zero chips
		while(myGame.continueGame() == true){
			Deck myDeck = new Deck();
			myDeck.shuffleCards();
			//Deals the pocket cards for each player
			for(int i=0; i <= playerArray.size()-1; i++){
				playerArray.get(i).getPlayerHand().clear();
				playerArray.get(i).getPlayerHand().add(myDeck.dealCard());
				playerArray.get(i).getPlayerHand().add(myDeck.dealCard());
				//Clears All players actions from previous hand
				playerArray.get(i).clearPlayerActions();
				playerArray.get(i).setPotContribution(0);
				playerArray.get(i).setBettinStatus(BettingStatus.StillBetting);
			}
			myGame.startPreFlop();
			if (myGame.checkForWinner()==false){
				Card flopCard1 = myDeck.dealCard();
				Card flopCard2 = myDeck.dealCard();
				Card flopCard3 = myDeck.dealCard();
				myGame.startFlop(flopCard1, flopCard2, flopCard3);
				if (myGame.checkForWinner()==false){
					Card turnCard = myDeck.dealCard();
					myGame.startTurn(turnCard);
					if (myGame.checkForWinner()==false){
						Card riverCard = myDeck.dealCard();
						myGame.startRiver(riverCard);
						if (myGame.checkForWinner()==false){
							myGame.showDown();
						}
					}
				}
			}
			gui.setFoldVisibility(false);
			gui.setCallVisibility(false);
			gui.setRaiseVisibility(false);
			gui.setContinueVisibility(true);
			
/*
 ///////////////////Add Continue button option here			
			
			 * while(gui.isContinuePressed() == false){ //infinite loop until continue
			 * button is pressed }
			 */
///////////////////////////////////////////////////////////////////////////////////////////////////			
			gui.setContinueVisibility(false);
			gui.resetButtons();
			//handCount++;
		}
		gui.setNewGameVisibility(true);
		gui.setQuitVisibility(true);
		while(gui.isNewGamePressed()==false && gui.isNewGamePressed()==false){
			//infinite loop until NewGame/Quit button is pressed
		}
		if(gui.isNewGamePressed()==true){
			gui.resetNewGame();
			return true;
		}
		else{
			return false;
		}
	} // end of startGame()
}