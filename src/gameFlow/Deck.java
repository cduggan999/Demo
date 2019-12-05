package gameFlow;

import java.util.Collections;
import java.util.Stack;

import gameFlow.Card.*;

public class Deck {
	
	//Creates a stack to hold our 52 cards
	private Stack<Card> deckOfCards;
	
	public Deck(){
		deckOfCards = new Stack<Card>();
		newDeal();		
	}
	
	private void newDeal(){
		deckOfCards.clear();
		
		//iterates through every suit and rank
		for( Suit suitIndex : Suit.values() )
		{   
			for( Rank rankIndex : Rank.values() )
            {
            	//adds all 52 cards to the stack 
            	deckOfCards.add( new Card( rankIndex, suitIndex ));
            }
		}
	}
	
	public void shuffleCards(){
		newDeal();
		Collections.shuffle( deckOfCards );
	}
	
	//Returns a card and removes it from the deck 
	public Card dealCard(){
		return deckOfCards.pop();
	}
	
	public Card getSpecificCard(int index){
		return deckOfCards.get(index);
	}
	
	public int getDeckSize(){
		return deckOfCards.size();
	}
	
	public void removeCard(Card card){
		deckOfCards.remove(card);
	}
	
}
