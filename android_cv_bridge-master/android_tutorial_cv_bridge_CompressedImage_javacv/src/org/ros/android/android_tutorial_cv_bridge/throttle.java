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
public class throttle extends AbstractNodeMain {

    private MainActivityCompressedJavacv mainActivityCompressedJavacv;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emergency__stop);
//    }

    public throttle(MainActivityCompressedJavacv mainActivityCompressedJavacv){
        this.mainActivityCompressedJavacv = mainActivityCompressedJavacv;
    }

    @Override
    public GraphName getDefaultNodeName() {
        Log.d(Emergency_Stop.class.getName(),"aaaaadcdcdcdscbsdbcjsjcbjdsjcjasnxkaksxnkaskxnksxksd");
        return GraphName.of("rosjava_tutorial_pubsub/throttle");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {

        final Publisher<std_msgs.Float32> publisher =
                connectedNode.newPublisher("throttle", std_msgs.Float32._TYPE);
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

//                 = (MainActivityCompressedJavacv) getAtivity()
                std_msgs.Float32 str = publisher.newMessage();
                if (mainActivityCompressedJavacv.status == 1){
                    str.setData(1);
                    publisher.publish(str);
                }else if (mainActivityCompressedJavacv.status == 2) {
                    str.setData(2);
                    publisher.publish(str);
                }else if (mainActivityCompressedJavacv.status == 3) {
                    str.setData(3);
                    publisher.publish(str);
                }else if (mainActivityCompressedJavacv.status == 4) {
                    str.setData(4);
                    publisher.publish(str);
                }else if (mainActivityCompressedJavacv.status == 0) {
                    str.setData(0);
                    publisher.publish(str);
                }
                 else {
                    str.setData(0);
                    publisher.publish(str);
                }
//                    str.setData(10);
//                Log.d(Emergency_Stop.class.getName(),"status"+mainActivityCompressedJavacv.status);
//                publisher.publish(str);

                Thread.currentThread().sleep(100);
//                break;
            }

        });
    }
}

