package org.warblr.core.analysis;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.Frame;

import java.util.TreeSet;

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

    private static final int AMPLITUDE_THRESHOLD = 5;
    public static final int MAX_NEAREST_PEAK_DIST = 6;

    public AnalysedClip(Clip clip, final int lowPassFrequencyThreshold, final int highPassFrequencyThreshold, boolean blankAudioDataNotAnalysed) {
        this.clip = clip;
        this.setFrameSize(clip.getFrameFreqSamples());
        this.setNumOfFrames(clip.getFrameCount());

        this.lowPassFrequencyThreshold = lowPassFrequencyThreshold;
        this.highPassFrequencyThreshold = highPassFrequencyThreshold;

        init();
        findPeaks(clip, blankAudioDataNotAnalysed);
    }

    protected abstract void init();

    protected double findMaxAmp(Clip clip) {
        double max = 0;

        for(int col = 0; col < clip.getFrameCount(); col++) {
            Frame f = clip.getFrame(col);
            for (int row = 0; row < clip.getFrameFreqSamples(); row++) {
                double amplitude = Math.abs(f.getReal(row));

                if(amplitude > max) {
                    max = amplitude;
                }
            }
        }

        return max;
    }

    public void findPeaks(Clip clip, boolean blankAudioDataNotAnalysed) {
        double maxAmp = findMaxAmp(clip);

        for(int col = 0; col < clip.getFrameCount(); col++) {
            Frame f = clip.getFrame(col);
            for (int row = 0; row < clip.getFrameFreqSamples(); row++) {
                if(row > lowPassFrequencyThreshold && row < highPassFrequencyThreshold) {
                    //double sourceFreqPow = Math.log1p(Math.abs(f.getReal(row)));
                    double amplitude = Math.abs(f.getReal(row));
                    int normalisedAmp = (int)((amplitude/maxAmp) * 100);


                    if(normalisedAmp > AMPLITUDE_THRESHOLD) {
                       handlePeak(col, row, normalisedAmp);
                    }
                    else if(blankAudioDataNotAnalysed) {
                        //Silence for preview
                        f.setReal(row, 0.0);
                    }
                }
                else if(blankAudioDataNotAnalysed) {
                    //Silence for preview
                    f.setReal(row, 0.0);
                }
            }
        }
    }

    protected abstract void handlePeak(int frame, int frequency, int amplitude);

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
    
    @Override
    public String toString() {
        return clip.getName();
    }
}
