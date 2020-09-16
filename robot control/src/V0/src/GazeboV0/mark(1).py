#!/usr/bin/env python
import rospy
from std_msgs.msg import Float32
from geometry_msgs.msg import Twist

def callback(data):
	if(data.data==1):
		autonomous_movement()

def autonomous_movement():
    global arr
    x_coord = 0
    y_coord = 0
    a=[[(x_coord,y_coord,0),(0,0,0,1)]]
    print(a)
    arr.append(a)
    print(arr)

    
def mark():

    rospy.init_node('listener', anonymous=True)
    rospy.Subscriber("mark", Float32, callback)
    
    
    rospy.spin()

if __name__ == '__main__':
    arr = []
    mark()