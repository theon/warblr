package org.songbird;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.Frame;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 28/04/11
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class ClipPeakMap {
    private HashMap<Integer,Double> peaks;
    private Clip clip;

    private int frameSize;
    private int numOfFrames;

    private static final int MAX_NEAREST_PEAK_DIST = 10;

    private static final float DISTANCE_FOR_FRAME_INCREMENT = 1f;
    private static final float DISTANCE_FOR_FREQ_INCREMENT = 4f;

    public ClipPeakMap(Clip clip, int lowPassFrequencyThreshold, int highPassFrequencyThreshold) {
        this(clip.getFrameFreqSamples(), clip.getFrameCount());
        this.clip = clip;

        new ClipPeakFinder() {
            @Override
            protected void handlePeak(int frame, int frequency, double amplitude) {
                peaks.put(getLookupKey(frame, frequency), amplitude);
            }
        }.findPeaks(clip, lowPassFrequencyThreshold, highPassFrequencyThreshold);
    }

    protected ClipPeakMap(int frameSize, int numOfFrames) {
        peaks = new HashMap<Integer,Double>();
        this.frameSize = frameSize;
        this.numOfFrames = numOfFrames;
    }

    public int getNearestPeakDistance(int frame, int frequency) {
        int distance = 0;
        boolean found = false;

        if(isPeak(frame, frequency)) {
            return distance;
        }

        while(!found && distance < MAX_NEAREST_PEAK_DIST) {

            distance++;

            if(distance % DISTANCE_FOR_FRAME_INCREMENT == 0) {
                final int frameDistance = (int)((float)distance / DISTANCE_FOR_FRAME_INCREMENT);
                final int startFrame = Math.max(frameDistance - distance + 1, 0);
                final int endFrame = Math.min(frameDistance + distance - 1, numOfFrames);

                for(int searchFrame = startFrame; searchFrame < endFrame; searchFrame++) {
                    if(isPeak(searchFrame, frequency + distance) || isPeak(searchFrame, frequency - distance)) {
                        found = true;
                        break;
                    }
                }

                if(found) {
                    break;
                }
            }

            if(distance % DISTANCE_FOR_FREQ_INCREMENT == 0) {
                final int freqDistance = (int)((float)distance / DISTANCE_FOR_FREQ_INCREMENT);
                final int startFrequency = Math.max(freqDistance - distance, 0);
                final int endFrequency = Math.min(freqDistance + distance, frameSize);

                for(int searchFrequency = startFrequency; searchFrequency < endFrequency; searchFrequency++) {
                    if(isPeak(frame - distance, searchFrequency) || isPeak(frame + distance, searchFrequency)) {
                        found = true;
                        break;
                    }
                }
            }

        }

        return found? distance: MAX_NEAREST_PEAK_DIST;
    }

    public double getAmplitude(int frame, int frequency) {
        return peaks.get(getLookupKey(frame, frequency));
    }

    public boolean isPeak(int frame, int frequency) {
        return peaks.containsKey(getLookupKey(frame, frequency));
    }

    private Integer getLookupKey(int frame, int frequency) {
        return frame * frameSize + frequency;
    }

    public Clip getClip() {
        return clip;
    }
}
