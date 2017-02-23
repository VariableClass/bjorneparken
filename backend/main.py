# [START imports]
import endpoints
from google.appengine.ext import ndb
from models.animal import Animal
from models.i18n import InternationalText
from models.species import Species
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]


# [START messages]
class InternationalMessage(messages.Message):
    text = messages.StringField(1, required=True)
    language_code = messages.StringField(2, required=True)

class SpeciesRequest(messages.Message):
    common_name = messages.MessageField(InternationalMessage, 1, repeated=True)
    latin = messages.StringField(2, required=True)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)

class SpeciesResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    common_name = messages.StringField(2, required=True)
    latin = messages.StringField(3, required=True)
    description = messages.StringField(4, required=True)

class SpeciesListResponse(messages.Message):
    species = messages.MessageField(SpeciesResponse, 1, repeated=True)

class AnimalRequest(messages.Message):
    name = messages.StringField(1, required=True)
    species_id = messages.IntegerField(2, required=True)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    is_available = messages.BooleanField(4, required=True)

class AnimalResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    name = messages.StringField(2, required=True)
    species = messages.MessageField(SpeciesResponse, 3, required=True)
    description = messages.StringField(4, required=True)
    is_available = messages.BooleanField(5, required=True)

class AnimalListResponse(messages.Message):
    animals = messages.MessageField(AnimalResponse, 1, repeated=True)

# [END messages]

# [START resources]
LANGUAGE_RESOURCE = endpoints.ResourceContainer(
    message_types.VoidMessage,
    language_code=messages.StringField(1, required=True))

UPDATE_SPECIES_RESOURCE = endpoints.ResourceContainer(
    SpeciesRequest,
    id=messages.IntegerField(1, required=True))

DELETE_ANIMAL_RESOURCE = endpoints.ResourceContainer(
    animal_id=messages.IntegerField(1, required=True),
    species_id=messages.IntegerField(2, required=True))

DELETE_SPECIES_RESOURCE = endpoints.ResourceContainer(
    species_id=messages.IntegerField(1, required=True))

# [END resources]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

    @endpoints.method(
        LANGUAGE_RESOURCE,
        AnimalListResponse,
        path='animals',
        http_method='GET',
        name='animals.list')
    def list_animals(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all animals
        animals = Animal.get_all()

        response = AnimalListResponse()

        # Build up response of all animals
        for animal in animals:

            species = animal.key.parent().get()

            # Translate translatable species resources
            species_common_name_translation = InternationalText.get_translation(
                    request.language_code,
                    species.common_name)
            species_description_translation = InternationalText.get_translation(
                    request.language_code,
                    species.description)

            # Create species response to include in animal response
            species_response = SpeciesResponse(
                id=species.key.id(),
                common_name=species_common_name_translation,
                latin=species.latin,
                description=species_description_translation)

            # Translate translatable animal resources
            animal_description_translation = InternationalText.get_translation(
                    request.language_code,
                    animal.description)

            # Add animal to return list
            response.animals.append(AnimalResponse(
                id=animal.key.id(),
                name=animal.name,
                species=species_response,
                description=animal_description_translation,
                is_available=animal.is_available))

        return response

    @endpoints.method(
        AnimalRequest,
        message_types.VoidMessage,
        path='animals',
        http_method='POST',
        name='animals.create')
    def create_animal(self, request):

        # Retrieve species from provided ID
        species = ndb.Key(Species, request.species_id).get()

        # If species not found, raise exception
        if not species:
            raise endpoints.BadRequestException("Species of ID '" + str(request.species_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new animal
        Animal(parent=species.key,
            name=request.name,
            description=description,
            is_available=request.is_available).put()

        return message_types.VoidMessage()

    @endpoints.method(
        DELETE_ANIMAL_RESOURCE,
        message_types.VoidMessage,
        path='animals',
        http_method='DELETE',
        name='animals.delete')
    def delete_animal(self, request):

        # Retrieve all animals
        animal = Animal.get_by_id(request.animal_id, parent=ndb.Key(Species, request.species_id))

        if not animal:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No animal found with ID '" +
                                                str(request.animal_id) +
                                                "' and species ID '" +
                                                str(request.species_id) +
                                                "'.")

        animal.key.delete()

        return message_types.VoidMessage()


    @endpoints.method(
        LANGUAGE_RESOURCE,
        SpeciesListResponse,
        path='species',
        http_method='GET',
        name='species.list')
    def list_species(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all species
        all_species = Species.get_all()

        response = SpeciesListResponse()

        # Build up response of all species
        for species in all_species:

            # Translate translatable species esources
            species_common_name_translation = InternationalText.get_translation(
                    request.language_code,
                    species.common_name)
            species_description_translation = InternationalText.get_translation(
                    request.language_code,
                    species.description)

            # Add species to return list
            response.species.append(SpeciesResponse(
                id=species.key.id(),
                common_name=species_common_name_translation,
                latin=species.latin,
                description=species_description_translation))

        return response

    @endpoints.method(
        SpeciesRequest,
        message_types.VoidMessage,
        path='species',
        http_method='POST',
        name='species.create')
    def create_species(self, request):

        pre_existing = Species.get_by_latin_name(request.latin)

        if pre_existing:
            raise endpoints.BadRequestException("A species with latin name '" + request.latin + "' already exists with ID '" + str(pre_existing.key.id()) + "'")

        # Convert InternationalMessage formats to InternationalText
        common_name = self.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new species
        Species(common_name=common_name,
            latin=request.latin,
            description=description).put()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_SPECIES_RESOURCE,
        message_types.VoidMessage,
        path='species/{id}',
        http_method='POST',
        name='species.update')
    def update_species(self, request):

        species = Species.get_by_id(request.id)

        if not species:
            raise endpoints.BadRequestException("No species with latin name '" + request.latin + "' found")

        if request.common_name:
            # Convert InternationalMessage formats to InternationalText
            common_name = self.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)

            # Update common name values
            species.common_name=common_name

        if request.description:
            # Convert InternationalMessage formats to InternationalText
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            species.description=description

        if request.latin:

            # Update latin value
            species.latin=request.latin

        # Write changes
        species.put()

        return message_types.VoidMessage()

    @endpoints.method(
        DELETE_SPECIES_RESOURCE,
        message_types.VoidMessage,
        path='species/{species_id}',
        http_method='DELETE',
        name='species.delete')
    def delete_species(self, request):

        # Retrieve species from ID
        species = Species.get_by_id(request.species_id)

        # If not found, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" + str(request.species_id) + "'.")

        # Retrieve all animals of species
        animals = species.get_animals()

        # Delete each
        for animal in animals:
            animal.key.delete()

        # Delete species
        species.key.delete()

        return message_types.VoidMessage()


    @staticmethod
    def check_language(language_code):
        # If code is not supported, raise BadRequestException
        if not InternationalText.validate_language_code(language_code):
            raise endpoints.BadRequestException("The provided language_code; "
                    + language_code + ", is not currently supported.")

    @staticmethod
    def convert_i18n_messages_to_i18n_texts(international_messages):
        international_texts = []

        # For each InternationalMessage
        for message in international_messages:

            # Append a new InternationalText to the return list, using the
            # values from the InternationalMessage
            international_texts.append(InternationalText(
                text=message.text,
                language_code=message.language_code))

        return international_texts

# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
