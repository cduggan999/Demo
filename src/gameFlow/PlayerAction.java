package gameFlow;

public enum PlayerAction {
	NotActed,
	Checked,
	//CalledSmallBlind,
	Called,	//No raises in front
	CalledRaise, //Calls a raise after the player has already contributed chips to the pot
	Raised,
	ColdCalled, //Differs from CalledRaise in that it represents a sum of bets or raises by more than one player
	ReRaised,
	ThreeBet//To be the first player to put in a 3rd unit of betting. For example, if Bob opens for $10, and 
	//Mary raises to make the bet $20, if Ted also raises to make the bet $30, this is to "three bet". (Before 
	//the flop, 3-betting means re-raising the first raiser.)
}
