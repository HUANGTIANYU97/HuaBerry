import time
import grovepi
import test_back
import test_back_post
import test_back_del

led = 5
button = 2
buttoninput = 3

grovepi.pinMode(buttoninput,"INPUT")
grovepi.pinMode(led, "OUTPUT")
grovepi.pinMode(button, "OUTPUT")
#time.sleep(1)
grovepi.ledBar_init(led,1)
#time.sleep(1)

grovepi.ledBar_setLevel(led,0)
#time.sleep(3)

limit = 31
len = test_back.get()
grovepi.ledBar_setBits(led, len)
grovepi.digitalWrite(button,1)
if(len >= 8 and len <16):
	grovepi.ledBar_setLed(led,8,1)
if(len >= 16 and len < 31):
	grovepi.ledBar_setLed(led,9,1)
if(len ==  31):
	grovepi.ledBar_setLed(led,10,1)
	grovepi.digitalWrite(button,0)

#while(len < limit):
while True:
        try:
                if(grovepi.digitalRead(buttoninput) == 0):
                        time.sleep(.2)
                        if(grovepi.digitalRead(buttoninput) == 1):
				if(test_back.get() < limit):
                                	test_back_post.add()
                                	len = test_back.get()
                        else:
                                time.sleep(.2)
				if(grovepi.digitalRead(buttoninput) == 1):
					if(test_back.get() < limit):
						test_back_post.add()
						len = test_back.get()
				else:
					test_back_del.delete()
					len = test_back.get()
                        grovepi.ledBar_setBits(led, len)
                        if(len >= 8 and len <16):
                                grovepi.ledBar_setLed(led,8,1)
                                countNumber = 0
                                while(countNumber < 2):
                                        try:
                                                grovepi.digitalWrite(button,0)
                                                time.sleep(.2)
                                                grovepi.digitalWrite(button,1)
                                                time.sleep(.2)
                                                countNumber += 1
                                        except IOError:
                                                print("Error")
                                        except KeyboardInterrupt:
                                                grovepi.digitalWrite(button,0)
                                                grovepi.ledBar_setBits(led,0)
                                                break
                        if(len >= 16 and len < 31):
                                grovepi.ledBar_setLed(led,9,1)
                                countNumber = 0
                                while(countNumber < 2):
                                        try:
                                                grovepi.digitalWrite(button,0)
                                                time.sleep(.1)
                                                grovepi.digitalWrite(button,1)
                                                time.sleep(.1)
                                                countNumber += 1
                                        except IOError:
                                                print("Error")
                                        except KeyboardInterrupt:
                                                grovepi.digitalWrite(button,0)
                                                grovepi.ledBar_setBits(led,0)
                                                break
                        if(len ==  31):
                                grovepi.ledBar_setLed(led,10,1)
                                grovepi.digitalWrite(button,0)
                        time.sleep(.5)
        except IOError:
                print("Error")
        except KeyboardInterrupt:
                grovepi.digitalWrite(button,0)
                grovepi.ledBar_setBits(led,0)
                break
