package org.songbird.tools;

import net.bluecow.spectro.SpectroEditSession;
import org.songbird.analysis.IndexedClip;
import org.songbird.matching.MatcherService;

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
        for(IndexedClip indexedClipPeak: MatcherService.INDEXED_CLIPS) {
            SpectroEditSession.createSession(indexedClipPeak.getClip());
        }
    }

}
