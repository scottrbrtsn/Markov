package com.scott.vision;

/**
 * Created by SCOTTRBRTSN on 11/9/17.
 */

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;


public class CaptureImage {
    private static void captureFrame() {
        // 0-default camera, 1 - next...so on
        final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        CanvasFrame canvas = new CanvasFrame("Test");
        while(true) {
            try {
                grabber.start();
                Frame img = grabber.grab();
                if (img != null) {
                    canvas.showImage(img);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        captureFrame();
    }
}
