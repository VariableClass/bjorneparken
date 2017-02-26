# coding=utf-8
# [START imports]
import datetime
import endpoints
from google.appengine.ext import ndb
from models.amenity import Amenity
from models.animal import Animal
from models.area import Area
from models.enclosure import Enclosure
from models.event import Event
from models.feeding import Feeding
from models.i18n import InternationalText
from models.keeper import Keeper
from models.species import Species
from models.time import Time
from models.version import Version
from models.visitor import Visitor
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]

# [START messages]
# [START api messages]
class InternationalMessage(messages.Message):
    text = messages.StringField(1, required=True)
    language_code = messages.StringField(2, required=True)
# [END api messages]

# [START keeper messages]
class KeeperRequest(messages.Message):
    name = messages.StringField(1)
    bio = messages.MessageField(InternationalMessage, 2, repeated=True)

class KeeperResponse(messages.Message):
    id = messages.IntegerField(1)
    name = messages.StringField(2)
    bio = messages.StringField(3)

class KeeperListResponse(messages.Message):
    keepers = messages.MessageField(KeeperResponse, 1, repeated=True)
# [END keeper messages]

# [START species messages]
class SpeciesRequest(messages.Message):
    common_name = messages.MessageField(InternationalMessage, 1, repeated=True)
    latin = messages.StringField(2)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)

class SpeciesResponse(messages.Message):
    id = messages.IntegerField(1)
    common_name = messages.StringField(2)
    latin = messages.StringField(3)
    description = messages.StringField(4)

class SpeciesListResponse(messages.Message):
    species = messages.MessageField(SpeciesResponse, 1, repeated=True)
# [END species messages]

# [START animal messages]
class AnimalReference(messages.Message):
    animal_id = messages.IntegerField(1, required=True)
    species_id = messages.IntegerField(2, required=True)

class AnimalRequest(messages.Message):
    name = messages.StringField(1)
    species_id = messages.IntegerField(2)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    is_available = messages.BooleanField(4)

class UpdateAnimalRequest(messages.Message):
    name = messages.StringField(1)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    is_available = messages.BooleanField(3)

class AnimalResponse(messages.Message):
    id = messages.IntegerField(1)
    name = messages.StringField(2)
    species = messages.MessageField(SpeciesResponse, 3)
    description = messages.StringField(4)
    is_available = messages.BooleanField(5)

class AnimalListResponse(messages.Message):
    animals = messages.MessageField(AnimalResponse, 1, repeated=True)
# [END animal messages]

# [START area messages]
# All Enclosure and Amenity messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance
class EnclosureRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2)
    coordinates = messages.StringField(3, repeated=True)
    animals = messages.MessageField(AnimalReference, 4, repeated=True)

class AmenityRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2)
    coordinates = messages.StringField(3, repeated=True)
    description = messages.MessageField(InternationalMessage, 4, repeated=True)
    amenity_type = messages.StringField(5)

class EnclosureResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    animals = messages.MessageField(AnimalResponse, 5, repeated=True)

class AmenityResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    description = messages.StringField(5)
    amenity_type = messages.StringField(6)

class AreaListResponse(messages.Message):
    enclosures = messages.MessageField(EnclosureResponse, 1, repeated=True)
    amenities = messages.MessageField(AmenityResponse, 2, repeated=True)
# [END area messages]

# [START event messages]
# All Event and Feeding messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance
class EventReference(messages.Message):
    event_id = messages.IntegerField(1, required=True)
    location_id = messages.IntegerField(2, required=True)

class EventRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    location_id = messages.IntegerField(3)
    start_time = messages.StringField(4)
    end_time = messages.StringField(5)
    is_active = messages.BooleanField(6)

class UpdateEventRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)

class FeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    location_id = messages.IntegerField(3)
    start_time = messages.StringField(4)
    end_time = messages.StringField(5)
    is_active = messages.BooleanField(6)
    keeper_id = messages.IntegerField(7)

class UpdateFeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)
    keeper_id = messages.IntegerField(6)

class EventResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    description = messages.StringField(3)
    location = messages.MessageField(AmenityResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)

class FeedingResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    description = messages.StringField(3)
    location = messages.MessageField(EnclosureResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)
    keeper = messages.MessageField(KeeperResponse, 8)

class EventListResponse(messages.Message):
    events = messages.MessageField(EventResponse, 1, repeated=True)
    feedings = messages.MessageField(FeedingResponse, 2, repeated=True)
# [END event messages]

# [START visitor messages]
class VisitorRequest(messages.Message):
    visit_start = message_types.DateTimeField(1)
    visit_end = message_types.DateTimeField(2)
    starred_species = messages.IntegerField(3, repeated=True)
    itinerary = messages.MessageField(EventReference, 4, repeated=True)

class VisitorResponse(messages.Message):
    id = messages.IntegerField(1)
# [END visitor messages]
# [END messages]

# [START request resources]
# [START list request resources]
LANGUAGE_CODE_REQUEST = endpoints.ResourceContainer(
    language_code=messages.StringField(1, required=True))
ID_LANGUAGE_REQUEST = endpoints.ResourceContainer(
    language_code=messages.StringField(1, required=True),
    id=messages.IntegerField(2, required=True))
# [END list request resources]

# [START get/delete request resources]
ID_REQUEST = endpoints.ResourceContainer(
    id=messages.IntegerField(1, required=True))
ANIMAL_ID_REQUEST = endpoints.ResourceContainer(
    animal_id=messages.IntegerField(1, required=True),
    species_id=messages.IntegerField(2, required=True))
EVENT_ID_REQUEST = endpoints.ResourceContainer(
    EventReference)
# [END get/delete request resources]

# [START update request resources]
UPDATE_AMENITY_REQUEST = endpoints.ResourceContainer(
    AmenityRequest,
    id=messages.IntegerField(1, required=True))
UPDATE_ANIMAL_REQUEST = endpoints.ResourceContainer(
    UpdateAnimalRequest,
    animal_id=messages.IntegerField(1, required=True),
    species_id=messages.IntegerField(2, required=True))
UPDATE_ENCLOSURE_REQUEST = endpoints.ResourceContainer(
    EnclosureRequest,
    id=messages.IntegerField(1, required=True))
UPDATE_EVENT_REQUEST = endpoints.ResourceContainer(
    UpdateEventRequest,
    event_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))
UPDATE_FEEDING_REQUEST = endpoints.ResourceContainer(
    UpdateFeedingRequest,
    feeding_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))
UPDATE_KEEPER_REQUEST = endpoints.ResourceContainer(
    KeeperRequest,
    id=messages.IntegerField(1, required=True))
UPDATE_SPECIES_REQUEST = endpoints.ResourceContainer(
    SpeciesRequest,
    id=messages.IntegerField(1, required=True))
UPDATE_VISITOR_REQUEST = endpoints.ResourceContainer(
    VisitorRequest,
    id=messages.IntegerField(1, required=True))
# [END update request resources]
# [END request resources]

# [START bjorneparkappen_api]
bjorneparkappen_api = endpoints.api(name='bjorneparkappen_api', version="v1.0", api_key_required=True)

# [START ApiHelper]
class ApiHelper():

    ### Static Methods ###

    # Executed whenever a write operation is performed on park data, which
    # includes creation, updating or deletion of any of the following;
    # Amenity, Animal, Enclosure, Event, Feeding, Keeper or Species
    @staticmethod
    def update_version():

        # Retrieve version
        version = Version.get()

        # If no version exists, create one
        if not version:
            version = Version()

        # Set version to current datetime
        version.version = datetime.datetime.now()

        # Write changes
        version.put()

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

    @staticmethod
    def get_enclosure_response(area, language_code):

        # Translate translatable area resources
        area_label_translation = InternationalText.get_translation(
                language_code,
                area.label)

        coordinates = []

        # Convert co-ordinate pairs to strings
        for coordinate in area.coordinates:
            coordinates.append(str(coordinate.lon) + ", " + str(coordinate.lat))

        animals = []

        # For each animal/species key set
        for animal_reference in area.animals:

            # Retrieve animal
            animal = Animal.get_by_id(animal_reference.animal_id, parent=ndb.Key(Species, animal_reference.species_id))

            # Retrieve species
            species = animal.key.parent().get()

            # Translate translatable species resources
            species_common_name_translation = InternationalText.get_translation(
                    language_code,
                    species.common_name)
            species_description_translation = InternationalText.get_translation(
                    language_code,
                    species.description)

            # Create species response to include in animal response
            species_response = SpeciesResponse(
                id=species.key.id(),
                common_name=species_common_name_translation,
                latin=species.latin,
                description=species_description_translation)

            # Translate translatable animal resources
            animal_description_translation = InternationalText.get_translation(
                    language_code,
                    animal.description)

            # Add animal to return list
            animals.append(AnimalResponse(
                id=animal.key.id(),
                name=animal.name,
                species=species_response,
                description=animal_description_translation,
                is_available=animal.is_available))

        return EnclosureResponse(
            id=area.key.id(),
            label=area_label_translation,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            animals=animals)

    @staticmethod
    def get_amenity_response(area, language_code):

        # Translate translatable area resources
        area_label_translation = InternationalText.get_translation(
                language_code,
                area.label)

        amenity_description_translation = InternationalText.get_translation(
                language_code,
                area.description)

        coordinates = []

        # Convert co-ordinate pairs to strings
        for coordinate in area.coordinates:
            coordinates.append(str(coordinate.lon) + ", " + str(coordinate.lat))

        return AmenityResponse(
            id=area.key.id(),
            label=area_label_translation,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            description=amenity_description_translation,
            amenity_type=area.amenity_type)


    ### Class Methods

    @classmethod
    def get_event_response(cls, event, language_code):

        # Translate translatable event resources
        event_label_translation = InternationalText.get_translation(
                language_code,
                event.label)

        event_description_translation = InternationalText.get_translation(
                language_code,
                event.description)

        # Retrieve location
        location = cls.get_amenity_response(event.key.parent().get(), language_code)

        return EventResponse(
            id=event.key.id(),
            label=event_label_translation,
            description=event_description_translation,
            location=location,
            start_time=event.start_time,
            end_time=event.end_time,
            is_active=event.is_active)

    @classmethod
    def get_feeding_response(cls, feeding, language_code):

        # Translate translatable event resources
        feeding_label_translation = InternationalText.get_translation(
                language_code,
                feeding.label)

        feeding_description_translation = InternationalText.get_translation(
                language_code,
                feeding.description)

        # Retrieve location
        location = cls.get_enclosure_response(feeding.key.parent().get(), language_code)

        # Retrieve keeper
        keeper = ndb.Key(Keeper, feeding.keeper_id).get()

        # Translate translatable keeper resources
        keeper_bio_translation = InternationalText.get_translation(
                language_code,
                keeper.bio)

        # Create keeper response to include in feeding response
        keeper_response = KeeperResponse(
            id=keeper.key.id(),
            name=keeper.name,
            bio=keeper_bio_translation)

        return FeedingResponse(
            id=feeding.key.id(),
            label=feeding_label_translation,
            description=feeding_description_translation,
            location=location,
            start_time=feeding.start_time,
            end_time=feeding.end_time,
            is_active=feeding.is_active,
            keeper=keeper_response)
# [END ApiHelper]

# [START Species API]
@bjorneparkappen_api.api_class(resource_name='species', path='species')
class SpeciesApi(remote.Service):

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        SpeciesListResponse,
        path='all',
        http_method='GET',
        name='species.all')
    def list_species(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all species
        all_species = Species.get_all()

        response = SpeciesListResponse()

        # Build up response of all species
        for species in all_species:

            # Translate translatable species resources
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
        path='create',
        http_method='POST',
        name='species.create')
    def create_species(self, request):

        # Validate all required values have been provided
        if not request.common_name and request.latin and request.description:
            raise endpoints.BadRequestException("Please provided values for 'common_name', 'latin' and 'description'.")

        # Attempt to retrieve species
        pre_existing = Species.get_by_latin_name(request.latin)

        # If species currently exists, raise BadRequestException
        if pre_existing:
            raise endpoints.BadRequestException("A species with latin name '" + request.latin + "' already exists with ID '" + str(pre_existing.key.id()) + "'")

        # Convert InternationalMessage formats to InternationalText
        common_name = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new species
        Species(common_name=common_name,
            latin=request.latin,
            description=description).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_SPECIES_REQUEST,
        message_types.VoidMessage,
        path='update',
        http_method='POST',
        name='species.update')
    def update_species(self, request):

        # Attempt to retrieve species
        species = Species.get_by_id(request.id)

        # If species does not exist, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" +
                                                str(request.id) + "'.")

        # If values for common_name provided
        if request.common_name:
            # Convert InternationalMessage formats to InternationalText
            common_name = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)

            # Update common name values
            species.common_name=common_name

        # If values for description provided
        if request.description:
            # Convert InternationalMessage formats to InternationalText
            description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            species.description=description

        # If value for latin provided
        if request.latin:

            # Update latin value
            species.latin=request.latin

        # Write changes
        species.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        ID_REQUEST,
        message_types.VoidMessage,
        path='delete',
        http_method='DELETE',
        name='species.delete')
    def delete_species(self, request):

        # Retrieve species from ID
        species = Species.get_by_id(request.id)

        # If not found, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" + str(request.id) + "'.")

        # Retrieve all animals of species
        animals = species.get_animals()

        # Delete each
        for animal in animals:
            animal.key.delete()

        # Retrieve all visitors who have starred the species
        visitors = Visitor.get_all_with_species_starred(request.id)

        # Remove from all visitors itineraries
        for visitor in visitors:
            visitor.starred_species.remove(request.id)
            visitor.put()

        # Delete species
        species.key.delete()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()
# [END Species API]

# [START Animals API]
@bjorneparkappen_api.api_class(resource_name='animals', path='animals')
class AnimalsApi(remote.Service):

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        AnimalListResponse,
        path='all',
        http_method='GET',
        name='animals.all')
    def list_animals(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all animals
        animals = Animal.get_all()

        response = AnimalListResponse()

        # Build up response of all animals
        for animal in animals:

            # Retrieve species
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
        path='create',
        http_method='POST',
        name='animals.create')
    def create_animal(self, request):

        # Validate all required values have been provided
        if not request.name and request.species_id and request.description and request.is_available is not None:
            raise endpoints.BadRequestException("Please provided values for 'name', 'species_id', 'description' and 'is_available'.")

        # Retrieve species from provided ID
        species = ndb.Key(Species, request.species_id).get()

        # If species not found, raise exception
        if not species:
            raise endpoints.BadRequestException("Species of ID '" + str(request.species_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new animal
        Animal(parent=species.key,
            name=request.name,
            description=description,
            is_available=request.is_available).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_ANIMAL_REQUEST,
        message_types.VoidMessage,
        path='update',
        http_method='POST',
        name='animals.update')
    def update_animal(self, request):

        # Retrieve animal
        animal = Animal.get_by_id(request.animal_id, parent=ndb.Key(Species, request.species_id))

        # If not found, raise BadRequestException
        if not animal:
            raise endpoints.BadRequestException("No animal found with ID '" +
                                                str(request.animal_id) +
                                                "' and species ID '" +
                                                str(request.species_id) +
                                                "'.")

        # If value for name provided
        if request.name:
            # Update name value
            animal.name = request.name

        # If values for description provided
        if request.description:
            # Convert InternationalMessage formats to InternationalText
            description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            animal.description=description

        # If value for is_available provided
        if not request.is_available is None:
            # Update is_available value
            animal.is_available = request.is_available

        # Write changes
        animal.put()

        # Perform updates to any feedings including the animal
        if not request.is_available is None:
            # Retrieve animal enclosure
            enclosure = Enclosure.get_for_animal(animal_id=request.animal_id, species_id=request.species_id)

            # Retrieve all feedings which include the enclosure
            feedings = Feeding.get_all_for_enclosure(enclosure)

            # Set feeding to active state of enclosure
            for feeding in feedings:
                feeding.is_active = enclosure.is_available()
                feeding.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        ANIMAL_ID_REQUEST,
        message_types.VoidMessage,
        path='delete',
        http_method='DELETE',
        name='animals.delete')
    def delete_animal(self, request):

        # Retrieve animal
        animal = Animal.get_by_id(request.animal_id, parent=ndb.Key(Species, request.species_id))

        # If not found, raise BadRequestException
        if not animal:
            raise endpoints.BadRequestException("No animal found with ID '" +
                                                str(request.animal_id) +
                                                "' and species ID '" +
                                                str(request.species_id) +
                                                "'.")

        # Retrieve animal enclosure
        enclosure = Enclosure.get_for_animal(animal_id=request.animal_id, species_id=request.species_id)

        # Remove animal from enclosure
        for animal_reference in enclosure:
            if animal_reference.animal_id == request.animal_id and animal_reference.species_id == request.species_id:
                enclosure.remove(animal_reference)

        # Retrieve all feedings which include the enclosure
        feedings = Feeding.get_all_for_enclosure(enclosure)

        # Set feeding to active state of enclosure
        for feeding in feedings:
            feeding.is_active = enclosure.is_available()
            feeding.put()

        # Delete animal
        animal.key.delete()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()
# [END Animals API]

# [START Areas API]
@bjorneparkappen_api.api_class(resource_name='areas', path='areas')
class AreasApi(remote.Service):

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        AreaListResponse,
        path='all',
        http_method='GET',
        name='areas.all')
    def list_areas(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Enclosure
            if type(area) is Enclosure:

                # Retrieve an Enclosure response
                area_response = ApiHelper.get_enclosure_response(area, request.language_code)

                # Add area to return list
                response.enclosures.append(area_response)

            # Else if area is an Amenity
            elif type(area) is Amenity:

                # Retrieve an Amenity response
                area_response = ApiHelper.get_amenity_response(area, request.language_code)

                # Add area to return list
                response.amenities.append(area_response)

            else:
                raise endpoints.UnexpectedException("Area found which is neither Enclosure nor Amenity, consult developer.")

        return response

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        AreaListResponse,
        path='enclosures/all',
        http_method='GET',
        name='areas.enclosures.all')
    def list_enclosures(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Enclosure
            if type(area) is Enclosure:

                # Retrieve an enclosure response
                area_response = ApiHelper.get_enclosure_response(area, request.language_code)

                # Add area to return list
                response.enclosures.append(area_response)

        return response

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        AreaListResponse,
        path='amenities/all',
        http_method='GET',
        name='areas.amenities.all')
    def list_amenities(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Amenity
            if type(area) is Amenity:

                # Retrieve an amenity response
                area_response = ApiHelper.get_amenity_response(area, request.language_code)

                # Add area to return list
                response.amenities.append(area_response)

        return response

    @endpoints.method(
        EnclosureRequest,
        message_types.VoidMessage,
        path='enclosures/create',
        http_method='POST',
        name='areas.enclosures.create')
    def create_enclosure(self, request):

        # TODO Check for intersection of any existing areas

        # Validate all required values have been provided
        if not request.label and request.visitor_destination and request.coordinates:
            raise endpoints.BadRequestException("Please provided values for 'label', 'visitor_destination' and 'coordinates'.")

        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

        try:
            # Convert visitor destination from string to GeoPt
            visitor_destination_array = request.visitor_destination.split(", ")
            visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

        except:
            raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        coordinates = []

        # Convert coordinates into usable format
        for coordinate_string in request.coordinates:
            coordinate_array = coordinate_string.split(", ")
            coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))

        animals = []

        # For each animal/species key set
        for animal_reference in request.animals:

            # Retrieve animal
            animal = Animal.get_by_id(animal_reference.animal_id, parent=ndb.Key(Species, animal_reference.species_id))

            if not animal:
                # If not found, raise BadRequestException
                raise endpoints.BadRequestException("No animal found with ID '" +
                                                    str(animal_reference.animal_id) +
                                                    "' and species ID '" +
                                                    str(animal_reference.species_id) +
                                                    "'.")

            # Add animal reference to list
            animals.append(Animal.AnimalLookup(animal_id=animal_reference.animal_id,
                                                species_id=animal_reference.species_id))

        # Create new enclosure
        Enclosure(label=label,
                visitor_destination=visitor_destination,
                coordinates=coordinates,
                animals=animals).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        AmenityRequest,
        message_types.VoidMessage,
        path='amenities/create',
        http_method='POST',
        name='areas.amenities.create')
    def create_amenity(self, request):

        # TODO Check for intersection of any existing areas

        # Validate all required values have been provided
        if not request.label and request.visitor_destination and request.coordinates and request.description and request.amenity_type:
            raise endpoints.BadRequestException("Please provided values for 'label', 'visitor_destination', 'coordinates', 'description' and 'amenity_type'.")


        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        try:
            # Convert visitor destination from string to GeoPt
            visitor_destination_array = request.visitor_destination.split(", ")
            visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

        except:
            raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        coordinates = []

        # Convert coordinates into usable format
        for coordinate_string in request.coordinates:
            coordinate_array = coordinate_string.split(", ")
            coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))

        # Validate AmenityType is existing AmenityType
        if not Amenity.AmenityType.validate(request.amenity_type):

            # Generate a string representation of the Amenity.AmenityType enum
            amenity_types = [amenity_type.name for amenity_type in Amenity.AmenityType]
            amenity_types.sort()
            string_amenity_types = ', '.join(amenity_types)

            # Raise BadRequestException
            raise endpoints.BadRequestException("No AmenityType found by the name '" + request.amenity_type +
                                                "'. Amenities may be any of the following: " + string_amenity_types)

        # Set amenity type
        amenity_type = Amenity.AmenityType[request.amenity_type]

        # Create new amenity
        Amenity(label=label,
                visitor_destination=visitor_destination,
                coordinates=coordinates,
                description=description,
                amenity_type=amenity_type.name).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_ENCLOSURE_REQUEST,
        message_types.VoidMessage,
        path='enclosures/update',
        http_method='POST',
        name='areas.enclosures.update')
    def update_enclosure(self, request):

        # Attempt to retrieve enclosure
        enclosure = Enclosure.get_by_id(request.id)

        # If enclosure does not exist, raise BadRequestException
        if not enclosure:
            raise endpoints.BadRequestException("No enclosure found with ID '" +
                                                str(request.id) + "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            enclosure.label=label

        # If value for visitor destination provided
        if request.visitor_destination:

            try:
                # Convert visitor destination from string to GeoPt
                visitor_destination_array = request.visitor_destination.split(", ")

                # Update visitor destination value
                enclosure.visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

            except:
                raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")


        # If values for co-ordinates provided
        if request.coordinates:

            coordinates = []

            # Convert coordinates into usable format
            for coordinate_string in request.coordinates:
                coordinate_array = coordinate_string.split(", ")
                coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))

        # If values for animals provided
        if request.animals:

            animals = []

            # For each animal/species key set
            for animal_reference in request.animals:

                # Retrieve animal
                animal = Animal.get_by_id(animal_reference.animal_id, parent=ndb.Key(Species, animal_reference.species_id))

                if not animal:
                    # If not found, raise BadRequestException
                    raise endpoints.BadRequestException("No animal found with ID '" +
                                                        str(animal_reference.animal_id) +
                                                        "' and species ID '" +
                                                        str(animal_reference.species_id) +
                                                        "'.")

                # Add animal reference to list
                animals.append(Animal.AnimalLookup(animal_id=animal_reference.animal_id,
                                                    species_id=animal_reference.species_id))

                enclosure.animals = animals

        # Write changes
        enclosure.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_AMENITY_REQUEST,
        message_types.VoidMessage,
        path='amenities/update',
        http_method='POST',
        name='areas.amenities.update')
    def update_amenity(self, request):

        # Attempt to retrieve amenity
        amenity = Amenity.get_by_id(request.id)

        # If amenity does not exist, raise BadRequestException
        if not amenity:
            raise endpoints.BadRequestException("No amenity found with ID '" +
                                                str(request.id) + "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            amenity.label=label


        # If value for visitor destination provided
        if request.visitor_destination:

            try:
                # Convert visitor destination from string to GeoPt
                visitor_destination_array = request.visitor_destination.split(", ")

                # Update visitor destination value
                amenity.visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

            except:
                raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        # If values for co-ordinates provided
        if request.coordinates:

            coordinates = []

            # Convert coordinates into usable format
            for coordinate_string in request.coordinates:
                coordinate_array = coordinate_string.split(", ")
                coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))

        # If values for description provided
        if request.description:

            # Convert InternationalMessage formats to InternationalText
            description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update common name values
            amenity.description=description

        # If values for amenity type provided
        if request.amenity_type:

            if not Amenity.AmenityType.validate(request.amenity_type):

                # Generate a string representation of the Amenity.AmenityType enum
                amenity_types = [amenity_type.name for amenity_type in Amenity.AmenityType]
                amenity_types.sort()
                string_amenity_types = ', '.join(amenity_types)

                # Raise BadRequestException
                raise endpoints.BadRequestException("No AmenityType found by the name '" + request.amenity_type +
                                                    "'. Amenities may be any of the following: " + string_amenity_types)

        # Write changes
        amenity.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        ID_REQUEST,
        message_types.VoidMessage,
        path='delete',
        http_method='DELETE',
        name='areas.delete')
    def delete_area(self, request):

        # Retrieve area from ID
        area = Area.get_by_id(request.id)

        # If not found, raise BadRequestException
        if not area:
            raise endpoints.BadRequestException("No area found with ID '" + str(request.id) + "'.")

        # Retrieve all events at the area
        events = area.get_events()

        # Delete each
        for event in events:
            event.key.delete()

        # Delete area
        area.key.delete()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()
# [END Areas API]

# [START Events API]
@bjorneparkappen_api.api_class(resource_name='events', path='events')
class EventsApi(remote.Service):

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        EventListResponse,
        path='all',
        http_method='GET',
        name='events.all')
    def list_all_events(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Event
            if type(event) is Event:

                # Retrieve an Event response
                event_response = ApiHelper.get_event_response(event, request.language_code)

                # Add event to return list
                response.events.append(event_response)

            # Else if event is a Feeding
            elif type(event) is Feeding:

                # Retrieve a Feeding response
                feeding_response = ApiHelper.get_feeding_response(event, request.language_code)

                # Add feeding to return list
                response.feedings.append(feeding_response)

        return response

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        EventListResponse,
        path='events',
        http_method='GET',
        name='events.events')
    def list_events(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Event
            if type(event) is Event:

                # Retrieve an Event response
                event_response = ApiHelper.get_event_response(event, request.language_code)

                # Add event to return list
                response.events.append(event_response)

        return response

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        EventListResponse,
        path='feedings/all',
        http_method='GET',
        name='events.feedings.all')
    def list_feedings(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Feeding
            if type(event) is Feeding:

                # Retrieve an Feeding response
                feeding_response = ApiHelper.get_feeding_response(event, request.language_code)

                # Add event to return list
                response.feedings.append(feeding_response)

        return response

    @endpoints.method(
        ID_LANGUAGE_REQUEST,
        EventListResponse,
        path='feedings/species',
        http_method='GET',
        name='events.feedings.species')
    def list_feedings_for_species(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve species from ID
        species = Species.get_by_id(request.id)

        # If not found, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" + str(request.id) + "'.")

        # Retrieve all events
        events = Feeding.get_all_for_species(request.id)

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # Retrieve an Feeding response
            feeding_response = ApiHelper.get_feeding_response(event, request.language_code)

            # Add event to return list
            response.feedings.append(feeding_response)

        return response

    @endpoints.method(
        ID_LANGUAGE_REQUEST,
        EventListResponse,
        path='amenity',
        http_method='GET',
        name='events.amenity')
    def list_events_for_amenity(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve amenity
        amenity = Amenity.get_by_id(request.id)

        # If amenity does not exist, raise BadRequestException
        if not amenity:
            raise endpoints.BadRequestException("No amenity found with ID '" +
                                                str(request.id) + "'.")

        # Retrieve all events for amenity
        events = Event.get_all_for_location(amenity)

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # Retrieve an Event response
            event_response = ApiHelper.get_event_response(event, request.language_code)

            # Add event to return list
            response.events.append(event_response)

        return response

    @endpoints.method(
        EventRequest,
        message_types.VoidMessage,
        path='create',
        http_method='POST',
        name='events.create')
    def create_event(self, request):

        # TODO Check for clashes at a location

        # Validate all required values have been provided
        if not request.label and request.description and request.location_id and request.start_time and request.end_time and is_active is not None:
            raise endpoints.BadRequestException("Please provided values for 'label', 'description', 'location_id', 'start_time', 'end_time' and 'is_active'.")

        # Retrieve location from provided ID
        location = Area.get_by_id(request.location_id)

        # If location not found, raise exception
        if not location or location._class_name() is Enclosure._class_name():
            raise endpoints.BadRequestException("Amenity with ID '" + str(request.location_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Validate times
        if not Time.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # Create new event
        Event(parent=location.key,
            label=label,
            description=description,
            start_time=request.start_time,
            end_time=request.end_time,
            is_active=request.is_active).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_EVENT_REQUEST,
        message_types.VoidMessage,
        path='update',
        http_method='POST',
        name='events.update')
    def update_event(self, request):

        # Attempt to retrieve event
        event = Event.get_by_id(request.event_id, parent=ndb.Key(Amenity, request.location_id))

        # If event does not exist, raise BadRequestException
        if not event:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No event found with ID '" +
                                                str(request.event_id) +
                                                "' and location ID '" +
                                                str(request.location_id) +
                                                "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            event.label=label

        # If values for description provided
        if request.description:

            # Convert InternationalMessage formats to InternationalText
            description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            event.description=description

        # If value for start_time provided
        if request.start_time:
            # Temporarily store the passed start time
            temp_start_time = request.start_time

        # If value for start_time provided
        if request.end_time:
            # Temporarily store the passed end time
            temp_end_time = request.end_time

        # Validate times
        if not Time.validate_times(temp_start_time, temp_end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # If value for start_time provided
        if request.start_time:
            event.start_time = request.start_time

        # If value for end_time provided
        if request.end_time:
            event.start_time = request.start_time

        # Check if value for is_active provided
        try:
            request.is_active

        except NameError:
            # Ignore if not
            pass

        else:
            # Update is_active value
            event.is_active=request.is_active

        # Write changes
        event.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        FeedingRequest,
        message_types.VoidMessage,
        path='feedings/create',
        http_method='POST',
        name='events.feedings.create')
    def create_feeding(self, request):

        # TODO Check for clashes at a location

        # Validate all required values have been provided
        if not request.label and request.description and request.location_id and request.start_time and request.end_time and is_active is not None:
            raise endpoints.BadRequestException("Please provided values for 'label', 'description', 'location_id', 'start_time', 'end_time' and 'is_active'.")

        # Retrieve location from provided ID
        location = Area.get_by_id(request.location_id)

        # If location not found, raise exception
        if not location or location._class_name() is Amenity._class_name():
            raise endpoints.BadRequestException("Enclosure of ID '" + str(request.location_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Validate times
        if not Time.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # If keeper provided
        if request.keeper_id:
            # Retrieve keeper
            keeper = ndb.Key(Keeper, request.keeper_id).get()

            # If keeper not found, raise BadRequestException
            if not keeper:
                raise endpoints.BadRequestException("No keeper by ID '" + str(request.keeper_id) + "' found.")

            # If keeper busy, raise BadRequestException
            if not keeper.is_available(request.start_time, request.end_time):
                raise endpoints.BadRequestException("Keeper '" + str(request.keeper_id) + "' busy during requested times.")

            # Create new feeding with keeper
            feeding = Feeding(parent=location.key,
                label=label,
                description=description,
                start_time=request.start_time,
                end_time=request.end_time,
                is_active=request.is_active,
                keeper_id=request.keeper_id)

        else:

            # Create new feeding without keeper
            feeding = Feeding(parent=location.key,
                label=label,
                description=description,
                start_time=request.start_time,
                end_time=request.end_time,
                is_active=request.is_active)

        # Write changes
        feeding.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_FEEDING_REQUEST,
        message_types.VoidMessage,
        path='feedings/update',
        http_method='POST',
        name='events.feedings.update')
    def update_feeding(self, request):

        # Attempt to retrieve feeding
        feeding = Feeding.get_by_id(request.feeding_id, parent=ndb.Key(Enclosure, request.location_id))

        # If feeding does not exist, raise BadRequestException
        if not feeding:
            raise endpoints.BadRequestException("No feeding found with ID '" +
                                                str(request.feeding_id) +
                                                "' and location ID '" +
                                                str(request.location_id) +
                                                "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            feeding.label=label

        # If values for description provided
        if request.description:

            # Convert InternationalMessage formats to InternationalText
            description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            feeding.description=description

        # If value for start_time provided
        if request.start_time:
            # Temporarily store the passed start time
            temp_start_time = request.start_time

        # If value for start_time provided
        if request.end_time:
            # Temporarily store the passed end time
            temp_end_time = request.end_time

        # Validate times
        if not Time.validate_times(temp_start_time, temp_end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # Retrieve keeper
        keeper = ndb.Key(Keeper, request.keeper_id).get()

        # If keeper not found, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper by ID '" + str(request.keeper_id) + "' found.")

        # If keeper busy, raise BadRequestException
        if not keeper.is_available(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Keeper '" + str(request.keeper_id) + "' busy during requested times.")

        # If value for start_time provided
        if request.start_time:
            feeding.start_time = request.start_time

        # If value for end_time provided
        if request.end_time:
            feeding.start_time = request.start_time

        # Check if value for is_active provided
        try:
            request.is_active

        except NameError:
            # Ignore if not
            pass

        else:
            # Update is_active value
            feeding.is_active=request.is_active

        # If value for keeper_id provided
        if request.keeper_id:

            # Retrieve keeper from provided ID
            keeper = ndb.Key(Keeper, request.keeper_id).get()

            # If keeper not found, raise exception
            if not keeper:
                raise endpoints.BadRequestException("Keeper of ID '" + str(request.keeper_id) + "' not found")

            # Update keeper_id value
            feeding.keeper_id = keeper_id

        # Write changes
        feeding.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        EVENT_ID_REQUEST,
        message_types.VoidMessage,
        path='delete',
        http_method='DELETE',
        name='events.delete')
    def delete_event(self, request):

        # Attempt to retrieve event
        event = Event.get_by_id(request.event_id, parent=ndb.Key(Amenity, request.location_id))

        # If event not found against any amenities, search against enclosures
        if not event:
            event = Event.get_by_id(request.event_id, parent=ndb.Key(Enclosure, request.location_id))

        # If event does not exist, raise BadRequestException
        if not event:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No event found with ID '" +
                                                str(request.event_id) +
                                                "' and location ID '" +
                                                str(request.location_id) +
                                                "'.")

        # Retrieve all visitors who have added the event to their itineraries
        visitors = Visitor.get_all_with_event_in_itinerary(request.event_id, request.location_id)

        # For each visitor with the event in their itinerary
        for visitor in visitors:

            # For each event
            for event_reference in visitor.itinerary:

                # If the event reference matches the event to be deleted
                if event_reference.event_id == request.event_id and event_reference.location_id == request.location_id:

                    # Remove the event from the visitor's itinerary
                    visitor.itinerary.remove(event_reference)

            # Write changes
            visitor.put()

        # Delete area
        event.key.delete()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()
# [END Events API]

# [START Keepers API]
@bjorneparkappen_api.api_class(resource_name='keepers', path='keepers')
class KeepersApi(remote.Service):

    @endpoints.method(
        LANGUAGE_CODE_REQUEST,
        KeeperListResponse,
        path='all',
        http_method='GET',
        name='keepers.all')
    def list_keepers(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Retrieve all keepers
        keepers = Keeper.get_all()

        response = KeeperListResponse()

        # Build up response of all keepers
        for keeper in keepers:

            # Translate translatable keeper resources
            keeper_bio_translation = InternationalText.get_translation(
                    request.language_code,
                    keeper.bio)

            # Add keeper to return list
            response.keepers.append(KeeperResponse(
                id=keeper.key.id(),
                name=keeper.name,
                bio=keeper_bio_translation))

        return response

    @endpoints.method(
        KeeperRequest,
        message_types.VoidMessage,
        path='create',
        http_method='POST',
        name='keepers.create')
    def create_keeper(self, request):

        # Validate all required values have been provided
        if not request.name and request.bio:
            raise endpoints.BadRequestException("Please provided values for 'name' and 'bio'.")


        # Convert InternationalMessage formats to InternationalText
        bio = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.bio)

        # Create new keeper
        Keeper(name=request.name,
            bio=bio).put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_KEEPER_REQUEST,
        message_types.VoidMessage,
        path='update',
        http_method='POST',
        name='keepers.update')
    def update_keeper(self, request):

        # Attempt to retrieve keeper
        keeper = Keeper.get_by_id(request.id)

        # If keeper does not exist, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper found with ID '" +
                                                str(request.id) + "'.")

        # If value for name provided
        if request.name:
            # Update name value
            keeper.name=request.name

        # If values for bio provided
        if request.bio:
            # Convert InternationalMessage formats to InternationalText
            bio = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.bio)

            # Update bio values
            keeper.bio=bio

        # Write changes
        keeper.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        ID_REQUEST,
        message_types.VoidMessage,
        path='delete',
        http_method='DELETE',
        name='keepers.delete')
    def delete_keeper(self, request):

        # Retrieve keeper from ID
        keeper = Keeper.get_by_id(request.id)

        # If not found, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper found with ID '" + str(request.id) + "'.")

        # Delete species
        keeper.key.delete()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()
# [END Keepers API]

# [START Visitors API]
@bjorneparkappen_api.api_class(resource_name='visitors', path='visitors')
class VisitorsApi(remote.Service):

    @endpoints.method(
        VisitorRequest,
        VisitorResponse,
        path='create',
        http_method='POST',
        name='visitors.create')
    def create_visitor(self, request):

        # Validate all required values have been provided
        if not request.visit_start and request.visit_end:
            raise endpoints.BadRequestException("Please provided values for 'visit_start' and 'visit_end'.")

        # Validate duration of visit is correct and less than 1 week
        if (request.visit_end - request.visit_start).total_seconds() < 0 or (request.visit_end - request.visit_start).total_seconds() > 604800:
            raise endpoints.BadRequestException("End date must be equal to or greater than start date. Duration may not exceed 14 days.")

        # Create new visitor
        visitor_key = Visitor(starred_species=[],
            itinerary=[],
            visit_start=request.visit_start,
            visit_end=request.visit_start).put()

        return VisitorResponse(id=visitor_key.id())

    @endpoints.method(
        UPDATE_VISITOR_REQUEST,
        message_types.VoidMessage,
        path='update',
        http_method='POST',
        name='visitors.update')
    def update_visitor(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.id) + "'.")

        # If values for species provided
        if request.starred_species:

            # For each species
            for species_id in request.starred_species:

                # Attempt to retrieve species
                species = Species.get_by_id(species_id)

                # If species does not exist, raise BadRequestException
                if not species:
                    raise endpoints.BadRequestException("No species found with ID '" +
                                                        str(species_id) + "'.")
            # Update values for species
            visitor.starred_species = request.starred_species

        # If values for itinerary provided
        if request.itinerary:

            itinerary = []

            # For each event
            for event_reference in request.itinerary:

                # Attempt to retrieve event
                event = Event.get_by_id(event_reference.event_id, parent=ndb.Key(Enclosure, event_reference.location_id))

                # If event not found against any amenities, search against amenities
                if not event:
                    event = Event.get_by_id(event_reference.event_id, parent=ndb.Key(Amenity, event_reference.location_id))

                # If event does not exist, raise BadRequestException
                if not event:
                    # If not found, raise BadRequestException
                    raise endpoints.BadRequestException("No event found with ID '" +
                                                        str(event_reference.event_id) +
                                                        "' and location ID '" +
                                                        str(event_reference.location_id) +
                                                        "'.")

                # Add event to return list
                itinerary.append(Event.EventLookup(event_id=event_reference.event_id, location_id=event_reference.location_id))

            # Update values for itinerary
            visitor.itinerary = itinerary

        # If value for visit_start provided
        if request.visit_start:

            # Update visit_start values
            visitor.visit_start = request.visit_start

        # If value for visit_end provided
        if request.visit_end:

            # Update visit_end values
            visitor.visit_end = request.visit_end

        # Write changes
        visitor.put()

        return message_types.VoidMessage()

    @endpoints.method(
        ID_LANGUAGE_REQUEST,
        EventListResponse,
        path='itinerary',
        http_method='GET',
        name='visitors.itinerary')
    def get_itinerary(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.id)

        response = EventListResponse()

        # For all events in the visitor's itinerary
        for event_reference in visitor.itinerary:

            # Attempt to retrieve the event
            event = Event.get_by_id(event_reference.event_id, parent=ndb.Key(Area, event_reference.location_id))

            # If event successffully retrieved
            if event:

                # If event is an Event
                if type(event) is Event:

                    # Retrieve an Event response
                    event_response = ApiHelper.get_event_response(event, request.language_code)

                    # Add event to return list
                    response.events.append(event_response)

                # Else if event is a Feeding
                elif type(event) is Feeding:

                    # Retrieve a Feeding response
                    feeding_response = ApiHelper.get_feeding_response(event, request.language_code)

                    # Add feeding to return list
                    response.feedings.append(feeding_response)

        return response
# [END Visitors API]
# [END bjorneparkappen_api]

# [START api_server]
api = endpoints.api_server([bjorneparkappen_api])
# [END api_server]
