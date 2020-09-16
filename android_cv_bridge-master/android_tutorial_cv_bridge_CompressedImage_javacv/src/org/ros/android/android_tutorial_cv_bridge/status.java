package org.ros.android.android_tutorial_cv_bridge;

import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.logging.Log;
import org.bytedeco.javacpp.presets.opencv_core;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

//import std_msgs.String;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class status extends AbstractNodeMain {
    static String i;
    private MainActivity mainActivity;


    public status(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/robot_status");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        super.onStart(connectedNode);
        final Log log = connectedNode.getLog();
        log.info("I heard: \"ppppppppp" +"kkkkkkkkk"+ "\"");
        Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("robot_status", std_msgs.String._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
            @Override
            public void onNewMessage(final std_msgs.String message) {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
//                        MainActivity m = new MainActivity();
                        EditText status = (EditText)mainActivity.findViewById(R.id.status);
//                        EditText battery = (EditText)mainActivity.findViewById(R.id.battery_level);
                        status.setText("Robot Status = "+i+"");
//                        battery.setText(i+"%");
                    }
                });

                i=message.getData();
//                MainActivity m = new MainActivity();
//                m.locationFrameIdView.setText(i+"");
                log.info("I heard: \"" + i +"kkkkkkkkk"+ "\"");
            }
        });
    }

//    Battery (int x){
//        this.x = message.
//    }

    public String getLevel(){
        //final Log log = .getLog();
//        Log.class("I heard: \"" + i + "\"");
        return i;
    }
}