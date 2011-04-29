package org.songbird.matching;

import net.bluecow.spectro.Clip;
import org.songbird.ClipPeakMap;
import org.songbird.IndexService;
import org.songbird.IndexedClipPeaks;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
public final class MatcherService {

    public static final int FRAME_SEARCH_INCREMENT = 10;

    public final static List<IndexedClipPeaks> INDEXED_CLIPS = getIndexedClips();

    private MatcherService() {
    }

    public static List<ClipMatch> getNearestPeakMatches(Clip sourceClip) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        List<ClipMatch> matches = new ArrayList<ClipMatch>();

        for(IndexedClipPeaks indexedClipPeak: INDEXED_CLIPS) {
            ClipPeakMap sourcePeakMap = new ClipPeakMap(sourceClip.copy(), indexedClipPeak.getLowPassFrequencyThreshold(), indexedClipPeak.getHighPassFrequencyThreshold());

            ClipMatch match = getClosestPeakMatch(sourcePeakMap, indexedClipPeak);

            if(match != null) {
                matches.add(match);
            }
        }

        Collections.sort(matches);

        return matches;
    }

    protected static ClipMatch getClosestPeakMatch(ClipPeakMap sourcePeakMap, IndexedClipPeaks indexedClipPeaks) {
        System.out.println("Getting closest peak match from: " + indexedClipPeaks.getClip().getName());

        int windowSize = indexedClipPeaks.getNumOfFrames();


        if(sourcePeakMap.getClip().getFrameCount() < windowSize) {
            windowSize = sourcePeakMap.getClip().getFrameCount();
        }

        final int lastStartCol = sourcePeakMap.getClip().getFrameCount() - windowSize;

        int lowestTotalDistance = Integer.MAX_VALUE;
        int lowestDistanceStartFrame = 0;

        for (int startCol = 0; startCol < lastStartCol; startCol += FRAME_SEARCH_INCREMENT) {

            int thisTotalDistance = 0;

            for(IndexedClipPeaks.IndexedClipPeak peak: indexedClipPeaks.getPeaks()) {
                thisTotalDistance += sourcePeakMap.getNearestPeakDistance(startCol + peak.getFrame(), peak.getFrequency());

                if(thisTotalDistance > lowestTotalDistance) {
                    break;
                }
            }

            if(thisTotalDistance < lowestTotalDistance) {
                lowestTotalDistance = thisTotalDistance;
                lowestDistanceStartFrame = startCol;
            }
        }

        double lowestAvgDistance = ((double)lowestTotalDistance/(double)indexedClipPeaks.getPeaks().size());
        return new ClipMatch(lowestDistanceStartFrame, lowestAvgDistance, indexedClipPeaks.getClip());
    }

    private static List<IndexedClipPeaks> getIndexedClips() {
        try {
            return IndexService.getIndexedClipsPeaks();
        } catch (Exception e) {
            return null;
        }
    }
}
