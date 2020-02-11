package UserInterface;
import gameFlow.BettingRound;
import gameFlow.Player;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Human extends Player{
	BufferedReader dataIn = new BufferedReader( new InputStreamReader(System.in) );
	String playerAction = "";
	protected boolean isHuman = true;
	// Constructor
	public Human(String name, int chips){
		//startingHands = 100, preFlopAgression = 0, positionalAwareness =0
		super(name, chips);
	}
	public int getDecision(int totalPot, int costToCall, ArrayList<Player> playerList,
			int playersLeftToAct, BettingRound round, int minRaise, HoldemGUI myGui){
	
		int activePlayers = getActivePlayerCount(playerList);
		int playerBet;
		//No decision needs to be made unless there are active players and we are facing a bet
		if(activePlayers <= 1 && costToCall ==0){
			setBettinStatus(BettingStatus.Checked);
			playerBet= betAdjustedForChipstack(costToCall);
		}
		else{
			int raiseButtonMin = betAdjustedForChipstack(potContribution + costToCall +
					minRaise);
			int raiseButtonMax = raiseButtonMin;
			myGui.resetButtons();
			myGui.setCallButton(costToCall);
			//Checks to see if the player has enough chips to make a raise, call
			if(chips == 0){
				myGui.setFoldVisibility(false);
				myGui.setCallVisibility(false);
				myGui.setRaiseVisibility(false);
				return playerBet =0;
			}
			else if(costToCall >= chips ){
				myGui.setFoldVisibility(true);
				myGui.setCallVisibility(true);
				myGui.setRaiseVisibility(false);
			}
			else{
				myGui.setFoldVisibility(true);
				myGui.setCallVisibility(true);
				myGui.setRaiseVisibility(true);
				if(chips >= costToCall + minRaise){
					raiseButtonMin = potContribution + costToCall + minRaise;
					raiseButtonMax = potContribution + chips;
					//myGui.setR
				}
				else{
					raiseButtonMin = potContribution + chips;
					raiseButtonMax = potContribution + chips;
				}
			}
			myGui.setMinMaxRaise(raiseButtonMin, raiseButtonMax);
			myGui.setRaiseButton(raiseButtonMin);
			
			while(myGui.isCallPressed == false && myGui.isFoldPressed == false && myGui.raisePressed == false){
				//Wait until a button is pressed
				System.out.print(".");
			}
			if(myGui.isFoldPressed() == true){
				playerBet = 0;
				setBettinStatus(BettingStatus.Folded);
			}
			else if(myGui.isCallPressed() == true){
				playerBet = costToCall;
				if(costToCall==0){
					setBettinStatus(BettingStatus.Checked);
				}else{
					setBettinStatus(BettingStatus.Called);
				}
			}
			else{ //Raised
				playerBet = myGui.getRaiseAmount();
				setBettinStatus(BettingStatus.Raised);
				myGui.resetRaiseText();
			}
		}
		//checks to see if the player has tried to bet more than they can afford
		playerBet = betAdjustedForChipstack(playerBet);
		if(playerBet != 0){
			myGui.updateSummaryBox(name + " " + bettingStatus + " " + playerBet);
		}
		else{
			myGui.updateSummaryBox(name + " " + bettingStatus);
		}
		lastBet = playerBet;
		return playerBet;
	}

	// Old getRaiseAmount method used for a console output display instead of the GUI
	/*
	 * private int getRaiseAmount(){ try{ playerAction = dataIn.readLine(); } catch(
	 * IOException e ){ System.out.println("Error!"); } int raiseAmount =
	 * Integer.parseInt(playerAction); if(raiseAmount < minRaise){ //If the player
	 * raises less the the minimum allowed if(chips >= minRaise + costToCall){ //if
	 * the player can afford to raise the minimum raiseAmount = minRaise; //sets the
	 * raise amount to the min allowed bet } else //if the player can't afford the
	 * min raise then bet however many chips they have left raiseAmount = chips -
	 * costToCall; } return raiseAmount; }
	 */
	public boolean isHuman(){
		return isHuman;
	}
}
