#!/usr/bin/env python

import rospy
from sensor_msgs.msg import Range
from sensor_msgs.msg import LaserScan
import message_filters


def callback(sub,sub5):
    l = [sub.ranges,sub5.range]
    ir = sub5.range
    #print sub.ranges[320], "The Laser Scan Data"
    #print ir, "The IR sensor Data"
    pub = rospy.Publisher('scan2', LaserScan,  queue_size=0)
    
    

    #rospy.init_node('laser_scan_publisher')

    scan_pub = rospy.Publisher('my_laser_scan', LaserScan, queue_size=0)

    num_readings = 720
    laser_frequency = 60

    count = 0
    r = rospy.Rate(1.0)
    while not rospy.is_shutdown():
      current_time = rospy.Time.now()

      scan = LaserScan()

      scan.header.stamp = current_time
      scan.header.frame_id = 'hokuyo'
      scan.angle_min = -1.57
      scan.angle_max = 1.57
      scan.angle_increment = 3.14 / num_readings
      scan.time_increment = (1.0 / laser_frequency) / (num_readings)
      scan.range_min = 0.1
      scan.range_max = 10.0
      #l = []
      #for i in range(720):
        #l.append(sub.ranges[i])
       
      #scan.ranges = l
      scan.ranges = sub.ranges
      if sub5.range < 0.1:
        for i in range(320,340):
          a = sub5.range
          scan.ranges[i] = a
          scan.intensities = []
      else:
        scan.ranges = sub.ranges
        scan.intensities = sub.intensities
      #for i in range(0, 100):
        #if i<=50:
          #scan.ranges.append(100)
        #else:
          #scan.ranges.append(20)

   
        
      print len(scan.ranges)
      scan_pub.publish(scan)
      count += 1
      r.sleep()

    
    
    
  
   



    
rospy.init_node('scan_values')


sub = message_filters.Subscriber('/mybot/laser/scan', LaserScan, callback)
sub5 = message_filters.Subscriber('/sensor/ir_front_5',Range, callback)






ts = message_filters.TimeSynchronizer([sub,sub5], 10)
ts.registerCallback(callback)
rospy.spin()





