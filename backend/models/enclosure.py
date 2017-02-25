from animal import Animal
from area import Area
from google.appengine.ext import ndb
from species import Species

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

    def is_available(self):

        unavailable_animals = 0

        # For each animal/species ID pair
        for animal_reference in self.animals:

            # Retrieve animal
            animal = Animal.get_by_id(animal_reference.animal_id, parent=ndb.Key(Species, animal_reference.species_id))

            # If an animal is not available, increase count
            if not animal.is_available:
                unavailable_animals += 1

        # Returns true if some animals are available, false if none are
        return unavailable_animals < len(self.animals)


    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()

    @classmethod
    def get_all_containing_species(cls, species_id):

        # Retrieve all enclosures including animals with the passed species ID
        return cls.query(cls.animals == Animal.AnimalLookup(species_id=species_id)).fetch()

    @classmethod
    def get_for_animal(cls, animal_id, species_id):

        return cls.query(cls.animals == Animal.AnimalLookup(animal_id=animal_id,
                                                            species_id=species_id)).get()
