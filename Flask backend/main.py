from app import *
from app.db import db, Product, Category


@app.before_first_request
def database_initialization():
    create_tables()
    fill_tables()


def create_tables():
    db.drop_all()
    db.create_all()
    print('tables has been created')


def fill_tables():
    fill_category()
    fill_product()
    print('tables has been filled')


def fill_category():
    category = Category(name="Category A");
    category.save()
    category = Category(name="Category B");
    category.save()
    category = Category(name="Category C");
    category.save()
    category = Category(name="Category D");
    category.save()
    category = Category(name="Category E");
    category.save()


def fill_product():
    category_a = Category.find_by_category_name("Category A");
    category_b = Category.find_by_category_name("Category B");
    category_c = Category.find_by_category_name("Category C");
    category_d = Category.find_by_category_name("Category D");
    category_e = Category.find_by_category_name("Category E");

    product = Product(name="Product A1", price=1, quantity=10, category=category_a)
    product.save()
    product = Product(name="Product A2", price=2, quantity=10, category=category_a)
    product.save()
    product = Product(name="Product A3", price=3, quantity=10, category=category_a)
    product.save()

    product = Product(name="Product b1", price=14, quantity=10, category=category_b)
    product.save()
    product = Product(name="Product b2", price=15, quantity=10, category=category_b)
    product.save()

    product = Product(name="Product c1", price=12, quantity=10, category=category_c)
    product.save()
    product = Product(name="Product c2", price=13, quantity=10, category=category_c)
    product.save()

    product = Product(name="Product d1", price=122, quantity=10, category=category_d)
    product.save()

    product = Product(name="Product e1", price=100, quantity=10, category=category_e)
    product.save()


if __name__ == '__main__':
    app.run()


