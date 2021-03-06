from feeding import Feeding
from google.appengine.ext import ndb
from i18n import InternationalText
from time import Time

class Keeper(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    bio = ndb.LocalStructuredProperty(InternationalText, repeated=True)

    # Methods
    def is_available(self, start_time, end_time):

        # Retrieve all feedings for keeper
        feedings = Feeding.get_all_for_keeper(self)

        # For each feeding, check
        for feeding in feedings:

            # If end time after start_time
            if Time.validate_times(feeding.start_time, end_time):
                # Check start time not before end time
                if Time.validate_times(start_time, feeding.end_time):
                    return False

            # If start time before end_time
            if Time.validate_times(start_time, feeding.end_time):
                # Check end time not after end time
                if Time.validate_times(feeding.start_time, end_time):
                    return False

        return True

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()
