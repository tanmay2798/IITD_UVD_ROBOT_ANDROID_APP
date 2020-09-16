/*
 * Copyright (c) 2015, Tal Regev
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Android Sensors Driver nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.ros.android.android_tutorial_cv_bridge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.ros.android.RosActivity;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import cv_bridge.CvImage;
import cv_bridge.Format;
import sensor_msgs.CompressedImage;
import sensor_msgs.ImageEncodings;


/**
 * @author Tal Regev
 */
@SuppressWarnings("WeakerAccess")
public class MainActivityCompressedJavacv extends RosActivity implements NodeMain{

    protected Publisher<CompressedImage> imagePublisher;
    protected Subscriber<CompressedImage> imageSubscriber;
    protected ConnectedNode node;
    protected static final String TAG = "compressed Tutorial";
    protected Bitmap bmp;
    protected ImageView imageView;
    protected Runnable displayImage;
    Button throttle1,left1,right1,back1,stop1,mark1;
    NodeConfiguration nodeConfiguration;
    public int status = 0;
    public int mark_status = 0;


    public MainActivityCompressedJavacv() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("micron Tutorial", "micron Tutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        imageView = (ImageView) findViewById(R.id.imageView);
        throttle1 = findViewById(R.id.throttle);
        left1 = findViewById(R.id.left);
        right1 = findViewById(R.id.right);
        back1 = findViewById(R.id.back);
        stop1 = findViewById(R.id.stop);
        mark1 = findViewById(R.id.mark);
        displayImage = new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                imageView.setImageBitmap(bmp);
            }
        };
    }

    @Override
    protected void init(final NodeMainExecutor nodeMainExecutor) {
        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.

        final throttle t1 = new throttle(this);
        final mark m1 = new mark(this);
        final Battery1 b = new Battery1(this);

        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(this, nodeConfiguration);
        nodeMainExecutor.execute(t1, nodeConfiguration);
        nodeMainExecutor.execute(m1, nodeConfiguration);
        nodeMainExecutor.execute(b, nodeConfiguration);
//        nodeMainExecutor.execute(s1, nodeConfiguration);
//        nodeMainExecutor.execute(se1, nodeConfiguration);

        throttle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                status=1;
                Toast.makeText(getApplicationContext(), "Forward", Toast.LENGTH_SHORT).show();

            }
        });
        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                status=0;
                Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
            }
        });
        left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                status=2;
                Toast.makeText(getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
            }
        });
        right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                status=3;
                Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT).show();
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                status=4;
                Toast.makeText(getApplicationContext(), "Reverse", Toast.LENGTH_SHORT).show();
            }
        });
        mark1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mark_status=1;
            }
        });

        onResume();
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_tutorial_cv_bridge");
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        this.node = connectedNode;
        final org.apache.commons.logging.Log log = node.getLog();
        imagePublisher = node.newPublisher("/image_converter/output_video/compressed", CompressedImage._TYPE);
        imageSubscriber = node.newSubscriber("/camera/image/compressed", CompressedImage._TYPE);
        imageSubscriber.addMessageListener(new MessageListener<CompressedImage>() {
            @Override
            public void onNewMessage(CompressedImage message) {
                CvImage cvImage;
                try {
                    cvImage = CvImage.toCvCopy(message, ImageEncodings.RGBA8);
                } catch (Exception e) {
                    log.error("cv_bridge exception: " + e.getMessage());
                    return;
                }

                //make sure the picture is big enough for my circle.
                if (cvImage.image.rows() > 110 && cvImage.image.cols() > 110) {
                    //place the circle in the middle of the picture with radius 100 and color red.
                    opencv_imgproc.circle(cvImage.image, new Point(cvImage.image.cols() / 2, cvImage.image.rows() / 2), 100, new Scalar(255, 0, 0,0));
                }

                cvImage.image = cvImage.image.t().asMat();
                opencv_core.flip(cvImage.image, cvImage.image, 1);

                //from https://code.google.com/p/javacv/issues/detail?id=67
                bmp = Bitmap.createBitmap(cvImage.image.cols(), cvImage.image.rows(), Bitmap.Config.ARGB_8888);
                bmp.copyPixelsFromBuffer(cvImage.image.createBuffer());
                runOnUiThread(displayImage);

                opencv_core.flip(cvImage.image, cvImage.image, 1);
                cvImage.image = cvImage.image.t().asMat();

                try {
                    imagePublisher.publish(cvImage.toCompressedImageMsg(imagePublisher.newMessage(), Format.JPG));
                } catch (Exception e) {
                    log.error("cv_bridge exception: " + e.getMessage());
                }
            }
        });
        Log.i(TAG, "called onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onShutdown(Node node) {
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public void onError(Node node, Throwable throwable) {
    }

//    public void throttle(View v){
//        Intent in = new Intent(this,Main2Activity.class);
//        startActivity(in);
//        finish();
//    }

    public void nodes(View v){
        Intent in = new Intent(this,MainActivity.class);
        startActivity(in);
        finish();
    }

    public void map(View v){
        Intent in = new Intent(this,map1.class);
        startActivity(in);
        finish();
    }

    public void back_nav(View v){
        finish();
    }

//    public void throttle(View v){
//        Intent in = new Intent(this,MainActivity.class);
//        startActivity(in);
//        finish();
//    }
//
//    public void points_node(View v){
//        Intent in = new Intent(this,MainActivity.class);
//        startActivity(in);
//        finish();
//    }
//
//    public void stop(View v){
//        Intent in = new Intent(this,MainActivity.class);
//        startActivity(in);
//        finish();
//    }
}
