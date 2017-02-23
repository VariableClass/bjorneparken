from google.appengine.ext import ndb
from i18n import InternationalText

class Event(ndb.Model):

    # Properties
    label = ndb.LocalStructuredProperty(InternationalText, repeated=True)
    description = ndb.LocalStructuredProperty(InternationalText, repeated=True)
    start_time = ndb.DateProperty()
    end_time = ndb.DateProperty()
    is_active = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query()
