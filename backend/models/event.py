import re
from google.appengine.ext import ndb
from google.appengine.ext.ndb import polymodel
from i18n import InternationalText

class Event(polymodel.PolyModel):

    # Static Methods
    @staticmethod
    def validate_times(start_time, end_time):

        pattern = re.compile("([01][0-9]|2[0-3])\.?:?[0-5][0-9]")

        # Check start time is in correct format
        if not pattern.match(start_time):
            return False

        # Check end time is in correct format
        if not pattern.match(end_time):
            return False

        if len(start_time) > 4:
            start_time_array = start_time.split("(\.)|(:)")
            return start_time_array
            start_time = start_time_array[0] + start_time_array[1]

        if len(end_time) > 4:
            end_time_array = end_time.split("(\.)|(:)")
            end_time = end_time_array[0] + end_time_array[1]

        # Check end_time after start_time
        if start_time > end_time:
            return False

        return True

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
