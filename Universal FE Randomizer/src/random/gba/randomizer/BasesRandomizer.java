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
	
	private static WeightedDistributor<Integer> statDistributor;
	private static boolean statDistributorMade = false;


	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void MakeDistributor()
	{
		if(!statDistributorMade)
		{
			//We're going to make the Stat Distributor and put in weights
			statDistributor = new WeightedDistributor<Integer>();
			statDistributor.addItem( 0, 5 ); // HP
			statDistributor.addItem( 1, 3 ); // STR
			statDistributor.addItem( 2, 4 ); // SKL
			statDistributor.addItem( 3, 4 ); // SPD
			statDistributor.addItem( 4, 3 ); // LCK
			statDistributor.addItem( 5, 2 ); // DEF
			statDistributor.addItem( 6, 2 ); // RES
			statDistributorMade = true; // Never need to do it again
		}
	}


	/*****************************************************************
	 * 
	 ****************************************************************/
	public static void randomizeBasesByRedistribution(int variance, CharacterDataLoader charactersData, ClassDataLoader classData, FERandom rng)
	{
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();
		MakeDistributor();

		for (GBAFECharacterData character : allPlayableCharacters) 
		{
			int baseTotal = character.getBaseHP() + 
					character.getBaseSTR() + character.getBaseSKL() +
					character.getBaseSPD() + character.getBaseDEF() + 
					character.getBaseRES() + character.getBaseLCK();
			// Need to pull down class information to make sure we don't go above max'es
			int classID = character.getClassID();
			GBAFEClassData charClass = classData.classForID(classID);
			// Adjust the base stat total by the variance
			int baseVar = rng.sample( variance ); 
			baseTotal += baseVar;

			// Bases tart at 0, except for luck.
			int[] newBase = {0,0,0,0,rng.sampleRange( 0, 2 ),0,0};
			// Also calculate the maximum for each base, based on class
			int[] maxBase = {charClass.getMaxHP() - charClass.getBaseHP(), 
					charClass.getMaxSTR() - charClass.getBaseSTR(), charClass.getMaxSKL() - charClass.getBaseSKL(),
					charClass.getMaxSPD() - charClass.getBaseSPD(), charClass.getMaxLCK() - charClass.getBaseLCK(),
					charClass.getMaxDEF() - charClass.getBaseDEF(), charClass.getMaxRES() - charClass.getBaseRES() };

			// Then, subtract out the luck we pre-allocated (presumably for non-0 luck)
			baseTotal = Math.max( baseTotal - newBase[4], 0 );
			if (baseTotal > 0) // If there is work to do
			{
				do // Allocate bases until there are no more
				{
					// First, use the weighted distributor to select the stat
					int sStat = statDistributor.getRandomItem( rng );
					// We bump by 1-3, but no more than the remaining total
					int amount = Math.min( rng.sampleRange( 1, 3 ), baseTotal );
					// If the amount would put us over the maximum for the base, loop again
					if( newBase[sStat] + amount > maxBase[sStat] ) continue;
					// If we are good to apply,
					newBase[sStat] += amount; // Add the amount to base
					baseTotal -= amount; // And reduce our reserve of base stats
				} while (baseTotal > 0);
			}
			// Then, slot in those bases in order
			character.setBaseHP( newBase[0] );
			character.setBaseSTR( newBase[1] );
			character.setBaseSKL( newBase[2] );
			character.setBaseSPD( newBase[3] );
			character.setBaseLCK( newBase[4] );
			character.setBaseDEF( newBase[5] );
			character.setBaseRES( newBase[6] );
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
