import time
import grovepi
import test_back_origin
import test_back_post_origin
import test_back_del_origin

led = 5
button = 2
buttoninput = 3

grovepi.pinMode(buttoninput,"INPUT")
grovepi.pinMode(led, "OUTPUT")
grovepi.pinMode(button, "OUTPUT")
grovepi.ledBar_init(led,1)

grovepi.ledBar_setLevel(led,0)

limit = 7
len = test_back_origin.get()
grovepi.ledBar_setLevel(led, len)
grovepi.digitalWrite(button,1)
if(len <= 3):
	grovepi.ledBar_setLed(led,8,1)
elif(len <= 6):
	grovepi.ledBar_setLed(led,9,1)
else:
	grovepi.ledBar_setLed(led,10,1)
	grovepi.digitalWrite(button,0)

while True:
        try:
                if(grovepi.digitalRead(buttoninput) == 0):
                        time.sleep(.6)
                        if(grovepi.digitalRead(buttoninput) == 1):
				if(test_back_origin.get() < limit):
                                	test_back_post_origin.add()
                                	len = test_back_origin.get()
                        else:
				while(grovepi.digitalRead(buttoninput) == 0):
					time.sleep(.01)
				test_back_del_origin.delete()
				len = test_back_origin.get()

                        grovepi.ledBar_setLevel(led, len)
                        if(len <= 3):
                                grovepi.ledBar_setLed(led,8,1)
                        elif(len <= 6):
                                grovepi.ledBar_setLed(led,9,1)
                        else:
                                grovepi.ledBar_setLed(led,10,1)
                                grovepi.digitalWrite(button,0)
                       #time.sleep(.2)
        except IOError:
                print("Error")
        except KeyboardInterrupt:
                grovepi.digitalWrite(button,0)
                grovepi.ledBar_setBits(led,0)
                break
