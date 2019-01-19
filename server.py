import loc
import tetris
from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World'

@app.route('/loc')
def get_loc():
    return str(loc.getLoc())

@app.route('/board')
def getBoard():
    return tetris.getBoard()
