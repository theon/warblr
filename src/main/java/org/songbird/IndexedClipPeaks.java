package org.songbird;

import net.bluecow.spectro.Clip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 28/04/11
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class IndexedClipPeaks {

    private Clip clip;
    private int numOfFrames;

    private int highPassFrequencyThreshold;
    private int lowPassFrequencyThreshold;

    private List<IndexedClipPeak> peaks;

    public IndexedClipPeaks(Clip clip, int lowPassFrequencyThreshold, int highPassFrequencyThreshold) {
        this.lowPassFrequencyThreshold = lowPassFrequencyThreshold;
        this.highPassFrequencyThreshold = highPassFrequencyThreshold;

        this.clip = clip;
        this.numOfFrames = clip.getFrameCount();
        this.peaks = new ArrayList<IndexedClipPeak>();

        new ClipPeakFinder() {
            @Override
            protected void handlePeak(int frame, int frequency, double amplitude) {
                peaks.add(new IndexedClipPeak(frame, frequency, amplitude));
            }
        }.findPeaks(clip, lowPassFrequencyThreshold, highPassFrequencyThreshold);
    }

    public int getNumOfFrames() {
        return numOfFrames;
    }

    public List<IndexedClipPeak> getPeaks() {
        return peaks;
    }

    public Clip getClip() {
        return clip;
    }

    public int getHighPassFrequencyThreshold() {
        return highPassFrequencyThreshold;
    }

    public int getLowPassFrequencyThreshold() {
        return lowPassFrequencyThreshold;
    }

    public static class IndexedClipPeak {
        private int frame;
        private int frequency;
        private double amplitude;

        public IndexedClipPeak(int frame, int frequency, double amplitude) {
            this.frame = frame;
            this.frequency = frequency;
            this.amplitude = amplitude;
        }

        public int getFrame() {
            return frame;
        }

        public int getFrequency() {
            return frequency;
        }

        public double getAmplitude() {
            return amplitude;
        }
    }
}
