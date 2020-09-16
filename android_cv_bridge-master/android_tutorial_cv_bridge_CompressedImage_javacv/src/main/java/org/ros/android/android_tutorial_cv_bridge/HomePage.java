package org.ros.android.android_tutorial_cv_bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.ros.android.RosActivity;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

public class HomePage extends RosActivity implements NodeMain {

    protected ConnectedNode node;
    NodeConfiguration nodeConfiguration;

    public HomePage() {
        // The RosActivity constructor configures the notification title and ticker messages.
        super("home", "home");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    @Override
    protected void init(final NodeMainExecutor nodeMainExecutor) {

//        points_node t1 = new points_node(this);
//
//
//        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
//        nodeConfiguration.setMasterUri(getMasterUri());
//        nodeMainExecutor.execute(this, nodeConfiguration);
//        nodeMainExecutor.execute(t1, nodeConfiguration);
//        nodeMainExecutor.execute(s1, nodeConfiguration);
//        nodeMainExecutor.execute(se1, nodeConfiguration);
        onResume();
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        this.node = connectedNode;
        final org.apache.commons.logging.Log log = node.getLog();
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_tutorial_cv_bridge");
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

    public void manual(View v){
        Intent in = new Intent(this,MainActivityCompressedJavacv.class);
        startActivity(in);
    }

    public void autonomous(View v){
        Intent in = new Intent(this,MainActivity.class);
        startActivity(in);
    }

    public void map(View v){
        Intent in = new Intent(this,map1.class);
        startActivity(in);
    }
}
