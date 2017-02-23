import InternationalText
from google.appengine.ext import ndb

class Area(ndb.Model):

    # Static Methods
    @staticmethod
    def convert_to_ndb_geo_pt(lat, lon):
        return ndb.GeoPt(lat, lon)

    # Properties
    label = ndb.StructuredProperty(InternationalText, repeated=True)
    visitor_destination = ndb.GeoPtProperty()
    coordinates = ndb.GeoPtProperty(repeated=True)

    # Constructors
    def __init__(self, label, visitor_destination, coordinates):
        self.label = label
        self.visitor_destination = visitor_destination
        self.coordinates = coordinates

    # Methods
    def get_area():
        area = 0
        previous_point = len(coordinates) - 1

        for current_point in range(0, len(coordinates) - 1):
            area = area +
                    (coordinates[previous_point].lat +
                        coordinates[current_point].lat) *
                    (coordinates[previous_point].lon -
                        coordinates[current_point].lon)
            previous_point = current_point

        return area/2

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name)
