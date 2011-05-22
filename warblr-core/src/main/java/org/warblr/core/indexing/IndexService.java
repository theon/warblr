package org.warblr.core.indexing;

import net.bluecow.spectro.Clip;
import org.warblr.core.analysis.IndexedClip;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public final class IndexService {

    private IndexService() {}

    private final static String INDEX_WAV_DIR = "org/warblr/core/wavs/index";

    public static List<IndexedClip> getIndexedClipsPeaks(boolean blankAudioDataNotAnalysed) {

        List<IndexedClip> indexedClips = new ArrayList<IndexedClip>();

        try {
            //TODO: Move to an external configuration file
            indexedClips.add(getIndexedClipPeaks("bbc/collared-dove-song.wav", 0, 50, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/wood-pigeon-song.wav", 10, 75, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/blue-tit-alarm.wav", 50, 300, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/coal-tit-song.wav", 50, 300, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/lesser-spotted-woodpecker-drum.wav", 0, 150, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/robin-song.wav", 50, 150, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/blackbird-song-1.wav", 50, 150, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/blackbird-song-2.wav", 50, 150, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/blackbird-song-3.wav", 50, 200, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/song-thrush-song-1.wav", 100, 150, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/song-thrush-song-2.wav", 50, 150, blankAudioDataNotAnalysed));
            //indexedClips.add(getIndexedClipPeaks("bbc/song-thrush-song-3.wav", 100, 250, blankAudioDataNotAnalysed)); //Clashes with robin
            indexedClips.add(getIndexedClipPeaks("bbc/song-thrush-song-4.wav", 100, 175, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/swallow-song.wav", 50, 200, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/swift-song.wav", 250, 300, blankAudioDataNotAnalysed));
            indexedClips.add(getIndexedClipPeaks("bbc/wren-song.wav", 200, 300, blankAudioDataNotAnalysed));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return indexedClips;
    }

    private static IndexedClip getIndexedClipPeaks(String indexWavFile, int lowPassFrequencyThreshold, int highPassFrequencyThreshold, boolean blankAudioDataNotAnalysed) throws URISyntaxException, UnsupportedAudioFileException, IOException {
        URL indexWavUrl = IndexService.class.getClassLoader().getResource(INDEX_WAV_DIR + "/" + indexWavFile);
        return new IndexedClip(Clip.newInstance(new File(indexWavUrl.toURI())), lowPassFrequencyThreshold, highPassFrequencyThreshold, blankAudioDataNotAnalysed);
    }
}
