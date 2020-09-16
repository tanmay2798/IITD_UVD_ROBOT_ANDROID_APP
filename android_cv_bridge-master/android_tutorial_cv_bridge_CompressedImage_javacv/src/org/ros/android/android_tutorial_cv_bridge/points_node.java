package org.ros.android.android_tutorial_cv_bridge;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class points_node extends AbstractNodeMain {

    private map1 map1;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emergency__stop);
//    }

    public points_node(map1 map1){
        this.map1 = map1;
    }

    @Override
    public GraphName getDefaultNodeName() {
        Log.d(Emergency_Stop.class.getName(),"aaaaadcdcdcdscbsdbcjsjcbjdsjcjasnxkaksxnkaskxnksxksd");
        return GraphName.of("rosjava_tutorial_pubsub/points_node");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
//        final MainActivityCompressedJavacv m = new MainActivityCompressedJavacv();
        final Publisher<std_msgs.Float32> publisherx =
                connectedNode.newPublisher("x_nodes", std_msgs.Float32._TYPE);
        final Publisher<std_msgs.Float32> publishery =
                connectedNode.newPublisher("y_nodes", std_msgs.Float32._TYPE);
        // This CancellableLoop will be canceled automatically when the node shuts
        // down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

//            @Override
//            protected void setup() {
//
//                for( sequenceNumber=0;sequenceNumber<10000;sequenceNumber++) {
//                    std_msgs.Float32 str = publisher.newMessage();
//                    str.setData(10);
////                    str.setData(10);
//                    publisher.publish(str);
////                    Log.d(Emergency_Stop.class.getName(), "dxdsxdxsdcsdcdc");
//                    sequenceNumber++;
//                }
//            }

            @Override
            protected void loop() throws InterruptedException {
//                MainActivityCompressedJavacv m = new MainActivityCompressedJavacv();
                if(map1.status==1) {
                    std_msgs.Float32 strx = publisherx.newMessage();
                    std_msgs.Float32 stry = publishery.newMessage();
                    strx.setData(map1.x);
                    stry.setData(map1.y);
                    publisherx.publish(strx);
                    publishery.publish(stry);
                    map1.status=0;
                }
//                if(map.status==2) {
//                    str.setData(1);
//                    publisher.publish(str);
//                }
//                else
//                    str.setData(0);
//                    str.setData(10);
//                Log.d(Emergency_Stop.class.getName(),"status"+mainActivityCompressedJavacv.status);
//                publisher.publish(str);

//                Thread.currentThread().interrupt();
//                break;
            }

        });
    }
}

