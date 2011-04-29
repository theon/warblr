package org.songbird.matching;

import net.bluecow.spectro.Clip;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
public class ClipMatch implements Comparable<ClipMatch> {
    private Clip clip;

    private int indexFrame;
    private double distance;

    public ClipMatch(int indexFrame, double distance, Clip clip) {
        this.indexFrame = indexFrame;
        this.distance = distance;
        this.clip = clip;
    }

    public int getIndexFrame() {
        return indexFrame;
    }

    public double getDistance() {
        return distance;
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public int compareTo(ClipMatch o) {
        return Double.compare(distance, o.distance);
    }
}
