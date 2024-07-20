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
	
	public GrowthOptions(Mode mode, MinMaxVarOption parameters, boolean adjustHP, boolean adjustSTRMAGSplit) {
		super();
		this.mode = mode;
		this.parameters = parameters;
		this.adjustSTRMAGSplit = adjustSTRMAGSplit;
		this.adjustHP = adjustHP;
	}
}
