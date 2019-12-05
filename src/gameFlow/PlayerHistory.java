package gameFlow;

public class PlayerHistory {
	private int preFlopFoldCount = 0;
	private int preFlopCheckCount = 0;
	private int preFlopCallCount = 0;
	private int preFlopRaiseCount = 0;
	private int postFlopFoldCount = 0;
	private int postFlopCheckCount = 0;
	private int postFlopCallCount = 0;
	private int postFlopRaiseCount = 0;
	
	public void incrementPreFlopFoldCount(){
		preFlopFoldCount++;
	}
	public void incrementPreCheckFoldCount(){
		preFlopCheckCount++;
	}
	public void incrementPreFlopCallCount(){
		preFlopCallCount++;
	}
	public void incrementPreFlopRaiseCount(){
		preFlopRaiseCount++;
	}
	public void incrementPostFlopFoldCount(){
		postFlopFoldCount++;
	}
	public void incrementPostCheckFoldCount(){
		postFlopCheckCount++;
	}
	public void incrementPostFlopCallCount(){
		postFlopCallCount++;
	}
	public void incrementPostFlopRaiseCount(){
		postFlopRaiseCount++;
	}
	//gets the looseness of the player, i.e how often they elected to play
	//the hand rather than fold.
	public double getPreFlopLooseness(){
		//We don't include preFlopCheckCount because it doesn't factor into looseness we
		//are only interested in hands the player choose to play when folding was an option
		double totalHandsPlayed = preFlopCallCount + preFlopRaiseCount;
		double totalHands = totalHandsPlayed + preFlopFoldCount;
		return totalHandsPlayed / totalHands;
	}
	public double getPreFlopAgression(){
		double totalHandsPlayed = preFlopCallCount + preFlopRaiseCount + preFlopCheckCount;
		return preFlopRaiseCount / totalHandsPlayed;
	}
}