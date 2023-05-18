class Employee:
    def __init__(self, **entries):
        self.id = None
        self.firstName = None
        self.lastName = None
        self.email = None
        self.__dict__.update(entries)

class Embedded:
    def __init__(self, **entries):
        self.__dict__.update(entries)
        self.employees = [Employee(**employee) for employee in entries.get("employees")]

class Page:
    def __init__(self, **entries):
        self.__dict__.update(entries)        

class Root:
    def __init__(self, **entries):
        self.__dict__.update(entries)
        self._embedded = Embedded(**entries.get("_embedded"))
        self._page = Page(**entries.get("page"))
