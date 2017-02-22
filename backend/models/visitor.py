from google.appengine.ext import ndb

class Visitor(ndb.Model):

    # Properties
    starred_species = ndb.StructuredProperty(Species, repeated=True)
    itinerary = ndb.StructuredProperty(Event, repeated=True)
    visit_start = ndb.DateProperty()
    visit_end = ndb.DateProperty()

    # Methods
    def star_species(species_to_add):
        added = False

        # Check whether species is already favourited
        for species in starred_species
            if species.common_name == species_to_add.common_name
                added = True

        # If species not yet favourited, add to favourites
        if added == False
            starred_species.append(species_to_add)

    def unstar_species(species_to_remove):
        removed = True

        # Check whether species is currently favourited
        for species in starred_species
            if species.common_name == species_to_remove.common_name
                removed = False

        # If species IS favourited, remove from favourites
        if removed == False
            starred_species.remove(species_to_remove)

    def _add_to_itinerary(event_to_add):
        added = False

        # Check whether event is already in the itinerary
        for event in itinerary
            if event == event_to_add
                added = True

        # If event not yet in itinerary, add it
        if added == False
            itinerary.append(event_to_add)

    def _remove_from_itinerary(event_to_remove):
        removed = True

        # Check whether event is currently in itinerary
        for event in itinerary
            if event == event_to_remove
                removed = False

        # If event IS in itinerary, remove it
        if removed == False
            itinerary.remove(event_to_remove)
