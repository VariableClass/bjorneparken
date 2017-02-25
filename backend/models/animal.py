from google.appengine.ext import ndb
from i18n import InternationalText

class Animal(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    description = ndb.LocalStructuredProperty(InternationalText, repeated=True)
    is_available = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name).fetch()


    class AnimalLookup(ndb.Model):
        animal_id = ndb.IntegerProperty()
        species_id = ndb.IntegerProperty()
