package org.songbird;

import net.bluecow.spectro.Clip;

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

    private final static String INDEX_WAV_DIR = "wavs/index";

    public static List<IndexedClipPeaks> getIndexedClipsPeaks() {

        List<IndexedClipPeaks> indexedClips = new ArrayList<IndexedClipPeaks>();

        try {
            //TODO: Move to an external configuration file
            indexedClips.add(getIndexedClipPeaks("bbc/collared-dove-song.wav", 0, 50));
            indexedClips.add(getIndexedClipPeaks("bbc/blue-tit-alarm.wav", 50, 300));
            indexedClips.add(getIndexedClipPeaks("bbc/coal-tit-song.wav", 50, 300));
            indexedClips.add(getIndexedClipPeaks("bbc/lesser-spotted-woodpecker-drum.wav", 25, 150));
            indexedClips.add(getIndexedClipPeaks("bbc/robin-song.wav", 50, 150));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return indexedClips;
    }

    private static IndexedClipPeaks getIndexedClipPeaks(String indexWavFile, int lowPassFrequencyThreshold, int highPassFrequencyThreshold) throws URISyntaxException, UnsupportedAudioFileException, IOException {
        URL indexWavUrl = IndexService.class.getClassLoader().getResource(INDEX_WAV_DIR + "/" + indexWavFile);
        return new IndexedClipPeaks(Clip.newInstance(new File(indexWavUrl.toURI())), lowPassFrequencyThreshold, highPassFrequencyThreshold);
    }
}
