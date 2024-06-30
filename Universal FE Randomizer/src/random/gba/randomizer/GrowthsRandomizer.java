package random.gba.randomizer;

import fedata.gba.GBAFECharacterData;
import random.gba.loader.CharacterDataLoader;
import util.WhyDoesJavaNotHaveThese;
import random.general.FERandom;

public class GrowthsRandomizer 
{	
	// Declarations
	static final int rngSalt = 124;
	static final int hpBonus = 25;

	//////////////////////////////////////////////////////////////////
	// UTILITIES
	//////////////////////////////////////////////////////////////////

	//================================================================
	// Utility Function to set the randomized growths
	//================================================================
	private static void setRandGrowths( boolean adjustHP, int min, int max, CharacterDataLoader charactersData, GBAFECharacterData character, int newHPGrowth, int newSTRGrowth, int newSKLGrowth, int newSPDGrowth, int newLCKGrowth, int newDEFGrowth, int newRESGrowth )
	{
		for (GBAFECharacterData thisCharacter : charactersData.linkedCharactersForCharacter(character))
		{
			if(adjustHP)
			{
				thisCharacter.setHPGrowth(WhyDoesJavaNotHaveThese.clamp( newHPGrowth, min + hpBonus, max + hpBonus) );
			}
			thisCharacter.setSTRGrowth( WhyDoesJavaNotHaveThese.clamp(newSTRGrowth, min, max) );
			thisCharacter.setSKLGrowth( WhyDoesJavaNotHaveThese.clamp(newSKLGrowth, min, max) );
			thisCharacter.setSPDGrowth( WhyDoesJavaNotHaveThese.clamp(newSPDGrowth, min, max) );
			thisCharacter.setLCKGrowth( WhyDoesJavaNotHaveThese.clamp(newLCKGrowth, min, max) );
			thisCharacter.setDEFGrowth( WhyDoesJavaNotHaveThese.clamp(newDEFGrowth, min, max) );
			thisCharacter.setRESGrowth( WhyDoesJavaNotHaveThese.clamp(newRESGrowth, min, max) );
		}

	}

	//////////////////////////////////////////////////////////////////
	// MAIN METHODS
	//////////////////////////////////////////////////////////////////

	//================================================================
	// Redistribute Growths, so that total growth rate amount stays
	//  nearly the same, but get re-focused into different stats. 
	//================================================================
	public static void randomizeGrowthsByRedistribution(int variance, int min, int max, boolean adjustHP, CharacterDataLoader charactersData, FERandom rng)
	{
		// Get the playable characters, since only they have character growths
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();
		
		// Commit outstanding changes to start from a clean slate.
		charactersData.commit();

		// Check for doing it by 5%, which is more common
		int mult = 1;
		if( variance % 5 == 0 && min % 5 == 0 && max % 5 == 0 )
		{
			mult = 5; //Set the multiplier to five
			variance = variance / 5; //And divide the Delta by five
		}

		// For each playable character		
		for (GBAFECharacterData character : allPlayableCharacters)
		{	
			// Do not modify anything that was already modified.
			// This is here because some characters are linked (for example, FE7 Lyn has two variants: Tutorial and Not Tutorial).
			// If we generate growths for one, we apply it to all linked characters at the end of this loop.
			if (character.wasModified()) { continue; }

			// Get the character's overall growth amount.
			int growthTotal = (adjustHP ? character.getHPGrowth() - hpBonus : 0 ) + character.getSTRGrowth() + character.getSKLGrowth() + character.getSPDGrowth() + 
					character.getLCKGrowth() + character.getDEFGrowth() + character.getRESGrowth();

			// Sample from a distribution
			growthTotal += mult * rng.sample( variance );

			// Start with growths at the minimum value			
			int newHPGrowth = min + hpBonus;
			int newSTRGrowth = min;
			int newSKLGrowth = min;
			int newSPDGrowth = min;
			int newLCKGrowth = min;
			int newDEFGrowth = min;
			int newRESGrowth = min;
			
			// Then, figure out how much growth we can do
			growthTotal -= (min * (adjustHP ? 7 : 6));
			int availableGrowthRemaining = (adjustHP ? 7 : 6) * (max - min);
			int randomNum = 0;

			// If there are growths to distribute, do so			
			if (availableGrowthRemaining > growthTotal) 
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
						increaseAmount = Math.min(amount, max - newSTRGrowth);
						growthTotal -= increaseAmount;
						newSTRGrowth += increaseAmount;
						break;
					case 1:
						increaseAmount = Math.min(amount, max - newSKLGrowth);
						growthTotal -= increaseAmount;
						newSKLGrowth += increaseAmount;
						break;
					case 2:
						increaseAmount = Math.min(amount, max - newSPDGrowth);
						growthTotal -= increaseAmount;
						newSPDGrowth += increaseAmount;
						break;
					case 3:
						increaseAmount = Math.min(amount, max - newLCKGrowth);
						growthTotal -= increaseAmount;
						newLCKGrowth += increaseAmount;
						break;
					case 4: 
						increaseAmount = Math.min(amount, max - newDEFGrowth);
						growthTotal -= increaseAmount;
						newDEFGrowth += increaseAmount;
						break;
					case 5:
						increaseAmount = Math.min(amount, max - newRESGrowth);
						growthTotal -= increaseAmount;
						newRESGrowth += increaseAmount;
						break;
					default:
						increaseAmount = Math.min(amount, max + hpBonus - newHPGrowth);
						growthTotal -= increaseAmount;
						newHPGrowth += increaseAmount;
						break;
					}
				}
			} 
			else 
			{
				// We can't satisfy the max constraints.
				// Just max out everything.
				newHPGrowth = max + hpBonus;
				newSTRGrowth = max;
				newSKLGrowth = max;
				newSPDGrowth = max;
				newLCKGrowth = max;
				newDEFGrowth = max;
				newRESGrowth = max;
			}

			// Finally, just set the growths
			setRandGrowths( adjustHP, min, max, charactersData, character, newHPGrowth, newSTRGrowth, newSKLGrowth, newSPDGrowth, newLCKGrowth, newDEFGrowth, newRESGrowth );
		}		
		//Commit changes
		charactersData.commit();
	}
	
	//================================================================
	// Adjusts existing growths by adding a delta to the 
	//  existing growth amount.
	//================================================================
	public static void randomizeGrowthsByRandomDelta(int maxDelta, int min, int max, boolean adjustHP, CharacterDataLoader charactersData, FERandom rng)
	{
		// Get all the playable characters and make sure any outstanding changes are committed.
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();	
		charactersData.commit();
		
		// Check for doing it by 5%, which is more common
		int mult = 1;
		if( maxDelta % 5 == 0 && min % 5 == 0 && max % 5 == 0 )
		{
			mult = 5; //Set the multiplier to five
			maxDelta = maxDelta / 5; //And divide the Delta by five
		}

		// Then, for each of the playable characters
		for (GBAFECharacterData character : allPlayableCharacters) 
		{
			//Skip them if they are already modified via link mondification	
			if (character.wasModified()) { continue; }
			
			int newHPGrowth = character.getHPGrowth() + (mult * rng.sample( maxDelta ));
			int newSTRGrowth = character.getSTRGrowth() + (mult * rng.sample( maxDelta ));
			int newSKLGrowth = character.getSKLGrowth() + (mult * rng.sample( maxDelta ));
			int newSPDGrowth = character.getSPDGrowth() + (mult * rng.sample( maxDelta ));
			int newLCKGrowth = character.getLCKGrowth() + (mult * rng.sample( maxDelta ));
			int newDEFGrowth = character.getDEFGrowth() + (mult * rng.sample( maxDelta ));
			int newRESGrowth = character.getRESGrowth() + (mult * rng.sample( maxDelta ));
			
			// Finally, just set the growths
			setRandGrowths( adjustHP, min, max, charactersData, character, newHPGrowth, newSTRGrowth, newSKLGrowth, newSPDGrowth, newLCKGrowth, newDEFGrowth, newRESGrowth );
		}
		//Commit changes
		charactersData.commit();
	}
	
	//================================================================
	// Completely re-randomizes growths, keeping within a range
	//================================================================
	public static void fullyRandomizeGrowthsWithRange(int minGrowth, int maxGrowth, boolean adjustHP, CharacterDataLoader charactersData, FERandom rng)
	{
		// Get all the playable characters and commit any outstanding changes
		GBAFECharacterData[] allPlayableCharacters = charactersData.playableCharacters();	
		charactersData.commit();

		// Check for doing it by 5%, which is more common
		int mult = 1;
		int range = maxGrowth - minGrowth + 1;
		if( minGrowth % 5 == 0 && maxGrowth % 5 == 0 )
		{
			mult = 5; //Set the multiplier to five
			range = ((maxGrowth - minGrowth) / 5) + 1; //And divide the Delta by five
		}
		
		// Then, for each character
		for (GBAFECharacterData character : allPlayableCharacters)
		{	
			// If they are previously modified for being a linked character, skip
			if (character.wasModified()) { continue; }

			// Determine max spread/range and add uniform random to the minimum			
			int newHPGrowth = (mult * rng.nextInt(range)) + minGrowth + hpBonus;
			int newSTRGrowth = (mult * rng.nextInt(range)) + minGrowth;
			int newSKLGrowth = (mult * rng.nextInt(range)) + minGrowth;
			int newSPDGrowth = (mult * rng.nextInt(range)) + minGrowth;
			int newLCKGrowth = (mult * rng.nextInt(range)) + minGrowth;
			int newDEFGrowth = (mult * rng.nextInt(range)) + minGrowth;
			int newRESGrowth = (mult * rng.nextInt(range)) + minGrowth;
			
			// Finally, just set the growths
			setRandGrowths( adjustHP, minGrowth, maxGrowth, charactersData, character, newHPGrowth, newSTRGrowth, newSKLGrowth, newSPDGrowth, newLCKGrowth, newDEFGrowth, newRESGrowth );
		}
		//Then commit the character changes		
		charactersData.commit();
	}

}
