package gameFlow;

public class Table {
	
	private int totalPlayers;
	private int buttonPosition;
	
	//Creates a Table object
	public Table(int totalPlayers, int buttonPos) {
        this.totalPlayers = totalPlayers;
        this.buttonPosition = buttonPos;
    }
	
	public int getTotalPlayers() {
		 return totalPlayers;
	 }
	
	public int getButtonPosition() {
		 return buttonPosition;
	 }
	
	//Button needs to move one position clockwise after every hand
	public void incremementButton() {
		if (buttonPosition >= totalPlayers -1){
			buttonPosition = 0;
		}
		else
			buttonPosition++;
	 }

}
