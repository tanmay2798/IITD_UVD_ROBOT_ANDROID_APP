package org.ros.android.android_tutorial_cv_bridge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import sensor_msgs.CompressedImage;

public class PaintView extends View implements OnTouchListener{

    Paint mPaint,p,p1;
    float mX;
    float mY;
    TextView mTVCoordinates;
    map1 m;

    public PaintView(Context context,AttributeSet attributeSet){
        super(context,attributeSet);

        /** Initializing the variables */
        mPaint = new Paint();
        p = new Paint();
        p1 = new Paint();
        mX = mY = -100;
        mTVCoordinates = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Setting the color of the circle
        mPaint.setColor(Color.GREEN);
        p.setColor(Color.YELLOW);
        p1.setColor(Color.RED);
        m = new map1();
//        if(m.area==1){
//            canvas.drawCircle(mX, mY, 20, p);
//        }else{
//            canvas.drawCircle(mX, mY, 40, p);
//        }
        //Toast.makeText(getContext(), "hh"+m.area, Toast.LENGTH_LONG).show();
        // Draw the circle at (x,y) with radius 15
//        canvas.drawCircle(mX, mY, 40, p1);
        canvas.drawCircle(mX, mY, 100, p);
        canvas.drawCircle(mX, mY, 5, mPaint);

        // Redraw the canvas
        invalidate();
    }

    public void setTextView(TextView tv){
        // Reference to TextView Object
        mTVCoordinates = tv;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            // When user touches the screen
            case MotionEvent.ACTION_DOWN:
                // Getting X coordinate
                mX = event.getX();
                // Getting Y Coordinate
                mY = event.getY();

                // Setting the coordinates on TextView
                if(mTVCoordinates!=null){
                    mTVCoordinates.setText("X :" + mX + " , " + "Y :" + mY);
                }
        }
        return true;
    }
}
