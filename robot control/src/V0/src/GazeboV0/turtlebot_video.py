from __future__ import division

import cv2

import argparse
import sys
import time
import math
import rospy
import numpy as np
from sensor_msgs.msg import CompressedImage

from std_msgs.msg import Float32
from sensor_msgs.msg import Image
from cv_bridge import CvBridge, CvBridgeError
#from statistics import mean 


parser = argparse.ArgumentParser()
parser.add_argument("--path", type=str, help="Video path")

class image_converter:

    def __init__(self):
        self.bridge = CvBridge()
        self.image_sub = rospy.Subscriber("/Camera/image_raw/compressed",CompressedImage,self.callback,queue_size=10)
        #self.image_pub = rospy.Publisher("angle",Twist,queue_size=1)

    def callback(self,data):
    	pub = rospy.Publisher('/camera/image/compressed', CompressedImage, queue_size=10)
    	#image_message = CvBridge().cv2_to_imgmsg(frame)
    	pub.publish(data)
        

def main(args):
    ic = image_converter()
    rospy.init_node('image_converter', anonymous=True)
    try:
        rospy.spin()
    except KeyboardInterrupt:
        print("Shutting down")
    cv2.destroyAllWindows()


if __name__ == "__main__":
    #args = parser.parse_args()
    ticks = 0
    rospy.init_node('image_converter', anonymous=True)
    main(sys.argv)
