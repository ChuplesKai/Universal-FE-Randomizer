package fedata.gba;

import fedata.gba.GBAFEStatDto;

import util.YuneUtil;

/**
 * Shell class for loading custom class information
 */
public class GBAFEClassDto
{
    public String name; // Class Name used internally
    public GBAFEStatDto growths; // Class Growths DTO

    /*****************************************************************
     * Default Constructor - uninitialized
     ****************************************************************/
    public GBAFEClassDto()
    {
        name = "UNINITIALIZED";
    }

    /*****************************************************************
     * Parameterized Constructor
     ****************************************************************/
    public GBAFEClassDto( String name, GBAFEStatDto growths )
    {
        this.name = name;
        this.growths = growths;
    }
}