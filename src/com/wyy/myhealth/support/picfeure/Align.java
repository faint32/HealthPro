package com.wyy.myhealth.support.picfeure;


//package mpi.fruitfly.registration;

/**
 * Align a stack consecutively using automatically extracted robust landmark
 * correspondences.
 * 
 * The plugin uses the Scale Invariant Feature Transform (SIFT) by David Lowe
 * \cite{Lowe04} and the Random Sample Consensus (RANSAC) by Fishler and Bolles
 * \citet{FischlerB81} to identify landmark correspondences.
 * 
 * It identifies a rigid transformation for the second of two slices that maps
 * the correspondences of the second optimally to those of the first.
 * 
 * BibTeX:
 * <pre>
 * &#64;article{Lowe04,
 *   author    = {David G. Lowe},
 *   title     = {Distinctive Image Features from Scale-Invariant Keypoints},
 *   journal   = {International Journal of Computer Vision},
 *   year      = {2004},
 *   volume    = {60},
 *   number    = {2},
 *   pages     = {91--110},
 * }
 * &#64;article{FischlerB81,
 *	 author    = {Martin A. Fischler and Robert C. Bolles},
 *   title     = {Random sample consensus: a paradigm for model fitting with applications to image analysis and automated cartography},
 *   journal   = {Communications of the ACM},
 *   volume    = {24},
 *   number    = {6},
 *   year      = {1981},
 *   pages     = {381--395},
 *   publisher = {ACM Press},
 *   address   = {New York, NY, USA},
 *   issn      = {0001-0782},
 *   doi       = {http://doi.acm.org/10.1145/358669.358692},
 * }
 * </pre>
 * 
 * License: GPL
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * NOTE:
 * The SIFT-method is protected by U.S. Patent 6,711,293: "Method and
 * apparatus for identifying scale invariant features in an image and use of
 * same for locating an object in an image" by the University of British
 * Columbia.  That is, for commercial applications the permission of the author
 * is required.
 *
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 * @version 0.1b
 */


import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import mpi.cbg.fly.Feature;
import mpi.cbg.fly.Filter;
import mpi.cbg.fly.FloatArray2D;
import mpi.cbg.fly.FloatArray2DSIFT;


public class Align {
    // steps
    private static int steps = 3;

    // initial sigma
    private static float initial_sigma = 1.6f;

    // feature descriptor size
    private static int fdsize = 4;

    // feature descriptor orientation bins
    private static int fdbins = 8;

    // size restrictions for scale octaves, use octaves < max_size and >
    // min_size only
    private static int min_size = 32;

    private static int max_size = 256;

    // minimal allowed alignment error in px
    @SuppressWarnings("unused")
	private static float min_epsilon = 2.0f;

    // maximal allowed alignment error in px
    @SuppressWarnings("unused")
	private static float max_epsilon = 100.0f;

    @SuppressWarnings("unused")
	private static float min_inlier_ratio = 0.05f;

    static final public int MIN_SET_SIZE = 2;

    public static final int MIN_FEATURE_NUMBER = 20;

  
//    public List<Feature> createHistogram(String path) {
//        ImagePlus imagePlus = new ImagePlus(path);
//        return createHistogram(imagePlus);
//    }
//    public List<Feature> createHistogram(BufferedImage image) {
//        ImagePlus imagePlus = new ImagePlus("" ,image);
//        return createHistogram(imagePlus);
//    }
//    public List<Feature> createHistogram(ImagePlus imagePlus) {
//
//        ImageProcessor ip = imagePlus.getProcessor().convertToFloat();
//
//        FloatArray2DSIFT sift = new FloatArray2DSIFT(fdsize, fdbins);
//
//        FloatArray2D fa = ImageArrayConverter.ImageToFloatArray2D(ip);
//        Filter.enhance(fa, 1.0f);
//        fa = Filter.computeGaussianFastMirror(fa,
//                (float) Math.sqrt(initial_sigma * initial_sigma - 0.25));
//        sift.init(fa, steps, initial_sigma, min_size, max_size);
//        List<Feature> fs = sift.run(max_size);
//        Collections.sort(fs);
//        return fs;
//    }
//    public List<Feature> createHistogram(BufferedImage image) {
//        FloatArray2DSIFT sift = new FloatArray2DSIFT(fdsize, fdbins);
//
//        FloatArray2D fa = convert(image);
//        Filter.enhance(fa, 1.0f);
//        fa = Filter.computeGaussianFastMirror(fa,
//                (float) Math.sqrt(initial_sigma * initial_sigma - 0.25));
//        sift.init(fa, steps, initial_sigma, min_size, max_size);
//        List<Feature> fs = sift.run(max_size);
//        Collections.sort(fs);
//        return fs;
//    }
    
    
    public List<Feature> createHistogram(Bitmap bitmap) {
        FloatArray2DSIFT sift = new FloatArray2DSIFT(fdsize, fdbins);

        FloatArray2D fa = convert(bitmap);
        Filter.enhance(fa, 1.0f);
        fa = Filter.computeGaussianFastMirror(fa,
                (float) Math.sqrt(initial_sigma * initial_sigma - 0.25));
        sift.init(fa, steps, initial_sigma, min_size, max_size);
        List<Feature> fs = sift.run(max_size);
        Collections.sort(fs);
        return fs;
    }
    
//    public  FloatArray2D convert( BufferedImage image ) {
//        FloatArray2D ret = new FloatArray2D(image.getWidth(),image.getHeight());
//
//        for( int y = 0; y < ret.height; y++ ) {
//                for( int x = 0; x < ret.width; x++ ) {
//                        int rgb = image.getRGB(x,y);
//                        float v = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF))/2;
//                        ret.set(v,x,y);
//                }
//        }
//
//        return ret;
//}
    
    
    public  FloatArray2D convert(Bitmap bitmap ) {
        FloatArray2D ret = new FloatArray2D(bitmap.getWidth(),bitmap.getHeight());
        int mheight=ret.height;
        int mweight=ret.width;
        int[] pixs=new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pixs, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for( int y = 0; y < mheight; y++ ) {
                for( int x = 0; x < mweight; x++ ) {
//                        int rgb = bitmap.getPixel(x, y);
                        int rgb = pixs[x+y*mheight];
                        float v = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF))/2;
                        ret.set(v,x,y);
                }
        }

        return ret;
}


}
