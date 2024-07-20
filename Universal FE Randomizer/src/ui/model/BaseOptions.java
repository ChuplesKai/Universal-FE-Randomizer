package ui.model;

public class BaseOptions
{	
	public enum Mode
	{
		REDISTRIBUTE, DELTA, HYBRID
	}
	
	public final Mode mode;
	
	public final int variance;
	
	public final boolean adjustSTRMAGByClass;
	
	public BaseOptions(Mode mode, int variance, boolean adjustSTRMAGByClass)
	{
		super();
		this.mode = mode;
		this.variance = variance;
		this.adjustSTRMAGByClass = adjustSTRMAGByClass;
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	public static String getModeDescription( Mode mode )
	{
		switch(mode)
		{
		case REDISTRIBUTE:
			return "Randomly redistrubtes a character's base stats, using the variance to randomly adjust the character's base stat total.";
		case DELTA:
			return "Applies a random delta (positive or negative) within the variance to each base stat.";
		case HYBRID:
			return "Hybridizes/Averages a random redistribution of the character's base stats with their original bases.";
		default:
			return "Unknown Bases Mode Selected.";
		}
	}

}
