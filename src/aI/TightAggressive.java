package aI;

public class TightAggressive extends Player_AI{
	//constructor
	public TightAggressive(String name, int chips){
		super(name, chips);
		this.startingHandRange =10;
		this.speculativeHandRange = 8;
		this.preFlopAgression = 75; //player raises 75% of the time & calls 25%
		this.positionalAwareness = 1;
		this.postFlopAgression = 75;
		this.bluffFrequency = 10;
		/*postFlopRaiseRequirement is the default value to used to determine the range of
hands required to
		 *raise, a value of 8 means this player will raise with the top 8% of hands (before
other factors
		 *such as raise number of players etc is taking into account).*/
		this.postFlopRaiseRequirement = 8;
		//Fold/Call/Raise Percentages given a Rate of Return(RoR) of less than 1
		this.ror_1_Fold_Percentage = 95; //Fold 95%
		this.ror_1_Call_Percentage = 0; //Call 0%, Raise 5%(bluff)
		/*No need for stating Raise Percentage as it will simply be 100% - (Fold & Call)
percentages*/
		//Fold/Call/Raise Percentages given a RoR of between 1 and 1.2
		this.ror_1_2_Fold_Percentage = 0;
		this.ror_1_2_Call_Percentage = 50;
		//Fold/Call/Raise Percentages given a RoR of between 1.2 and 1.4
		this.ror_1_4_Fold_Percentage = 0;
		this.ror_1_4_Call_Percentage = 25;
		//Fold/Call/Raise Percentages given a RoR of 1.4 and over
		this.ror_Over_1_4_Fold_Percentage = 0;
		this.ror_Over_1_4_Call_Percentage = 10;
	}
}