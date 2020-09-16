package org.ros.android.android_tutorial_cv_bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.android.RosActivity;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

public class map1 extends RosActivity implements NodeMain {

    int status = 0;
    int x;
    int y;
    int area=1;

    protected ConnectedNode node;
    NodeConfiguration nodeConfiguration;
    PaintView paintView;

    public map1() {
        // The RosActivity constructor configures the notification title and ticker messages.
        super("map", "map");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

        // Getting reference to PaintView
        paintView = (PaintView)findViewById(R.id.paint_view);

//         Getting reference to TextView tv_cooridinate
        TextView tvCoordinates = (TextView)findViewById(R.id.tv_coordinates);

//         Passing reference of textview to PaintView object to update on coordinate changes
        paintView.setTextView(tvCoordinates);
//        Toast.makeText(getApplicationContext(), ""+touchPlace, Toast.LENGTH_SHORT).show();

        // Setting touch event listener for the PaintView
        paintView.setOnTouchListener(paintView);

    }

    public void back_map(View v){
//        Intent in = new Intent(this,MainActivityCompressedJavacv.class);
//        startActivity(in);
//        Toast.makeText(getApplicationContext(), ""+x+" "+y, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void ok(View v){
        x = (int)paintView.mX;
        y = (int)paintView.mY;
        status = 1;
    }

    public void area1(View v){
        area=1;
    }

    public void area2(View v){
        area=2;
    }

    @Override
    protected void init(final NodeMainExecutor nodeMainExecutor) {

        points_node t1 = new points_node(this);


        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(this, nodeConfiguration);
        nodeMainExecutor.execute(t1, nodeConfiguration);
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
}
