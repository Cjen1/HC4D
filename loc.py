import os, sys, inspect, thread, time
src_dir = os.path.dirname(inspect.getfile(inspect.currentframe()))
# Windows and Linux
arch_dir = '../lib/x64' if sys.maxsize > 2**32 else '../lib/x86'
# Mac
#arch_dir = os.path.abspath(os.path.join(src_dir, '../lib'))

sys.path.insert(0, os.path.abspath(os.path.join(src_dir, arch_dir)))

import Leap
 
class CListener(Leap.Listener):
    def on_connect (self, controller):
        print("Connected to leap controller")

    def on_frame(self, controller):
        frame = controller.frame()
        pointable = frame.pointables.frontmost
        lloc = pointable.tip_position
        global loc
        loc = pointable.tip_position
        #print(lloc)

loc = (0,0,0)

def getLoc():
    return loc

listener = CListener()
controller = Leap.Controller()

controller.add_listener(listener)
