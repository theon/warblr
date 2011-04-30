package org.songbird.analysis;

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
public class IndexedClip extends AnalysedClip {

    private List<IndexedClipPeak> peaks;

    public IndexedClip(Clip clip, final int lowPassFrequencyThreshold, final int highPassFrequencyThreshold) {
        super(clip, lowPassFrequencyThreshold, highPassFrequencyThreshold);
    }

    @Override
    protected void init() {
        this.setPeaks(new ArrayList<IndexedClipPeak>());
    }

    @Override
    protected void handlePeak(int frame, int frequency, double amplitude) {
        getPeaks().add(new IndexedClipPeak(frame, frequency, amplitude));
    }

    public List<IndexedClipPeak> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<IndexedClipPeak> peaks) {
        this.peaks = peaks;
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
