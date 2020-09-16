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
public class work_completion extends AbstractNodeMain {
    static float i;
    private MainActivity mainActivity;


    public work_completion(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/work_comp");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        super.onStart(connectedNode);
        final Log log = connectedNode.getLog();
        Subscriber<std_msgs.Float32> subscriber = connectedNode.newSubscriber("work_comp", std_msgs.Float32._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.Float32>() {
            @Override
            public void onNewMessage(final std_msgs.Float32 message) {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        EditText work_comp = (EditText)mainActivity.findViewById(R.id.work);
                        work_comp.setText("Sanitation Completed = "+i+"%");
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

    public float getLevel(){
        //final Log log = .getLog();
//        Log.class("I heard: \"" + i + "\"");
        return i;
    }
}