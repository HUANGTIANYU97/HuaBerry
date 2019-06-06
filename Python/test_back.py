
import paho.mqtt.publish as publish
import paho.mqtt.client as mqtt


def on_message(client, userdata, msg):
  print(msg.topic + " " + str(msg.payload))


def on_connect(client, userdata, flags, rc):
  print("Connected with result code " + str(rc))
  client.subscribe("len")

def get():
    publish.single("button", payload="buttonGet", qos=0, hostname="172.20.10.8")
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message

    try:
      client.connect("172.20.10.8", 1883, 60)
      client.loop_forever()
    except KeyboardInterrupt:
      client.disconnect()




get()
