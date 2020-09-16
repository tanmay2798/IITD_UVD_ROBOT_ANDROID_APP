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
public class Battery1 extends AbstractNodeMain {
    static int i;
    private MainActivityCompressedJavacv mainActivityCompressedJavacv;


    public Battery1(MainActivityCompressedJavacv mainActivityCompressedJavacv){
        this.mainActivityCompressedJavacv = mainActivityCompressedJavacv;
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
                mainActivityCompressedJavacv.runOnUiThread(new Runnable() {
                    public void run() {
                        EditText battery = (EditText)mainActivityCompressedJavacv.findViewById(R.id.battery_level);
                        battery.setText("Battery = "+i+"%");
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