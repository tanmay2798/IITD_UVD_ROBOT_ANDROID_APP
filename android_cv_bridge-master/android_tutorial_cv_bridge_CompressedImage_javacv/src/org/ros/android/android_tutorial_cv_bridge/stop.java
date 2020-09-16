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
public class stop extends AbstractNodeMain {

    private MainActivityCompressedJavacv mainActivityCompressedJavacv;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emergency__stop);
//    }

    public stop(MainActivityCompressedJavacv mainActivityCompressedJavacv){
        this.mainActivityCompressedJavacv = mainActivityCompressedJavacv;
    }

    @Override
    public GraphName getDefaultNodeName() {
        Log.d(Emergency_Stop.class.getName(),"aaaaadcdcdcdscbsdbcjsjcbjdsjcjasnxkaksxnkaskxnksxksd");
        return GraphName.of("rosjava_tutorial_pubsub/stop");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
//        final MainActivityCompressedJavacv m = new MainActivityCompressedJavacv();
        final Publisher<std_msgs.Float32> publisher =
                connectedNode.newPublisher("stop", std_msgs.Float32._TYPE);
        // This CancellableLoop will be canceled automatically when the node shuts
        // down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {

//                for( sequenceNumber=0;sequenceNumber<10000;sequenceNumber++) {
//                    std_msgs.Float32 str = publisher.newMessage();
//                    str.setData(0);
////                    str.setData(10);
//                    publisher.publish(str);
////                    Log.d(Emergency_Stop.class.getName(), "dxdsxdxsdcsdcdc");
//                    sequenceNumber++;
//                }
            }

            @Override
            protected void loop() throws InterruptedException {
//                MainActivityCompressedJavacv m = new MainActivityCompressedJavacv();
                std_msgs.Float32 str = publisher.newMessage();
                if(mainActivityCompressedJavacv.status==0) {
                    str.setData(-1);
                    publisher.publish(str);
                }
//                else
//                    str.setData(-1);

               // Log.d(Emergency_Stop.class.getName(),"statuscdclsfcnejwncn"+mainActivityCompressedJavacv.status);
//                    str.setData(10);
//                publisher.publish(str);

//                Thread.currentThread().interrupt();
//                break;
            }

        });
    }
}

