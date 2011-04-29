package org.songbird;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.Frame;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 28/04/11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class ClipPeakFinder {

    private static final double AMPLITUDE_THRESHOLD = 0.3;

    public void findPeaks(Clip clip, int lowPassFreqThreshold, int highPassFreqThreshold) {
        for(int col = 0; col < clip.getFrameCount(); col++) {
            Frame f = clip.getFrame(col);
            for (int row = 0; row < clip.getFrameFreqSamples(); row++) {
                if(row > lowPassFreqThreshold && row < highPassFreqThreshold) {
                    //double sourceFreqPow = Math.log1p(Math.abs(f.getReal(row)));
                    double amplitude = Math.abs(f.getReal(row));

                    if(amplitude > AMPLITUDE_THRESHOLD) {
                       handlePeak(col, row, amplitude);
                    }
                    else {
                        //Silence for preview
                        f.setReal(row, 0.0);
                    }
                }
                else {
                    //Silence for preview
                    f.setReal(row, 0.0);
                }
            }
        }
    }

    protected abstract void handlePeak(int frame, int frequency, double amplitude);
}
