from animal import Animal
from i18n import InternationalText
from google.appengine.ext import ndb

class Species(ndb.Model):

    # Properties
    common_name = ndb.StructuredProperty(InternationalText, repeated=True)
    latin = ndb.StringProperty()
    description = ndb.StructuredProperty(InternationalText, repeated=True)

    # Methods
    def get_animals(self):
        return Animal.query(ancestor=self.key).fetch()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query()

    @classmethod
    def get_by_latin_name(cls, latin):
        return cls.query().filter(cls.latin == latin).get()
