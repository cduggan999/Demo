package gameFlow;

import java.util.ArrayList;
import aI.Player_AI;
import UserInterface.HoldemGUI;

public class Player{
	protected String name;
	protected int chips;
	protected int cash = 0;
	protected int potContribution = 0; //no of chips contributed to the pot this round
	protected Hand playerHand = new Hand(); //Complete hand
	protected Player_AI playerType;
	protected BettingStatus bettingStatus;
	protected int minRaise =0;
	protected int costToCall = 0;
	protected double m; //Harrington's M zone
	protected int lastBet; //Keeps track of players last bet, used for display purpose
	protected boolean isHuman; // false if this player is a poker bot
	//Represents the actions the player has taken in each betting round
	protected PlayerAction preFlopAction = PlayerAction.NotActed;
	protected PlayerAction flopAction = PlayerAction.NotActed;
	protected PlayerAction turnAction = PlayerAction.NotActed;
	protected PlayerAction riverAction = PlayerAction.NotActed;
	protected int tablePosition;
	PlayerHistory playerHistory = new PlayerHistory();
	//Creates a player object
	public Player(String name, int chips) {
		this.name = name;
		this.chips = chips;
		bettingStatus = BettingStatus.StillBetting;
	}
	public enum BettingStatus{
		Folded, StillBetting, Checked, Called, Raised, Allin
	};
	public String getName() {
		return name;
	}
	public int getChips() {
		return chips;
	}
	public int getCash() {
		return cash;
	}
	public int getPotContribution() {
		return potContribution;
	}
	public void setPotContribution(int newContribution) {
		this.potContribution = newContribution;
	}
	public Hand getPlayerHand() {
		return playerHand;
	}
	public void setPlayerHand(Hand newHand) {
		this.playerHand = newHand;
	}
	public void setChips(int newChipsValue) {
		this.chips = newChipsValue;
	}
	public void setCash(int newCashValue) {
		this.cash = newCashValue;
	}
	public void setBettinStatus(BettingStatus newBetStatus) {
		this.bettingStatus = newBetStatus;
	}
	public Player_AI getPlayerType(){
		return playerType;
	}
	public BettingStatus getBettingStatus() {
		return this.bettingStatus;
	}
	public PlayerAction getPreFlopAction() {
		return preFlopAction;
	}
	public void setPreFlopAction(PlayerAction action) {
		preFlopAction = action;
	}
	public PlayerAction getFlopAction() {
		return flopAction;
	}
	public void setFlopAction(PlayerAction action) {
		flopAction = action;
	}
	public PlayerAction getTurnAction() {
		return turnAction;
	}
	public void setTurnAction(PlayerAction action) {
		turnAction = action;
	}
	public PlayerAction getRiverAction() {
		return riverAction;
	}
	public void setRiverAction(PlayerAction action) {
		riverAction = action;
	}
	public int getTablePosition() {
		return tablePosition;
	}
	public void setTablePosition(int newPos) {
		tablePosition = newPos;
	}
	//Resets all the players actions
	public void clearPlayerActions() {
		preFlopAction = PlayerAction.NotActed;
		flopAction = PlayerAction.NotActed;
		turnAction = PlayerAction.NotActed;
		riverAction = PlayerAction.NotActed;
	}
	/* public int getDecision(int totalPot, int costToCall, int activePlayers, HoldemGUI gui){
return 0;
}*/
	public int getDecision(int totalPot, int costToCall, ArrayList<Player> playerList,
			int playersLeftToAct, BettingRound round, int minRaise, HoldemGUI gui){
		System.out.print("Get Decision");
		return 0;
	}
	//Checks that the player can afford the proposed bet
	protected int betAdjustedForChipstack(int bet){
		if(bet >= chips){
			setBettinStatus(BettingStatus.Allin);
			return chips;
		}
		else
			return bet;
	}
	protected int getActivePlayerCount(ArrayList<Player> playerList){
		int activePlayerCount = 0;
		for(int i=0; i<playerList.size(); i++){
			if(playerList.get(i).bettingStatus != BettingStatus.Allin
					&& playerList.get(i).bettingStatus != BettingStatus.Folded){
				activePlayerCount++;
			}
		}
		return activePlayerCount;
	}
	public boolean isHuman(){
		return isHuman;
	}
	//Set and get the players M values
	public void setM(int totalblindsInPot){
		this.m = (double)chips/(double)totalblindsInPot;
	}
	public double getM(){
		return this.m;
	}
	public int getLastBet(){
		return lastBet;
	}
	//Returns the player action for the betting round, e.g. Check, Re-Raised, 3-Bet etc
	public PlayerAction determinePlayerAction(BettingStatus betStatus, int raiseCount){
		PlayerAction action;
		if(betStatus==BettingStatus.Raised){
			if(raiseCount == 0){
				action = PlayerAction.Raised;
			}
			else if(raiseCount == 1){
				action = PlayerAction.ReRaised;
			}
			else{ // 2 or more raises
				action = PlayerAction.ThreeBet;
			}
		}
		else if(betStatus==BettingStatus.Called){
			if(raiseCount == 0){
				action = PlayerAction.Called;
			}
			else{ //One or more raises
				if(potContribution == 0){
					action = PlayerAction.ColdCalled;
				}
				else{
					action = PlayerAction.CalledRaise;
				}
			}
		}
		else if(betStatus==BettingStatus.Checked){
			action = PlayerAction.Checked;
		}
		else{//The player neither called or raised
			action = PlayerAction.NotActed;
		}
		return action;
	} //end determinePlayerAction()
	public double getPreFlopLooseness(){
		return playerHistory.getPreFlopLooseness();
	}
	public double getPreFlopAggression(){
		return playerHistory.getPreFlopAgression();
	}
}