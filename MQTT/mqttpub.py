import paho.mqtt.publish as publish

publish.single("serial", payload="hello world!",qos=0, hostname="localhost")
