from google.appengine.ext import ndb

from species import Species

class Animal(ndb.Model):

    # Static Methods
    @staticmethod
    def get_all_animals():

        bear = Species(common_name="Bear", latin="Ursus Arctos", description="The brown bear is native to Scandinavia.")
        moose = Species(common_name="Moose", latin="Alces Alces", description="The moose is dumb.")

        rugg = Animal(name="Rugg", species=bear, description="Rugg is the father of Trym, Anton and Brutus", is_available=True)
        nora = Animal(name="Nora", species=bear, description="Nora is the mother of Anton, Marit and Brutus", is_available=True)
        leif = Animal(name="Leif", species=moose, description="Leif is a 5 year-old bull")

        animals = []

        animals.append(rugg)
        animals.append(nora)
        animals.append(leif)

        return animals


    # Properties
    name = ndb.StringProperty()
    species = ndb.StructuredProperty(Species)
    description = ndb.StringProperty()
    is_available = ndb.BooleanProperty()
