package org.songbird;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.PlayerThread;
import net.bluecow.spectro.SpectroEditSession;
import org.songbird.matching.ClipMatch;
import org.songbird.matching.MatcherService;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 28/04/11
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        peformSearch();

        //playIndexClips();

    }

    private static void playIndexClips() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        List<IndexedClipPeaks> indexedClipPeaks = IndexService.getIndexedClipsPeaks();

        PlayerThread resultPlayerThread = new PlayerThread(indexedClipPeaks.get(0).getClip());

        resultPlayerThread.start();
        resultPlayerThread.startPlaying();

        Thread.sleep(5000);
        System.exit(1);
    }

    public static void peformSearch() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        //String someString = JOptionPane.showInputDialog("Enter someString");

        Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\gardenbirds\\blue-tit-alarm.wav"));
        //Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\gardenbirds\\robin-song.wav"));
        //Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\gardenbirds\\coal-tit-song.wav"));
        //Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\gardenbirds\\collared-dove-song.wav"));
        //Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\jay-song.wav"));
        //Clip sourceClip = Clip.newInstance(new File("C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\test\\resources\\wavs\\gardenbirds\\lesser-spotted-woodpecker-drum.wav"));

        //List<ClipMatch> matches = getMatches(sourceClip);
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(sourceClip);

        for(ClipMatch match: matches) {
            SpectroEditSession.createSession(match.getClip());
        }

        for(ClipMatch match: matches) {
            System.out.println(match.getClip().getName() + " (" + match.getIndexFrame() + ") = " + match.getMatchPercentage());
        }

        ClipMatch bestMatch = matches.get(0);
        PlayerThread resultPlayerThread = new PlayerThread(sourceClip);
        resultPlayerThread.setPlaybackPosition(sourceClip.getClipLength(bestMatch.getIndexFrame()), bestMatch.getClip().getClipLength());

        //PlayerThread resultPlayerThread = new PlayerThread(bestMatch.getClip());

        resultPlayerThread.start();
        resultPlayerThread.startPlaying();
    }
}
