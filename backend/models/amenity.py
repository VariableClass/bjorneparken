from google.appengine.ext import ndb

class Amenity(Area):

    # Properties
    description = ndb.StringProperty()
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
        ATTRACTION = "Attraction",
        PLAY_AREA = "Play Area",
        RESTAURANT = "Restaurant",
        BBQ = "Barbeque",
        SNACKS = "Snacks",
        WC = "Toilets"
