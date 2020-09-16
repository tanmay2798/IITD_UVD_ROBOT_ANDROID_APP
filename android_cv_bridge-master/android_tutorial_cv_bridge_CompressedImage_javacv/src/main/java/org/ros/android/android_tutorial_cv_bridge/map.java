package org.ros.android.android_tutorial_cv_bridge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.ros.android.RosActivity;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;

import cv_bridge.CvImage;
import cv_bridge.Format;
import sensor_msgs.CompressedImage;
import sensor_msgs.ImageEncodings;
import std_msgs.Float32;
import std_msgs.Int32;
import std_msgs.Float32MultiArray;

public class map extends RosActivity implements NodeMain {

    BitmapFactory.Options myOptions;
    Canvas canvas;
    Bitmap bitmap;
    Paint paint;
    Bitmap mutableBitmap;
    Bitmap workingBitmap;
    ImageView mainLayout;
    int status = 0;
    int x;
    int y;
//    private Canvas canvas;
    protected ConnectedNode node;
    NodeConfiguration nodeConfiguration;
    static int i=0;

    public map() {
        // The RosActivity constructor configures the notification title and ticker messages.
        super("map", "map");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        x.getData()

        mainLayout = (ImageView) findViewById(R.id.imageView);

        myOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.map,myOptions);
        paint= new Paint();
//        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);

        workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_4444, true);
        canvas = new Canvas(mutableBitmap);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point touchPlace = new Point();
                touchPlace.set((int)event.getX(), (int)event.getY());
//                arr.add(touchPlace);
                x= (int)event.getX();
                y=(int)event.getY();
                Toast.makeText(getApplicationContext(), "hh"+touchPlace, Toast.LENGTH_LONG).show();
                i++;
                drawpoint(mainLayout,x,y,100);

//                mainLayout.drawCircle((int)event.getX(),(int)event.getY(),4,p);
                return true;
            }

        });
    }

    private void drawpoint(ImageView imageView,float x,float y, int raduis) {
        Toast.makeText(getApplicationContext(), "ch"+x+" "+y, Toast.LENGTH_SHORT).show();
//        myOptions.inDither = true;
//        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;// important
        myOptions.inPurgeable = true;
//  ArrayList<Point> list= new ArrayList<>();
        canvas.drawCircle(4*x, 3*y, raduis, paint);

        imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }

    public void back_map(View v){
        Intent in = new Intent(this,MainActivityCompressedJavacv.class);
        startActivity(in);
        Toast.makeText(getApplicationContext(), ""+x+" "+y, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void ok(View v){
        status = 1;
    }

    @Override
    protected void init(final NodeMainExecutor nodeMainExecutor) {

//        points_node t1 = new points_node(this);

        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(this, nodeConfiguration);
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
}
