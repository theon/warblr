package org.warblr.tools;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.PlayerThread;
import org.warblr.core.indexing.IndexService;
import org.warblr.core.analysis.IndexedClip;
import org.warblr.core.analysis.SearchClip;
import org.warblr.core.util.TestClipUtils;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 30/04/11
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
public class PlaySearchClip {

    public static void main(String[] args) throws Throwable {
        String clipName = "swallow-song.wav";

        for(IndexedClip indexedClip: IndexService.getIndexedClipsPeaks(true)) {
            if(indexedClip.getClip().getName().equals(clipName)) {

                SearchClip searchClip = new SearchClip(TestClipUtils.getTestSearchClip("gardenbirds", clipName), indexedClip.getLowPassFrequencyThreshold(), indexedClip.getHighPassFrequencyThreshold(), true);

                PlayerThread thread = new PlayerThread(searchClip.getClip());
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
