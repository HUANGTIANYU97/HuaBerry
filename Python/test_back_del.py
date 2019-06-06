import paho.mqtt.publish as publish

def delete():
  publish.single("button", payload="buttonDelte", qos=0, hostname="172.20.10.8")

delete()
