package random.gcnwii.fe9.randomizer;

import java.util.List;
import java.util.Random;

import fedata.gcnwii.fe9.FE9Item;
import io.gcn.GCNISOHandler;
import random.gcnwii.fe9.loader.FE9CommonTextLoader;
import random.gcnwii.fe9.loader.FE9ItemDataLoader;
import random.gcnwii.fe9.loader.FE9ItemDataLoader.WeaponEffect;
import random.gcnwii.fe9.loader.FE9ItemDataLoader.WeaponType;
import random.general.WeightedDistributor;
import ui.model.WeaponEffectOptions;
import util.YuneUtil;

public class FE9WeaponRandomizer {
	
	static final int rngSalt = 92209;
	
	public static void randomizeWeaponMight(int variance, int min, int max, FE9ItemDataLoader itemData, Random rng) {
		if (variance == 0) { return; }
		List<FE9Item> items = itemData.allItems();
		for (FE9Item item : items) {
			if (itemData.isBlacklisted(item)) { continue; }
			if (!itemData.isWeapon(item) || itemData.isStaff(item)) { continue; }
			int factor = rng.nextInt(2) == 0 ? 1 : -1;
			item.setItemMight(YuneUtil.clamp(item.getItemMight() + (rng.nextInt(variance + 1) * factor), min, max));
			item.commitChanges();
		}
	}
	
	public static void randomizeWeaponAccuracy(int variance, int min, int max, FE9ItemDataLoader itemData, Random rng) {
		if (variance == 0) { return; }
		List<FE9Item> items = itemData.allItems();
		for (FE9Item item : items) {
			if (itemData.isBlacklisted(item)) { continue; }
			if (!itemData.isWeapon(item) || itemData.isStaff(item)) { continue; }
			int factor = rng.nextInt(2) == 0 ? 1 : -1;
			item.setItemAccuracy(YuneUtil.clamp(item.getItemAccuracy() + (rng.nextInt(variance + 1) * factor), min, max));
			item.commitChanges();
		}
	}
	
	public static void randomizeWeaponWeight(int variance, int min, int max, FE9ItemDataLoader itemData, Random rng) {
		if (variance == 0) { return; }
		List<FE9Item> items = itemData.allItems();
		for (FE9Item item : items) {
			if (itemData.isBlacklisted(item)) { continue; }
			if (!itemData.isWeapon(item) || itemData.isStaff(item)) { continue; }
			int factor = rng.nextInt(2) == 0 ? 1 : -1;
			item.setItemWeight(YuneUtil.clamp(item.getItemWeight() + (rng.nextInt(variance + 1) * factor), min, max));
			item.commitChanges();
		}
	}

	public static void randomizeWeaponDurability(int variance, int min, int max, FE9ItemDataLoader itemData, Random rng) {
		if (variance == 0) { return; }
		List<FE9Item> items = itemData.allItems();
		for (FE9Item item : items) {
			if (itemData.isBlacklisted(item)) { continue; }
			if (!itemData.isWeapon(item) || itemData.isStaff(item)) { continue; }
			if (itemData.isSiegeTome(item)) { continue; }
			int factor = rng.nextInt(2) == 0 ? 1 : -1;
			item.setItemDurability(YuneUtil.clamp(item.getItemDurability() + (rng.nextInt(variance + 1) * factor), min, max));
			item.commitChanges();
		}
	}
	
	public static void addRandomEffects(int effectChance, boolean safeBasicWeapons, boolean includeLaguzWeapons, WeaponEffectOptions effectOptions, GCNISOHandler handler, FE9ItemDataLoader itemData, FE9CommonTextLoader textData, Random rng) {
		if (effectChance == 0) { return; }
		List<FE9Item> items = itemData.allItems();
		
		WeightedDistributor<WeaponEffect> effects = new WeightedDistributor<WeaponEffect>();
		if (effectOptions.statBoosts > 0) { effects.addItem(WeaponEffect.STAT_BOOST, effectOptions.statBoosts); }
		if (effectOptions.effectiveness > 0) { effects.addItem(WeaponEffect.EFFECTIVENESS, effectOptions.effectiveness); }
		if (effectOptions.unbreakable > 0) { effects.addItem(WeaponEffect.UNBREAKABLE, effectOptions.unbreakable); }
		if (effectOptions.brave > 0) { effects.addItem(WeaponEffect.BRAVE, effectOptions.brave); }
		if (effectOptions.reverseTriangle > 0) { effects.addItem(WeaponEffect.REVERSE_TRIANGLE, effectOptions.reverseTriangle); }
		if (effectOptions.extendedRange > 0) { effects.addItem(WeaponEffect.EXTEND_RANGE, effectOptions.extendedRange); }
		if (effectOptions.highCritical > 0) {
			WeaponEffect effect = WeaponEffect.CRITICAL;
			effect.additionalInfo.put(WeaponEffect.InfoKey.CRITICAL_RANGE, effectOptions.criticalRange);
			effects.addItem(effect, effectOptions.highCritical);
		}
		if (effectOptions.magicDamage > 0) { effects.addItem(WeaponEffect.MAGIC_DAMAGE, effectOptions.magicDamage); }
		if (effectOptions.poison > 0) { effects.addItem(WeaponEffect.POISON, effectOptions.poison); }
		if (effectOptions.stealHP > 0) { effects.addItem(WeaponEffect.STEAL_HP, effectOptions.stealHP); }
		if (effectOptions.critImmune > 0) { effects.addItem(WeaponEffect.CRIT_IMMUNE, effectOptions.critImmune); }
		if (effectOptions.noCrit > 0) { effects.addItem(WeaponEffect.NO_CRIT, effectOptions.noCrit); }
		
		if (effects.possibleResults().isEmpty()) { return ; }
		
		for (FE9Item item : items) {
			if (itemData.isBlacklisted(item)) { continue; }
			if (!itemData.isWeapon(item)) { continue; }
			if (safeBasicWeapons && itemData.isBasicWeapon(item)) { continue; }
			if (!includeLaguzWeapons && itemData.isLaguzWeapon(item)) { continue; }
			if (rng.nextInt(100) >= effectChance) { continue; }
			
			WeightedDistributor<WeaponEffect> possibleEffects = new WeightedDistributor<WeaponEffect>(effects);
			WeaponType weaponType = itemData.weaponTypeForItem(item);
			if (weaponType == WeaponType.FIRE || weaponType == WeaponType.WIND || weaponType == WeaponType.THUNDER || weaponType == WeaponType.LIGHT) {
				// Magic doesn't seem to work with the steal hp trait properly (doesn't show it until battle is resolved).
				possibleEffects.removeItem(WeaponEffect.STEAL_HP);
			}
			boolean success = false;
			do {
				WeaponEffect selectedEffect = possibleEffects.getRandomItem(rng);
				success = itemData.applyEffectToWeapon(selectedEffect, item, handler, textData, rng);
				possibleEffects.removeItem(selectedEffect);
			} while (!success && !possibleEffects.possibleResults().isEmpty());
			
			item.commitChanges();
		}
	}
}
