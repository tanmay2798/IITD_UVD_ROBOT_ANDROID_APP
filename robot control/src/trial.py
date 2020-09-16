#!/usr/bin/env python

#
import rospy
from sensor_msgs.msg import Range
from sensor_msgs.msg import LaserScan

x = 0
def callback2(msg): # call Back for IR data
  global x
  x = msg.range


def callback(msg):   #function callback which recieves a parameters named msg
  l = msg.ranges 
  #print x
  
  list_l = list(l)  # List of the original ranges data
  r = rospy.Rate(1.0)

            # define lidar msg to new scan object


  if x > 1.9: # IR distance is more than 0.4
    list_l = list(l)
    print list_l     # No change in the original Lidar data
  if x <= 1.9 and x !=0 : # If IR data is less than 0.4 
    for i in range(0,400):
      list_l[i] = x       # replace the 320 to 330 index in the lidar with x
      print list_l

  scan = LaserScan()  # Laserscan object
  scan = msg
  count = 0
  scan.ranges = list_l
  scan.intensities = []
  scan_pub.publish(scan)
    
    
 




if __name__=='__main__':
  rospy.init_node('laser1')
  laser = rospy.Subscriber('/mybot/laser/scan', LaserScan, callback) # laser is a subscriiber object which listens to /mybot/laser/scan topic and it will call the function callback each time
  ir = rospy.Subscriber('/sensor/ir_front_5', Range, callback2)
  scan_pub = rospy.Publisher('my_laser_scan', LaserScan, queue_size=10)
  rospy.spin()
  
  
  
 
  
  




