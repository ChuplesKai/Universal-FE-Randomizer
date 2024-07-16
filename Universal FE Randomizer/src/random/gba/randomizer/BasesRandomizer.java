package random.gba.randomizer;

import fedata.gba.GBAFECharacterData;
import fedata.gba.GBAFEClassData;
import fedata.gba.GBAFEStatDto;
import random.general.FERandom;
import random.general.WeightedDistributor;
import random.gba.loader.CharacterDataLoader;
import random.gba.loader.ClassDataLoader;
import ui.model.BaseOptions;
import util.YuneUtil;

public class BasesRandomizer 
{
	public static int rngSalt = 9001;

	// Used for base stat redistribution for reasonable bases
	private static WeightedDistributor<Integer> statDistributor;
	private static boolean statDistributorMade = false;


	/*****************************************************************
	 * Make the distributor for redistribution mode.
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
	 * Main Method - redistributes all playables' base stats.
	 ****************************************************************/
	public static void randomizePartyBases(BaseOptions.Mode method, int variance, CharacterDataLoader charactersData, ClassDataLoader classData, FERandom rng)
	{
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();
		MakeDistributor();

		for (GBAFECharacterData character : allPlayableCharacters) 
		{
			// Get the character's bases
			GBAFEStatDto bases = character.getBases();
			// Fetch up the class info
			int classID = character.getClassID();
			GBAFEClassData charClass = classData.classForID(classID);			
			// Also get the class's bases
			GBAFEStatDto classBases = charClass.getBases();
			// Finally, get class's maxes/caps
			GBAFEStatDto classCaps = charClass.getCaps();
			
			// Then, get the new bases depending on the randomization method
			GBAFEStatDto newBases; // Pointer to put the new bases info into
			switch(method)
			{
			case REDISTRIBUTE: // Base stat redistribution
				newBases = redistributeBases( variance, bases, classBases, classCaps, rng );
				break;
			case DELTA: // Random delta/variance
				newBases = deltaRandomizeBases( variance, bases, classBases, classCaps, rng );
				break;
			default: // If for some reason we fall through with no method, just return the bases
				newBases = new GBAFEStatDto( bases );
			}

			// Set the character's bases // Is there a reason we don't do links?
			character.setBases( newBases );
		}
		// Commit the character changes
		charactersData.commit();
	}


	/*****************************************************************
	 * Takes input bases, adds a variance to the total, and then
	 *  redistributes them in compliance with class bases/caps. 
	 ****************************************************************/
	public static GBAFEStatDto redistributeBases(int variance, GBAFEStatDto inBases, GBAFEStatDto classBases, GBAFEStatDto classCaps, FERandom rng)
	{
		// Get the current total bases and add the variance
		int baseTotal = inBases.total() + rng.sample( variance );

		// Bases start at 0, except for luck.
		int[] newBaseAry = {0,0,0,0,rng.sampleRange( 0, 4 ),0,0};

		// Then, the maximum *personal* bases are the difference between the class caps and bases
		GBAFEStatDto maxBases = new GBAFEStatDto( classCaps );
		maxBases.subtract( classBases );
		// Also put this into an array for ease of implementation
		int[] maxBaseAry = {maxBases.hp, maxBases.str, maxBases.skl, maxBases.spd, maxBases.lck, maxBases.def, maxBases.res};

		// Then, subtract out the luck we pre-allocated 
		baseTotal = Math.max( baseTotal - newBaseAry[4], 0 );
		if (baseTotal > 0) // If there is work to do
		{
			do // Allocate bases until there are no more
			{
				// First, use the weighted distributor to select the stat
				int sStat = statDistributor.getRandomItem( rng );
				// We bump by 1-3, but no more than the remaining total
				int amount = Math.min( rng.sampleRange( 1, 3 ), baseTotal );
				// If the amount would put us over the maximum for the base, loop again
				if( newBaseAry[sStat] + amount > maxBaseAry[sStat] ) continue;
				// If we are good to apply,
				newBaseAry[sStat] += amount; // Add the amount to base
				baseTotal -= amount; // And reduce our reserve of base stats
			} while (baseTotal > 0);
		}

		// Then, once work is done, we'll return the DTO with the array info
		return new GBAFEStatDto( newBaseAry[0], newBaseAry[1], newBaseAry[2], newBaseAry[3], newBaseAry[4], newBaseAry[5], newBaseAry[6] );
	}

	
	/*****************************************************************
	 * Takes input bases and applies variance to each according to the
	 *  rng distribution selected, while complying with class bases/caps.
	 ****************************************************************/
	public static GBAFEStatDto deltaRandomizeBases(int variance, GBAFEStatDto inBases, GBAFEStatDto classBases, GBAFEStatDto classCaps, FERandom rng)
	{
		// First, copy over the character bases
		GBAFEStatDto newBases = new GBAFEStatDto( inBases );

		// Then, figure out the min personal bases
		GBAFEStatDto minBases = new GBAFEStatDto( classBases );
		minBases.multiply( -1 ); // Personal bases cannot go below negative class base
		// Then, figure out the max personal bases
		GBAFEStatDto maxBases = new GBAFEStatDto( classCaps );
		maxBases.subtract( classBases ); // Personal bases cannot exceed the cap-base gap

		// Modify each of the bases by a sample according to the variance - seven samples
		newBases.add( new GBAFEStatDto( rng.sample(variance), rng.sample(variance), rng.sample(variance), rng.sample(variance), rng.sample(variance), rng.sample(variance), rng.sample(variance) ) );

		// Clamp the new bases so that they are within range
		newBases.clamp( minBases, maxBases );

		// Return the bases that we calculated
		return newBases;
	}
}
