package fedata.gba;

import fedata.gba.general.WeaponType;
import util.YuneUtil;

/**
 * Shell class for loading custom weapon information
 */
public class GBAFEWeaponDto
{
    public String targetName;
    public String newDisplayName;
    public String icon;
    public WeaponType newType;
    public int newMt;
    public int newWt;
    public int newHit;
    public int newCrt;
    public int newMinRange;
    public int newMaxRange;
    public int newDurability;
    public int animation;
    public int flash;

    /*****************************************************************
     * Default Constructor - uninitialized
     ****************************************************************/
    public GBAFEWeaponDto()
    {
        targetName = "UNINITIALIZED";
        newDisplayName = "";
        icon = "";
        newType = WeaponType.NOT_A_WEAPON;
        newMt = 0;
        newWt = 0;
        newHit = 0;
        newCrt = 0;
        newMinRange = 0;
        newMaxRange = 0;
        newDurability = 0;
        animation = 0;
        flash = 0;
    }

    /*****************************************************************
     * Parameterized Constructor
     ****************************************************************/
    public GBAFEWeaponDto( String targetName, String newDisplayName, String icon, WeaponType newType, int newMt, int newWt, int newHit, int newCrt, int newMinRange, int newMaxRange, int newDurability, int animation, int flash )
    {
        this.targetName = targetName;
        this.newDisplayName = newDisplayName;
        this.icon = icon;
        this.newType = newType;
        this.newMt = newMt;
        this.newWt = newWt;
        this.newHit = newHit;
        this.newCrt = newCrt;
        this.newMinRange = newMinRange;
        this.newMaxRange = newMaxRange;
        this.newDurability = newDurability;
        this.animation = animation;
        this.flash = flash;
    }

    /*****************************************************************
     * Copy Constructor
     ****************************************************************/
    public GBAFEWeaponDto( GBAFEWeaponDto other )
    {
        this.targetName = other.targetName;
        this.newDisplayName = other.newDisplayName;
        this.icon = other.icon;
        this.newType = other.newType;
        this.newMt = other.newMt;
        this.newWt = other.newWt;
        this.newHit = other.newHit;
        this.newCrt = other.newCrt;
        this.newMinRange = other.newMinRange;
        this.newMaxRange = other.newMaxRange;
        this.newDurability = other.newDurability;
        this.animation = other.animation;
        this.flash = other.flash;
    }

    /*****************************************************************
     * output string function
     ****************************************************************/
    public String toString()
    {
        return String.format( "%s -> %s\nMt.%d  Wt.%d  Hit.%d  Crt.%d  Dur.%d  Rng(%d-%d)", targetName, newDisplayName, newMt, newWt, newHit, newCrt, newDurability, newMinRange, newMaxRange );
    }
}
