import urllib
import json

def get():
	response = urllib.urlopen('http://172.20.10.9:9400/api/clients')
	result = json.loads(response.read())

	print(len(result))
	return(len(result))
get()
