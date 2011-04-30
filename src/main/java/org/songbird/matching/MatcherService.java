package org.songbird.matching;

import net.bluecow.spectro.Clip;
import org.songbird.analysis.AnalysedClip;
import org.songbird.IndexService;
import org.songbird.analysis.IndexedClip;
import org.songbird.analysis.SearchClip;

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

    public final static List<IndexedClip> INDEXED_CLIPS = IndexService.getIndexedClipsPeaks();

    private MatcherService() {
    }

    public static List<ClipMatch> getNearestPeakMatches(Clip sourceClip) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        List<ClipMatch> matches = new ArrayList<ClipMatch>();

        for(IndexedClip indexedClip: INDEXED_CLIPS) {
            SearchClip searchClip = new SearchClip(sourceClip/*.copy()*/, indexedClip.getLowPassFrequencyThreshold(), indexedClip.getHighPassFrequencyThreshold());

            ClipMatch match = getClosestPeakMatch(searchClip, indexedClip);

            if(match != null) {
                matches.add(match);
            }
        }

        Collections.sort(matches);

        return matches;
    }

    protected static ClipMatch getClosestPeakMatch(SearchClip searchClip, IndexedClip indexedClip) {
        //System.out.println("Getting closest peak match from: " + indexedClip.getClip().getName());

        int windowSize = indexedClip.getNumOfFrames();


        if(searchClip.getClip().getFrameCount() < windowSize) {
            windowSize = searchClip.getClip().getFrameCount();
        }

        final int lastStartCol = searchClip.getClip().getFrameCount() - windowSize;

        int lowestTotalDistance = AnalysedClip.MAX_NEAREST_PEAK_DIST * indexedClip.getPeaks().size();
        int lowestDistanceStartFrame = 0;

        for (int startCol = 0; startCol < lastStartCol; startCol += FRAME_SEARCH_INCREMENT) {

            int thisTotalDistance = 0;

            for(IndexedClip.IndexedClipPeak peak: indexedClip.getPeaks()) {
                thisTotalDistance += searchClip.getNearestPeakDistance(startCol + peak.getFrame(), peak.getFrequency());

                if(thisTotalDistance > lowestTotalDistance) {
                    break;
                }
            }

            if(thisTotalDistance < lowestTotalDistance) {
                lowestTotalDistance = thisTotalDistance;
                lowestDistanceStartFrame = startCol;
            }
        }

        double lowestAvgDistance = ((double)lowestTotalDistance/(double) indexedClip.getPeaks().size());
        double matchPercentage = (1 - (lowestAvgDistance/ AnalysedClip.MAX_NEAREST_PEAK_DIST)) * 100;
        return new ClipMatch(lowestDistanceStartFrame, matchPercentage, indexedClip.getClip());
    }
}
