package random.general;

import java.util.Random;

/*********************************************************************
 ********************************************************************/
public class FERandom extends Random 
{
    // An enumeration for which distribution to draw from
    public enum randDist
    {
        UNIFORM,
        TRIANGLE
    }

    // Private Members
    private boolean useMemory;
    private int trend;
    private randDist distribution;

    /*****************************************************************
     * Basic Constructor for this updated FE Random Module.
     ****************************************************************/
    public FERandom( long seed )
    {
        super( seed );
        distribution = randDist.UNIFORM; //Default to uniform
        trend = 0;
        useMemory = false;
    }

    /*****************************************************************
     * Re-initialize the parameters of this random object to set the
     *  desired distribution and memory settings.
     ****************************************************************/
    public void initialize( randDist inDistribution, boolean inMemory )
    {
        distribution = inDistribution;
        useMemory = inMemory;
    }

    /*****************************************************************
     * sample - draw a sample from the active distribution.
     ****************************************************************/
    public int sample( int maxDev )
    {
        // Sample based on the chosen/current Distribution
        switch( distribution )
        {
        case UNIFORM:
            return sampleUniform( maxDev );
        case TRIANGLE:
            return sampleTriangle( maxDev );
        }
        // We shouldn't ever fall through to this, but a default 0
        return 0;
    }

    /*****************************************************************
     * sampleRange - draw a sample from the active distribution.
     ****************************************************************/
    public int sampleRange( int minimum, int maximum )
    {
        // First, check that the input makes any sense
        if( minimum > maximum ) return 0;
        // Need to compute the midpoint, and see if it is whole
        double midpoint = (double)(minimum + maximum)/2.0;
        int offset = (int)Math.floor( midpoint );
        // Then, we'll do different things based on the distribution
        switch( distribution )
        {
        case UNIFORM:
            return sampleUniformRange( minimum, maximum, offset );
        case TRIANGLE:
            int sample = sampleTriangleRange( minimum, maximum, offset );
            if( midpoint > (double)(offset + 0.1) ) sample += nextInt(1);
            return sample;
        }
        // We shouldn't end up here, but return something just in case
        return minimum;
    }


    /*****************************************************************
     * sampleTriangle - draw a sample from a triangular distribution
     *  centered on 0 within a maximum deviation.
     ****************************************************************/
    public int sampleTriangle( int maxDev )
    {
        int rone = __st( maxDev );
        int rtwo = __st( maxDev );
        return __decide( rone, rtwo, maxDev );
    }


    /*****************************************************************
     * sampleUniform - draw a sample from a uniform distribution
     *  centered on 0 within a maximum deviation.
     ****************************************************************/
    public int sampleUniform( int maxDev )
    {
        int rone = __su( maxDev );
        int rtwo = __su( maxDev );
        return __decide( rone, rtwo, maxDev );
    }


    /*****************************************************************
     * sampleUniformRange - draw a sample from a uniform distribution
     *  within a specified range.
     ****************************************************************/
    public int sampleUniformRange( int minimum, int maximum, int offset )
    {
        // Draw the numbers from uniform
        int rone = nextInt( maximum - minimum + 1 );
        int rtwo = nextInt( maximum - minimum + 1 );
        return __decide( rone - offset, rtwo - offset, maximum - offset ) + offset;
    }


    /*****************************************************************
     * sampleTriangleRange - draw a sample from a uniform distribution
     *  within a specified range.
     ****************************************************************/
    public int sampleTriangleRange( int minimum, int maximum, int offset )
    {
        int rone = __st( offset - minimum );
        int rtwo = __st( offset - minimum );
        return __decide( rone, rtwo, maximum - offset ) + offset;
    }


    /*****************************************************************
     * Internal function to decide the result based on whether or not
     *  the FERandom module is using memory.  If not, just returns the
     *  first number.  Automatically tracks good and bad trends.
     ****************************************************************/
    private int __decide( int rone, int rtwo, int maxDev )
    {
        // Default answer is the first number
        int outcome = rone;
        // If we're using memory, we'll do something much more fancy.
        if( useMemory )
        {
            // We'll need an outcome and a threshold to decide "goodness"
            int thresh = (int)Math.ceil( maxDev / 2.0 ); //
            // First, use the memory results to decide the outcome
            if( trend == 2 ) outcome = Math.min( rone, rtwo ); // Take the smaller
            if( trend == -2 ) outcome = Math.max( rone, rtwo ); // Take the larger
            //Then, update trend based on outcome being good or bad
            if( outcome >= thresh ) trend = Math.min( trend + 1, 2 );
            if( outcome <= -thresh ) trend = Math.max( trend - 1, -2 );
            //System.out.println( String.format("Result [%d (%d,%d)]: Max/Thresh [%d - %d] with Trend [%d]", outcome, rone, rtwo, maxDev, thresh, trend) );
        }
        return outcome;
    }

    /*****************************************************************
     * Internal Triangular Distribution sample function.
     ****************************************************************/
    private int __st( int maxDev )
    {
        return ( nextInt(maxDev + 1) + nextInt(maxDev + 1) - maxDev );
    }

    /*****************************************************************
     * Internal Uniform Distribution sample function.
     ****************************************************************/
    private int __su( int maxDev )
    {
        return nextInt(2 * maxDev + 1) - maxDev;
    }

}