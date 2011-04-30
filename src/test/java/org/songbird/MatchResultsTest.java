package org.songbird;

import net.bluecow.spectro.Clip;
import org.junit.Test;
import org.songbird.matching.ClipMatch;
import org.songbird.matching.MatcherService;

import javax.swing.*;
import java.io.File;
import java.net.URL;
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

    private static final String TEST_WAVS_DIR = "wavs";

    @Test
    public void testBlueTitQuality() throws Throwable {
        testQuality("gardenbirds", "blue-tit-alarm.wav", 0, 8.0f);
    }

    @Test
    public void testCoalTitQuality() throws Throwable {
        testQuality("gardenbirds", "coal-tit-song.wav", 0, 21.0f);
    }

    @Test
    public void testCollaredDoveQuality() throws Throwable {
        testQuality("gardenbirds", "collared-dove-song.wav", 0, 60.0f);
    }

    @Test
    public void testLesserSpottedWoodpeckerQuality() throws Throwable {
        testQuality("gardenbirds", "lesser-spotted-woodpecker-drum.wav", 0, 34.0f);
    }

    @Test
    public void testRobinQuality() throws Throwable {
        testQuality("gardenbirds", "robin-song.wav", 0, 32.0f);
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
        
        assertThat("Performance is too slow!", durationPerIteration < 500, is(true));
    }

    public void testQuality(String directory, String wavFilename, int expectedPosition, float expectedDistanceFromNextMatch) throws Throwable {
        List<ClipMatch> matches = runMatcher(directory, wavFilename);

        //Some debug
        System.out.println("====================");
        System.out.println("Testing: " + wavFilename);
        System.out.println("====================");

        for(ClipMatch debugMatch: matches) {
            System.out.println(debugMatch.getClip().getName() + "  (" + debugMatch.getMatchPercentage() + ")");
        }
        System.out.println("");

        assertThat("We got no results for " + wavFilename,
                matches.isEmpty(), is(false));

        ClipMatch match = matches.get(expectedPosition);

        assertThat(wavFilename + " didn't come back as the number " + expectedPosition + " result",
                match.getClip().getName(), is(equalTo(wavFilename)));

        if(expectedPosition < matches.size() -1) {
            double matchDistance = match.getMatchPercentage();
            double nextMatchDistance = matches.get(expectedPosition + 1).getMatchPercentage();
            double distanceFromBestMatch = matchDistance - nextMatchDistance;

             assertThat("The next match after the best for " + wavFilename + " is getting a bit close. It is only " + distanceFromBestMatch + " away from the best match. We at least than " + expectedDistanceFromNextMatch,
                (distanceFromBestMatch > expectedDistanceFromNextMatch), is(true));
        }
    }

    private List<ClipMatch> runMatcher(String directory, String wavFilename) throws Throwable {

        URL searchWavUrl = this.getClass().getClassLoader().getResource(TEST_WAVS_DIR + "/" + directory + "/" +  wavFilename);
        Clip searchClip = Clip.newInstance(new File(searchWavUrl.toURI()));
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(searchClip);

        return matches;
    }
}
