from google.appengine.ext import ndb

from species import Species

class Animal(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    species = ndb.StructuredProperty(Species)
    description = ndb.StringProperty()
    is_available = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name)

    @classmethod
    def get_all_of_species(cls, species):
        return cls.query().filter(cls.species == species).order(cls.name)
