package scoring;

import gameFlow.Card;
import gameFlow.Hand;
import gameFlow.Card.Rank;
import gameFlow.Card.Suit;
import java.lang.Math;

public class HandEvaluator implements Comparable<HandEvaluator>{
	private HandCategory handCategory;
	private Hand hand;
	private double handScore = 0;
	private int handSize = 5;
	private int playerNo;
	//Creates a HandEvaluator object
	public HandEvaluator(Hand pHand, int playerNo) {
		this.hand = pHand;
		this.handCategory = returnHandCategory(pHand );
		this.playerNo = playerNo;
		if(pHand.getHandSize() <= 5){
			this.handSize = pHand.getHandSize();
		}
		else
			this.handSize = 5;
	}
	//Creates a HandEvaluator object
	public HandEvaluator(Hand pHand) {
		this.hand = pHand;
		this.handCategory = returnHandCategory(pHand );
		if(pHand.getHandSize() <= 5){
			this.handSize = pHand.getHandSize();
		}
		else
			this.handSize = 5;
	}
	public HandCategory getHandCategory(){
		return handCategory;
	}
	public double getHandScore(){
		return handScore;
	}
	public int getPlayerNo(){
		return playerNo;
	}

	private HandCategory returnHandCategory( Hand pHand ){
		Card[] myHand = new Card[pHand.getHandSize()];
		for(int i = 0; i <= myHand.length - 1; i++){
			myHand[i] = pHand.getCard(i);
		}
		if(checkForRoyalFlush(myHand) == true){
			return HandCategory.RoyalFlush;
		}
		else if(checkForStraightFlush(myHand) == true){
			return HandCategory.StraightFlush;
		}
		else if(checkForAceLowStraightFlush(myHand) == true){
			return HandCategory.StraightFlushWheel;
		}
		else if(checkForFourOfAKind(myHand) == true){
			return HandCategory.FourOfAKind;
		}
		else if(checkForFullHouse(myHand) == true){
			return HandCategory.FullHouse;
		}
		else if(checkForFlush(myHand) == true){
			return HandCategory.Flush;
		}
		else if(checkForStraight(myHand) == true){
			return HandCategory.Straight;
		}
		else if(checkForAceLowStraight(myHand) == true){
			return HandCategory.Wheel;
		}
		else if(checkForThreeOfAKind(myHand) == true){
			return HandCategory.ThreeOfAKind;
		}
		else if(checkForTwoPair(myHand) == true){
			return HandCategory.TwoPair;
		}
		else if(checkForPair(myHand) == true){
			return HandCategory.Pair;
		}
		else{ //HandCategory == HighCard
			if(myHand.length >= 5){
				//set handScore value
				for(int i = 0; i < 5; i++){
					handScore += (hand.getCard(i).getRankValue() - 1) *
							Math.pow(13, 4-i);
				}
			}
			return HandCategory.HighCard;
		}
	}
	private boolean checkForRoyalFlush(Card[] mHand){
		int decrementCounter =0;// this is used to count the increments e.g K to Q
		for(int i = 0; i < mHand.length - 1; i++){
			//If the next card is 1 rank below and of the same suit
			if(mHand[0].getRank() == Rank.Ace && mHand[i+1].compareTo(mHand[i]) == 1 &&
					mHand[i+1].getSuit() == mHand[i].getSuit()){
				decrementCounter ++;
			}
			if(decrementCounter >= 4){ //Indicates a Royal Flush
				return true;
			}
			if(mHand[i+1].compareTo(mHand[i]) > 1 ||mHand[i+1].getSuit() !=
					mHand[i].getSuit()){
				//If there is a gap of more the one reset and count again
				decrementCounter = 0;
			}
		}
		if(decrementCounter >= 4){ //Indicates a Royal Flush
			return true;
		}
		else
			return false;
	}
	private boolean checkForStraightFlush(Card[] mHand){
		int decrementCounter =0;// this is used to count the increments e.g K to Q
		for(int i = 0; i < mHand.length - 1; i++){
			//If the next card is 1 rank below and of the same suit
			if(mHand[i+1].compareTo(mHand[i]) == 1 && mHand[i+1].getSuit() ==
					mHand[i].getSuit()){
				decrementCounter ++;
			}
			if(decrementCounter == 4){ //Indicates a straight Flush
				//hansScore = 1 for a 6-2 SFlush, 8 for a K-9 SFlush
				handScore = mHand[i+1].getRankValue() -2;
				return true;
			}
			if(mHand[i+1].compareTo(mHand[i]) > 1 ||mHand[i+1].getSuit() !=
					mHand[i].getSuit()){
				//If there is a gap of more the one reset and count again
				decrementCounter = 0;
			}
		}
		return false;
	}
	private boolean checkForFourOfAKind(Card[] mHand){
		int sameCardCounter =0;
		boolean isQuads = false;
		int kicker =0;
		for(int i = 0; i < mHand.length - 1; i++){
			if(mHand[i].compareTo(mHand[i+1]) == 0){
				sameCardCounter ++;
			}
			else{
				if(sameCardCounter >= 3){
					isQuads = true;
				}
				if(sameCardCounter < 3){
					if(kicker ==0){
						kicker = mHand[i].getRankValue() -1;
					}
				}
				sameCardCounter = 0; //reset the counter
			}
		}
		if(sameCardCounter >= 3){
			isQuads = true;
		}
		if(isQuads == true){
			// because the 4th card in the hand must be one of the quads
			handScore = ((mHand[3].getRankValue() -1) * 14) + kicker;
		}
		return isQuads;
	}
	private boolean checkForFullHouse(Card[] mHand){
		int sameCardCounter =0;
		int pairCounter =0;
		int pairValue = 0;
		int tripsCounter =0;
		int tripsValue = 0;
		for(int i = 0; i < mHand.length - 1; i++){
			if(mHand[i].compareTo(mHand[i+1]) == 0){
				sameCardCounter ++;
			}
			else{
				if(sameCardCounter == 1){
					pairCounter++;
					if (pairValue == 0){//ensure we don't overwrite a previously found pair
						pairValue = mHand[i].getRankValue() - 1;
					}
				}
				if(sameCardCounter == 2){
					tripsCounter++;
					if (tripsValue == 0){//ensure we don't overwrite a previously found trips
						tripsValue = mHand[i].getRankValue() - 1;
					}
				}
				sameCardCounter = 0; //reset the counter
			}
		}
		if(sameCardCounter == 1){ //The last 2 cards of the hand were the same
			pairCounter++;
			if (pairValue == 0){//ensure we don't overwrite a previously found pair
				//finds the value of the last pair
				pairValue = mHand[mHand.length - 1].getRankValue() - 1;
			}
		}
		if(sameCardCounter == 2){ //The last 3 cards of the hand were the same
			tripsCounter++;
			if (tripsValue == 0){//ensure we don't overwrite a previously found trips
				tripsValue = mHand[mHand.length - 1].getRankValue() - 1;
			}
		}
		if(tripsCounter >= 1 && pairCounter >= 1){
			handScore = (tripsValue *14) + pairValue;
			return true;
		}
		return false;
	}
	/* private boolean checkForFlush(Card[] mHand){
		int clubCounter =0;
		int spadeCounter =0;
		int diamondCounter =0;
		int heartCounter =0;
		int flushValue[] = {0,0,0,0,0};
		int flushCounter = 0;
		for(int i = 0; i < mHand.length; i++){
		if(mHand[i].getSuit() == Suit.Clubs){
		clubCounter ++;
		}
		if(mHand[i].getSuit() == Suit.Diamonds){
		diamondCounter ++;
		}
		if(mHand[i].getSuit() == Suit.Hearts){
		heartCounter ++;
		}
		if(mHand[i].getSuit() == Suit.Spades){
		spadeCounter ++;
		}
		if(clubCounter >= 5 || diamondCounter >= 5 || spadeCounter >= 5 ||
		heartCounter >= 5){ //Indicates a flush
		for(int j = 0; j < mHand.length; j++){//gets the ranks of the flush
		if(mHand[j].getSuit()== mHand[i].getSuit() && flushCounter <
		5){
		flushValue[flushCounter] = mHand[j].getRankValue()
		-1;
		flushCounter++;
		}
		}
		handScore = (flushValue[0] * Math.pow(14, 4)) + (flushValue[1] *
		Math.pow(14, 3)) +
		(flushValue[2] * Math.pow(14, 2)) + (flushValue[3] * 14) +
		flushValue[4];
		return true;
		}
		}
		return false;
		}*/
	private boolean checkForFlush(Card[] mHand){
		int clubCounter =0;
		int spadeCounter =0;
		int diamondCounter =0;
		int heartCounter =0;
		int flushValue[] = {0,0,0,0,0};
		int flushCounter = 0;
		Suit flushSuit;
		for(int i = 0; i < mHand.length; i++){
			if(mHand[i].getSuit() == Suit.Clubs){
				clubCounter ++;
			}
			else if(mHand[i].getSuit() == Suit.Diamonds){
				diamondCounter ++;
			}
			else if(mHand[i].getSuit() == Suit.Hearts){
				heartCounter ++;
			}
			else{
				spadeCounter ++;
			}
		}
		if(clubCounter >= 5){
			flushSuit = Suit.Clubs;
		}
		else if(diamondCounter >= 5){
			flushSuit = Suit.Diamonds;
		}
		else if(heartCounter >= 5){
			flushSuit = Suit.Hearts;
		}
		else if(spadeCounter >= 5){
			flushSuit = Suit.Spades;
		}
		else{//Not a flush
			return false;
		}
		//Is a Flush
		for(int i =0; i<mHand.length; i++){//gets the ranks of the flush
			if(mHand[i].getSuit()== flushSuit && flushCounter < 5){
				flushValue[flushCounter] = mHand[i].getRankValue() -1;
				flushCounter++;
			}
		}
		handScore = (flushValue[0] * Math.pow(14, 4)) + (flushValue[1] * Math.pow(14, 3)) +
				(flushValue[2] * Math.pow(14, 2)) + (flushValue[3] * 14) + flushValue[4];
		return true;
	}
	private boolean checkForStraight(Card[] mHand){
		int decrementCounter =0;// this is used to count the increments e.g K to Q
		for(int i = 0; i < mHand.length - 1; i++){
			if(mHand[i+1].compareTo(mHand[i]) == 1){
				decrementCounter ++;
			}
			if(decrementCounter == 4){ //Indicates a straight
				handScore = mHand[i-3].getRankValue() - 1;//The value of top card of straight
				return true;
			}
			if(mHand[i+1].compareTo(mHand[i]) > 1){
				//If there is a gap of more the one reset and count again
				decrementCounter = 0;
			}
		}
		return false;
	}
	private boolean checkForThreeOfAKind(Card[] mHand){
		boolean isTrips = false;
		int tripsValue = 0;
		//Kickers to be used as a tiebreaker when comparing pairs of the same rank
		int kicker[] = {0, 0}; //Default kicker values set to zero
		int kickerIndex = 0;
		for(int i = 0; i < mHand.length - 1; i++){
			if(mHand[i].compareTo(mHand[i+1]) == 0){
				for(int j = i+ 2; j < mHand.length; j++){
					if(mHand[i].compareTo(mHand[j]) == 0){
						tripsValue = mHand[i].getRankValue() - 1;
						isTrips = true;
						i+=2; // increment by 2
					}
				}
			}
			else{
				if(kickerIndex <= 1){ //We are only interested in the first 2 kickers
					kicker[kickerIndex] = mHand[i].getRankValue() - 1;
					kickerIndex++;
				}
			}
		}
		if(isTrips == true){
			handScore = (tripsValue * Math.pow(14, 2)) + (kicker[0] * Math.pow(14, 1))
					+ kicker[1];
		}
		return isTrips;
	}
	private boolean checkForTwoPair(Card[] mHand){
		boolean is2Pair = false;
		int pairValue[] = {0, 0};
		int pairIndex = 0;
		int kicker = 0;
		int pairCount =0;
		for(int i = 0; i < mHand.length - 1; i++){
			if((mHand[i].compareTo(mHand[i+1]) == 0) && pairCount < 2){
				pairValue[pairIndex] = mHand[i].getRankValue() - 1;
				pairCount ++;
				pairIndex++;
				i++; //skips ahead to the next 2 unread cards
			}
			else{
				if (kicker == 0){ //check that we haven't already set the kicker
					kicker = mHand[i].getRankValue() - 1;
				}
			}
			if(pairCount == 2){
				is2Pair = true;
			}
		}
		if(is2Pair == true){
			handScore = (pairValue[0] * Math.pow(14, 2)) + (pairValue[1] * Math.pow(14,
					1)) + kicker;
		}
		return is2Pair;
	}
	private boolean checkForPair(Card[] mHand){
		boolean isPair = false;
		//The rank of the pair
		int pairValue = 0;
		//Kickers to be used as a tiebreaker when comparing pairs of the same rank
		int kicker[] = {0, 0, 0}; //Default kicker values set to zero
		int kickerIndex =0;
		for(int i = 0; i < mHand.length - 1; i++){
			if(mHand[i].compareTo(mHand[i+1]) == 0){
				pairValue = mHand[i].getRankValue() - 1;
				isPair = true;
				i++; //we want to skip 2 cards next iteration
			}
			else{
				if(kickerIndex <= 2){ //We are only interested in the first 3 kickers
					kicker[kickerIndex] = mHand[i].getRankValue() - 1;
					kickerIndex++;
				}
			}
		}
		if(isPair == true){
			handScore = (pairValue * Math.pow(14, 3)) + (kicker[0] * Math.pow(14, 2))
					+ (kicker[1] * Math.pow(14, 1)) + kicker[2];
		}
		return isPair;
	}
	//Checks for A2345 straight
	private boolean checkForAceLowStraight(Card[] mHand){
		//Check the the hand contains an ace and a 2
		if(mHand[0].getRankValue() != 14 || mHand[mHand.length-1].getRankValue() !=2){
			return false;
		}
		int incrementCounter =0;// this is used to count the increments e.g 2 to 3
		for(int i = mHand.length - 1; i > 0; i--){
			if(mHand[i].compareTo(mHand[i-1]) == 1){
				incrementCounter ++;
			}
			if(incrementCounter == 3){ //Indicates a A2345 straight
				return true;
			}
			if(mHand[i].compareTo(mHand[i-1]) > 1){
				return false;
			}
		}
		return false;
	}
	private boolean checkForAceLowStraightFlush(Card[] mHand){
		//Check the the hand contains an ace and a 2
		if(mHand[0].getRankValue() != 14 || mHand[mHand.length-1].getRankValue() !=2){
			return false;
		}
		int incrementCounter =0;// this is used to count the increments e.g 2 to 3
		for(int i = mHand.length - 1; i > 0; i--){
			if(mHand[i].compareTo(mHand[i-1]) == 1 && mHand[i].getSuit() == mHand[i-
			                                                                      1].getSuit()){
				incrementCounter ++;
			}
			if(incrementCounter == 3){ //Indicates a A2345 straight
				return true;
			}
			if(mHand[i].compareTo(mHand[i-1]) > 1 || mHand[i].getSuit() != mHand[i-
			                                                                     1].getSuit()){
				return false;
			}
		}
		return false;
	}
	//public
	//Compares two different hands
	@Override
	public int compareTo(HandEvaluator o) {
		int categoryGap; //the difference between the 2 categories of hands
		double handScoreGap;
		categoryGap = o.getHandCategory().ordinal() - this.handCategory.ordinal();
		if (categoryGap == 0){
			handScoreGap = o.getHandScore() - this.handScore;
			return (int)handScoreGap; //convert to integer so we can return it
		}
		else
			return categoryGap;
	}
	//Returns just the difference in category, doesn't take hand score into account
	public int compareCategory(HandEvaluator o) {
		int categoryGap; //the difference between the 2 categories of hands
		categoryGap = o.getHandCategory().ordinal() - this.handCategory.ordinal();
		return categoryGap;
	}
}