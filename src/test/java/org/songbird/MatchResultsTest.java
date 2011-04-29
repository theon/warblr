package org.songbird;

import net.bluecow.spectro.Clip;
import org.junit.Test;
import org.songbird.matching.ClipMatch;
import org.songbird.matching.MatcherService;

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
        testQuality("gardenbirds", "blue-tit-alarm.wav", 0, 5.0f);
    }

    @Test
    public void testCoalTitQuality() throws Throwable {
        testQuality("gardenbirds", "coal-tit-song.wav", 0, 20.0f);
    }

    @Test
    public void testCollaredDoveQuality() throws Throwable {
        testQuality("gardenbirds", "collared-dove-song.wav", 0, 30.0f);
    }

    @Test
    public void testLesserSpottedWoodpeckerQuality() throws Throwable {
        testQuality("gardenbirds", "lesser-spotted-woodpecker-drum.wav", 0, 15.0f);
    }

    @Test
    public void testRobinQuality() throws Throwable {
        testQuality("gardenbirds", "robin-song.wav", 0, 9.0f);
    }

    @Test
    public void testPerformance() throws Throwable {
        final int ITERATIONS = 5;
        final long time = System.currentTimeMillis();

        for(int i = 0; i< ITERATIONS; i++) {
            runMatcher("gardenbirds", "lesser-spotted-woodpecker-drum.wav");
        }

        long duration = System.currentTimeMillis() - time;

        assertThat("Performance is too slow!", duration < 4000, is(true));

        System.out.println("Took " + duration + "ms, " + (duration/ITERATIONS) + "ms per iteration");
    }

    public void testQuality(String directory, String wavFilename, int expectedPosition, float expectedDistanceFromNextMatch) throws Throwable {
        List<ClipMatch> matches = runMatcher(directory, wavFilename);

        assertThat("We got no results for " + wavFilename,
                matches.isEmpty(), is(false));

        ClipMatch expectedMatch = matches.get(expectedPosition);

        assertThat(wavFilename + " didn't come back as the number " + expectedPosition + " result",
                wavFilename, is(equalTo(expectedMatch.getClip().getName())));

        if(expectedPosition < matches.size() -1) {
            double matchDistance = expectedMatch.getMatchPercentage();
            double nextMatchDistance = matches.get(expectedPosition + 1).getMatchPercentage();
            double distanceFromBestMatch = matchDistance - nextMatchDistance;

             assertThat("The next match after the best for " + wavFilename + " is getting a bit close. It is only " + distanceFromBestMatch + " away from the best match. We at least than " + expectedDistanceFromNextMatch,
                (distanceFromBestMatch > expectedDistanceFromNextMatch), is(true));
        }

        //Some debug
        System.out.println("====================");
        System.out.println("Testing: " + wavFilename);
        System.out.println("====================");

        for(ClipMatch match: matches) {
            System.out.println(match.getClip().getName() + "  (" + match.getMatchPercentage() + ")");
        }
        System.out.println("");
    }

    private List<ClipMatch> runMatcher(String directory, String wavFilename) throws Throwable {

        URL searchWavUrl = this.getClass().getClassLoader().getResource(TEST_WAVS_DIR + "/" + directory + "/" +  wavFilename);
        Clip searchClip = Clip.newInstance(new File(searchWavUrl.toURI()));
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(searchClip);

        return matches;
    }
}
