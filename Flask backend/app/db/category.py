from app.db import db

class Category(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), unique=True , nullable=False)

    def save(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def find_by_category_name(cls, name):
        return cls.query.filter_by(name=name).first()
