from animal import Animal
from area import Area
from google.appengine.ext import ndb

class Enclosure(Area):

    # Properties
    animals = ndb.StructuredProperty(Animal.AnimalLookup, repeated=True)

    # Methods
    def get_species():

        species = []

        # Retrieve the species of each animal in the enclosure and append
        # it to a return list
        for animal in animals:
            species.append(animal.species)

        return species

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query()

    @classmethod
    def get_all_containing_species(cls, species):

        species_enclosures = []

        # Retrieve all enclosures
        enclosures = cls.query().order(cls.name)

        # For each enclosure
        for enclosure in enclosures:

            # If the desired species exists within an enclosure
            if species in enclosure.get_species():

                # Add the enclosure to the list of compatible enclosures
                species_enclosures.append(enclosure)

        # Return all compatible enclosures
        return species_enclosures
