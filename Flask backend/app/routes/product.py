from flask_restful import Resource, reqparse, marshal_with , fields
from flask_jwt_extended import jwt_required, get_jwt_identity
from flask import request
from flask_mail import Message
from threading import Thread

from app.db.category import *
from app.db.product import *
from app.db.customer import *
from app import api, app
from app.routes.auth import ResetPassword

product_fields = {
    'id': fields.String(attribute=lambda x: x.id),
    'name': fields.String(attribute=lambda x: x.name),
    'price': fields.String(attribute=lambda x: x.price),
    'quantity': fields.String(attribute=lambda x: x.quantity),
    'category_name': fields.String(attribute=lambda x: x.category.name),
    'category_id': fields.String(attribute=lambda x: x.category.id)
}

product_filter_parser = reqparse.RequestParser()
product_filter_parser.add_argument('name', type=str, required=False)
product_filter_parser.add_argument('category', type=str, required=False)


product_array_parser = reqparse.RequestParser()
product_array_parser.add_argument('array', type=dict, action="append")


class ProductsResource(Resource):
    @marshal_with(product_fields)
    def get(self):
        print(request.headers,request.data, request.form)
        products = product_filter_parser.parse_args()
        query = Product.query.outerjoin(Category).all()
        print(products['name'])
        if products['name']:
            query = Product.find(name=products['name'])

        if products['category']:
            query = Product.find(name=products['category'])
        return query

    @jwt_required
    def post(self):
        products = product_array_parser.parse_args()['array']
        queries = []
        print(products, type(products))
        for product in products:
            id = product['id']
            quantity = product['quantity']
            query = Product.query.get(id)
            queries.append(query)
            print(query.quantity, quantity)
            if query and query.quantity >= quantity:
                query.quantity -= quantity
            else:
                return {'message': 'Out Of Stock'}

        for query in queries:
            query.save()

        username = get_jwt_identity()
        customer = Customer.find_by_username(username)
        msg = Message()
        msg.subject = "Reset Your Password"
        msg.recipients = [customer.email]
        msg.sender = 'ecommerce.4th.year.fcis@gmail.com'
        msg.body = 'Your order has been recorded'
        Thread(target=ResetPassword.send_email, args=(app, msg)).start()
        return {'message': 'Ordered successfully and an email has been sent to you'}




api.add_resource(ProductsResource, '/products')
