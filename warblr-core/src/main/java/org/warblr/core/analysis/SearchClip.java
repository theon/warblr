package org.warblr.core.analysis;

import net.bluecow.spectro.Clip;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 30/04/11
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class SearchClip extends AnalysedClip {

    private int[][] peaks;

    public SearchClip(Clip clip, final int lowPassFrequencyThreshold, final int highPassFrequencyThreshold, boolean blankAudioDataNotAnalysed) {
        super(clip, lowPassFrequencyThreshold, highPassFrequencyThreshold, blankAudioDataNotAnalysed);
    }

    @Override
    protected void init() {
        peaks = new int[getNumOfFrames()][highPassFrequencyThreshold - lowPassFrequencyThreshold];
    }

    @Override
    protected void handlePeak(int frame, int frequency, int amplitude) {
        peaks[frame][frequency - lowPassFrequencyThreshold] = (amplitude * 100);
    }

    public int getNearestPeakDistance(int frame, int frequency) {
        boolean found = false;
        int distance = 0;

        for(; distance <= MAX_NEAREST_PEAK_DIST; distance++) {

            final int startFrame = Math.max(frame - distance, 0);
            final int endFrame = Math.min(frame + distance, getNumOfFrames());

            for(int searchFrame = startFrame; searchFrame < endFrame; searchFrame++) {

                if(isPeak(searchFrame, frequency)) {
                    found = true;
                    break;
                }
            }

            if(found) {
                break;
            }
        }

        return distance;
    }

    public double getAmplitude(int frame, int frequency) {
        return peaks[frame][frequency - getLowPassFrequencyThreshold()];
    }

    public boolean isPeak(int frame, int frequency) {
        return getAmplitude(frame, frequency) > 0;
    }
}
