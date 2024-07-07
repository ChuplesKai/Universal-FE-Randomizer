package ui.model;

import random.general.FERandom;

/**
 * Options Model for Global Randomization options
 */
public class DistributionOptions 
{
    protected final FERandom.randDist distribution;
    protected final boolean useMemory;
    protected final String seed;

    /*****************************************************************
     * 
     ****************************************************************/
    public DistributionOptions(FERandom.randDist distribution, boolean useMemory, String seed)
    {
        this.distribution = distribution;
        this.useMemory = useMemory;
        this.seed = seed;
    }
    
    /*****************************************************************
     * 
     ****************************************************************/
    public FERandom.randDist getDistribution()
    {
        return distribution;
    }

    /*****************************************************************
     * 
     ****************************************************************/
    public boolean usingMemory()
    {
        return useMemory;
    }

    /*****************************************************************
     * 
     ****************************************************************/
    public String getSeed()
    {
        return seed;
    }

}
