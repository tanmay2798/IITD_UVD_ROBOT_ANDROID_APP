/*
 * Copyright (C) 2014 Oliver Degener.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_cv_bridge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.android.view.camera.CameraPreviewView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;


public class MainActivity extends RosActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    NodeConfiguration nodeConfiguration;
//    NodeMainExecutor nodeMainExecutor;

    public EditText locationFrameIdView, statusBar,workBar;
    Button applyB,emergencyb,resetB,batteryB,backB,light_onB,light_offB,continueB,autonomousB;
    private OnFrameIdChangeListener locationFrameIdListener, imuFrameIdListener;

    private int cameraId = 0;
    private RosCameraPreviewView rosCameraPreviewView;
    private CameraPreviewView cameraPreviewView;
    private ChannelBuffer cameraPreview;
    public int status = 0;
    public int autonomous_status = 0;

    private Handler handy = new Handler();


    public MainActivity() {
        super("RosAndroidExample", "RosAndroidExample");
    }

    Runnable sizeCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (rosCameraPreviewView.getHeight()== -1 || rosCameraPreviewView.getWidth()== -1) {
                handy.postDelayed(this, 100);
            } else {
                Camera camera = Camera.open(cameraId);
                rosCameraPreviewView.setCamera(camera);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_nodes);
        Log.d(TAG, "adxdcdecnkedncjksnjkcdn");
        locationFrameIdListener = new OnFrameIdChangeListener() {
            @Override
            public void onFrameIdChanged(String newFrameId) {
                Log.w(TAG, "Default location OnFrameIdChangedListener called");
            }
        };
        imuFrameIdListener = new OnFrameIdChangeListener() {
            @Override
            public void onFrameIdChanged(String newFrameId) {
                Log.w(TAG, "Default IMU OnFrameIdChangedListener called");
            }
        };

        locationFrameIdView = findViewById(R.id.et_location_frame_id);
//        imuFrameIdView = findViewById(R.id.et_imu_frame_id);

        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);

//        imuFrameIdView.setText(sp.getString("imuFrameId", getString(R.string.default_imu_frame_id)));

        applyB = findViewById(R.id.b_apply);
        resetB = findViewById(R.id.b_reset);
        emergencyb = findViewById(R.id.b_emergency);
        batteryB = findViewById(R.id.b_battery);
        backB = findViewById(R.id.b_back);
        light_offB = findViewById(R.id.b_light_off);
        light_onB = findViewById(R.id.b_light_on);
        continueB = findViewById(R.id.b_continue);
        statusBar = findViewById(R.id.status);
        workBar = findViewById(R.id.work);
        autonomousB = findViewById(R.id.b_autonomous);
        backB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getApplicationContext(), "throttle", Toast.LENGTH_SHORT).show();
//                Intent in = new Intent(getApplicationContext(),MainActivityCompressedJavacv.class);
//                startActivity(in);
//                Toast.makeText(getApplicationContext(), "Start Sanitisation", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        applyB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "throttle", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(in);
                finish();
            }
        });


        cameraPreviewView = (CameraPreviewView) findViewById(R.id.camera_preview_view);
        rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);


    }

    @Override
    protected void init(final NodeMainExecutor nodeMainExecutor) {
        Log.d(TAG, "init()");

        final LocationPublisherNode locationPublisherNode = new LocationPublisherNode();
        ImuPublisherNode imuPublisherNode = new ImuPublisherNode();
        final Emergency_Stop em = new Emergency_Stop(this);
        final Battery b = new Battery(this);
        final work_completion w = new work_completion(this);
        final status sta = new status(this);
        final CameraView c = new CameraView();
        final autonomous auto = new autonomous(this);


        MainActivity.this.locationFrameIdListener = locationPublisherNode.getFrameIdListener();
        MainActivity.this.imuFrameIdListener = imuPublisherNode.getFrameIdListener();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        final String provider = LocationManager.GPS_PROVIDER;
        String svcName = Context.LOCATION_SERVICE;
        final LocationManager locationManager = (LocationManager) getSystemService(svcName);
        final int t = 500;
        final float distance = 0.1f;

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean permissionFineLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                    boolean permissionCoarseLocation = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                    Log.d(TAG, "PERMISSION 1: " + String.valueOf(permissionFineLocation));
                    Log.d(TAG, "PERMISSION 2: " + String.valueOf(permissionCoarseLocation));
                    if (permissionFineLocation && permissionCoarseLocation) {
                        if (locationManager != null) {
                            Log.d(TAG, "Requesting location");
                            locationManager.requestLocationUpdates(provider, t, distance,
                                    locationPublisherNode.getLocationListener());
                        }
                    } else {
                        // Request permissions
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.GET_PERMISSIONS);
                    }
                } else {
                    locationManager.requestLocationUpdates(provider, t, distance, locationPublisherNode.getLocationListener());
                }
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(imuPublisherNode.getAccelerometerListener(), accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
            return;
        }

        SensorManager sensorManager1 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor gyroscope = sensorManager1.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager1.registerListener(imuPublisherNode.getGyroscopeListener(), gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
            return;
        }

        SensorManager sensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor orientation = sensorManager2.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorManager2.registerListener(imuPublisherNode.getOrientationListener(), orientation, SensorManager.SENSOR_DELAY_FASTEST);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
            return;
        }

        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.

        nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());

        //nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration);
        handy.post(sizeCheckRunnable);

//        nodeMainExecutor.execute(locationPublisherNode, nodeConfiguration);
//        nodeMainExecutor.execute(imuPublisherNode, nodeConfiguration);
        nodeMainExecutor.execute(b,nodeConfiguration);
        nodeMainExecutor.execute(sta,nodeConfiguration);
        nodeMainExecutor.execute(w,nodeConfiguration);
        nodeMainExecutor.execute(c,nodeConfiguration);
        nodeMainExecutor.execute(em, nodeConfiguration);
        nodeMainExecutor.execute(auto, nodeConfiguration);

//        runOnUiThread(new Runnable() {
//            public void run() {
//                int i = b.getLevel();
//                Log.i(TAG, "cfececnefjckack ");
////                Toast.makeText(getApplicationContext(), "reset " +i, Toast.LENGTH_SHORT).show();
//                locationFrameIdView.setText(i+"");
//            }
//        });

        batteryB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getApplicationContext(), "throttle", Toast.LENGTH_SHORT).show();
                locationFrameIdView.setText(b.getLevel()+"%");
                statusBar.setText(sta.getLevel()+"");
//                Log.d(TAG,c.getLevel()+"vdfnvndkfgvnfd");
//                cameraPreviewView.releaseCamera();
//                cameraPreviewView.setCamera(Camera.open(0));
            }
        });

        emergencyb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Emergency Stop", Toast.LENGTH_LONG).show();
                status=1;
            }
        });

        autonomousB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Start Sanitisation", Toast.LENGTH_LONG).show();
                autonomous_status=1;
            }
        });

        resetB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               status=0;
               Intent in = new Intent(getApplicationContext(),MainActivity.class);
               startActivity(in);
               finish();
            }
        });
        light_onB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                status=0;
            }
        });
        light_offB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                status=2;
            }
        });
        continueB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_LONG).show();
                status=0;
            }
        });
//        cameraPreview = (ChannelBuffer)findViewById(R.id.ros_camera_preview_view);

        onClick(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int numberOfCameras = Camera.getNumberOfCameras();
            final Toast toast;
            if (numberOfCameras > 1) {
                cameraId = (cameraId + 1) % numberOfCameras;
                rosCameraPreviewView.releaseCamera();
                rosCameraPreviewView.setCamera(Camera.open(cameraId));
                toast = Toast.makeText(this, "Switching cameras.", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(this, "No alternative cameras to switch to.", Toast.LENGTH_SHORT);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "Default IMU OnFrameIdChangedListener called");

        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        String newLocationFrameId = locationFrameIdView.getText().toString();
        if (!newLocationFrameId.isEmpty()) {
            locationFrameIdListener.onFrameIdChanged(newLocationFrameId);
            spe.putString("locationFrameId", newLocationFrameId);
        }
//        String newImuFrameId = imuFrameIdView.getText().toString();
//        if (!newLocationFrameId.isEmpty()) {
//            imuFrameIdListener.onFrameIdChanged(newImuFrameId);
//            spe.putString("imuFrameId", newImuFrameId);
//        }
//        spe.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PackageManager.GET_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissions granted!");
            } else {
                Log.e(TAG, "Permissions not granted.");
            }
        }
    }

    public void apply(View v){

    }
}
