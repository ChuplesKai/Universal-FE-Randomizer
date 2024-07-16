package random.gba.randomizer;

import fedata.gba.GBAFECharacterData;
import random.gba.loader.CharacterDataLoader;
import util.WhyDoesJavaNotHaveThese;
import random.general.FERandom;
import fedata.gba.GBAFEStatDto;
import ui.model.GrowthOptions;

public class GrowthsRandomizer 
{	
	// Declarations
	static final int rngSalt = 124; // RNG salt
	static final int hpBonus = 25; // Minimum HP growth/offset since HP tends to grow more

	//////////////////////////////////////////////////////////////////
	// UTILITIES
	//////////////////////////////////////////////////////////////////

	//================================================================
	// Utility Function to set the randomized growths
	//================================================================
	private static void setGrowths( CharacterDataLoader charactersData, GBAFECharacterData character, GBAFEStatDto newGrowths )
	{
		for (GBAFECharacterData thisCharacter : charactersData.linkedCharactersForCharacter(character))
		{
			thisCharacter.setGrowths(newGrowths);
		}
	}

	//////////////////////////////////////////////////////////////////
	// MAIN METHODS
	//////////////////////////////////////////////////////////////////
	
	//================================================================
	// Main Growths Randomization method.
	//================================================================
	public static void randomizeTeamGrowths(GrowthOptions.Mode method, int variance, int minGrowth, int maxGrowth, boolean adjustHP, CharacterDataLoader charactersData, FERandom rng)
	{
		// Get all the playable characters and commit any outstanding changes
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();	
		charactersData.commit();

		// Check for doing it by 5%, which is probably the expectation
		int mult = 1;
		int range = maxGrowth - minGrowth + 1;
		if( minGrowth % 5 == 0 && maxGrowth % 5 == 0 )
		{
			mult = 5; //Set the multiplier to five
			range = ((maxGrowth - minGrowth) / 5) + 1; //And divide the Delta by five
			variance = variance / 5; //Also reduce the variance
		}
		
		// Then, for each character
		for (GBAFECharacterData character : allPlayableCharacters)
		{
			// If they are previously modified for being a linked character, skip
			if (character.wasModified()) { continue; }
			
			// Generate the Growths
			GBAFEStatDto newGrowths; // Need a stat DTO to hold the new growths
			switch( method )
			{
			case FULL:
				newGrowths = fullRandomizeRange( mult, minGrowth, range, adjustHP, character.getGrowths(), rng );
				break;
			case DELTA:
				newGrowths = randomizeDelta( mult, variance, minGrowth, maxGrowth, adjustHP, character.getGrowths(), rng );
				break;
			case REDISTRIBUTE:
				newGrowths = randomizeRedistribute( mult, variance, minGrowth, maxGrowth, adjustHP, character.getGrowths(), rng );
				break;
			default: // If we somehow hit an unknown type, just set to old growths
				newGrowths = new GBAFEStatDto( character.getGrowths() );
			}

			// Finally, just set the growths
			setGrowths( charactersData, character, newGrowths );
		}
		//Then commit the character changes
		charactersData.commit();
	}


	/*****************************************************************
	 * 
	 ****************************************************************/
	public static GBAFEStatDto randomizeRedistribute(int mult, int variance, int minGrowth, int maxGrowth, boolean adjustHP, GBAFEStatDto inputGrowths, FERandom rng)
	{
		// Get the character's overall growth amount.
		int growthTotal = (adjustHP ? inputGrowths.hp - hpBonus : 0 ) + inputGrowths.str + inputGrowths.skl + 
				inputGrowths.spd + inputGrowths.lck + inputGrowths.def + inputGrowths.res;

		// Sample from a distribution
		growthTotal += mult * rng.sample( variance );

		// Start with growths at the minimum value
		GBAFEStatDto newGrowths = new GBAFEStatDto( adjustHP ? minGrowth + hpBonus : inputGrowths.hp, minGrowth, minGrowth, minGrowth, minGrowth, minGrowth, minGrowth );
		
		// Then, figure out how much growth we can do
		growthTotal -= (minGrowth * (adjustHP ? 7 : 6));
		int totalAvailableGrowth = (adjustHP ? 7 : 6) * (maxGrowth - minGrowth);
		int randomNum = 0;

		// If there are growths to distribute, do so			
		if (totalAvailableGrowth > growthTotal) 
		{
			// While there is still growth to dole out
			while (growthTotal > 0) 
			{
				// Depending on whether we're adjusting HP, pick a stat
				randomNum = rng.nextInt( adjustHP ? 7 : 6 );
				int amount = Math.min( 5, growthTotal ); // 5, unless we have less than that to give
				int increaseAmount = 0;
				switch (randomNum)
				{
				case 0:
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.str);
					growthTotal -= increaseAmount;
					newGrowths.str += increaseAmount;
					break;
				case 1:
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.skl);
					growthTotal -= increaseAmount;
					newGrowths.skl += increaseAmount;
					break;
				case 2:
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.spd);
					growthTotal -= increaseAmount;
					newGrowths.spd += increaseAmount;
					break;
				case 3:
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.lck);
					growthTotal -= increaseAmount;
					newGrowths.lck += increaseAmount;
					break;
				case 4: 
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.def);
					growthTotal -= increaseAmount;
					newGrowths.def += increaseAmount;
					break;
				case 5:
					increaseAmount = Math.min(amount, maxGrowth - newGrowths.res);
					growthTotal -= increaseAmount;
					newGrowths.res += increaseAmount;
					break;
				default:
					increaseAmount = Math.min(amount, maxGrowth + hpBonus - newGrowths.hp);
					growthTotal -= increaseAmount;
					newGrowths.hp += increaseAmount;
					break;
				}
			}
		} 
		else 
		{
			// We can't satisfy the max constraints. Just max out everything.
			newGrowths = new GBAFEStatDto( maxGrowth + hpBonus, maxGrowth, maxGrowth, maxGrowth, maxGrowth, maxGrowth, maxGrowth );
		}

		return newGrowths;
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	public static GBAFEStatDto randomizeDelta(int mult, int delta, int minGrowth, int maxGrowth, boolean adjustHP, GBAFEStatDto inputGrowths, FERandom rng)
	{
		return new GBAFEStatDto( WhyDoesJavaNotHaveThese.clamp( adjustHP ? inputGrowths.hp + (mult * rng.sample( delta )) : inputGrowths.hp, minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.str + (mult * rng.sample( delta )), minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.skl + (mult * rng.sample( delta )), minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.spd + (mult * rng.sample( delta )), minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.lck + (mult * rng.sample( delta )), minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.def + (mult * rng.sample( delta )), minGrowth, maxGrowth ),
				WhyDoesJavaNotHaveThese.clamp( inputGrowths.res + (mult * rng.sample( delta )), minGrowth, maxGrowth )
				);
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	public static GBAFEStatDto fullRandomizeRange(int mult, int minGrowth, int range, boolean adjustHP, GBAFEStatDto inputGrowths, FERandom rng)
	{
		return new GBAFEStatDto( adjustHP ? (mult * rng.nextInt(range)) + minGrowth + hpBonus : inputGrowths.hp,
				(mult * rng.nextInt(range)) + minGrowth, (mult * rng.nextInt(range)) + minGrowth,
				(mult * rng.nextInt(range)) + minGrowth, (mult * rng.nextInt(range)) + minGrowth,
				(mult * rng.nextInt(range)) + minGrowth, (mult * rng.nextInt(range)) + minGrowth );
	}

}
