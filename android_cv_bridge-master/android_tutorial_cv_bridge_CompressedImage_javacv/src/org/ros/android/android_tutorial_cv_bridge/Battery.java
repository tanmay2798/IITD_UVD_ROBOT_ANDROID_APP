package org.ros.android.android_tutorial_cv_bridge;

import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class Battery extends AbstractNodeMain {
    static int i;
    private MainActivity mainActivity;


    public Battery(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/battery");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        super.onStart(connectedNode);
        final Log log = connectedNode.getLog();
        Subscriber<std_msgs.Int32> subscriber = connectedNode.newSubscriber("battery", std_msgs.Int32._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.Int32>() {
            @Override
            public void onNewMessage(final std_msgs.Int32 message) {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
//                        MainActivity m = new MainActivity();
                        EditText locationFrameIdView = (EditText)mainActivity.findViewById(R.id.et_location_frame_id);
//                        EditText battery = (EditText)mainActivity.findViewById(R.id.battery_level);
                        locationFrameIdView.setText("Battery = "+i+"%");
//                        battery.setText(i+"%");
                    }
                });
                i = message.getData();
//                MainActivity m = new MainActivity();
//                m.locationFrameIdView.setText(i+"");
                log.info("I heard: \"" + i + "\"");
            }
        });
    }

//    Battery (int x){
//        this.x = message.
//    }

    public int getLevel(){
        //final Log log = .getLog();
//        Log.class("I heard: \"" + i + "\"");
        return i;
    }
}