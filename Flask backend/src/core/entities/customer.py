class Customer:
    def __init__(self, id, custName, password, gender, birthdate, job):
        self._id = id
        self._custName = custName
        self._password = password
        self._gender = gender
        self._birthdate = birthdate
        self._job = job

    def id(self):
        return self._id

    def custName(self):
        return self._custName

    def password(self):
        return self._password

    def gender(self):
        return self._gender

    def birthdate(self):
        return self._birthdate

    def job(self):
        return self._job

    def toJson(self):
        pass
