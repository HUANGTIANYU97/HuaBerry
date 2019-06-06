import paho.mqtt.publish as publish

def add():
  publish.single("button", payload="buttonAdd", qos=0, hostname="172.20.10.8")

add()
