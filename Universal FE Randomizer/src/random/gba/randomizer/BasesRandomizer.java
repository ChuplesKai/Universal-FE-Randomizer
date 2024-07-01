package random.gba.randomizer;

import fedata.gba.GBAFECharacterData;
import fedata.gba.GBAFEClassData;
import random.general.FERandom;
import random.general.WeightedDistributor;
import random.gba.loader.CharacterDataLoader;
import random.gba.loader.ClassDataLoader;
import util.WhyDoesJavaNotHaveThese;

public class BasesRandomizer 
{
	public static int rngSalt = 9001;
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeBasesByRedistribution(int variance, CharacterDataLoader charactersData, ClassDataLoader classData, FERandom rng)
	{
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();
		for (GBAFECharacterData character : allPlayableCharacters) 
		{
			int baseTotal = character.getBaseHP() + 
					character.getBaseSTR() + character.getBaseSKL() +
					character.getBaseSPD() + character.getBaseDEF() + 
					character.getBaseRES() + character.getBaseLCK();

			int classID = character.getClassID();
			GBAFEClassData charClass = classData.classForID(classID);

			// Adjust the base stat total by the variance
			baseTotal += rng.sample( variance );

			// Start at 0, except for luck for whatever reason.			
			int newHPBase = 0;
			int newSTRBase = 0;
			int newSKLBase = 0;
			int newSPDBase = 0;
			int newLCKBase = rng.sampleRange( 0, 4 ); //Reduced [0,6]->[0,4]
			int newDEFBase = 0;
			int newRESBase = 0;
			
			baseTotal -= newLCKBase;
			if (baseTotal < 0) baseTotal = 0;
			
			if (baseTotal > 0)
			{
				do
				{
					int randomNum = rng.nextInt( 23 ); // Selecting which stat to bump
					int amount = rng.sampleRange( 1, 3 ); // bump by 1 - 3
					
					switch (randomNum) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newHPBase + amount, -1 * charClass.getBaseHP(), charClass.getMaxHP() - charClass.getBaseHP())) continue;
						newHPBase += amount;
						break;
					case 5:
					case 6:
					case 7:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newSTRBase + amount, -1 * charClass.getBaseSTR(), charClass.getMaxSTR() - charClass.getBaseSTR())) continue;
						newSTRBase += amount;
						break;
					case 8:
					case 9:
					case 10:
					case 11:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newSKLBase + amount, -1 * charClass.getBaseSKL(), charClass.getMaxSKL() - charClass.getBaseSKL())) continue;
						newSKLBase += amount;
						break;
					case 12:
					case 13:
					case 14:
					case 15:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newSPDBase + amount, -1 * charClass.getBaseSPD(), charClass.getMaxSPD() - charClass.getBaseSPD())) continue;
						newSPDBase += amount;
						break;
					case 16:
					case 17:
					case 18:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newLCKBase + amount, -1 * charClass.getBaseLCK(), charClass.getMaxLCK() - charClass.getBaseLCK())) continue;
						newLCKBase += amount;
						break;
					case 19: 
					case 20:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newDEFBase + amount, -1 * charClass.getBaseDEF(), charClass.getMaxDEF() - charClass.getBaseDEF())) continue;
						newDEFBase += amount;
						break;
					case 21:
					case 22:
						if (!WhyDoesJavaNotHaveThese.isValueBetween(newRESBase + amount, -1 * charClass.getBaseRES(), charClass.getMaxRES() - charClass.getBaseRES())) continue;
						newRESBase += amount;
						break;
					}
					
					baseTotal -= amount;
				} while (baseTotal > 0);
			}
			
			character.setBaseHP(newHPBase);
			character.setBaseSTR(newSTRBase);
			character.setBaseSKL(newSKLBase);
			character.setBaseSPD(newSPDBase);
			character.setBaseLCK(newLCKBase);
			character.setBaseDEF(newDEFBase);
			character.setBaseRES(newRESBase);
		}
		
		charactersData.commit();
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeBasesByRandomDelta(int maxDelta, CharacterDataLoader charactersData, ClassDataLoader classData, FERandom rng) 
	{
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();
		for (GBAFECharacterData character : allPlayableCharacters) {
			
			int classID = character.getClassID();
			GBAFEClassData charClass = classData.classForID(classID);

			// Apply the deltas, order based on what I think should be related
			int newSTRBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseSTR() + rng.sample( maxDelta ),
					-1 * charClass.getBaseSTR(), charClass.getMaxSTR() - charClass.getBaseSTR() );
			int newSKLBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseSKL() + rng.sample( maxDelta ),
					-1 * charClass.getBaseSKL(), charClass.getMaxSKL() - charClass.getBaseSKL() );
			int newSPDBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseSPD() + rng.sample( maxDelta ),
					-1 * charClass.getBaseSPD(), charClass.getMaxSPD() - charClass.getBaseSPD() );
			int newLCKBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseLCK() + rng.sample( maxDelta ),
					-1 * charClass.getBaseLCK(), charClass.getMaxLCK() - charClass.getBaseLCK() );
			int newHPBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseHP() + rng.sample( maxDelta ),
					-1 * charClass.getBaseHP(), charClass.getMaxHP() - charClass.getBaseHP() );
			int newDEFBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseDEF() + rng.sample( maxDelta ),
					-1 * charClass.getBaseDEF(), charClass.getMaxDEF() - charClass.getBaseDEF() );
			int newRESBase = WhyDoesJavaNotHaveThese.clamp( character.getBaseRES() + rng.sample( maxDelta ),
					-1 * charClass.getBaseRES(), charClass.getMaxRES() - charClass.getBaseRES() );

			character.setBaseHP(newHPBase);
			character.setBaseSTR(newSTRBase);
			character.setBaseSKL(newSKLBase);
			character.setBaseSPD(newSPDBase);
			character.setBaseLCK(newLCKBase);
			character.setBaseDEF(newDEFBase);
			character.setBaseRES(newRESBase);
		}
		
		charactersData.commit();
	}
}
