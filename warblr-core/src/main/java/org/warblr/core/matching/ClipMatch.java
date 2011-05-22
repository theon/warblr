package org.warblr.core.matching;

import net.bluecow.spectro.Clip;
import org.warblr.core.analysis.IndexedClip;
import org.warblr.core.analysis.SearchClip;

/**
 * Created by IntelliJ IDEA.
 * User: Ian
 * Date: 29/04/11
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
public class ClipMatch implements Comparable<ClipMatch> {
    private SearchClip searchClip;
    private IndexedClip indexedClip;

    private int indexFrame;
    private double matchPercentage;

    public ClipMatch(int indexFrame, double matchPercentage, SearchClip searchClip, IndexedClip indexedClip) {
        this.indexFrame = indexFrame;
        this.matchPercentage = matchPercentage;
        this.searchClip = searchClip;
        this.indexedClip = indexedClip;
    }

    public int getIndexFrame() {
        return indexFrame;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public SearchClip getSearchClip() {
        return searchClip;
    }

    public void setSearchClip(SearchClip searchClip) {
        this.searchClip = searchClip;
    }

    public int compareTo(ClipMatch o) {
        return Double.compare(o.matchPercentage, matchPercentage);
    }

    public IndexedClip getIndexedClip() {
        return indexedClip;
    }

    public void setIndexedClip(IndexedClip indexedClip) {
        this.indexedClip = indexedClip;
    }
}
