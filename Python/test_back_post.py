import urllib
from urlparse import urlparse

def add():
	data = bytes(urllib.urlencode(''))
	response = urllib.urlopen('http://172.20.10.9:9400/api/clients/add',data=data)
	print(response.read())
add()
