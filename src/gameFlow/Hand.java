package gameFlow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Hand implements Iterable<Card>{
	boolean swap = true;
	private ArrayList<Card> myCards;
	private ArrayList<Card> myCardsOriginalOrder;
	public Hand(){
		myCards = new ArrayList<Card>();
		myCardsOriginalOrder = new ArrayList<Card>();
	}
	public void add( Card card )
	{
		myCards.add( card );
		myCardsOriginalOrder.add( card );
		//orders cards from highest rank to lowest i.e Ace highest, Deuce lowest
		Collections.sort(myCards);
	}
	public void clear()
	{
		myCards.clear();
		myCardsOriginalOrder.clear();
	}
	public void displayHand(){
		//iterates through every card
		for( int i =0; i < myCards.size(); i++ )
		{
			System.out.print(myCards.get(i).cardAsString() + " ");
		}
	}
	public int getHandSize()
	{
		return myCards.size();
	}
	public Card getCard(int index){
		return myCards.get(index);
	}
	/*Used when we want to retrieve the cards in the order we originally received them
	 * e.g. If we want to retrieve our original pocket cards*/
	public Card getCardOriginalOrder(int index){
		return myCardsOriginalOrder.get(index);
	}
	@Override
	public Iterator<Card> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}