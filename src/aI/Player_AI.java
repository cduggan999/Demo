package aI;

import gameFlow.BettingRound;
import gameFlow.Card;
import gameFlow.Deck;
import gameFlow.Hand;
import gameFlow.Player;
import gameFlow.PlayerAction;
import gameFlow.Player.BettingStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import scoring.HandEvaluator;
import scoring.StartingHandsEvaluator;
import UserInterface.HoldemGUI;

public class Player_AI extends Player{
	protected StartingHandsEvaluator startingHandValue;
	protected int startingHandRange; /*the range of hands the player is willing to
	 * open with, a low value indicates a loose player whilst a tight player will have
	 * a high value. Range(-1 to 20) with -1 being the worst possible hand and 20
	 * being the best possible hand*/
	protected int speculativeHandRange;
	protected int preFlopAgression; //how agressive/passive when they do play
	protected int postFlopAgression;
	protected int bluffFrequency;
	/*postFlopRaiseRequirement is the default value to used to determine the range of hands
required to
	 *raise, a value of 8 for example means this player will raise with the top 8% of hands
(before other factors
	 *such as raise number of players etc is taking into account).*/
	protected double postFlopRaiseRequirement;
	protected double positionalAwareness; //how much value the player assigns position
	protected double positonalBonus;
	protected HandEvaluator completeHandEvaluator;
	//Fold/Call/Raise Percentages given a Rate of Return(RoR) of less than 1
	protected int ror_1_Fold_Percentage;
	protected int ror_1_Call_Percentage;
	/*No need for stating Raise Percentage as it will simply be 100% - (Fold & Call)
percentages*/
	//Fold/Call/Raise Percentages given a RoR of between 1 and 1.2
	protected int ror_1_2_Fold_Percentage;
	protected int ror_1_2_Call_Percentage;
	//Fold/Call/Raise Percentages given a RoR of between 1.2 and 1.4
	protected int ror_1_4_Fold_Percentage;
	protected int ror_1_4_Call_Percentage;
	//Fold/Call/Raise Percentages given a RoR of 1.4 and over
	protected int ror_Over_1_4_Fold_Percentage;
	protected int ror_Over_1_4_Call_Percentage;
	protected boolean isHuman = false;
	//Constructor
	public Player_AI(String name, int chips){
		//this.startingHands = startingHands;
		//this.preFlopAgression = preFlopAgression;
		//this.positionalAwareness = positionalAwareness;
		super(name, chips);
	}
	public int getDecision(int totalPot, int costToCall, ArrayList<Player> playerList
			, int playersLeftToAct, BettingRound round, int minRaise, HoldemGUI gui){
		int activePlayers = getActivePlayerCount(playerList);
		int playerBet;
		//No decision needs to be made unless there are active players and we are facing a bet
		if(activePlayers <= 1 && costToCall ==0){
			setBettinStatus(BettingStatus.Checked);
			playerBet= betAdjustedForChipstack(costToCall);
		}
		else{
			int raiseCounter = getRaiseCount(playerList);
			positonalBonus = getPositionalBonus(playersLeftToAct, round );
			int noOfPlayers = getActivePlayerCount(playerList);
			/*We don't want the player calling with speculative hands if there has
being a raise
			 * hence the (raiseCounter*5) requirement */
			if(round == BettingRound.PreFlop){
				startingHandValue = new StartingHandsEvaluator(playerHand, m);
				if(startingHandValue.getStartingRaiseScore() + positonalBonus >=
						startingHandRange
						+ (raiseCounter*2) || startingHandValue.getSpeculativeScore() >=
						speculativeHandRange
						+ (raiseCounter*5)){
					if (raiseOrCallPreflop(costToCall) == false){ //Player Calls
						if (costToCall != 0){
							setBettinStatus(BettingStatus.Called);
						}
						else{
							setBettinStatus(BettingStatus.Checked);
						}
						playerBet= betAdjustedForChipstack(costToCall);
					}
					else{ //RaiseOrCall() == true //Player Raises
						setBettinStatus(BettingStatus.Raised);
						double amountToRaise;
						if(costToCall != 0){
							amountToRaise = costToCall + (costToCall *
									raiseFactor());
						}
						else{
							amountToRaise = costToCall +
									(((double)totalPot/2.5) * raiseFactor());
						}
						//amountToRaise needs to be cast back to an integer
						playerBet=
								betAdjustedForChipstack((int)amountToRaise);
					}
				}
				else if(costToCall == 0){
					setBettinStatus(BettingStatus.Checked);
					playerBet= betAdjustedForChipstack(costToCall);
				}
				else if(potContribution > 0 &&
						rateOfReturn(playerHand,playerList,costToCall, totalPot) > 1){
					setBettinStatus(BettingStatus.Called);
					playerBet= betAdjustedForChipstack(costToCall);
				}
				else{
					setBettinStatus(BettingStatus.Folded);
					playerBet= 0;
				}
			}
			else{ //post flop
				playerBet = getFCR_Decision(playerList, playersLeftToAct, totalPot,
						costToCall, round, raiseCounter);
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
	/*Compares the players hand to the community cards, i.e how well their hand connected
	 * with the board. Returns the difference in hand category between the just community
	 * cards & the full hand(community cards + hole cards) If the persons hold cards didn't
improve
	 * the hand then this returns zero.*/
	protected int getRelativeHandStrenght(Hand completeHand){
		Hand communityCards = new Hand();
		if(completeHand.getHandSize() >= 5){
			for(int i = 2; i < completeHand.getHandSize(); i++){
				//start index at 2 because the first 2 cards of the hand are the players hole cards
				communityCards.add(completeHand.getCardOriginalOrder(i));
			}
			HandEvaluator communityCardsEvaluator = new HandEvaluator(communityCards);
			completeHandEvaluator = new HandEvaluator(completeHand);
			return communityCardsEvaluator.compareCategory(completeHandEvaluator);
		}
		else
			return 0;
	}
	//counts the number of raises that have been made in the pot
	protected int getRaiseCount(ArrayList<Player> playerList){
		int raiseCounter = 0;
		for(int i=0; i < playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() == BettingStatus.Raised){
				raiseCounter++;
			}
		}
		if(getBettingStatus() == BettingStatus.Raised){
			raiseCounter--; //We don't want to count the players own raise
		}
		return raiseCounter;
	}
	protected int getActivePlayerCount(ArrayList<Player> playerList){
		int apCounter = 0; //active player counter
		for(int i=0; i < playerList.size(); i++){
			if(playerList.get(i).getBettingStatus() != BettingStatus.Folded){
				apCounter++;
			}
		}
		return apCounter;
	}
	/*Determines the players position at the table i.e. early, mid, late and returns
	 * value that will be used in calculating betting decisions */
	protected int getPositionalBonus(int playersLeftToAct, BettingRound round){
		if(round == BettingRound.PreFlop){
			if(playersLeftToAct <=3){ //late position
				return 2;
			}
			else if(playersLeftToAct <=6){ //mid position
				return 1;
			}
			else{
				return 0;
			}
		}
		else{ //Post Flop
			if(playersLeftToAct <=2){ //late position
				return 2;
			}
			else if(playersLeftToAct <=5){ //mid position
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	/*Will randomly vary between raising and calling, returns true when the player
	 * intends to raise*/
	protected boolean raiseOrCallPreflop(int costToCall){
		Random myRandom = new Random(59580427);
		int randomInt = myRandom.nextInt(100); //0-100
		//Checks preFlop agreesion value and if we have more chips than the min needed to call
		if(randomInt < preFlopAgression && costToCall < chips){
			return true;
		}
		else
			return false;
	}
	/*Will randomly vary between raising and calling, returns true when the player
	 * intends to raise*/
	protected boolean raiseCallRandomizer(){
		Random myRandom = new Random(59580427);
		int randomInt = myRandom.nextInt(100); //0-100
		//Checks preFlop aggression value and if we have more chips than the min needed to call
		if(randomInt < postFlopAgression){
			return true;
		}
		else
			return false;
	}
	//Randomly varies the amount the player raises by
	protected double raiseFactor(){
		Random myRandom = new Random();
		//randomRaise will be one of following values: 1, 1.5, 2, 2.5, 3, 3.5 or 4
		double randomRaise = (myRandom.nextInt(7) + 2)/2;
		return randomRaise;
	}
	//Determines what percentage of hands you are beating
	protected double getHandRankPercentage(Hand myHand){
		Hand communityCards = new Hand();
		//Get the community cards
		for(int i = 2; i < myHand.getHandSize(); i++){
			communityCards.add(myHand.getCardOriginalOrder(i));
		}
		HandEvaluator myHandEval = new HandEvaluator(myHand);
		double totalCount =0;
		double noOfWins =0;
		Deck myDeck2 = new Deck();
		for(int i=0; i < 52; i++){
			for(int j=1; j < 52; j++){
				if(i != j){ //checks that the 2 hole cards aren't the same
					Hand tempHand = new Hand();
					for(int k=0; k< communityCards.getHandSize(); k++){
						tempHand.add(communityCards.getCard(k));
					}
					tempHand.add(myDeck2.getSpecificCard(i));
					tempHand.add(myDeck2.getSpecificCard(j));
					HandEvaluator tempHandEval = new HandEvaluator(tempHand);
					if(tempHandEval.compareTo(myHandEval) > 0){
						noOfWins++;
					}
					totalCount++;
					tempHand.clear();
				}
			}
		}
		return noOfWins/totalCount;
	}
	//Returns how often we will win the hand by randomizing the unknown cards
	//and running it a number of times
	protected double simulateHand(Hand myHand, ArrayList<Player> playerList){
		int noOfPlayers = getActivePlayerCount(playerList);
		int noOfOtherPlayers = noOfPlayers - 1;
		Hand communityCards = new Hand();
		Hand tempCommunityCards = new Hand();
		Hand myHandTemp = new Hand();
		ArrayList<Hand> opponentHands = new ArrayList<Hand>();
		double noOfWins = 0;
		double noOfSimulations = 2000;
		Deck deck = new Deck();
		//Removes the players card from the remaining deck
		for(int i=0; i< myHand.getHandSize(); i++){
			deck.removeCard(myHand.getCard(i));
		}
		//Get the community cards
		for(int j = 2; j < myHand.getHandSize(); j++){
			communityCards.add(myHand.getCardOriginalOrder(j));
		}
		//Simulate the hand 2000 times
		for(int c=0; c<noOfSimulations; c++){
			for(int h=0; h<communityCards.getHandSize();h++){
				tempCommunityCards.add(communityCards.getCard(h));
			}
			//Hand tempCommunityCards = communityCards; //gets reset after each iteration
			//Hand myHandTemp = myHand; //gets reset after each iteration
			for(int g=0; g < myHand.getHandSize(); g++){
				myHandTemp.add(myHand.getCard(g));
			}
			Deck tempDeck = deck; //gets reset after each iteration
			deck.shuffleCards();
			for(int b=0; b<noOfOtherPlayers; b++){
				//Deal random hold cards for other players
				Hand tempHand = new Hand();
				if(tempDeck.getDeckSize() >= 2){//To prevent Empty Stack Exception error
					tempHand.add(tempDeck.dealCard());
					tempHand.add(tempDeck.dealCard());
				}
				opponentHands.add(tempHand);
			}
			//Deal the remainder of the community cards
			for(int a=communityCards.getHandSize(); a<5; a++){
				Card comCard = tempDeck.dealCard();
				tempCommunityCards.add(comCard);
				myHandTemp.add(comCard);
			}
			//Add the simulated community cards to the opponents hands
			for(int e=0; e <= opponentHands.size()-1; e++){
				for(int f=0; f <= tempCommunityCards.getHandSize()-1; f++){
					opponentHands.get(e).add(tempCommunityCards.getCard(f));
				}
			}
			ArrayList<HandEvaluator> playerHandRanks = new ArrayList<HandEvaluator>();
			//I choose a playerNo of 10 below because opponents playerNo may be anything
			//from 1 to 9.
			HandEvaluator myEval = new HandEvaluator(myHandTemp, 10);
			playerHandRanks.add(myEval);
			for(int d=0; d <= opponentHands.size()-1; d++){
				HandEvaluator oppEval = new HandEvaluator(opponentHands.get(d), d);
				playerHandRanks.add(oppEval);
			}
			Collections.sort(playerHandRanks); // Arranges hands from best to worst
			if(playerHandRanks.get(0).getPlayerNo() == 10){
				//If this player has the best hand
				//To take into account split pots see class GameHand line 199
				noOfWins++;
			}
			opponentHands.clear();//Clears the list of hands
			myHandTemp.clear();
			tempCommunityCards.clear();
		}
		return noOfWins/noOfSimulations;
	}//Simulate hand
	//The pot odds number is the ratio of your bet or call to the size of
	//the pot after you bet
	protected double getPotOdds(int costToCall, int totalPot){
		double newTotalPot = (double)totalPot + (double)costToCall;
		double potOdds = (double)costToCall/newTotalPot;
		return potOdds;
	}
	/*For this method a rate of return of less than 1 represents a loss whilst
	 * a ROR greater than 1 represents a profit*/
	protected double rateOfReturn(Hand myHand,ArrayList<Player> playerList, int costToCall,
			int totalPot){
		double handStrenght = simulateHand(myHand, playerList);
		double potOdds;
		double oppsBetRatio = costToCall/(totalPot-costToCall);
		if(myHand.getHandSize()== 5){ //Flop, 3 betting rounds left
			double finalFlopPotTotal = totalPot+costToCall; //amount in flop after all betting has ended
			double finalTurnPotTotal = (finalFlopPotTotal) +
			(2*oppsBetRatio*(finalFlopPotTotal));
			int oppRiverBet = (int)(oppsBetRatio*(finalTurnPotTotal));
			int riverTotalPot = (int)finalTurnPotTotal + oppRiverBet;
			potOdds = getPotOdds(oppRiverBet, riverTotalPot);
		}
		else if(myHand.getHandSize()== 6){//Turn, 2 betting rounds left
			double finalTurnPotTotal = totalPot+costToCall; //amount in flop after all betting has ended
			int oppRiverBet = (int)(oppsBetRatio*(finalTurnPotTotal));
			int riverTotalPot = (int)finalTurnPotTotal + oppRiverBet;
			potOdds = getPotOdds(oppRiverBet, riverTotalPot);
		}
		else{//River, final betting round
			potOdds = getPotOdds(costToCall, totalPot);
		}
		return handStrenght/potOdds;
	}
	//Calculates the rate of return for the river
	protected double rateOfReturnRiver(Hand myHand,int noOfPlayers,int costToCall, int
			totalPot){
		int noOfOtherPlayers = noOfPlayers - 1;
		double handStrenght = getHandRankPercentage(myHand);
		//
		double relativeHandStrenght = 1;
		//Hand strength when no of other players is taken into account
		for(int x=0; x < noOfOtherPlayers; x++){
			relativeHandStrenght = relativeHandStrenght * handStrenght;
		}
		double potOdds = getPotOdds(costToCall, totalPot);
		return relativeHandStrenght/potOdds;
	}
	//Decides whether to fold, call or raise based on the RoR as well as adding an element of
	//randomness to the decision in order to avoid being too predictable
	protected int getFCR_Decision(ArrayList<Player> playerList, int playersLeftToAct, int
			totalPot, int costToCall, BettingRound round,
			int raiseCount){
		int noOfPlayers = getActivePlayerCount(playerList);
		int myBet;
		//Generates a random number which is used to randomise our decisions to a
		//certain extent in order to add an element of unpredictability to our play
		Random myRandom = new Random();
		int randomInt = myRandom.nextInt(100);
		if(costToCall !=0){
			double RoR;
			//RoR Rate of Return
			if(round != BettingRound.River){
				RoR = rateOfReturn(playerHand,playerList,costToCall,totalPot);
			}
			else{
				RoR = rateOfReturnRiver(playerHand,noOfPlayers,costToCall,totalPot);
			}
			if(RoR < 1){ //Fold 90%/Raise 10%(bluff)
				//A rate of return less than 1 means that on average we will make a loss, so
				//we should generally fold but from time to time it is desirable to make a
				//bluff in order to keep opponents from being able to read your play.
				if(randomInt <= ror_1_Fold_Percentage){
					setBettinStatus(BettingStatus.Folded);
					myBet= 0;
				}
				else if(randomInt <= ror_1_Fold_Percentage + ror_1_Call_Percentage
						|| costToCall >= chips){
					setBettinStatus(BettingStatus.Called);
					myBet= betAdjustedForChipstack(costToCall);
				}
				else{
					setBettinStatus(BettingStatus.Raised);
					//Returns a random raise between 40% of the pot to 100% of the pot
					double amountToRaise = 0.2 * totalPot * raiseFactor();
					//amountToRaise needs to be cast back to an integer
					myBet= betAdjustedForChipstack(costToCall +
							(int)amountToRaise);
				}
			}
			else if(RoR < 1.2){ //Fold 50% of the time. A RoR of between 1 and 1.2 means this will
				//show a slight profit but since it is very borderline and this player type is
				//a Tight player then we will fold 50% of the time
				if(randomInt <= ror_1_2_Fold_Percentage){
					setBettinStatus(BettingStatus.Folded);
					myBet= 0;
				}
				else if(randomInt<=ror_1_2_Fold_Percentage+ror_1_2_Call_Percentage||
						costToCall>=chips){
					setBettinStatus(BettingStatus.Called);
					myBet= betAdjustedForChipstack(costToCall);
				}
				else{
					setBettinStatus(BettingStatus.Raised);
					//Returns a random raise between 40% of the pot to 100% of the pot
					double amountToRaise = 0.2 * totalPot * raiseFactor();
					//amountToRaise needs to be cast back to an integer
					myBet= betAdjustedForChipstack(costToCall +
							(int)amountToRaise);
				}
			}
			else if(RoR < 1.4){
				if(randomInt <= ror_1_4_Fold_Percentage){
					setBettinStatus(BettingStatus.Folded);
					myBet= 0;
				}
				else if(randomInt<=ror_1_4_Fold_Percentage+ror_1_4_Call_Percentage||
						costToCall>=chips){
					setBettinStatus(BettingStatus.Called);
					myBet= betAdjustedForChipstack(costToCall);
				}
				else{
					setBettinStatus(BettingStatus.Raised);
					//Returns a random raise between 40% of the pot to 100% of the pot
					double amountToRaise = 0.2 * totalPot * raiseFactor();
					//amountToRaise needs to be cast back to an integer
					myBet= betAdjustedForChipstack(costToCall +
							(int)amountToRaise);
				}
			}
			else{ //RoR equal to 1.4 or more. A RoR of greater than 1.4 shows a more
				//significant profit so we don't want to fold these hands.
				if(randomInt <= ror_Over_1_4_Fold_Percentage){
					setBettinStatus(BettingStatus.Folded);
					myBet= 0;
				}
				else
					if(randomInt<=ror_Over_1_4_Fold_Percentage+ror_Over_1_4_Call_Percentage||costToCall>=chips){
						setBettinStatus(BettingStatus.Called);
						myBet= betAdjustedForChipstack(costToCall);
					}
					else{
						setBettinStatus(BettingStatus.Raised);
						//Returns a random raise between 40% of the pot to 100% of the pot
						double amountToRaise = 0.2 * totalPot * raiseFactor();
						//amountToRaise needs to be cast back to an integer
						myBet= betAdjustedForChipstack(costToCall +
								(int)amountToRaise);
					}
			}
		}
		else{ //Cost to call = 0
			//We cant use getFCRDecision here because is the cost to is zero and this would result in a division
			//by zero
			myBet = checkOrRaise(noOfPlayers, playersLeftToAct, totalPot, raiseCount);
		}
		return myBet;
	}//end getFCRDecision()
	//Used when the cost to call is zero
	protected int checkOrRaise(int noOfPlayers, int playersLeftToAct, int totalPot, int
			raiseCount){
		//rR: Raise requirement
		double rR = getRaiseRequirement(noOfPlayers-1, playersLeftToAct, raiseCount);
		double myHandRankPercentage = getHandRankPercentage(playerHand);
		int bet;
		if(myHandRankPercentage >= rR){
			if(raiseCallRandomizer() == true){
				setBettinStatus(BettingStatus.Raised);
				//Returns a random raise between 40% of the pot to 100% of the pot
				double amountToRaise = 0.25 * totalPot * raiseFactor();
				//amountToRaise needs to be cast back to an integer
				bet= betAdjustedForChipstack(costToCall + (int)amountToRaise);
			}
			else{
				setBettinStatus(BettingStatus.Checked);
				bet= costToCall;
			}
		}
		else{
			Random myRandom = new Random(59580427);
			int randomInt = myRandom.nextInt(100);
			if(randomInt < bluffFrequency){
				setBettinStatus(BettingStatus.Raised);
				//Returns a random raise between 40% of the pot to 100% of the pot
				double amountToRaise = 0.2 * totalPot * raiseFactor();
				//amountToRaise needs to be cast back to an integer
				bet= betAdjustedForChipstack(costToCall + (int)amountToRaise);
			}
			else{
				setBettinStatus(BettingStatus.Checked);
				bet= costToCall;
			}
		}
		return bet;
	}
	//returns the strength of hand required tomake a raise
	protected double getRaiseRequirement(int noOfOtherPlayers, int playersLeftToAct, int
			noOfRaises){
		//player left to act are counted twice as we require a stronger hand against a player who
		//hasn't acted yet
		double otherPlayersPenalty = noOfOtherPlayers + playersLeftToAct;
		double newRaiseRequirement = postFlopRaiseRequirement/otherPlayersPenalty;
		if(noOfRaises >=2){
			postFlopRaiseRequirement = postFlopRaiseRequirement/(java.lang.Math.pow(2,
					(noOfRaises-1)));
		}
		return 1 - postFlopRaiseRequirement;
	}
	public boolean isHuman(){
		return isHuman;
	}
}