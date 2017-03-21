from event import Event
from google.appengine.ext import ndb
from google.appengine.ext.ndb import polymodel
from i18n import InternationalText
from image import Image
from time import Time

class Area(polymodel.PolyModel):

    # Static Methods
    @staticmethod
    def convert_to_ndb_geo_pt(lat, lon):
        return ndb.GeoPt(lat, lon)

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    visitor_destination = ndb.GeoPtProperty()
    coordinates = ndb.GeoPtProperty(repeated=True)
    image = ndb.StructuredProperty(Image)

    # Methods
    def get_events(self):
        return Event.query(ancestor=self.key).fetch()

    # Methods
    def is_available(self, start_time, end_time):

        # Retrieve all events for the area
        events = Event.get_all_for_location(self)

        # For each event, check
        for event in events:

            # If end time after start_time
            if Time.validate_times(event.start_time, end_time):
                # Check start time not before end time
                if Time.validate_times(start_time, event.end_time):
                    return False

            # If start time before end_time
            if Time.validate_times(start_time, event.end_time):
                # Check end time not after end time
                if Time.validate_times(event.start_time, end_time):
                    return False

        return True

    def get_area(self):
        area = 0
        previous_point = len(self.coordinates) - 1

        for current_point in range(0, len(self.coordinates) - 1):
            area = area + (self.coordinates[previous_point].lat + self.coordinates[current_point].lat) * (self.coordinates[previous_point].lon - self.coordinates[current_point].lon)
            previous_point = current_point

        return area/2

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()
