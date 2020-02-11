package aI;

import gameFlow.BettingRound;
import gameFlow.Player;
import java.util.ArrayList;
import java.util.Random;
import scoring.StartingHandsEvaluator;
import UserInterface.HoldemGUI;

public class TightPassive extends Player_AI{
	protected StartingHandsEvaluator startingHandValue;
	protected int startingHandRange = 10;
	protected double speculativeHandRange = 8;
	protected int preFlopAgression = 0;
	protected double positionalAwareness = 0.5;
	protected double positonalBonus;
	protected double postFlopCallingRange = 0.15; //calls with top 15% of hands
	// Constructor
	public TightPassive(String name, int chips){
		//startingHands = 100, preFlopAgression = 0, positionalAwareness =0
		super(name, chips);
	}
	public int getDecision(int totalPot, int costToCall, ArrayList<Player> playerList,
			int playersLeftToAct, BettingRound round, int minRaise, HoldemGUI gui){
		int raiseCounter = getRaiseCount(playerList);
		int playerBet;
		positonalBonus = getPositionalBonus(playersLeftToAct, round) * positionalAwareness;
		/*We don't want the player calling with speculative hands if there has being a
raise
		 * hence the (raiseCounter*5) requirement */
		if(round == BettingRound.PreFlop){
			if(costToCall ==0){
				setBettinStatus(BettingStatus.Checked);
				playerBet= costToCall;
			}
			else{
				startingHandValue = new StartingHandsEvaluator(playerHand, m);
				if(startingHandValue.getStartingRaiseScore() + positonalBonus >=
						startingHandRange +
						(raiseCounter*2) || startingHandValue.getSpeculativeScore() >=
						speculativeHandRange
						+ (raiseCounter*5) || costToCall == 0){
					setBettinStatus(BettingStatus.Called);
					playerBet= betAdjustedForChipstack(costToCall);
				}
				else{
					setBettinStatus(BettingStatus.Folded);
					playerBet = 0;
				}
			}
		}
		else{//Flop, Turn & River
			double handRankPercentage = getHandRankPercentage(playerHand);
			if(costToCall ==0){
				if(round == BettingRound.River && raiseCounter == 0 &&
						playersLeftToAct ==0
						&& handRankPercentage >= 0.9){
					setBettinStatus(BettingStatus.Raised);
					Random myRandom = new Random(13589427); //13589427 random seed
					int randomInt = myRandom.nextInt(6) + 1;
					//since we are dividing we need a temporary double variable to hold the value
					double doublePlayerBet = totalPot*randomInt/4;
					int intPlayerBet = (int)doublePlayerBet; //convert back to an integer
					playerBet= betAdjustedForChipstack(intPlayerBet);
				}
				else{
					setBettinStatus(BettingStatus.Checked);
					playerBet= costToCall;
				}
			}
			else{
				double potOdds = (double)totalPot/costToCall;
				//Value to me modifed by game conditions such as raises, pot odds etc
				double currentCallingRange = postFlopCallingRange;
				if(potOdds < 1.2){ //someone raised more than 5 times the pot
					currentCallingRange = currentCallingRange/5;
				}
				else if(potOdds < 1.3){
					currentCallingRange = currentCallingRange/4;
				}
				else if(potOdds < 1.5){
					currentCallingRange = currentCallingRange/3.1;
				}
				else if(potOdds < 2){
					currentCallingRange = currentCallingRange/2.3;
				}
				else if(potOdds < 2.5){
					currentCallingRange = currentCallingRange/1.7;
				}
				else if(potOdds < 3){
					currentCallingRange = currentCallingRange/1.2;
				}
				else if(potOdds < 4){
					currentCallingRange = currentCallingRange*1.2;
				}
				else if(potOdds < 6){
					currentCallingRange = currentCallingRange*1.7;
				}
				else if(potOdds < 11){
					currentCallingRange = currentCallingRange*2.4;
				}
				else{
					/*If a player is getting pot odds of greater that 10-to-1
they
should be calling with almost any hand as the odds are so
favorable*/
					currentCallingRange = currentCallingRange*6;
				}
				//Hand is within calling range
				if(currentCallingRange >= 1 - handRankPercentage){
					//If its the river and the player has a hand significantly better than what is
					//just required to call then the player will raise
					if(round == BettingRound.River && currentCallingRange>=(1-
							handRankPercentage)*4){
						setBettinStatus(BettingStatus.Raised);
						double amountToRaise = costToCall + (totalPot/2);
						//amountToRaise needs to be cast back to an integer
						playerBet=
								betAdjustedForChipstack((int)amountToRaise);
					}
					else{ //Hand is good enough to call but not raise
						setBettinStatus(BettingStatus.Called);
						playerBet= betAdjustedForChipstack(costToCall);
					}
				}
				else{
					setBettinStatus(BettingStatus.Folded);
					playerBet= 0;
				}
			}
		}
		if(playerBet != 0){
			gui.updateSummaryBox(name + " " + bettingStatus + " " + playerBet);
		}
		else{
			gui.updateSummaryBox(name + " " + bettingStatus);
		}
		lastBet = playerBet;
		return playerBet;
	}
}