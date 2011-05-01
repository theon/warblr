package org.warblr.tools;

import net.bluecow.spectro.SpectroEditSession;
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
public class ViewIndexedSpectrogams {

    public static void main(String[] args) throws LineUnavailableException {
        String clipName = "blackbird-song-1.wav";

        for(IndexedClip indexedClip: IndexService.getIndexedClipsPeaks(true)) {
            if(indexedClip.getClip().getName().equals(clipName)) {
                SpectroEditSession.createSession(indexedClip.getClip());
                break; //Linux can only handle one at a time :(
            }
        }
    }

}
