package fedata.gba;

import util.YuneUtil;

/**
 * Shell class for loading custom weapon information
 */
public class GBAFEWeaponDto
{
    public String targetName;
    public String newDisplayName;
    public int newMt;
    public int newWt;
    public int newHit;
    public int newCrt;
    public int newMinRange;
    public int newMaxRange;
    public int newDurability;

    /*****************************************************************
     * Default Constructor - uninitialized
     ****************************************************************/
    public GBAFEWeaponDto()
    {
        targetName = "UNINITIALIZED";
        newDisplayName = "";
        newMt = 0;
        newWt = 0;
        newHit = 0;
        newCrt = 0;
        newMinRange = 0;
        newMaxRange = 0;
        newDurability = 0;
    }

    /*****************************************************************
     * Parameterized Constructor
     ****************************************************************/
    public GBAFEWeaponDto( String targetName, String newDisplayName, int newMt, int newWt, int newHit, int newCrt, int newMinRange, int newMaxRange, int newDurability )
    {
        this.targetName = targetName;
        this.newDisplayName = newDisplayName;
        this.newMt = newMt;
        this.newWt = newWt;
        this.newHit = newHit;
        this.newCrt = newCrt;
        this.newMinRange = newMinRange;
        this.newMaxRange = newMaxRange;
        this.newDurability = newDurability;
    }

    /*****************************************************************
     * output string function
     ****************************************************************/
    public String toString()
    {
        return String.format( "%s -> %s\nMt.%d  Wt.%d  Hit.%d  Crt.%d  Dur.%d  Rng(%d-%d)", targetName, newDisplayName, newMt, newWt, newHit, newCrt, newDurability, newMinRange, newMaxRange );
    }
}
