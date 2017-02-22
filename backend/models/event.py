from google.appengine.ext import ndb

class Event(ndb.Model):

    # Properties
    label = ndb.StringProperty()
    description = ndb.StringProperty()
    start_time = ndb.DateProperty()
    end_time = ndb.DateProperty()
    location = ndb.StructuredProperty(Area)
    is_active = ndb.BooleanProperty()
