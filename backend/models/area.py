from event import Event
from google.appengine.ext import ndb
from google.appengine.ext.ndb import polymodel
from i18n import InternationalText

class Area(polymodel.PolyModel):

    # Static Methods
    @staticmethod
    def convert_to_ndb_geo_pt(lat, lon):
        return ndb.GeoPt(lat, lon)

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    visitor_destination = ndb.GeoPtProperty()
    coordinates = ndb.GeoPtProperty(repeated=True)

    # Methods
    def get_events(self):
        return Event.query(ancestor=self.key).fetch()

    def get_area():
        area = 0
        previous_point = len(coordinates) - 1

        for current_point in range(0, len(coordinates) - 1):
            area = area + (coordinates[previous_point].lat + coordinates[current_point].lat) * (coordinates[previous_point].lon - coordinates[current_point].lon)
            previous_point = current_point

        return area/2

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()
