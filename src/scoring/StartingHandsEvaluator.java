package scoring;

import gameFlow.Card;
import gameFlow.Hand;
import gameFlow.Card.Rank;
/*I'm using the Chen formula to rank starting hands.
 * ref: http://www.thepokerbank.com/strategy/basic/starting-hand-selection/chen-formula/
 */
public class StartingHandsEvaluator {
	private Card holeCard1;
	private Card holeCard2;
	private int chenHandValue;
	private double myHandValue;
	private double speculativeValue;
	private boolean isPair;
	private boolean isSuited;
	private int cardGap;
	double mValue;
	public StartingHandsEvaluator(Hand holeCards, double m){
		holeCard1 = holeCards.getCard(0);
		holeCard2 = holeCards.getCard(1);
		isPair = checkForPair(holeCard1, holeCard2);
		isSuited = checkIfSuited(holeCard1, holeCard2);
		cardGap = getCardGap(holeCard1, holeCard2);
		chenHandValue = calculateChenScore(holeCard1, holeCard2);
		myHandValue = calculateScore(holeCard1, holeCard2);
		speculativeValue = calculateSpeculativeScore(holeCard1, holeCard2);
		mValue = m;
	}
	private boolean checkForPair(Card hc1, Card hc2){
		if(hc1.getRankValue() == hc2.getRankValue()){
			return true;
		}
		else
			return false;
	}
	private boolean checkIfSuited(Card hc1, Card hc2){
		if(hc1.getSuit() == hc2.getSuit()){
			return true;
		}
		else
			return false;
	}
	private int getCardGap(Card hc1, Card hc2){
		if(hc1.getRank() == Rank.Ace && hc2.getRankValue() <= 5){
			return hc2.getRankValue() -2;
		}
		else
			return hc1.getRankValue() - hc2.getRankValue() - 1;
	}
	//Tight aggressive player raise: early >=10, mid >=8, late >=6,
	private double calculateScore(Card hc1, Card hc2){
		//Step 1: Score your highest card only
		double handValue;
		if(hc1.getRank() == Rank.Ace)
			handValue = 10;
		else if(hc1.getRank() == Rank.King)
			handValue = 8;
		else if(hc1.getRank() == Rank.Queen)
			handValue = 7;
		else if(hc1.getRank() == Rank.Jack)
			handValue = 6;
		else{
			//divide by 2 and add remainder
			float rankValue = hc1.getRankValue();
			handValue = rankValue/2;
		}
		//Step 2: Multiply pairs by 2 of one card's value
		if(isPair == true){
			handValue = handValue*2;
			//minimum score for a pair is 5
			if(handValue < 5){
				handValue = 5;
			}
		}
		//Step 3: Add 1 points if cards are suited.
		if(isSuited == true){
			handValue = handValue + 1;
		}
		//Step 4: Subtract points if their is a gap between the two cards.
		if(cardGap == 1){
			handValue = handValue - 1;
		}
		if(cardGap == 2){
			handValue = handValue - 2;
		}
		if(cardGap == 3){
			handValue = handValue - 4;
		}
		if(cardGap >= 4){
			handValue = handValue - 5;
		}
		//Step 5: Add 0.8 points if there is a 0 or 1 card gap and both cards are lower than a Q.
		if((cardGap == 0 || cardGap == 1) && hc1.getRankValue() < 12){
			handValue = handValue + 0.8;
		}
		return handValue;
	}
	
	// The Chen formula is a system for scoring different starting hands in Texas Hold’em.
	public int getStartingChenScore(){
		return chenHandValue;
	}
	private int calculateChenScore(Card hc1, Card hc2){
		//Step 1: Score your highest card only
		int handValue;
		if(hc1.getRank() == Rank.Ace)
			handValue = 10;
		if(hc1.getRank() == Rank.King)
			handValue = 8;
		if(hc1.getRank() == Rank.Queen)
			handValue = 7;
		if(hc1.getRank() == Rank.Jack)
			handValue = 6;
		else{
			//divide by 2 and add remainder
			handValue = (hc1.getRankValue()/2) + (hc1.getRankValue()%2);
		}
		//Step 2: Multiply pairs by 2 of one card's value
		if(isPair == true){
			handValue = handValue*2;
			//minimum score for a pair is 5
			if(handValue < 5){
				handValue = 5;
			}
		}
		//Step 3: Add 2 points if cards are suited.
		if(isSuited == true){
			handValue = handValue + 2;
		}
		//Step 4: Subtract points if their is a gap between the two cards.
		if(cardGap == 1){
			handValue = handValue - 1;
		}
		if(cardGap == 2){
			handValue = handValue - 2;
		}
		if(cardGap == 3){
			handValue = handValue - 4;
		}
		if(cardGap >= 4){
			handValue = handValue - 5;
		}
		//Step 5: Add 1 point if there is a 0 or 1 card gap and both cards are lower than a Q.
		if((cardGap == 0 || cardGap == 1) && hc1.getRankValue() < 12){
			handValue = handValue + 1;
		}
		return handValue;
	}
	//Speculative hands are hands that aren't initially that strong but have the potential
	//to become very strong hands e.g. suited connectors, small pairs etc
	private double calculateSpeculativeScore(Card hc1, Card hc2){
		double handValue;
		//Step 1: Add points for an ace
		if(hc1.getRank() == Rank.Ace)
			handValue = 3;
		else{
			handValue = (hc1.getRankValue()/6);
		}
		//Step 2: Pairs
		if(isPair == true){
			handValue = handValue + 5;
		}
		//Step 3: Add 4 points if cards are suited.
		if(isSuited == true){
			handValue = handValue + 4;
		}
		//Step 4: Add points for connectors.
		if(cardGap == 0){
			//JT to 54
			if(hc1.getRankValue() <= 11 && hc1.getRankValue() >= 5){
				handValue = handValue + 4;
			}
			//QJ or 43
			if(hc1.getRankValue() == 12 || hc1.getRankValue() == 4){
				handValue = handValue + 3;
			}
			//KQ or 32
			if(hc1.getRankValue() == 13 || hc1.getRankValue() == 3){
				handValue = handValue + 2;
			}
			//AK or A2
			if(hc1.getRankValue() == 14 || (hc1.getRankValue() == 14 &&
					hc2.getRankValue() == 2)){
				handValue = handValue + 1;
			}
		}
		if(cardGap == 1){
			//QT to 53
			if(hc1.getRankValue() <= 12 && hc1.getRankValue() >= 5){
				handValue = handValue + 3;
			}
			//KJ or 42
			if(hc1.getRankValue() == 13 || hc1.getRankValue() == 4){
				handValue = handValue + 2;
			}
			//AQ or A3
			if(hc1.getRankValue() == 14 || (hc1.getRankValue() == 14 &&
					hc2.getRankValue() == 3)){
				handValue = handValue + 1;
			}
		}
		if(cardGap == 2){
			handValue = handValue + 2;
			//KT to 42
			if(hc1.getRankValue() <= 13 && hc1.getRankValue() >= 4){
				handValue = handValue + 2;
			}
			else
				handValue = handValue + 1;
		}
		if(cardGap == 3){
			handValue = handValue + 1;
		}
		//Step 5: Add 0.8 points if there is a 0 or 1 card gap and both cards are lower than a Q.
		if((cardGap == 0 || cardGap == 1) && hc1.getRankValue() < 12){
			handValue = handValue + 0.8;
		}
		if(mValue >= 20){
			return handValue;
		}
		else{//If our M is below 20 then we need to reduce the score
			return handValue * mValue/20;
		}
	}
	public double getStartingRaiseScore(){
		return myHandValue;
	}
	public double getSpeculativeScore(){
		return speculativeValue;
	}
}