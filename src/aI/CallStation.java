package aI;

import gameFlow.BettingRound;
import gameFlow.Player;
import java.util.ArrayList;
import java.util.Random;
import UserInterface.HoldemGUI;

public class CallStation extends Player_AI{
	//constuctor
	public CallStation(String name, int chips){
		//startingHands = 100, preFlopAgression = 0, positionalAwareness =0
		super(name, chips);
	}
	public int getDecision(int totalPot, int costToCall, ArrayList<Player> playerList,
			int playersLeftToAct, BettingRound round, int minRaise, HoldemGUI gui){
		int raiseCounter = getRaiseCount(playerList);
		int playerBet;
		if(round == BettingRound.River){ // round = RIVER
			/*HandRankPercentage(playerHand, commCards);*/
			int myHandStrenght = getRelativeHandStrenght(playerHand);
			/*If the play has a combined hand at least 1 category better than the 5
			 * community cards */
			if(myHandStrenght > 0){
				//If the player is last to act and nobody has bet,
				//bet between 1/4 and 1.5 the pot
				if(raiseCounter == 0 && playersLeftToAct ==0){
					setBettinStatus(BettingStatus.Raised);
					Random myRandom = new Random(13589427); //13589427 random seed
					int randomInt = myRandom.nextInt(6) + 1;
					//since we are dividing we need a tempory double variable to hold the value
					double doublePlayerBet = totalPot*randomInt/4;
					int intPlayerBet = (int)doublePlayerBet; //convert back to an integer
					playerBet= betAdjustedForChipstack(intPlayerBet);
				}
				else if(raiseCounter >= 1){
					if(myHandStrenght <= 1){
						Random myRandom = new Random(43589427); //43589427 random seed
						// generates a random int between 1 and 17
						int randomInt = myRandom.nextInt(17) + 1;
						double randomDouble = (double)randomInt; //convert to double
						// converts to a variable between 0.5 and 0.66 which represents
						// pot odds the player is willing to call randomDouble = 0.67 - (randomDouble/100);
						if(costToCall <= randomDouble){
							setBettinStatus(BettingStatus.Called);
							playerBet=
									betAdjustedForChipstack(costToCall);
						}
						else{
							setBettinStatus(BettingStatus.Folded);
							playerBet= 0;
						}
					}
					else{//HandStrenght is greater than 1 then call
						setBettinStatus(BettingStatus.Called);
						playerBet= betAdjustedForChipstack(costToCall);
					}
				}
				else{ //No raises but players still to act
					setBettinStatus(BettingStatus.Called);
					playerBet= betAdjustedForChipstack(costToCall);
				}
			}
			else{//The players hand did not connect with the community cards, i.e the
				//player has a very weak hand
				if(costToCall == 0){ //Check, since it doesn't cost us anything
					setBettinStatus(BettingStatus.Checked);
					playerBet= betAdjustedForChipstack(costToCall);
				}
				else{ //Otherwise fold
					setBettinStatus(BettingStatus.Folded);
					playerBet= 0;
				}
			}
		}
		else{
			if(costToCall == 0){
				setBettinStatus(BettingStatus.Checked);
			}
			else{
				setBettinStatus(BettingStatus.Called);
			}
			playerBet= betAdjustedForChipstack(costToCall);
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