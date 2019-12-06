/**
 * Author: Ciaran Duggan
 * Created: 01.01.2012
 **/
package gameFlow;

import gameFlow.Card;
import gameFlow.Player.BettingStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import scoring.HandEvaluator;
import scoring.StartingHandsEvaluator;
import UserInterface.HoldemGUI;

public class Game {
	private ArrayList<Player> playerList;
	private Pot pot;
	ArrayList<Pot> sidePots = new ArrayList<Pot>();
	private ArrayList<HandEvaluator> playerHandRanks;
	private int smallBlind = 20;
	private int blindLevel = 1;
	private int blindsInceaseIn = 10; // rounds until blinds increase
	private int dealerPos = 0;
	private int smallBlindPos;
	private int bigBlindPos;
	private BettingRound betRound;
	int totalCostToCall;
	HoldemGUI myGui;
	int guiDealerPos; //the GUI position of the dealer button
	int guiSmallBlindPos;
	int guiBigBlindPos;
	BufferedReader dataIn = new BufferedReader( new InputStreamReader(System.in) );
	String playerAction = "";
	int delay = 250; //Adds a delay between dealt cards
	int allinCount;
	int handNumber = 0; //Keeps track of how many hands have been played
	boolean[] elibilityList;
	//Creates a GameHand object
	public Game(ArrayList<Player> players, HoldemGUI myGui) {
		this.myGui = myGui;
		playerList = players;
		setPlayerPositions();
		smallBlindPos = (dealerPos + 1) % playerList.size();
		bigBlindPos = (dealerPos + 2) % playerList.size();
		if(myGui.getBoolStart()==false){
			myGui.initilizeGUI();
		}
	}
	public int getActivePlayers(){
		int activePlayerCount = 0;
		for(int i = 0; i < playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
				activePlayerCount ++;}
		}
		return activePlayerCount;
	}
	/*Updates the positions of the dealer button as well as the small and big
blinds. Also increases the size of blinds after a certain number of hands*/
	private void incrementBlindsPositions(){
		dealerPos = (dealerPos+1) % playerList.size();
		smallBlindPos = (dealerPos + 1) % playerList.size();
		bigBlindPos = (dealerPos + 2) % playerList.size();
		//Increase the blinds after every 10 hand
		blindsInceaseIn--;
		if (blindsInceaseIn <= 0){
			blindLevel++;
			smallBlind = smallBlind*2;
			blindsInceaseIn = 10;
		}
	}
	//If all but one player folds, return true
	public boolean checkForWinner(){
		if(getActivePlayers()==1){
			for(int i=0; i < playerList.size(); i++){
				if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
					//adds the pot to the winners chip stack
					myGui.updateSummaryBox(playerList.get(i).getName()+" wins" + pot.getPot()+" chips");
							playerList.get(i).setChips(playerList.get(i).getChips() +
									pot.getPot());
					for(int j=0; j <= sidePots.size()-1; j++){
						myGui.updateSummaryBox(playerList.get(i).getName()+" wins " + pot.getPot()+" chips");
								playerList.get(i).setChips(playerList.get(i).getChips() + pot.getPot());
					}
				}
			}
			myGui.setPlayersHoleCards(playerList);
			updatePlayerList();
			incrementBlindsPositions();
			sidePots.clear();//removes any sidepots
			return true;
		}
		else
			return false;
	}
	//Sets where the players will be positioned in the GUI
	private void setPlayerPositions(){
		for(int i=0; i < playerList.size();i++){
			if(playerList.get(i).isHuman() == true){
				//playerList.get(i).setTablePosition(0);
				for(int tabPosition=0; tabPosition < playerList.size();tabPosition++){
					//Use modula to wrap around
					int playerIndex = (i+tabPosition) % playerList.size();
					playerList.get(playerIndex).setTablePosition(tabPosition);
				}
			}
		}
	}
	public void startPreFlop(){
		handNumber++;
		allinCount=0;// used to keep track of how many players have gone allin
		int blindsContribution = 0;
		pot = new Pot(playerList.size());
		myGui.setHandNumber(handNumber);
		myGui.displayFlop(null, null, null);
		myGui.displayTurn(null);
		myGui.displayRiver(null);
		guiDealerPos = playerList.get(dealerPos).getTablePosition();
		guiSmallBlindPos = playerList.get(smallBlindPos).getTablePosition();
		guiBigBlindPos = playerList.get(bigBlindPos).getTablePosition();
		myGui.setDealerPos(guiDealerPos, guiSmallBlindPos, guiBigBlindPos);
		myGui.setBlindAmounts(smallBlind, blindLevel, blindsInceaseIn);
		//checks that the player can afford to pay the small blind
		if(playerList.get(smallBlindPos).getChips() >= smallBlind){
			blindsContribution += smallBlind;
			playerList.get(smallBlindPos).setChips(playerList.get(smallBlindPos).getChips() -
					smallBlind);
			playerList.get(smallBlindPos).setPotContribution(smallBlind);
		}
		else{ //If the player can't afford the small blind they just pay however many chips theey have left
			blindsContribution += playerList.get(smallBlindPos).getChips();
			playerList.get(smallBlindPos).setPotContribution(playerList.get(smallBlindPos).getChips());
			playerList.get(smallBlindPos).setChips(0);
		}
		//checks that the player can afford to pay the big blind
		if(playerList.get(bigBlindPos).getChips() >= smallBlind*2){
			blindsContribution += smallBlind*2;
			playerList.get(bigBlindPos).setChips(playerList.get(bigBlindPos).getChips()
					- (smallBlind*2));
			playerList.get(bigBlindPos).setPotContribution(smallBlind*2);
		}
		else{
			blindsContribution += playerList.get(bigBlindPos).getChips();
			playerList.get(bigBlindPos).setPotContribution(playerList.get(bigBlindPos).getChips());
			playerList.get(bigBlindPos).setChips(0);
		}
		pot.setPot(blindsContribution);//sets the pot
		myGui.setPot(getTotalPot());
		totalCostToCall = smallBlind*2;
		betRound = BettingRound.PreFlop;
		displayInfo();
		for(int i=0; i <= playerList.size() - 1; i++){
			playerList.get(i).setM(smallBlind*3);
		}
		int plta = playerList.size(); //plta(players left to act) in the bettin round
		int fta = (dealerPos+3) % playerList.size(); //fta(first to act) player who opens the betting
		startBettingRound(fta, plta, smallBlind*2, smallBlind*2);
	}
	public void startFlop(Card flopCard1, Card flopCard2, Card flopCard3){
		resetBettingStatus(); //Clears the bets from the previous round
		myGui.displayFlop(flopCard1.cardAsString(), flopCard2.cardAsString(),
				flopCard3.cardAsString());
		totalCostToCall = 0;
		betRound = BettingRound.Flop;
		displayInfo();
		for(int i=0; i <= playerList.size() - 1; i++){
			playerList.get(i).setPotContribution(0);//resets the players pot contribution for this round
			playerList.get(i).getPlayerHand().add(flopCard1);
					playerList.get(i).getPlayerHand().add(flopCard2);
					playerList.get(i).getPlayerHand().add(flopCard3);
		}
		startBettingRound(dealerPos+1, playerList.size(), 0, smallBlind*2);
	}
	public void startTurn(Card turnCard){
		resetBettingStatus();
		myGui.displayTurn(turnCard.cardAsString());
		totalCostToCall = 0;
		betRound = BettingRound.Turn;
		displayInfo();
		for(int i=0; i <= playerList.size() - 1; i++){
			playerList.get(i).setPotContribution(0);//resets the players pot contribution for this round
			playerList.get(i).getPlayerHand().add(turnCard);
		}
		startBettingRound(dealerPos+1, playerList.size(), 0, smallBlind*2);
	}
	public void startRiver(Card riverCard){
		resetBettingStatus();
		myGui.displayRiver(riverCard.cardAsString());
		totalCostToCall = 0;
		betRound = BettingRound.River;
		displayInfo();
		for(int i=0; i <= playerList.size() - 1; i++){
			playerList.get(i).setPotContribution(0);//resets the players pot contribution for this round
			playerList.get(i).getPlayerHand().add(riverCard);
		}
		startBettingRound(dealerPos+1, playerList.size(), 0, smallBlind*2);
	}
	//After all betting has finished
	public void showDown(){
		potWinners(pot);
		if(sidePots.isEmpty()==false){ //If there are sidepots
			for(int i=0; i <= sidePots.size()-1; i++){
				potWinners(sidePots.get(i));
			}
		}
		myGui.setPlayersHoleCards(playerList);
		updatePlayerList();
		incrementBlindsPositions();
		sidePots.clear();//removes any sidepots
	}
	public void displayInfo(){
		myGui.updateSummaryBox("...................................");
		myGui.updateSummaryBox(betRound.toString());
		myGui.updateSummaryBox("...................................");
	}
	private void potWinners(Pot pott){ // decides how the pot/s will be distributed
		playerHandRanks = new ArrayList<HandEvaluator>();
		for(int i=0; i < playerList.size(); i++){
			if (pott.getPlayerEligibility(i) == true){
				System.out.print("\nElible: " + playerList.get(i).getName());
				HandEvaluator temp = new
						HandEvaluator(playerList.get(i).getPlayerHand(), i);
				playerHandRanks.add(temp);
				myGui.showCards(playerList, i);
			}
		}
		Collections.sort(playerHandRanks);
		if(playerHandRanks.size() >= 2){
			for(int i=0; i <= playerHandRanks.size()-2; i++){
				//Compare the top hand with 2nd, 3rd etc to check for split pots
				if(playerHandRanks.get(0).compareTo(playerHandRanks.get(i+1)) == 0)
					pott.incrementWinningHands();
			}
		}
		if(playerHandRanks.isEmpty() == false){
			//divide the pot by the number of winners
			int winningPot = pott.getPot()/pott.getNoOfWinningHands();
			for(int i=0; i < pott.getNoOfWinningHands(); i++){
				int playerIndex; //The index of the player/s with the winning hand
				playerIndex = playerHandRanks.get(i).getPlayerNo();
				playerList.get(playerIndex).setChips(playerList.get(playerHandRanks.get(i).getPlayerNo()).getChips() + winningPot);
				myGui.updateSummaryBox(playerList.get(playerIndex).getName() + " wins " + winningPot + " chips");
						myGui.updateSummaryBox(playerList.get(playerIndex).getName() +
								"hand: ");
				myGui.updateSummaryBox(playerHandRanks.get(i).getHandCategory().toString());
			}
		}
	}
	/*playersStillToAct is the number of players still to act in this betting round provided
nobody raises, if somebody raises
a new betting round will be created*/
	private void startBettingRound(int first, int playersStillToAct, int totalCostToCall, int
			minRaise){
		for (int i = 0; i <= playersStillToAct - 1; i++){
			myGui.setPlayersHoleCards(playerList);
			try
			{
				Thread.sleep(delay);
			}catch (InterruptedException ie)
			{
				System.out.println(ie.getMessage());
			}
			int index = (first + i) % playerList.size(); //use modula to wrap around if we go past last player in array
			//Checks to ensure the player hasn't already folded or is Allin
			if(playerList.get(index).getBettingStatus() != BettingStatus.Folded
			&& playerList.get(index).getBettingStatus() != BettingStatus.Allin){
				int playersCurrentContribuion =
						playerList.get(index).getPotContribution();
				int costToCall = totalCostToCall - playersCurrentContribuion;
				System.out.print("Get player " + Integer.toString(index) + "s decision");
				int playersBet = playerList.get(index).getDecision(getTotalPot(), costToCall, playerList,
						playersStillToAct-1, betRound, minRaise, myGui);
				//Records the players actions for this round so that other poker bots have memory of how the player acted in
				//previous betting rounds
				if (betRound == BettingRound.PreFlop){
					playerList.get(index).setPreFlopAction(playerList.get(index).determinePlayerAction(playerList
							.get(index).getBettingStatus(), getRaiseCount(playerList)));
				}
				else if(betRound == BettingRound.Flop){
					playerList.get(index).setFlopAction(playerList.get(index).determinePlayerAction(playerList
							.get(index).getBettingStatus(), getRaiseCount(playerList)));
				}
				else if(betRound == BettingRound.Turn){
					playerList.get(index).setTurnAction(playerList.get(index).determinePlayerAction(playerList
							.get(index).getBettingStatus(), getRaiseCount(playerList)));
				}
				else{//river
					playerList.get(index).setRiverAction(playerList.get(index).determinePlayerAction(playerList
							.get(index).getBettingStatus(),
							getRaiseCount(playerList)));
				}
				pot.setPot(pot.getPot() + playersBet);
				myGui.setPot(getTotalPot());
				//myGui.setPlayersHoleCards(playerList);
				//Updates chipstack & how much the player has contributed to the pot this round
				playerList.get(index).setPotContribution(playersCurrentContribuion +
						playersBet);
				playersCurrentContribuion =
						playerList.get(index).getPotContribution();
				playerList.get(index).setChips(playerList.get(index).getChips() -
						playersBet);
				//if a player raises, a new betting round commences
				if(playersBet > costToCall){
					int raisedBy = playersBet - costToCall;
					//If the player raises more than the minimum raise then set minRaise equal to this value
					if(raisedBy > minRaise){
						minRaise = raisedBy;
					}
					totalCostToCall = playersCurrentContribuion;
					int nextPlayer = (index+1) % playerList.size();
					startBettingRound(nextPlayer,playerList.size()-1,
							totalCostToCall, minRaise);
					i = playersStillToAct;//Forces the end of for loop
				}
				//if a player folds, check if there is more than 1 player left in pot
				if(playerList.get(index).bettingStatus == BettingStatus.Folded){
					//make the player not eligible for all pots if they fold
					pot.setPlayerEligibility(index, false);
					for(int j=0; j < sidePots.size(); j++){
						sidePots.get(j).setPlayerEligibility(index, false);
					}
					if(getActivePlayers()==1){
						i = playerList.size();//Forces the end of for loop
					}
				}
				if(playersBet < costToCall){
					while(getPlayersCurrentlyAllin() > allinCount){
						int allinPlayerIndex = getAllinPlayerIndex();
						int smallestContribution =
								playerList.get(allinPlayerIndex).getPotContribution();
						int potReturned=0;
						for (int k =0; k <= playerList.size() - 1; k++){
							if (playerList.get(k).getPotContribution() >
							smallestContribution){
								potReturned = potReturned
										+
										(playerList.get(k).getPotContribution()-smallestContribution);
							}
						}
						int sidePotAmout = pot.getPot() - potReturned;
						int listLenght = pot.getEligibilityList().length;
						elibilityList = new boolean[listLenght];
						for(int j=0; j<listLenght; j++){
							elibilityList[j] =
									pot.getPlayerEligibility(j);
						}
						if(potReturned > 0){
							addSidePot(sidePotAmout, elibilityList);
							pot.setPot(potReturned);
							pot.setPlayerEligibility(allinPlayerIndex,
									false);
						} //pot.setPlayerEligibility(index, false);
						allinCount++;
					}
				}
			}
		}
		//We need a check condition is needed for situations where a player has exactly enough chips
		//to call i.e their call puts them allin but isn't over or under the minimum amount to call
		while(getPlayersCurrentlyAllin() > allinCount){
			int allinPlayerIndex = getAllinPlayerIndex();
			int smallestContribution =
					playerList.get(allinPlayerIndex).getPotContribution();
			int potReturned=0;
			for (int k =0; k <= playerList.size() - 1; k++){
				if (playerList.get(k).getPotContribution() > smallestContribution){
					potReturned = potReturned
							+ (playerList.get(k).getPotContribution()-
									smallestContribution);
				}
			}
			int sidePotAmout = pot.getPot() - potReturned;
			int listLenght = pot.getEligibilityList().length;
			elibilityList = new boolean[listLenght];
			for(int j=0; j<listLenght; j++){
				elibilityList[j] = pot.getPlayerEligibility(j);
			}
			addSidePot(sidePotAmout, elibilityList);
			pot.setPot(potReturned);
			pot.setPlayerEligibility(allinPlayerIndex, false);
			allinCount++;
		}
		myGui.setPot(getTotalPot());
	}
	private int getPlayersCurrentlyAllin(){
		int count=0;
		for(int i=0; i< playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() == BettingStatus.Allin){
				count++;
			}
		}
		return count;
	}
	//Gets the index of the smallest Allin stack
	private int getAllinPlayerIndex(){//only use if there are players allin
		int playerIndex = 0;
		int smallestStack;
		for(int i=0; i< playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() == BettingStatus.Allin &&
					pot.getPlayerEligibility(i) == true){
				smallestStack = playerList.get(i).getPotContribution();
				playerIndex = i;
				smallestStack = playerList.get(i).getPotContribution();
				for(int j=i+1; j< playerList.size(); j++){
					if(playerList.get(j).getBettingStatus() ==
							BettingStatus.Allin
							&& pot.getPlayerEligibility(i) == true
							&& smallestStack > playerList.get(j).getPotContribution()){
						playerIndex = j;
						smallestStack =
								playerList.get(j).getPotContribution();
					}
				}
			}
		}
		return playerIndex;
	}
	/* //Checks for players who are allin and if their status has been updated to 'allin'
private boolean allinsUpdated(int totalCostToCall){
for (int i =0; i <= playerList.size() - 1; i++){
if (playerList.get(i).getPotContribution() < totalCostToCall
&& playerList.get(i).getBettingStatus() ==
BettingStatus.StillBetting){
return false;
}
}
return true;
}*/
	public boolean continueGame(){
		if(playerList.size()>=2){
			return true;
		}
		else
			return false;
	}
	private void updatePlayerList(){
		for(int i =0 ; i<=playerList.size()-1; i++){
			if(playerList.get(i).getChips()<=0){
				playerList.remove(i);
				//We need to decrement i in this case because the elements in the array have shifted one to the left
				i--;
			}
		}
	}
	/*private void updatePlayerList(){
for(int i =0 ; i<=playerList.size()-1; i++){
int currentCash = playerList.get(i).getCash();
int currentChips = playerList.get(i).getChips();
int profitLoss = currentChips - 2000;
playerList.get(i).setCash(currentCash + profitLoss);
playerList.get(i).setChips(2000);
}
}*/
	//Clears the bets from the previous round apart from folds and allins
	public void resetBettingStatus(){
		for(int i=0; i< playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() != BettingStatus.Folded
					&& playerList.get(i).getBettingStatus() != BettingStatus.Allin){
				playerList.get(i).setBettinStatus(BettingStatus.StillBetting);
			}
		}
	}
	private void addSidePot(int amount, boolean[] list){
		int noOfSidePots = sidePots.size();
		switch(noOfSidePots){
		case 0: Pot sidePot1 = new Pot(list);
		sidePot1.setPot(amount);
		sidePots.add(sidePot1);
		break;
		case 1: Pot sidePot2 = new Pot(list);
		sidePot2.setPot(amount);
		sidePots.add(sidePot2);
		break;
		case 2: Pot sidePot3 = new Pot(list);
		sidePot3.setPot(amount);
		sidePots.add(sidePot3);
		break;
		case 3: Pot sidePot4 = new Pot(list);
		sidePot4.setPot(amount);
		sidePots.add(sidePot4);
		break;
		case 4: Pot sidePot5 = new Pot(list);
		sidePot5.setPot(amount);
		sidePots.add(sidePot5);
		break;
		case 5: Pot sidePot6 = new Pot(list);
		sidePot6.setPot(amount);
		sidePots.add(sidePot6);
		break;
		default:Pot sidePot7 = new Pot(list);
		sidePot7.setPot(amount);
		sidePots.add(sidePot7);
		break;
		}
	}
	private int getTotalPot(){
		int totalPot = pot.getPot();
		for(int potIndex=0; potIndex<sidePots.size(); potIndex++){
			totalPot += sidePots.get(potIndex).getPot();
		}
		return totalPot;
	}
	//Determines what percentage of hands you are beating
	protected double getOpeningPercentage(){
		double totalCount =0;
		double openingHand =0;
		Deck myDeck2 = new Deck();
		for(int i=0; i < 52; i++){
			for(int j=1; j < 52; j++){
				if(i != j){ //checks that the 2 hole cards aren't the same
					Hand tempHand = new Hand();
					tempHand.add(myDeck2.getSpecificCard(i));
					tempHand.add(myDeck2.getSpecificCard(j));
					StartingHandsEvaluator myStartingHandEval
					= new StartingHandsEvaluator(tempHand, 20);
					double startingHandScore =
							myStartingHandEval.getStartingRaiseScore();
					double speculativeScore =
							myStartingHandEval.getSpeculativeScore();
					if(startingHandScore >= 9 || speculativeScore >=8){
						openingHand++;
					}
					totalCount++;
					tempHand.clear();
				}
			}
		}
		return openingHand/totalCount;
	}
	protected int getRaiseCount(ArrayList<Player> playerList){
		int raiseCount = 0;
		for(int i=0; i<playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() == BettingStatus.Raised){
				raiseCount++;
			}
		}
		return raiseCount;
	}
}