package gameFlow;
//Comparable interface is needed to allow the cards to be sorted
public class Card implements Comparable<Card>{
	private final Rank rank;
	private final Suit suit;
	//Creates a card object
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	//returns the rank and suit of the card
	public Rank getRank() { return rank; }
	public Suit getSuit() { return suit; }
	//Prints the rank and suit of the card
	public String cardAsString() { return rank.getRankName() + suit.getSuitName(); }
	//private static final List<Card> protoDeck = new ArrayList<Card>();
	public enum Rank {
		Two("2"), Three("3"), Four("4"), Five("5"), Six("6"), Seven("7"), Eight("8"),
		Nine("9"), Ten("T"), Jack("J"), Queen("Q"), King("K"), Ace("A");
		private String rankName;
		private Rank(String stringRank) {
			rankName = stringRank;
		}
		public String getRankName() {
			return rankName;
		}
	}
	public enum Suit {
		Spades('s'), Hearts('h'), Clubs('c'), Diamonds('d');
		private char suitName;
		private Suit(char charSuit) {
			suitName = charSuit;
		}
		public char getSuitName() {
			return suitName;
		}
	}
	//Returns the value of the card, e.g Two = 2, Jack = 11, Ace = 14 etc
	public int getRankValue() {
		int x = this.getRank().ordinal()+2;
		return x;
	}
	@Override
	//Returns a negative if the object to be compared to is smaller, if it is larger then returns a positive
	public int compareTo(Card arg0) {
		return arg0.getRankValue() - 2 - this.getRank().ordinal() ;
	}
}