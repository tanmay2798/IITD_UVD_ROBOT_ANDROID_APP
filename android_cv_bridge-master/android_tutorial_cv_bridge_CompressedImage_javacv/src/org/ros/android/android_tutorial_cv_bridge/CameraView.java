package org.ros.android.android_tutorial_cv_bridge;

import org.apache.commons.logging.Log;
import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

//import cv_bridge.CvImage;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class CameraView extends AbstractNodeMain {
    static ChannelBuffer rosCameraPreviewView;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/camera");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Log log = connectedNode.getLog();
        Subscriber<sensor_msgs.Image> subscriber = connectedNode.newSubscriber("camera", sensor_msgs.CompressedImage._TYPE);
        subscriber.addMessageListener(new MessageListener<sensor_msgs.Image>() {
            @Override
            public void onNewMessage(final sensor_msgs.Image message) {

                rosCameraPreviewView = message.getData();
//                CvImage cv_image = CvImage.toCvCopy(image_message, desired_encoding="passthrough");

                log.info("I heardjjjjjjnjjnknjkkbkjbjbj: \"" + rosCameraPreviewView + "\"");
            }
        });
    }

//    Battery (int x){
//        this.x = message.
//    }

    public ChannelBuffer getLevel(){
        //final Log log = .getLog();
//        Log.class("I heard: \"" + i + "\"");
        return rosCameraPreviewView;
    }
}