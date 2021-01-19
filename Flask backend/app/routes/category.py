from flask_restful import Resource, reqparse, marshal_with , fields

from app.db.category import *
from app.db.product import *
from app import api

category_fileds = {
    'category_name': fields.String(attribute=lambda x: x.name),
    'id': fields.String(attribute=lambda x: x.id),
}

category_parser = reqparse.RequestParser()
category_parser.add_argument('name', type=str)

class CategoryResource(Resource):
    @marshal_with(category_fileds)
    def get(self):
        query = Category.query.get(id).outerjoin(Product)
        return query

    def post(self):
        new_category = category_parser.parse_args()
        category_name = new_category['name']
        category = Category(name=category_name)
        category.save()
        return {'message': f'Category {category_name} added successfully'}


api.add_resource(CategoryResource, '/category')