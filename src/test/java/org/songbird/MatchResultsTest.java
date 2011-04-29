package org.songbird;

import net.bluecow.spectro.Clip;
import org.junit.Test;
import org.songbird.matching.ClipMatch;
import org.songbird.matching.MatcherService;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
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

    //TODO - make this relative, get resource from classpath
    private static final String TEST_WAVS_DIR = "C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs";

    @Test
    public void testBlueTitQuality() throws Throwable {
        testQuality("gardenbirds", "blue-tit-alarm.wav", 0, 7.0f);
    }

    @Test
    public void testCoalTitQuality() throws Throwable {
        testQuality("gardenbirds", "coal-tit-song.wav", 0, 8.7f);
    }

    @Test
    public void testCollaredDoveQuality() throws Throwable {
        testQuality("gardenbirds", "collared-dove-song.wav", 0, 3.9f);
    }

    @Test
    public void testLesserSpottedWoodpeckerQuality() throws Throwable {
        testQuality("gardenbirds", "lesser-spotted-woodpecker-drum.wav", 0, 4.9f);
    }

    @Test
    public void testRobinQuality() throws Throwable {
        testQuality("gardenbirds", "robin-song.wav", 0, 6.3f);
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

    public void testQuality(String directory, String wavFilename, int expectedPosition, float expectedScore) throws Throwable {
        List<ClipMatch> matches = runMatcher(directory, wavFilename);

        assertThat("We got no results for " + wavFilename,
                matches.isEmpty(), is(false));

        ClipMatch expectedMatch = matches.get(expectedPosition);

        assertThat(wavFilename + " didn't come back as the number " + expectedPosition + " result",
                wavFilename, is(equalTo(expectedMatch.getClip().getName())));

        double matchDistance = expectedMatch.getDistance();
        System.out.println("Match distance is: " + matchDistance);

        assertThat(wavFilename + " had a score of " + matchDistance + ". We expected less than " + expectedScore,
                (matchDistance < expectedScore), is(true));
    }

    private List<ClipMatch> runMatcher(String directory, String wavFilename) throws Throwable {

        Clip searchClip = Clip.newInstance(new File(TEST_WAVS_DIR + "\\" + directory + "\\" + wavFilename));
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(searchClip);


        return matches;
    }
}
