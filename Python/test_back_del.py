import urllib
import json

def delete():
	response = urllib.urlopen('http://172.20.10.9:9400/api/clients/cancelDate')

delete()
