from flask import Flask, Blueprint
from flask_restful import Api
from flask_jwt_extended import JWTManager
from flask_mail import  Mail

app = Flask(__name__)
app.config['SECRET_KEY'] = 'ThisIsHardestThing'
app.config['JWT_SECRET_KEY'] = 'Dude!WhyShouldYouEncryptIt'
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']
jwt = JWTManager(app)

app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_USERNAME'] = "ecommerce.4th.year.fcis@gmail.com"
app.config['MAIL_PASSWORD'] = "kjdfgdl;fg,;,.3-r--230fd"
mail = Mail(app)


api_bp = Blueprint('api', __name__, url_prefix='/api')
api = Api(api_bp)
app.register_blueprint(api_bp)

from app.routes import *

