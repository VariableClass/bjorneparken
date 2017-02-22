import Event
import Keeper
from google.appengine.ext import ndb

class Feeding(Event):

    # Properties
    keeper = Keeper

    # Constructors
    def __init__(self, label, description, start_time, end_time, location, active, keeper):

        Event.__init__(self, label, description, start_time, end_time, location, active)
        self.keeper = keeper
