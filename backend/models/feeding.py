from event import Event
from enclosure import Enclosure
from keeper import Keeper
from google.appengine.ext import ndb

class Feeding(Event):

    # Properties
    keeper_id = ndb.IntegerProperty()

    # Class Methods
    @classmethod
    def get_all_for_species(species):

        # Return all enclosures containing the specified species
        enclosures = Enclosure.get_all_containing_species(species)

        # Perform query to return feedings where location is compatible enclosure
        return cls.query(cls.location.IN(enclosures))
