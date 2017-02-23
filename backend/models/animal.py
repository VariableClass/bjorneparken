from i18n import InternationalText
from google.appengine.ext import ndb

class Animal(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    description = ndb.StructuredProperty(InternationalText, repeated=True)
    is_available = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name)
