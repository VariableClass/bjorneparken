from google.appengine.ext import ndb

class Enclosure(Area):

    # Static Methods
    @staticmethod
    def move_animal(animal, enclosure):

    # Properties
    animals = ndb.StructuredProperty(Animal, repeated=True)

    # Constructors
    def __init__(self, label, visitor_destination, coordinates, animals):
        Area.__init__(self, label, visitor_destination, coordinates)
        self.animals = animals

    # Methods
    def get_species():

        species = []

        # Retrieve the species of each animal in the enclosure and append
        # it to a return list
        for animal in animals:
            species.append(animal.species)

        return species
