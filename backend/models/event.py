from google.appengine.ext import ndb
from google.appengine.ext.ndb import polymodel
from i18n import InternationalText

class Event(polymodel.PolyModel):

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    description = ndb.StructuredProperty(InternationalText, repeated=True)
    start_time = ndb.StringProperty()
    end_time = ndb.StringProperty()
    is_active = ndb.BooleanProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()


    class EventLookup(ndb.Model):
        event_id = ndb.IntegerProperty()
        location_id = ndb.IntegerProperty()
