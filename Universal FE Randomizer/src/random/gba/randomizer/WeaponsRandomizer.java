package random.gba.randomizer;

import java.util.Random;

import fedata.gba.GBAFEItemData;
import fedata.gba.general.WeaponEffects;
import random.gba.loader.ItemDataLoader;
import random.gba.loader.TextLoader;
import random.general.FERandom;
import random.general.WeightedDistributor;
import ui.model.WeaponEffectOptions;
import util.WhyDoesJavaNotHaveThese;

public class WeaponsRandomizer {
	
	static final int rngSalt = 64;
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeMights(int minMT, int maxMT, int variance, ItemDataLoader itemsData, FERandom rng)
	{
		GBAFEItemData[] allWeapons = itemsData.getAllWeapons();
		
		for (GBAFEItemData weapon : allWeapons) {
			int originalMight = weapon.getMight();
			int newMight = originalMight + rng.sample( variance );
			weapon.setMight(WhyDoesJavaNotHaveThese.clamp(newMight, minMT, maxMT));
		}
		
		itemsData.commit();
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeHit(int minHit, int maxHit, int variance, ItemDataLoader itemsData, FERandom rng) {
		GBAFEItemData[] allWeapons = itemsData.getAllWeapons();

		// Hit numbers are high enough and tend to be multiples of 5 to try this check
		int mult = 1;
		if( minHit % 5 == 0 && maxHit % 5 == 0 && variance % 5 == 0 )
		{
			mult = 5;
			variance = variance / 5;
		}
		
		for (GBAFEItemData weapon : allWeapons) {
			int originalHit = weapon.getHit();
			int newHit = originalHit + mult * rng.sample( variance );
			weapon.setHit(WhyDoesJavaNotHaveThese.clamp(newHit, minHit, maxHit));
		}
		
		itemsData.commit();
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeDurability(int minDurability, int maxDurability, int variance, ItemDataLoader itemsData, FERandom rng) {
		GBAFEItemData[] allWeapons = itemsData.getAllWeapons();
		// For each weapon		
		for (GBAFEItemData weapon : allWeapons)
		{
			int originalDurability = weapon.getDurability();
			int durVar = variance;
			int minDur = minDurability;
			// Siege Tomes get special handling
			if (weapon.getMaxRange() > 5) 
			{
				durVar = (int)Math.ceil( (double)variance / 4.0 ); //Drop the variance down a ton
				minDur = 1;
			}
			weapon.setDurability(WhyDoesJavaNotHaveThese.clamp(originalDurability + rng.sample( durVar ), minDur, maxDurability));
		}
		
		itemsData.commit();
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeWeight(int minWT, int maxWT, int variance, ItemDataLoader itemsData, FERandom rng)
	{
		GBAFEItemData[] allWeapons = itemsData.getAllWeapons();
		
		for (GBAFEItemData weapon : allWeapons) {
			int originalWeight = weapon.getWeight();
			int newWeight = originalWeight + rng.sample( variance );
			weapon.setWeight(WhyDoesJavaNotHaveThese.clamp(newWeight, minWT, maxWT));
		}
		
		itemsData.commit();
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeEffects(WeaponEffectOptions effectOptions, ItemDataLoader itemsData, TextLoader textData, Boolean ignoreIronWeapons, Boolean ignoreSteelWeapons, Boolean ignoreThrownWeapons, int effectChance, Random rng)
	{
		GBAFEItemData[] allWeapons = itemsData.getAllWeapons();
		
		WeightedDistributor<WeaponEffects> enabledEffects = new WeightedDistributor<WeaponEffects>();
		
		if (effectOptions.statBoosts > 0) { enabledEffects.addItem(WeaponEffects.STAT_BOOSTS, effectOptions.statBoosts); }
		if (effectOptions.effectiveness > 0) { enabledEffects.addItem(WeaponEffects.EFFECTIVENESS, effectOptions.effectiveness); }
		if (effectOptions.unbreakable > 0) { enabledEffects.addItem(WeaponEffects.UNBREAKABLE, effectOptions.unbreakable); }
		if (effectOptions.brave > 0) { enabledEffects.addItem(WeaponEffects.BRAVE, effectOptions.brave); }
		if (effectOptions.reverseTriangle > 0) { enabledEffects.addItem(WeaponEffects.REVERSE_TRIANGLE, effectOptions.reverseTriangle); }
		if (effectOptions.extendedRange > 0) { enabledEffects.addItem(WeaponEffects.EXTEND_RANGE, effectOptions.extendedRange); }
		if (effectOptions.highCritical > 0) {
			WeaponEffects effect = WeaponEffects.HIGH_CRITICAL;
			effect.additionalInfo.put(WeaponEffects.InfoKeys.CRITICAL_RANGE, effectOptions.criticalRange);
			enabledEffects.addItem(effect, effectOptions.highCritical);
		}
		if (effectOptions.magicDamage > 0) { enabledEffects.addItem(WeaponEffects.MAGIC_DAMAGE, effectOptions.magicDamage); }
		if (effectOptions.poison > 0) { enabledEffects.addItem(WeaponEffects.POISON, effectOptions.poison); }
		if (effectOptions.eclipse > 0) { enabledEffects.addItem(WeaponEffects.HALF_HP, effectOptions.eclipse); }
		if (effectOptions.devil > 0) { enabledEffects.addItem(WeaponEffects.DEVIL, effectOptions.devil); }
		
		for (GBAFEItemData weapon : allWeapons) {
			if (ignoreIronWeapons && itemsData.isBasicWeapon(weapon.getID())) { continue; }
			if (ignoreSteelWeapons && itemsData.isSteelWeapon(weapon.getID())) { continue; }
			if (ignoreThrownWeapons && itemsData.isBasicThrowingWeapon(weapon.getID())) { continue; }
			
			if (rng.nextInt(100) < effectChance) {
				weapon.applyRandomEffect(new WeightedDistributor<WeaponEffects>(enabledEffects), itemsData, textData, itemsData.spellAnimations, rng);
			}
		}
		
		itemsData.commit();
	}
}
