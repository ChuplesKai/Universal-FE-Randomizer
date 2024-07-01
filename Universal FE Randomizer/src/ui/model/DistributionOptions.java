package ui.model;

import random.general.FERandom;

/**
 * Options Model for Global Randomization options
 */
public class DistributionOptions 
{
    protected final FERandom.randDist distribution;
    protected final boolean useMemory;

    public DistributionOptions(FERandom.randDist distribution, boolean useMemory)
    {
        this.distribution = distribution;
        this.useMemory = useMemory;
    }
    
    public FERandom.randDist getDistribution()
    {
        return distribution;
    }

    public boolean usingMemory()
    {
        return useMemory;
    }
}
