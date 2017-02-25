from event import Event
from google.appengine.ext import ndb

class Visitor(ndb.Model):

    # Properties
    visit_start = ndb.DateProperty()
    visit_end = ndb.DateProperty()
    starred_species = ndb.IntegerProperty(repeated=True)
    itinerary = ndb.StructuredProperty(Event.EventLookup, repeated=True)

    # Methods
    def star_species(species_to_add):
        added = False

        # Check whether species is already favourited
        for species in starred_species:
            if species.common_name == species_to_add.common_name:
                added = True

        # If species not yet favourited, add to favourites
        if added == False:
            starred_species.append(species_to_add)

    def unstar_species(species_to_remove):
        removed = True

        # Check whether species is currently favourited
        for species in starred_species:
            if species.common_name == species_to_remove.common_name:
                removed = False

        # If species IS favourited, remove from favourites
        if removed == False:
            starred_species.remove(species_to_remove)

    def _add_to_itinerary(event_to_add):
        added = False

        # Check whether event is already in the itinerary
        for event in itinerary:
            if event == event_to_add:
                added = True

        # If event not yet in itinerary, add it
        if added == False:
            itinerary.append(event_to_add)

    def _remove_from_itinerary(event_to_remove):
        removed = True

        # Check whether event is currently in itinerary
        for event in itinerary:
            if event == event_to_remove:
                removed = False

        # If event IS in itinerary, remove it
        if removed == False:
            itinerary.remove(event_to_remove)


    # Class methods

    @classmethod
    def get_all_with_species_starred(cls, species_id):
        return cls.query(cls.starred_species==species_id).fetch()

    @classmethod
    def get_all_with_event_in_itinerary(cls, event_id, location_id):
        return cls.query(cls.itinerary == Event.EventLookup(event_id=event_id, location_id=location_id)).fetch()
