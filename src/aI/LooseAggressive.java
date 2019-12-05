package aI;

public class LooseAggressive extends Player_AI{
	//constuctor
	public LooseAggressive(String name, int chips){
		//startingHands = 100, preFlopAgression = 0, positionalAwareness =0
		super(name, chips);
		this.startingHandRange =4;
		this.speculativeHandRange = 3;
		this.preFlopAgression = 75; //player raises 75% of the time & calls 25%
		this.postFlopAgression = 75;
		this.postFlopRaiseRequirement = 32; // Higher = looser
		this.bluffFrequency = 30;
		this.positionalAwareness = 1.5;
		//Fold/Call/Raise Percentages given a Rate of Return(RoR) of less than 1
		this.ror_1_Fold_Percentage = 50; //Fold 50%
		this.ror_1_Call_Percentage = 0; //Call 0%, Raise 50%(bluff)
		/*No need for stating Raise Percentage as it will simply be 100% - (Fold & Call)
percentages*/
		//Fold/Call/Raise Percentages given a RoR of between 1 and 1.2
		this.ror_1_2_Fold_Percentage = 0;
		this.ror_1_2_Call_Percentage = 40;
		//Fold/Call/Raise Percentages given a RoR of between 1.2 and 1.4
		this.ror_1_4_Fold_Percentage = 0;
		this.ror_1_4_Call_Percentage = 33;
		//Fold/Call/Raise Percentages given a RoR of 1.4 and over
		this.ror_Over_1_4_Fold_Percentage = 0;
		this.ror_Over_1_4_Call_Percentage = 20;
	}
}