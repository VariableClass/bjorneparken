from area import Area
from enum import Enum
from google.appengine.ext import ndb
from i18n import InternationalText

class Amenity(Area):

    # Properties
    description = ndb.StructuredProperty(InternationalText, repeated=True)
    amenity_type = ndb.StringProperty()

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()

    class AmenityType(Enum):
        ATTRACTION = "ATTRACTION",
        BBQ = "BBQ",
        PLAY_AREA = "PLAY_AREA",
        RECEPTION = "RECEPTION",
        RESTAURANT = "RESTAURANT",
        SNACKS = "SNACKS",
        WC = "WC"

        @classmethod
        def validate(cls, type_to_check):
            return type_to_check in cls.__members__
