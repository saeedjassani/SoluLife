from flask import Flask
from chatterbot import ChatBot
from chatterbot.trainers import ListTrainer
import json
import requests
from flask import jsonify
from flask import request
import urllib
import urllib2
from bs4 import BeautifulSoup


app = Flask(__name__)

chatbot = ChatBot("test")
chatbot.set_trainer(ListTrainer)
chatbot.train([
"how old am i?",
"ummmmm... you are old enough to enjoy your life :)",
])
chatbot.train([
"i am feeling lonely",
"oooou.... how about listening to some music?....just say to me (play grenade)",
])
@app.route('/bot/<string:query>')
def bot(query):
  print query
  reply = ""
  flag = 0
  if 'play' not in query:
    reply = str(chatbot.get_response(query))
    flag = str(0)
  else:
    textToSearch = query
    query = urllib.quote(textToSearch)
    links =[]
    url = "https://www.youtube.com/results?search_query=" + query
    response = urllib2.urlopen(url)
    html = response.read()
    soup = BeautifulSoup(html)
    for vid in soup.findAll(attrs={'class':'yt-uix-tile-link'}):
        links.append('https://www.youtube.com' + vid['href'])
    reply = links[1]
    flag = str(1)
  print "-----------------------------------------------"
  print reply
  print "-----------------------------------------------"
    
  return jsonify({
        'flag': flag,
        'reply': reply
        }
      ) 


if __name__ == "__main__":
	app.run(host = '0.0.0.0',debug=True)

