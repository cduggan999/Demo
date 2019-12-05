package aI;

public class MediumPassive extends TightPassive{
	
	//constructor
	public MediumPassive(String name, int chips){
		super(name, chips);
		
		this.startingHandRange = 6;
		this.speculativeHandRange = 6;
		this.postFlopCallingRange = 0.3;
	}  	
}

