package gameFlow;

public class Pot{
	private int potAmount;
	private boolean[] eligiblePlayers; //list of which players are eligible for pot
	private int noOfWinningHands =1;//default
	public Pot(){
		potAmount = 0;
	}
	//Creates a pot object
	public Pot(int noOfPlayers) {
		this.eligiblePlayers = new boolean[noOfPlayers];
		for(int i=0; i<=noOfPlayers-1; i++){
			eligiblePlayers[i] = true;
		}
	}
	public Pot(boolean[] list) {
		eligiblePlayers = list;
	}
	public int getPot() {
		return potAmount;
	}
	public void setPot(int newPotAmount) {
		this.potAmount = newPotAmount;
	}
	public boolean getPlayerEligibility(int index){ //returns players eligible for pot
		return eligiblePlayers[index];
	}
	public int getEligiblePlayerCount(){
		int count =0;
		for(int i=0; i<eligiblePlayers.length; i++){
			if(eligiblePlayers[i] == true){
				count++;
			}
		}
		return count;
	}
	public void setPlayerEligibility(int index, boolean newEligibility){ //Sets players eligible for pot
		this.eligiblePlayers[index] = newEligibility;
	}
	public boolean[] getEligibilityList(){ //Sets players eligible for pot
		return eligiblePlayers;
	}
	public int getNoOfWinningHands(){
		return noOfWinningHands;
	}
	public void incrementWinningHands(){
		noOfWinningHands++;
	}
}