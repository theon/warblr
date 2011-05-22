package org.warblr.core;

import net.bluecow.spectro.Clip;
import org.junit.Test;
import org.warblr.core.matching.ClipMatch;
import org.warblr.core.matching.MatcherService;
import org.warblr.core.util.TestClipUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Ensures the quality and performance of match results
 *
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 08:16
 * To change this template use File | Settings | File Templates.
 */
public class MatchResultsTest {

    @Test
    public void testBlueTitQuality() throws Throwable {
        testQuality("gardenbirds", "blue-tit-alarm.wav", 0, 8.0f);
    }

    @Test
    public void testCoalTitQuality() throws Throwable {
        testQuality("gardenbirds", "coal-tit-song.wav", 0, 11.0f);
    }

    @Test
    public void testCollaredDoveQuality() throws Throwable {
        testQuality("gardenbirds", "collared-dove-song.wav", 0, 25.0f);
    }

    @Test
    public void testLesserSpottedWoodpeckerQuality() throws Throwable {
        testQuality("gardenbirds", "lesser-spotted-woodpecker-drum.wav", 0, 0.5f);
    }

    @Test
    public void testRobinQuality() throws Throwable {
        testQuality("gardenbirds", "robin-song.wav", 0, 9.0f);
    }

    @Test
    public void testBlackbirdQuality() throws Throwable {
        testQuality("android", "blackbird-song.wav", 0, 10.0f);
        testQuality("gardenbirds", "blackbird-song.wav", 0, 10.0f);
    }

    /**
     * 
     * @throws Throwable
     */
    @Test
    public void testWoodPigeonQuality() throws Throwable {
        testQuality("gardenbirds", "wood-pigeon-song.wav", 1, 0.0f);
        testQuality("android", "wood-pigeon-song.wav", 1, 0.0f);
        testQuality("tenxfifty", "wood-pigeon-song.wav", 1, 0.0f);
    }


    @Test
    public void testPerformance() throws Throwable {
        //String someString = JOptionPane.showInputDialog("A naff way to pause to get time to get the profiler running");

        final int ITERATIONS = 10;
        final long time = System.currentTimeMillis();

        for(int i = 0; i< ITERATIONS; i++) {
            runMatcher("gardenbirds", "blue-tit-alarm.wav");
        }

        long duration = System.currentTimeMillis() - time;
        long durationPerIteration = (duration/ITERATIONS);

        System.out.println("Took " + duration + "ms, " + durationPerIteration + "ms per iteration");
        
        assertThat("Performance is too slow!", durationPerIteration < 200, is(true));
    }

    public void testQuality(String directory, String wavFilename, int expectedPosition, float expectedDistanceFromNextMatch) throws Throwable {
        List<ClipMatch> matches = runMatcher(directory, wavFilename);
        String wavFilenamePrefix = wavFilename.replace(".wav", "");

        //Some debug
        System.out.println("====================");
        System.out.println("Testing: " + wavFilename);
        System.out.println("====================");

        for(ClipMatch debugMatch: matches) {
            System.out.println(debugMatch.getIndexedClip().getClip().getName() + "  (" + debugMatch.getMatchPercentage() + ")");
        }
        System.out.println("");

        assertThat("We got no results for " + wavFilename,
                matches.isEmpty(), is(false));

        ClipMatch match = matches.get(expectedPosition);


        assertThat(wavFilename + " didn't come back as the number " + expectedPosition + " result",
                match.getIndexedClip().getClip().getName().startsWith(wavFilenamePrefix), is(equalTo(true)));

        for(int nextMatchPosition = expectedPosition + 1; nextMatchPosition < matches.size(); nextMatchPosition++) {

            ClipMatch nextMatch = matches.get(nextMatchPosition);
            if(!nextMatch.getIndexedClip().getClip().getName().startsWith(wavFilenamePrefix)) {
                double matchDistance = match.getMatchPercentage();

                double nextMatchDistance = nextMatch.getMatchPercentage();
                double distanceFromBestMatch = matchDistance - nextMatchDistance;

                 assertThat("The next match after the best for " + wavFilename + " is getting a bit close. It is only " + distanceFromBestMatch + " away from the best match. We at least than " + expectedDistanceFromNextMatch,
                    (distanceFromBestMatch > expectedDistanceFromNextMatch), is(true));
            }
        }
    }

    private List<ClipMatch> runMatcher(String directory, String wavFilename) throws Throwable {
        Clip searchClip = TestClipUtils.getTestSearchClip(directory, wavFilename);
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(searchClip, false);
        return matches;
    }
}
