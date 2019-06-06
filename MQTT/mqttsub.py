import paho.mqtt.client as mqtt



def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    client.subscribe("test")

def on_message(client, userdata, msg):
    print(msg.topic + " " + str(msg.payload))

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

try:
    client.connect("172.20.10.8", 1883, 60)
    client.loop_forever()
except KeyboardInterrupt:
    client.disconnect()
