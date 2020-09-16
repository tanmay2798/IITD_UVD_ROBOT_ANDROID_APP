#!/usr/bin/env python

import rospy
from sensor_msgs.msg import Range
from sensor_msgs.msg import LaserScan
import message_filters
def callback(sub,sub5):
    l = [sub.ranges,sub5.range]
    ir = sub5.range # sub5.range is the point values of the IR sensor
 
    num_readings = 720
    laser_frequency = 20

    count = 0
    r = rospy.Rate(1.0)
    while not rospy.is_shutdown():
      current_time = rospy.Time.now()
      scan_pub = rospy.Publisher('my_laser_scan', LaserScan, queue_size=0)# my_laser_scan is my topic

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
      if sub5.range < 0.1:# if obstacle from IR is less than 10 cm 
        for i in range(320,340):
          a = sub5.range # set a variable, to the value of reading
          scan.ranges[i] = a # add the values to the scan.ranges, of my Laserscan topic
          scan.intensities = []            
          
      if sub5.range > 0.1:
	  scan.ranges = sub.ranges # if IR data is more than 10 cm, let the scan.ranges(my laser scan topic) be equal to the actual Lidar scan
          scan.intensities = []
            
            
        
     
print len(scan.ranges)
scan_pub.publish(scan)
count += 1
r.sleep()
   
      
       

    
  
    
rospy.init_node('scan_values')


sub = message_filters.Subscriber('/mybot/laser/scan', LaserScan)# laser scan from where I subscribe
sub5 = message_filters.Subscriber('/sensor/ir_front_5',Range)# IR from where I subscribe
ts = message_filters.TimeSynchronizer([sub,sub5], 1)
ts.registerCallback(callback)
rospy.spin()





