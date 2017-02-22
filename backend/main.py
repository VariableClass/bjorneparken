# [START imports]
import endpoints
from google.appengine.ext import ndb
from models.animal import Animal
from models.species import Species
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]


# [START messages]
class SpeciesMessage(messages.Message):
    common_name = messages.StringField(1, required=True)
    latin = messages.StringField(2, required=True)
    description = messages.StringField(3, required=True)

class AnimalMessage(messages.Message):
    name = messages.StringField(1, required=True)
    species = messages.MessageField(SpeciesMessage, 2, required=True)
    description = messages.StringField(3, required=True)
    is_available = messages.BooleanField(4, required=True)

class CreateAnimalMessage(messages.Message):
    name = messages.StringField(1, required=True)
    species = messages.StringField(2, required=True)
    description = messages.StringField(3, required=True)
    is_available = messages.BooleanField(4, required=True)

class AnimalListResponse(messages.Message):
    animals = messages.MessageField(AnimalMessage, 1, repeated=True)

class SpeciesListResponse(messages.Message):
    species = messages.MessageField(SpeciesMessage, 1, repeated=True)

# [END messages]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

    @endpoints.method(
        message_types.VoidMessage,
        AnimalListResponse,
        path='animals',
        http_method='GET',
        name='animals.list')
    def list_animals(self, response):
        animals = Animal.get_all()

        response = AnimalListResponse()

        for animal in animals:
            species = SpeciesMessage(common_name=animal.species.common_name, latin=animal.species.latin, description=animal.species.description)
            response.animals.append(AnimalMessage(name=animal.name, species=species, description=animal.description, is_available=animal.is_available))

        return response


    @endpoints.method(
        CreateAnimalMessage,
        message_types.VoidMessage,
        path='animals',
        http_method='POST',
        name='animals.create')
    def create_animal(self, request):

        # Retrieve species from provided ID
        species = ndb.Key(Species, request.species).get()

        # Create new animal
        Animal(name=request.name, species=species, description=request.description, is_available=request.is_available).put()

        return message_types.VoidMessage()


    @endpoints.method(
        message_types.VoidMessage,
        SpeciesListResponse,
        path='species',
        http_method='GET',
        name='species.list')
    def list_species(self, response):
        all_species = Species.get_all()

        response = SpeciesListResponse()

        for species in all_species:
            response.species.append(SpeciesMessage(common_name=species.common_name, latin=species.latin, description=species.description))

        return response


    @endpoints.method(
        SpeciesMessage,
        message_types.VoidMessage,
        path='species',
        http_method='POST',
        name='species.create')
    def create_species(self, request):

        # Create new species
        Species(id=request.common_name, common_name=request.common_name, latin=request.latin, description=request.description).put()

        return message_types.VoidMessage()

# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
