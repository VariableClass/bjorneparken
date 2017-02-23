from google.appengine.ext import ndb
from i18n import InternationalText

class Event(ndb.Model):

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    description = ndb.StructuredProperty(InternationalText, repeated=True)
    start_time = ndb.DateProperty()
    end_time = ndb.DateProperty()
    location = ndb.StructuredProperty(Area)
    is_active = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name)
