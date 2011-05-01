package org.warblr.core.util;

import net.bluecow.spectro.Clip;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 01/05/11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public final class TestClipUtils {

    private static final String TEST_WAVS_DIR = "org/warblr/core/wavs";

    private TestClipUtils() {
    }

    /**
     * @param directory
     * @param wavFilename
     * @return
     * @throws Throwable
     */
    public static Clip getTestSearchClip(String directory, String wavFilename) throws Throwable {
        URL searchWavUrl = TestClipUtils.class.getClassLoader().getResource(TEST_WAVS_DIR + "/" + directory + "/" + wavFilename);
        Clip searchClip = Clip.newInstance(new File(searchWavUrl.toURI()));

        return searchClip;
    }
}
