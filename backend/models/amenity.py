from google.appengine.ext import ndb

class Amenity(Area):

    # Properties
    description = ndb.StringProperty()
    amenity_type = ndb.StringProperty()

    class AmenityType(Enum):
        ATTRACTION = "Attraction",
        PLAY_AREA = "Play Area",
        RESTAURANT = "Restaurant",
        BBQ = "Barbeque",
        SNACKS = "Snacks",
        WC = "Toilets"
