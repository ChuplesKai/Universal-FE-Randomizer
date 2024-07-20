package fedata.gba;

import java.util.Arrays;
import java.util.List;

import util.YuneUtil;

/**
 * Stat DAO for convenient setting / getting of the 7 Main Stats
 */
public class GBAFEStatDto {
	
	/**
	 * Static GBAFEStatDao which contains the minimum stats that a character may have
	 */
	public static final GBAFEStatDto MINIMUM_STATS = new GBAFEStatDto(0, 0, 0, 0, 0, 0 ,0);
	
	public int hp;
	public int str;
	public int skl;
	public int spd;
	public int lck;
	public int def;
	public int res;
	
	
	/**
	 * Empty default constructor 
	 */
	public GBAFEStatDto() {
	}
	
	/**
	 * Copy constructor
	 */
	public GBAFEStatDto(GBAFEStatDto other) {
		this.hp  = other.hp;
		this.str = other.str;
		this.skl = other.skl;
		this.spd = other.spd;
		this.def = other.def;
		this.res = other.res;
		this.lck = other.lck;
	}
	
	/**
	 * accumulation constructor
	 */
	public GBAFEStatDto(List<GBAFEStatDto> bonuses) {
		for (GBAFEStatDto bonus : bonuses) {
			this.add(bonus);
		}
	}
	
	/**
	 * Constructor with 7 int values, in order hp, str, skl, spd, def, res, lck
	 */
	public GBAFEStatDto(int... args) {
		assert args.length == 7;
		
		hp = args[0];
		str = args[1];
		skl = args[2];
		spd = args[3];
		lck = args[4];
		def = args[5];
		res = args[6];
	}
	
	/**
	 * Returns the stats as a list with stats in order hp, str, skl, spd, lck, def, res
	 */
	public List<Integer> asList(){
		return Arrays.asList(hp, str, skl, spd, lck, def, res);
	}

	/**
	 * Return the total (sum) of this data object 
	 */
	public int total()
	{
		return hp + str + skl + spd + lck + def + res;
	}
	
	/**
	 * Multiplies all the stas with the given multiplier
	 */
	public GBAFEStatDto multiply(int multiplier) {
		this.hp *= multiplier;
		this.str *= multiplier;
		this.skl *= multiplier;
		this.spd *= multiplier;
		this.def *= multiplier;
		this.res *= multiplier;
		this.lck *= multiplier;
		
		return this;
	}
	
	/**
	 * Adds the stats of the given DAO to the current instance
	 */
	public GBAFEStatDto add(GBAFEStatDto other) {
		this.hp += other.hp;
		this.str += other.str;
		this.skl += other.skl;
		this.spd += other.spd;
		this.lck += other.lck;
		this.def += other.def;
		this.res += other.res;
		return this;
	} 
	
	/**
	 * Subtracts the stats from the given DAO from the current instance
	 */
	public GBAFEStatDto subtract(GBAFEStatDto other) {
		this.hp  -= other.hp;
		this.str -= other.str;
		this.skl -= other.skl;
		this.spd -= other.spd;
		this.lck -= other.lck;
		this.def -= other.def;
		this.res -= other.res;
		return this;
	} 
	
	
	/**
	 * Clamps the stats of the current instance against the given upper and lower values
	 */
	public GBAFEStatDto clamp(GBAFEStatDto lower, GBAFEStatDto upper) {
		this.hp =  YuneUtil.clamp(this.hp,  lower.hp , upper.hp );
		this.str = YuneUtil.clamp(this.str, lower.str, upper.str);
		this.skl = YuneUtil.clamp(this.skl, lower.skl, upper.skl);
		this.spd = YuneUtil.clamp(this.spd, lower.spd, upper.spd);
		this.lck = YuneUtil.clamp(this.lck, lower.lck, upper.lck);
		this.def = YuneUtil.clamp(this.def, lower.def, upper.def);
		this.res = YuneUtil.clamp(this.res, lower.res, upper.res);
		
		return this;
	}

	/**
	 * Hybridizes this DTO with an input one, calculating an average.
	 *  If a multiplier other than 1 is given, the resulting values
	 *  will be adjusted to the nearest multiple (with bias towards
	 * 	the values of this/calling DTO).
	 */
	public GBAFEStatDto hybridize(GBAFEStatDto other, int multiplier)
	{
		this.hp = hybridRound(this.hp, other.hp, multiplier);
		this.str = hybridRound(this.str, other.str, multiplier);
		this.skl = hybridRound(this.skl, other.skl, multiplier);
		this.spd = hybridRound(this.spd, other.spd, multiplier);
		this.lck = hybridRound(this.lck, other.lck, multiplier);
		this.def = hybridRound(this.def, other.def, multiplier);
		this.res = hybridRound(this.res, other.res, multiplier);
		return this;
	}

	private int hybridRound( int baseVal, int otherVal, int multiplier )
	{
		int ret = (baseVal + otherVal)/2;
		int stmod = ret % multiplier;
		if (stmod != 0)
		{
			if( baseVal < otherVal ) ret -= stmod; // Bias Down
			else ret += (multiplier - stmod); // Bias Up
		}
		return ret;
	}
	
	/**
	 * Given a GBAFEStatDAO o1 that logically should have lower stats than another GBAFEStatDAO o2, but doesn't, 
	 * this will return a new DAO with the necessary decreases to get from the higher stat in o1 to the lower one in o2.
	 */
	public static GBAFEStatDto downAdjust(GBAFEStatDto o1, GBAFEStatDto o2) {
		GBAFEStatDto dao = new GBAFEStatDto();
		if (o1.hp  > o2.hp ) { dao.hp  -= o1.hp  - o2.hp ; }
		if (o1.str > o2.str) { dao.str -= o1.str - o2.str; }
		if (o1.skl > o2.skl) { dao.skl -= o1.skl - o2.skl; }
		if (o1.spd > o2.spd) { dao.spd -= o1.spd - o2.spd; }
		if (o1.lck > o2.lck) { dao.lck -= o1.lck - o2.lck; }
		if (o1.def > o2.def) { dao.def -= o1.def - o2.def; }
		if (o1.res > o2.res) { dao.res -= o1.res - o2.res; }
		return dao;
	}
	
	/**
	 * Given a GBAFEStatDAO o1 that logically should have higher stats than another GBAFEStatDAO o2, but doesn't, 
	 * this will return a new DAO with the necessary increases to get from the lower stat in o1 to the higher one in o2.
	 */
	public static GBAFEStatDto upAdjust(GBAFEStatDto o1, GBAFEStatDto o2) {
		GBAFEStatDto dao = new GBAFEStatDto();
		if (o1.hp  < o2.hp ) { dao.hp  += o1.hp  - o2.hp ; }
		if (o1.str < o2.str) { dao.str += o1.str - o2.str; }
		if (o1.skl < o2.skl) { dao.skl += o1.skl - o2.skl; }
		if (o1.spd < o2.spd) { dao.spd += o1.spd - o2.spd; }
		if (o1.lck < o2.lck) { dao.lck += o1.lck - o2.lck; }
		if (o1.def < o2.def) { dao.def += o1.def - o2.def; }
		if (o1.res < o2.res) { dao.res += o1.res - o2.res; }
		return dao;
	}
	
	@Override
	public String toString() {
		return String.format("GBAFEStatDAO: hp %d, str %d, skl %d, spd %d, lck %d, def %d, res %d", hp, str, skl, spd, lck, def, res);
	}
}
