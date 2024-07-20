package ui.model;

public class GrowthOptions 
{
	public enum Mode
	{
		REDISTRIBUTE, DELTA, FULL
	}
	
	public final Mode mode;
	
	public final MinMaxVarOption parameters;
	public final boolean adjustSTRMAGSplit;
	public final boolean adjustHP;
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public GrowthOptions(Mode mode, MinMaxVarOption parameters, boolean adjustHP, boolean adjustSTRMAGSplit)
	{
		super();
		this.mode = mode;
		this.parameters = parameters;
		this.adjustSTRMAGSplit = adjustSTRMAGSplit;
		this.adjustHP = adjustHP;
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	public static String getModeDescription( Mode mode )
	{
		switch(mode)
		{
		case REDISTRIBUTE:
			return "Randomly redistrubtes a character's total growths, using the variance to randomly adjust the total growths of each character.";
		case DELTA:
			return "Applies a random delta (positive or negative) within the variance to each growth.";
		case FULL:
			return "Generates fully random growth rates between the specified minimum and maximum, using the selected distribution.";
		default:
			return "Unknown Growths Mode Selected.";
		}
	}

}
