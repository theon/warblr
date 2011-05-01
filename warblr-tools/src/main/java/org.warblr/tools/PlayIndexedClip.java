package org.warblr.tools;

import net.bluecow.spectro.PlayerThread;
import org.warblr.core.indexing.IndexService;
import org.warblr.core.analysis.IndexedClip;

import javax.sound.sampled.LineUnavailableException;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
public class PlayIndexedClip {

    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        String clipName = "wren-song.wav";

        for(IndexedClip indexedClip: IndexService.getIndexedClipsPeaks(true)) {
            if(indexedClip.getClip().getName().equals(clipName)) {
                PlayerThread thread = new PlayerThread(indexedClip.getClip());
                thread.start();
                thread.startPlaying();

                while (thread.isPlaying()) {
                    //Wait
                    Thread.sleep(100);
                }
            }
        }

        System.exit(0);
    }

}
