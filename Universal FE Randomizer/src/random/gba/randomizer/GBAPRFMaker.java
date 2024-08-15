package random.gba.randomizer;

import fedata.gba.*;
import fedata.gba.general.WeaponType;
import fedata.gba.fe6.*;
import fedata.gba.fe7.*;
import fedata.gba.fe8.*;
import random.gba.loader.ClassDataLoader;
import util.YuneUtil;

import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GBAPRFMaker
{
    // DB/Map for the names of the various base PRF weapons
    public static Map<WeaponType, String[]> typeMap = createPRFTypeMap();
    private static Map<WeaponType, String[]> createPRFTypeMap()
    {
        Map<WeaponType, String[]> newTypeMap = new HashMap<>();
        newTypeMap.put(WeaponType.SWORD, new String[]{"Foil","Blade","Brand"});
        newTypeMap.put(WeaponType.LANCE, new String[]{"Pike","Spear","Lance"});
        newTypeMap.put(WeaponType.AXE, new String[]{"Cleaver","Handax","Hammer"});
        newTypeMap.put(WeaponType.BOW, new String[]{"Bow","Longbow"});
        newTypeMap.put(WeaponType.ANIMA, new String[]{"Cantrip","Spell"});
        newTypeMap.put(WeaponType.LIGHT, new String[]{"Flare","Scour"});
        newTypeMap.put(WeaponType.DARK, new String[]{"Eclipse","Shard"});
        return newTypeMap;
    }

    // DB/Map for the base stats of these PRF Weapons
    public static Map<String, GBAFEWeaponDto> weaponMap = createPRFBaseMap();
    private static Map<String, GBAFEWeaponDto> createPRFBaseMap()
    {
        Map<String, GBAFEWeaponDto> newWeapMap = new HashMap<>();
        newWeapMap.put("Foil", new GBAFEWeaponDto("Foil","Foil","",WeaponType.NOT_A_WEAPON,4,3,85,10,1,1,45,0,0) ); 
        newWeapMap.put("Blade", new GBAFEWeaponDto("Blade","Blade","",WeaponType.NOT_A_WEAPON,5,6,100,0,1,1,40,0,0) );
        newWeapMap.put("Brand", new GBAFEWeaponDto("Brand","Brand","",WeaponType.NOT_A_WEAPON,9,9,80,0,1,2,35,0,0) );
        newWeapMap.put("Pike", new GBAFEWeaponDto("Pike","Pike","",WeaponType.NOT_A_WEAPON,5,3,85,5,1,1,45,0,0) ); 
        newWeapMap.put("Spear", new GBAFEWeaponDto("Spear","Spear","",WeaponType.NOT_A_WEAPON,5,6,70,0,1,2,40,0,0) );
        newWeapMap.put("Lance", new GBAFEWeaponDto("Lance","Lance","",WeaponType.NOT_A_WEAPON,12,9,70,0,1,1,35,0,0) );
        newWeapMap.put("Cleaver", new GBAFEWeaponDto("Cleaver","Cleaver","",WeaponType.NOT_A_WEAPON,6,6,70,15,1,1,45,0,0) );
        newWeapMap.put("Handax", new GBAFEWeaponDto("Handax","Handax","",WeaponType.NOT_A_WEAPON,6,9,75,0,1,2,40,0,0) );
        newWeapMap.put("Hammer", new GBAFEWeaponDto("Hammer","Hammer","",WeaponType.NOT_A_WEAPON,14,12,65,0,1,1,35,0,0) ); 
        newWeapMap.put("Bow", new GBAFEWeaponDto("Bow","Bow","",WeaponType.NOT_A_WEAPON,4,3,85,0,2,2,45,0,0) );
        newWeapMap.put("Longbow", new GBAFEWeaponDto("Longbow","Longbow","",WeaponType.NOT_A_WEAPON,5,6,70,0,2,3,35,0,0) );
        newWeapMap.put("Cantrip", new GBAFEWeaponDto("Cantrip","Cantrip","",WeaponType.NOT_A_WEAPON,5,3,90,0,1,2,40,0,0) );
        newWeapMap.put("Spell", new GBAFEWeaponDto("Spell","Spell","",WeaponType.NOT_A_WEAPON,8,6,75,10,1,2,35,0,0) ); 
        newWeapMap.put("Flare", new GBAFEWeaponDto("Flare","Flare","",WeaponType.NOT_A_WEAPON,3,3,85,15,1,2,45,0,0) );
        newWeapMap.put("Scour", new GBAFEWeaponDto("Scour","Scour","",WeaponType.NOT_A_WEAPON,5,3,80,20,1,2,40,0,0) ); 
        newWeapMap.put("Eclipse", new GBAFEWeaponDto("Eclipse","Eclipse","",WeaponType.NOT_A_WEAPON,6,3,65,0,1,2,40,0,0) ); 
        newWeapMap.put("Shard", new GBAFEWeaponDto("Shard","Shard","",WeaponType.NOT_A_WEAPON,8,6,60,10,1,3,35,0,0) );
        return newWeapMap;
    }

    // DB/Map for the names of the various base PRF weapons
    public static Map<String, String[]> nameMap = createPRFNameMap();
    private static Map<String, String[]> createPRFNameMap()
    {
        Map<String, String[]> newNameMap = new HashMap<>();
        newNameMap.put("Foil", new String[]{"","","","","Moon Edge","Sun Edge"});
        newNameMap.put("Blade", new String[]{"","","","","Lunar Blade","Solar Blade"});
        newNameMap.put("Brand", new String[]{"","","","","Lunaris","Solaris"});
        newNameMap.put("Pike", new String[]{"","","","","Moon Pike","Sun Pike"});
        newNameMap.put("Spear", new String[]{"","","","","Lunar Spear","Solar Spear"});
        newNameMap.put("Lance", new String[]{"","","","","Moon Lance","Sun Lance"});
        newNameMap.put("Cleaver", new String[]{"","","","","Moon Cleaver","Sun Cleaver"});
        newNameMap.put("Handax", new String[]{"","","","","Nightfall","Daybreak"});
        newNameMap.put("Hammer", new String[]{"","","","","Lunar Hammer","Solar Hammer"});
        newNameMap.put("Bow", new String[]{"","","","","Moonbow","Sunbow"});
        newNameMap.put("Longbow", new String[]{"","","","","Lunar Shot","Solar Shot"});
        newNameMap.put("Cantrip", new String[]{"","","","","Lunar Bolt","Solar Ember"});
        newNameMap.put("Spell", new String[]{"","","","","Night Gale","Dawn Blaze"});
        newNameMap.put("Flare", new String[]{"","","","","Moonflare","Sunflare"});
        newNameMap.put("Scour", new String[]{"","","","","Lunar Scour","Solar Scour"});
        newNameMap.put("Eclipse", new String[]{"","","","","Lunar Eclipse","Solar Eclipse"});
        newNameMap.put("Shard", new String[]{"","","","","Moonshard","Sunshard"});
        return newNameMap;
    }

    // DB/Map for the icon paths for the various base PRF weapons
    public static Map<String, String[]> iconMap = createPRFIconMap();
    private static Map<String, String[]> createPRFIconMap()
    {
        Map<String, String[]> newNameMap = new HashMap<>();
        newNameMap.put("Foil", new String[]{"","","","","MoonBlade.png","SunBlade.png"});
        newNameMap.put("Blade", new String[]{"","","","","MoonBlade.png","SunBlade.png"});
        newNameMap.put("Brand", new String[]{"","","","","MoonBlade.png","SunBlade.png"});
        newNameMap.put("Pike", new String[]{"","","","","MoonSpear.png","SunSpear.png"});
        newNameMap.put("Spear", new String[]{"","","","","MoonSpear.png","SunSpear.png"});
        newNameMap.put("Lance", new String[]{"","","","","MoonSpear.png","SunSpear.png"});
        newNameMap.put("Cleaver", new String[]{"","","","","MoonHammer.png","SunMallet.png"});
        newNameMap.put("Handax", new String[]{"","","","","MoonHammer.png","SunMallet.png"});
        newNameMap.put("Hammer", new String[]{"","","","","MoonHammer.png","SunMallet.png"});
        newNameMap.put("Bow", new String[]{"","","","","MoonShot.png","SunShot.png"});
        newNameMap.put("Longbow", new String[]{"","","","","MoonShot.png","SunShot.png"});
        newNameMap.put("Cantrip", new String[]{"","","","","LunarBolt.png","SolarFlare.png"});
        newNameMap.put("Spell", new String[]{"","","","","LunarBolt.png","SolarFlare.png"});
        newNameMap.put("Flare", new String[]{"","","","","LunarBeam.png","SolarBeam.png"});
        newNameMap.put("Scour", new String[]{"","","","","LunarBeam.png","SolarBeam.png"});
        newNameMap.put("Eclipse", new String[]{"","","","","LunarEclipse.png","SolarEclipse.png"});
        newNameMap.put("Shard", new String[]{"","","","","LunarEclipse.png","SolarEclipse.png"});
        return newNameMap;
    }

    // DB/Map for the Animations for these PRF weapons
    public static Map<String, int[]> animationMap = createPRFAnimationMap();
    private static Map<String, int[]> createPRFAnimationMap()
    {
        Map<String, int[]> newAnimMap = new HashMap<>();
        newAnimMap.put("Foil", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Blade", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Brand", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.BOLTING.value,FE8SpellAnimationCollection.Animation.ELFIRE.value});
        newAnimMap.put("Pike", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Spear", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.JAVELIN.value,FE8SpellAnimationCollection.Animation.JAVELIN.value});
        newAnimMap.put("Lance", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Cleaver", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Handax", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.THROWN_AXE.value,FE8SpellAnimationCollection.Animation.THROWN_AXE.value});
        newAnimMap.put("Hammer", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.NONE2.value,FE8SpellAnimationCollection.Animation.NONE2.value});
        newAnimMap.put("Bow", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.ARROW.value,FE8SpellAnimationCollection.Animation.ARROW.value});
        newAnimMap.put("Longbow", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.ARROW.value,FE8SpellAnimationCollection.Animation.ARROW.value});
        newAnimMap.put("Cantrip", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.THUNDER.value,FE8SpellAnimationCollection.Animation.FIRE.value});
        newAnimMap.put("Spell", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.FIMBULVETR.value,FE8SpellAnimationCollection.Animation.ELFIRE.value});
        newAnimMap.put("Flare", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.LIGHTNING.value,FE8SpellAnimationCollection.Animation.LIGHTNING.value});
        newAnimMap.put("Scour", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.PURGE.value,FE8SpellAnimationCollection.Animation.PURGE.value});
        newAnimMap.put("Eclipse", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.ECLIPSE.value,FE8SpellAnimationCollection.Animation.ECLIPSE.value});
        newAnimMap.put("Shard", new int[]{0,0,0,0,FE8SpellAnimationCollection.Animation.CRIMSON_EYE.value,FE8SpellAnimationCollection.Animation.CRIMSON_EYE.value});
        return newAnimMap;
    }

    // DB/Map for animation flash color on hit for the PRFs
    public static Map<String, int[]> colorMap = createPRFColorMap();
    private static Map<String, int[]> createPRFColorMap()
    {
        Map<String, int[]> newColorMap = new HashMap<>();
        newColorMap.put("Foil", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Blade", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Brand", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.BLUE.value,FE8SpellAnimationCollection.Flash.RED.value});
        newColorMap.put("Pike", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Spear", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Lance", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Cleaver", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Handax", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Hammer", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Bow", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Longbow", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.WHITE.value,FE8SpellAnimationCollection.Flash.WHITE.value});
        newColorMap.put("Cantrip", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.BLUE.value,FE8SpellAnimationCollection.Flash.RED.value});
        newColorMap.put("Spell", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.GREEN.value,FE8SpellAnimationCollection.Flash.RED.value});
        newColorMap.put("Flare", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.BLUE.value,FE8SpellAnimationCollection.Flash.YELLOW.value});
        newColorMap.put("Scour", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.BLUE.value,FE8SpellAnimationCollection.Flash.YELLOW.value});
        newColorMap.put("Eclipse", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.DARK.value,FE8SpellAnimationCollection.Flash.DARK.value});
        newColorMap.put("Shard", new int[]{0,0,0,0,FE8SpellAnimationCollection.Flash.DARK.value,FE8SpellAnimationCollection.Flash.DARK.value});
        return newColorMap;
    }

    /*****************************************************************
     * makePRF - routine to actually stat up and get animation/icon
     *  for the created PRF weapon.
     ****************************************************************/
    public static GBAFEWeaponDto makePRF( GBAFECharacterData character, int charIndex, ClassDataLoader classData, boolean unbreakable, Random rng )
    {
        // Start by getting the types of weapons which should be valid for the character
        GBAFEWeaponDto prfData = null;
        GBAFEClassData charClass = classData.classForID(character.getClassID());
        List<WeaponType> weaponTypes = classData.usableTypesForClass(charClass);
        weaponTypes.remove(WeaponType.STAFF);
        // We'll also use the character's total CON to determine the base weapon
        int constitution = charClass.getCON() + character.getConstitution();
        if (!weaponTypes.isEmpty()) // Other constraints should make this always trigger
        {
            // Get the possible bases we could be looking at, and pick one
            WeaponType selType = weaponTypes.get(rng.nextInt(weaponTypes.size()));
            String[] bases = typeMap.get( selType );
            int selectedBase = 0;
            int selection = rng.nextInt( constitution ) + 1; // Plus 1 since RNG starts at 0
            for( int bIdx = 0; bIdx < bases.length; bIdx++ ) // Find whatever is max Con, default first index
            {
                if( selection >= weaponMap.get( bases[bIdx] ).newWt ) { selectedBase = bIdx; }
            }
            // Copy the information of the selected base, and then update some of the other info
            prfData = new GBAFEWeaponDto( weaponMap.get( bases[selectedBase] ) );
            String prfBaseName = prfData.newDisplayName;
            prfData.newType = selType;
            prfData.icon = "weaponIcons/" + iconMap.get( prfBaseName )[charIndex];
            prfData.animation = animationMap.get( prfBaseName )[charIndex];
            prfData.flash = colorMap.get( prfBaseName )[charIndex];
            // Final step is to copy over the name
            prfData.newDisplayName = nameMap.get( prfBaseName )[charIndex];
        }

        // Next, if it's unbreakable, we're going to power it down just a little
        if( unbreakable )
        {
            prfData.newMt -= 1;
            prfData.newHit -= 5;
            prfData.newCrt = YuneUtil.clamp( prfData.newCrt - 5, 0, 100 );
        }

        // I'll want to do random power-ups here
        int remainingCon = 0;
        int upgradeCheck = 0;
        int[] weaponStatAry;
        do
        {
            // Roll a die to see whether we are getting an upgrade
            remainingCon = constitution - prfData.newWt;
            upgradeCheck = rng.nextInt( remainingCon + 1 ); // Larger gap means more likely to upgrade
            if( upgradeCheck > 0 ) // If we are upgrading
            {
                // We get one of four random upgrades to the weapon
                switch( rng.nextInt( 5 ) )
                {
                case 0: // Make power most likely, since that's straightforwardly good
                case 1: // Power
                    weaponStatAry = new int[]{1,-3,0,0};
                    break;
                case 2: // Sharp
                    weaponStatAry = new int[]{0,5,2,0};
                    break;
                case 3: // Critical
                    if(unbreakable) { weaponStatAry = new int[]{0,0,4,0}; }
                    else { weaponStatAry = new int[]{0,0,5,-2}; }
                    break;
                case 4: // Trusty
                    if(unbreakable) { weaponStatAry = new int[]{0,10,0,0};}
                    else { weaponStatAry = new int[]{0,2,0,5}; }
                    break;
                default: // This shouldn't happen ever
                    weaponStatAry = new int[]{0,0,0,0};
                }
                // Then, actually apply the upgrade
                prfData.newWt += 1; // Always increase the weapon weight by 1 when improving
                prfData.newMt += weaponStatAry[0];
                prfData.newHit += weaponStatAry[1];
                prfData.newCrt += weaponStatAry[2];
                prfData.newDurability += YuneUtil.clamp( weaponStatAry[3], 0, 63 );
            }
        } while ( upgradeCheck != 0 );

        // Finally, return what we've made
        return prfData;
    }

}