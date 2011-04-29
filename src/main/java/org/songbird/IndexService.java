package org.songbird;

import net.bluecow.spectro.Clip;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
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

    public static List<IndexedClipPeaks> getIndexedClipsPeaks() throws UnsupportedAudioFileException, IOException {
        final String INDEX_WAV_DIR = "C:\\Users\\Ian\\IdeaProjects\\songbird\\src\\main\\resources\\wavs\\index";

        List<IndexedClipPeaks> indexedClips = new ArrayList<IndexedClipPeaks>();

        //TODO: Move to an external configuration file
        indexedClips.add(new IndexedClipPeaks(Clip.newInstance(new File(INDEX_WAV_DIR + "\\bbc\\collared-dove-song.wav")), 0, 50));
        indexedClips.add(new IndexedClipPeaks(Clip.newInstance(new File(INDEX_WAV_DIR + "\\bbc\\blue-tit-alarm.wav")), 50, 300));
        indexedClips.add(new IndexedClipPeaks(Clip.newInstance(new File(INDEX_WAV_DIR + "\\bbc\\coal-tit-song.wav")), 50, 300));
        indexedClips.add(new IndexedClipPeaks(Clip.newInstance(new File(INDEX_WAV_DIR + "\\bbc\\lesser-spotted-woodpecker-drum.wav")), 25, 150));
        indexedClips.add(new IndexedClipPeaks(Clip.newInstance(new File(INDEX_WAV_DIR + "\\bbc\\robin-song.wav")), 50, 150));

        return indexedClips;
    }
}
