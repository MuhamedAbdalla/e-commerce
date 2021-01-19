from app.db import db, Category

class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False)
    price = db.Column(db.Numeric, nullable=False)
    quantity = db.Column(db.Integer, nullable=False)

    category_id = db.Column(db.Integer, db.ForeignKey('category.id'), nullable=False)
    category = db.relationship('Category', backref=db.backref('products', lazy=True))

    def save(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def find(cls, name):
        return cls.query.outerjoin(Category).filter((Category.name.contains(name)) | (cls.name.contains(name))).all()

    @classmethod
    def find_by_category_name(cls, name):
        return cls.query.outerjoin(Category).filter(Category.name.contains(name)).all()