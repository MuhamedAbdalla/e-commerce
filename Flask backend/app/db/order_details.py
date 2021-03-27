from app.db import db

class OrderDetails(db.Model):
    quantity = db.Column(db.Integer, nullable=False)

    order_id = db.Column(db.Integer, db.ForeignKey('order.id'), primary_key=True, nullable=False)
    order = db.relationship('Order')

    product_id = db.Column(db.Integer, db.ForeignKey('product.id'), primary_key=True, nullable=False)
    product = db.relationship('Product')
