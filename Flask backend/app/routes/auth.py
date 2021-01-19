from app import app, api, jwt, mail
from flask import  request
from flask_restful import inputs, reqparse, Resource
from app.db.customer import *
from datetime import timedelta, datetime
from flask_mail import Message
from threading import Thread
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    jwt_required,
    get_raw_jwt,
    jwt_refresh_token_required,
    get_jwt_identity,
)

login_parser = reqparse.RequestParser()
login_parser.add_argument('username', required=True)
login_parser.add_argument('password', required=True)

register_parser = reqparse.RequestParser()
register_parser.add_argument('first_name', required=True)
register_parser.add_argument('last_name', required=True)
register_parser.add_argument('username', required=True)
register_parser.add_argument('email', required=True)
register_parser.add_argument('birthdate', type=lambda x: datetime.strptime(x,'%Y-%m-%d'), required=True)
register_parser.add_argument('gender', required=True)
register_parser.add_argument('job', required=True)
register_parser.add_argument('password', required=True)

reset_password_parser = reqparse.RequestParser()
reset_password_parser.add_argument('email', required=True)

update_password_parser = reqparse.RequestParser()
update_password_parser.add_argument('password', required=True)

class Register(Resource):
   def post(self):
       print('registering:')
       customer = register_parser.parse_args()
       username = customer['username']
       email = customer['email']
       print(customer)
       if Customer.find_by_username(username):
           return {'message': f'User {username} already exists'}
       if Customer.find_by_email(email):
           return {'message': f'email {email} already exists'}

       first_name = customer['first_name']
       last_name = customer['last_name']
       password = customer['password']
       gender = customer['gender']
       birthdate = customer['birthdate']
       job = customer['job']
       new_customer = Customer(first_name=first_name, last_name=last_name, job=job, username=username, email=email,
                               password=Customer.generate_hash(password), gender=gender, birthdate=birthdate)
       new_customer.save()
       access_token = create_access_token(identity=username)
       refresh_token = create_refresh_token(identity=username)
       return {
           'message': f'User {username} was created',
           'access_token': access_token,
           'refresh_token': refresh_token
       }


class Login(Resource):
    def post(self):
        customer = login_parser.parse_args()
        username = customer['username']
        password = customer['password']
        customer = Customer.find_by_username(username)

        if not customer:
            return {'message': f'User {username} doesn\'t exist'}

        if Customer.verify_hash(password, customer.password):
            access_token = create_access_token(identity=username)
            refresh_token = create_refresh_token(identity=username)
            return {
                'message': f'Logged in as {username}',
                'access_token': access_token,
                'refresh_token': refresh_token
            }
        else:
            return {'message': "Wrong credentials"}


class ResetPassword(Resource):
    @staticmethod
    def send_email(app, msg):
        with app.app_context():
            mail.send(msg)

    def post(self):
        print('Resetting Password')
        print(request.headers)
        print(request.data)

        email = reset_password_parser.parse_args()['email']
        customer = Customer.find_by_email(email);
        if not customer:
            return {'message': "Wrong credentials"}
        msg = Message()
        msg.subject = "Reset Your Password"
        msg.recipients = [email]

        expires = timedelta(minutes=5)
        token = create_access_token(customer.username, expires_delta=expires)

        msg.sender = 'ecommerce.4th.year.fcis@gmail.com'
        msg.body = token
        Thread(target=ResetPassword.send_email, args=(app, msg)).start()
        return {'message': f"An email has been sent to {email}"}


class UpdatePassword(Resource):
    @jwt_required
    def post(self):
        print('updating Password')
        password = update_password_parser.parse_args()['password']
        username = get_jwt_identity()
        customer = Customer.find_by_username(username)
        customer.password = Customer.generate_hash(password)
        customer.save()
        return {'message': f"Password has been updated"}


class Logout(Resource):
    @jwt_required
    def post(self):
        jti = get_raw_jwt()['jti']
        revoked_token = RevokedTokenModel(jti=jti)
        revoked_token.save()
        return {'message': 'Access token has been revoked'}


class TokenRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        current_user = get_jwt_identity()
        access_token = create_access_token(identity=current_user)
        return {'access_token': access_token}


@jwt.token_in_blacklist_loader
def token_in_blacklist(decrypted_token):
    jti = decrypted_token['jti']
    return RevokedTokenModel.is_jti_blacklisted(jti)

api.add_resource(Register, '/register');
api.add_resource(Login, '/login');
api.add_resource(Logout, '/logout');
api.add_resource(TokenRefresh, '/refresh');
api.add_resource(ResetPassword, '/reset-password');
api.add_resource(UpdatePassword, '/update-password');


