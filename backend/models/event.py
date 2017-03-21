from google.appengine.ext import ndb
from google.appengine.ext.ndb import polymodel
from i18n import InternationalText
from image import Image

class Event(polymodel.PolyModel):

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    description = ndb.StructuredProperty(InternationalText, repeated=True)
    start_time = ndb.StringProperty()
    end_time = ndb.StringProperty()
    is_active = ndb.BooleanProperty()
    image = ndb.StructuredProperty(Image)

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()

    @classmethod
    def get_all_for_location(cls, amenity):
        return cls.query(ancestor=amenity.key).fetch()

    class EventLookup(ndb.Model):
        event_id = ndb.IntegerProperty()
        location_id = ndb.IntegerProperty()
