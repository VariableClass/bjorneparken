import InternationalText
from google.appengine.ext import ndb

class Amenity(Area):

    # Properties
    description = ndb.LocalStructuredProperty(InternationalText, repeated=True)
    amenity_type = ndb.StringProperty()

    # Constructors
    def __init__(self, description, amenity_type):
        Area.__init__(self, label, visitor_destination, coordintes):
        self.description = description
        self.amenity_type = amenity_type

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name)

    class AmenityType(Enum):
        ATTRACTION = "ATTRACTION",
        PLAY_AREA = "PLAY_AREA",
        RESTAURANT = "RESTAURANT",
        BBQ = "BARBEQUE",
        SNACKS = "SNACKS",
        WC = "TOILETS"
