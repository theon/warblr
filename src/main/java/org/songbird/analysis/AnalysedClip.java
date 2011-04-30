package org.songbird.analysis;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.Frame;
import org.songbird.ClipPeakFinder;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 28/04/11
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class AnalysedClip {
    protected Clip clip;

    protected int frameSize;
    protected int numOfFrames;

    protected int highPassFrequencyThreshold;
    protected int lowPassFrequencyThreshold;

    private static final double AMPLITUDE_THRESHOLD = 0.3;
    public static final int MAX_NEAREST_PEAK_DIST = 5;

    public AnalysedClip(Clip clip, final int lowPassFrequencyThreshold, final int highPassFrequencyThreshold) {
        this.clip = clip;
        this.setFrameSize(clip.getFrameFreqSamples());
        this.setNumOfFrames(clip.getFrameCount());

        this.lowPassFrequencyThreshold = lowPassFrequencyThreshold;
        this.highPassFrequencyThreshold = highPassFrequencyThreshold;

        init();
        findPeaks(clip);
    }

    protected abstract void init();


    protected void findPeaks(Clip clip) {
        for(int col = 0; col < clip.getFrameCount(); col++) {
            Frame f = clip.getFrame(col);
            for (int row = 0; row < clip.getFrameFreqSamples(); row++) {
                if(row > lowPassFrequencyThreshold && row < highPassFrequencyThreshold) {
                    //double sourceFreqPow = Math.log1p(Math.abs(f.getReal(row)));
                    double amplitude = Math.abs(f.getReal(row));

                    if(amplitude > AMPLITUDE_THRESHOLD) {
                       handlePeak(col, row, amplitude);
                    }
                    else {
                        //Silence for preview
                        //f.setReal(row, 0.0);
                    }
                }
                else {
                    //Silence for preview
                    //f.setReal(row, 0.0);
                }
            }
        }
    }

    protected abstract void handlePeak(int frame, int frequency, double amplitude);

    public Clip getClip() {
        return clip;
    }

    public int getHighPassFrequencyThreshold() {
        return highPassFrequencyThreshold;
    }

    public int getLowPassFrequencyThreshold() {
        return lowPassFrequencyThreshold;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getNumOfFrames() {
        return numOfFrames;
    }

    public void setNumOfFrames(int numOfFrames) {
        this.numOfFrames = numOfFrames;
    }
}
