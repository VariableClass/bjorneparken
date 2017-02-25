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
from models.version import Version
from models.visitor import Visitor
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]


# [START messages]
class InternationalMessage(messages.Message):
    text = messages.StringField(1, required=True)
    language_code = messages.StringField(2, required=True)

### Keepers ###
class KeeperRequest(messages.Message):
    name = messages.StringField(1, required=True)
    bio = messages.MessageField(InternationalMessage, 2, repeated=True)

class KeeperResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    name = messages.StringField(2, required=True)
    bio = messages.StringField(3, required=True)

class KeeperListResponse(messages.Message):
    keepers = messages.MessageField(KeeperResponse, 1, repeated=True)

### Species ###
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

### Animals ###
class AnimalReference(messages.Message):
    animal_id = messages.IntegerField(1, required=True)
    species_id = messages.IntegerField(2, required=True)

class AnimalRequest(messages.Message):
    name = messages.StringField(1, required=True)
    species_id = messages.IntegerField(2, required=True)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    is_available = messages.BooleanField(4, required=True)

class UpdateAnimalRequest(messages.Message):
    name = messages.StringField(1)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    is_available = messages.BooleanField(3)

class AnimalResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    name = messages.StringField(2, required=True)
    species = messages.MessageField(SpeciesResponse, 3, required=True)
    description = messages.StringField(4, required=True)
    is_available = messages.BooleanField(5, required=True)

class AnimalListResponse(messages.Message):
    animals = messages.MessageField(AnimalResponse, 1, repeated=True)

### Areas (inc. Enclosures & Amenities) ###
# All Enclosure and Amenity messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance
class EnclosureRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2)
    coordinates = messages.StringField(3, repeated=True)
    animals = messages.MessageField(AnimalReference, 4, repeated=True)

class AmenityRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2, required=True)
    coordinates = messages.StringField(3, repeated=True)
    description = messages.MessageField(InternationalMessage, 4, repeated=True)
    amenity_type = messages.StringField(5)

class EnclosureResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    label = messages.StringField(2, required=True)
    visitor_destination = messages.StringField(3, required=True)
    coordinates = messages.StringField(4, repeated=True)
    animals = messages.MessageField(AnimalResponse, 5, repeated=True)

class AmenityResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    label = messages.StringField(2, required=True)
    visitor_destination = messages.StringField(3, required=True)
    coordinates = messages.StringField(4, repeated=True)
    description = messages.StringField(5)
    amenity_type = messages.StringField(6)

class AreaListResponse(messages.Message):
    enclosures = messages.MessageField(EnclosureResponse, 1, repeated=True)
    amenities = messages.MessageField(AmenityResponse, 2, repeated=True)

### Events (inc. Feedings) ###
# All Event and Feeding messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance
class EventReference(messages.Message):
    event_id = messages.IntegerField(1, required=True)
    location_id = messages.IntegerField(2, required=True)

class EventRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    location_id = messages.IntegerField(3, required=True)
    start_time = messages.StringField(4, required=True)
    end_time = messages.StringField(5, required=True)
    is_active = messages.BooleanField(6, required=True)

class UpdateEventRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)

class FeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    location_id = messages.IntegerField(3, required=True)
    start_time = messages.StringField(4, required=True)
    end_time = messages.StringField(5, required=True)
    is_active = messages.BooleanField(6, required=True)
    keeper_id = messages.IntegerField(7)

class UpdateFeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)

class EventResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    label = messages.StringField(2, required=True)
    description = messages.StringField(3, required=True)
    location = messages.MessageField(AmenityResponse, 4, required=True)
    start_time = messages.StringField(5, required=True)
    end_time = messages.StringField(6, required=True)
    is_active = messages.BooleanField(7, required=True)

class FeedingResponse(messages.Message):
    id = messages.IntegerField(1, required=True)
    label = messages.StringField(2, required=True)
    description = messages.StringField(3, required=True)
    location = messages.MessageField(EnclosureResponse, 4, required=True)
    start_time = messages.StringField(5, required=True)
    end_time = messages.StringField(6, required=True)
    is_active = messages.BooleanField(7, required=True)
    keeper = messages.MessageField(KeeperResponse, 8)

class EventListResponse(messages.Message):
    events = messages.MessageField(EventResponse, 1, repeated=True)
    feedings = messages.MessageField(FeedingResponse, 2, repeated=True)

### Visitors ###
class VisitorRequest(messages.Message):
    visit_start = message_types.DateTimeField(1, required=True)
    visit_end = message_types.DateTimeField(2, required=True)
    starred_species = messages.IntegerField(3, repeated=True)
    itinerary = messages.MessageField(EventReference, 4, repeated=True)

class VisitorResponse(messages.Message):
    id = messages.IntegerField(1, required=True)

# [END messages]

# [START resources]
LANGUAGE_RESOURCE = endpoints.ResourceContainer(
    message_types.VoidMessage,
    language_code=messages.StringField(1, required=True))

SPECIES_RESOURCE = endpoints.ResourceContainer(
    species_id=messages.IntegerField(1, required=True))
UPDATE_SPECIES_RESOURCE = endpoints.ResourceContainer(
    SpeciesRequest,
    species_id=messages.IntegerField(1, required=True))

ANIMAL_RESOURCE = endpoints.ResourceContainer(
    animal_id=messages.IntegerField(1, required=True),
    species_id=messages.IntegerField(2, required=True))
UPDATE_ANIMAL_RESOURCE = endpoints.ResourceContainer(
    UpdateAnimalRequest,
    animal_id=messages.IntegerField(1, required=True),
    species_id=messages.IntegerField(2, required=True))

AREA_RESOURCE = endpoints.ResourceContainer(
    area_id=messages.IntegerField(1, required=True))
UPDATE_ENCLOSURE_RESOURCE = endpoints.ResourceContainer(
    EnclosureRequest,
    enclosure_id=messages.IntegerField(1, required=True))
UPDATE_AMENITY_RESOURCE = endpoints.ResourceContainer(
    AmenityRequest,
    amenity_id=messages.IntegerField(1, required=True))

EVENT_RESOURCE = endpoints.ResourceContainer(
    event_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))
UPDATE_EVENT_RESOURCE = endpoints.ResourceContainer(
    UpdateEventRequest,
    event_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))

FEEDING_RESOURCE = endpoints.ResourceContainer(
    feeding_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))
UPDATE_FEEDING_RESOURCE = endpoints.ResourceContainer(
    UpdateFeedingRequest,
    feeding_id=messages.IntegerField(1, required=True),
    location_id=messages.IntegerField(2, required=True))
FEEDING_SPECIES_RESOURCE = endpoints.ResourceContainer(
    message_types.VoidMessage,
    language_code=messages.StringField(1, required=True),
    species_id=messages.IntegerField(2, required=True))

KEEPER_RESOURCE = endpoints.ResourceContainer(
    keeper_id=messages.IntegerField(1, required=True))
UPDATE_KEEPER_RESOURCE = endpoints.ResourceContainer(
    KeeperRequest,
    keeper_id=messages.IntegerField(1, required=True))

UPDATE_VISITOR_RESOURCE = endpoints.ResourceContainer(
    VisitorRequest,
    visitor_id=messages.IntegerField(1, required=True))
# [END resources]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

    # Executed whenever a write operation is performed on park data, which
    # includes creation, updating or deletion of any of the following;
    # Amenity, Animal, Enclosure, Event, Feeding, Keeper or Species
    def update_version(self):

        # Retrieve version
        version = Version.get()

        # If no version exists, create one
        if not version:
            version = Version()

        # Set version to current datetime
        version.version = datetime.datetime.now()

        # Write changes
        version.put()


    ### Species ###

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
        path='species',
        http_method='POST',
        name='species.create')
    def create_species(self, request):

        # Attempt to retrieve species
        pre_existing = Species.get_by_latin_name(request.latin)

        # If species currently exists, raise BadRequestException
        if pre_existing:
            raise endpoints.BadRequestException("A species with latin name '" + request.latin + "' already exists with ID '" + str(pre_existing.key.id()) + "'")

        # Convert InternationalMessage formats to InternationalText
        common_name = self.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new species
        Species(common_name=common_name,
            latin=request.latin,
            description=description).put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_SPECIES_RESOURCE,
        message_types.VoidMessage,
        path='species/{species_id}',
        http_method='POST',
        name='species.update')
    def update_species(self, request):

        # Attempt to retrieve species
        species = Species.get_by_id(request.species_id)

        # If species does not exist, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" +
                                                str(request.species_id) + "'.")

        # If values for common_name provided
        if request.common_name:
            # Convert InternationalMessage formats to InternationalText
            common_name = self.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)

            # Update common name values
            species.common_name=common_name

        # If values for description provided
        if request.description:
            # Convert InternationalMessage formats to InternationalText
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update description values
            species.description=description

        # If value for latin provided
        if request.latin:

            # Update latin value
            species.latin=request.latin

        # Write changes
        species.put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        SPECIES_RESOURCE,
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

        # Update version
        self.update_version()

        return message_types.VoidMessage()


    ### Animals ###

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

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_ANIMAL_RESOURCE,
        message_types.VoidMessage,
        path='animals/{animal_id}',
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
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

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
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        ANIMAL_RESOURCE,
        message_types.VoidMessage,
        path='animals',
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

        # Delete animal
        animal.key.delete()

        # Update version
        self.update_version()

        return message_types.VoidMessage()


    ### Areas (inc. Enclosures & Amenities) ###

    @endpoints.method(
        LANGUAGE_RESOURCE,
        AreaListResponse,
        path='areas',
        http_method='GET',
        name='areas.list')
    def list_areas(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Enclosure
            if type(area) is Enclosure:

                # Retrieve an Enclosure response
                area_response = self.get_enclosure_response(area, request.language_code)

                # Add area to return list
                response.enclosures.append(area_response)

            # Else if area is an Amenity
            elif type(area) is Amenity:

                # Retrieve an Amenity response
                area_response = self.get_amenity_response(area, request.language_code)

                # Add area to return list
                response.amenities.append(area_response)

            else:
                raise endpoints.UnexpectedException("Area found which is neither Enclosure nor Amenity, consult developer.")

        return response

    @endpoints.method(
        LANGUAGE_RESOURCE,
        AreaListResponse,
        path='areas/enclosures',
        http_method='GET',
        name='enclosures.list')
    def list_enclosures(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Enclosure
            if type(area) is Enclosure:

                # Retrieve an enclosure response
                area_response = self.get_enclosure_response(area, request.language_code)

                # Add area to return list
                response.enclosures.append(area_response)

        return response

    @endpoints.method(
        LANGUAGE_RESOURCE,
        AreaListResponse,
        path='areas/amenities',
        http_method='GET',
        name='amenities.list')
    def list_amenities(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Amenity
            if type(area) is Amenity:

                # Retrieve an amenity response
                area_response = self.get_amenity_response(area, request.language_code)

                # Add area to return list
                response.amenities.append(area_response)

        return response

    @endpoints.method(
        EnclosureRequest,
        message_types.VoidMessage,
        path='areas/enclosures',
        http_method='POST',
        name='enclosures.create')
    def create_enclosure(self, request):

        # TODO Check for intersection of any existing areas

        # Convert InternationalMessage formats to InternationalText
        label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

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
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        AmenityRequest,
        message_types.VoidMessage,
        path='areas/amenities',
        http_method='POST',
        name='amenities.create')
    def create_amenity(self, request):

        # TODO Check for intersection of any existing areas

        # Convert InternationalMessage formats to InternationalText
        label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

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
            raise endpoints.BadRequestException("No AmenityType found by the name'" + request.amenity_type + "'.")

        # Set amenity type
        amenity_type = Amenity.AmenityType[request.amenity_type]

        # Create new amenity
        Amenity(label=label,
                visitor_destination=visitor_destination,
                coordinates=coordinates,
                description=description,
                amenity_type=amenity_type.name).put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_ENCLOSURE_RESOURCE,
        message_types.VoidMessage,
        path='areas/enclosures/{enclosure_id}',
        http_method='POST',
        name='enclosures.update')
    def update_enclosure(self, request):

        # Attempt to retrieve enclosure
        enclosure = Enclosure.get_by_id(request.enclosure_id)

        # If enclosure does not exist, raise BadRequestException
        if not enclosure:
            raise endpoints.BadRequestException("No enclosure found with ID '" +
                                                str(request.enclosure_id) + "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

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
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_AMENITY_RESOURCE,
        message_types.VoidMessage,
        path='areas/amenities/{amenity_id}',
        http_method='POST',
        name='amenities.update')
    def update_amenity(self, request):

        # Attempt to retrieve amenity
        amenity = Amenity.get_by_id(request.amenity_id)

        # If amenity does not exist, raise BadRequestException
        if not amenity:
            raise endpoints.BadRequestException("No amenity found with ID '" +
                                                str(request.amenity_id) + "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

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
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

            # Update common name values
            amenity.description=description

        # If values for amenity type provided
        if request.amenity_type:

            if not Amenity.AmenityType.validate(request.amenity_type):
                raise endpoints.BadRequestException("No AmenityType found by the name'" + request.amenity_type + "'.")

        # Write changes
        amenity.put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        AREA_RESOURCE,
        message_types.VoidMessage,
        path='areas/{area_id}',
        http_method='DELETE',
        name='areas.delete')
    def delete_area(self, request):

        # Retrieve area from ID
        area = Area.get_by_id(request.area_id)

        # If not found, raise BadRequestException
        if not area:
            raise endpoints.BadRequestException("No area found with ID '" + str(request.area_id) + "'.")

        # Retrieve all events at the area
        events = area.get_events()

        # Delete each
        for event in events:
            event.key.delete()

        # Delete area
        area.key.delete()

        # Update version
        self.update_version()

        return message_types.VoidMessage()


    ### Events (inc. Feedings) ###

    @endpoints.method(
        LANGUAGE_RESOURCE,
        EventListResponse,
        path='events/all',
        http_method='GET',
        name='events.list_all')
    def list_all_events(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Event
            if type(event) is Event:

                # Retrieve an Event response
                event_response = self.get_event_response(event, request.language_code)

                # Add event to return list
                response.events.append(event_response)

            # Else if event is a Feeding
            elif type(event) is Feeding:

                # Retrieve a Feeding response
                feeding_response = self.get_feeding_response(event, request.language_code)

                # Add feeding to return list
                response.feedings.append(feeding_response)

        return response

    @endpoints.method(
        LANGUAGE_RESOURCE,
        EventListResponse,
        path='events',
        http_method='GET',
        name='events.list')
    def list_events(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Event
            if type(event) is Event:

                # Retrieve an Event response
                event_response = self.get_event_response(event, request.language_code)

                # Add event to return list
                response.events.append(event_response)

        return response

    @endpoints.method(
        LANGUAGE_RESOURCE,
        EventListResponse,
        path='events/feedings',
        http_method='GET',
        name='feedings.list')
    def list_feedings(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Event.get_all()

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Feeding
            if type(event) is Feeding:

                # Retrieve an Feeding response
                feeding_response = self.get_feeding_response(event, request.language_code)

                # Add event to return list
                response.feedings.append(feeding_response)

        return response

    @endpoints.method(
        FEEDING_SPECIES_RESOURCE,
        EventListResponse,
        path='events/feedings/{species_id}',
        http_method='GET',
        name='feedings.species.list')
    def list_feedings_for_species(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

        # Retrieve all events
        events = Feeding.get_all_for_species(request.species_id)

        response = EventListResponse()

        # Build up response of all events
        for event in events:

            # Retrieve an Feeding response
            feeding_response = self.get_feeding_response(event, request.language_code)

            # Add event to return list
            response.feedings.append(feeding_response)

        return response

    # def list_events_for_location(self, request):

    @endpoints.method(
        EventRequest,
        message_types.VoidMessage,
        path='events',
        http_method='POST',
        name='events.create')
    def create_event(self, request):

        # TODO Check for clashes at a location

        # Retrieve location from provided ID
        location = ndb.Key(Amenity, request.location_id).get()

        # If location not found, raise exception
        if not location:
            raise endpoints.BadRequestException("Location of ID '" + str(request.location_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Validate times
        if not Event.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH:MM' and end time must not exceed start time.")

        # Create new event
        Event(parent=location.key,
            label=label,
            description=description,
            start_time=request.start_time,
            end_time=request.end_time,
            is_active=request.is_active).put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_EVENT_RESOURCE,
        message_types.VoidMessage,
        path='events/{event_id}',
        http_method='POST',
        name='events.update')
    def update_event(self, request):

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

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            event.label=label

        # If values for description provided
        if request.description:

            # Convert InternationalMessage formats to InternationalText
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

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
        if not Event.validate_times(temp_start_time, temp_end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH:MM' and end time must not exceed start time.")

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
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        FeedingRequest,
        message_types.VoidMessage,
        path='events/feedings',
        http_method='POST',
        name='feedings.create')
    def create_feeding(self, request):

        # TODO Check for clashes at a location

        # Retrieve location from provided ID
        location = ndb.Key(Enclosure, request.location_id).get()

        # If location not found, raise exception
        if not location:
            raise endpoints.BadRequestException("Location of ID '" + str(request.location_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Validate times
        if not Event.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH:MM' and end time must not exceed start time.")

        # Retrieve keeper
        keeper = ndb.Key(Keeper, request.keeper_id).get()

        # If keeper not found, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper by ID '" + str(request.keeper_id) + "' found.")

        # Create new feeding
        Feeding(parent=location.key,
            label=label,
            description=description,
            start_time=request.start_time,
            end_time=request.end_time,
            is_active=request.is_active,
            keeper_id=request.keeper_id).put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_FEEDING_RESOURCE,
        message_types.VoidMessage,
        path='events/feedings/{feeding_id}',
        http_method='POST',
        name='feedings.update')
    def update_feeding(self, request):

        # Attempt to retrieve feeding
        feeding = Feeding.get_by_id(request.feeding_id, parent=ndb.Key(Enclosure, request.location_id))

        # If feeding does not exist, raise BadRequestException
        if not feeding:
            raise endpoints.BadRequestException("No feeding found with ID '" +
                                                str(request.feeding_id) + "'.")

        # If values for label provided
        if request.label:

            # Convert InternationalMessage formats to InternationalText
            label = self.convert_i18n_messages_to_i18n_texts(international_messages=request.label)

            # Update label values
            feeding.label=label

        # If values for description provided
        if request.description:

            # Convert InternationalMessage formats to InternationalText
            description = self.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

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
        if not Event.validate_times(temp_start_time, temp_end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH:MM' and end time must not exceed start time.")

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
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        EVENT_RESOURCE,
        message_types.VoidMessage,
        path='events',
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

        # Delete area
        event.key.delete()

        # Update version
        self.update_version()

        return message_types.VoidMessage()


    ### Keeper ###

    @endpoints.method(
        LANGUAGE_RESOURCE,
        KeeperListResponse,
        path='keepers',
        http_method='GET',
        name='keepers.list')
    def list_keepers(self, request):

        # Validate language code
        self.check_language(language_code=request.language_code)

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
        path='keepers',
        http_method='POST',
        name='keepers.create')
    def create_keeper(self, request):

        # Convert InternationalMessage formats to InternationalText
        bio = self.convert_i18n_messages_to_i18n_texts(international_messages=request.bio)

        # Create new keeper
        Keeper(name=request.name,
            bio=bio).put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_KEEPER_RESOURCE,
        message_types.VoidMessage,
        path='keepers/{keeper_id}',
        http_method='POST',
        name='keepers.update')
    def update_keeper(self, request):

        # Attempt to retrieve keeper
        keeper = Keeper.get_by_id(request.keeper_id)

        # If keeper does not exist, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper found with ID '" +
                                                str(request.keeper_id) + "'.")

        # If value for name provided
        if request.name:
            # Update name value
            keeper.name=request.name

        # If values for bio provided
        if request.bio:
            # Convert InternationalMessage formats to InternationalText
            bio = self.convert_i18n_messages_to_i18n_texts(international_messages=request.bio)

            # Update bio values
            keeper.bio=bio

        # Write changes
        keeper.put()

        # Update version
        self.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        KEEPER_RESOURCE,
        message_types.VoidMessage,
        path='keepers/{keeper_id}',
        http_method='DELETE',
        name='keepers.delete')
    def delete_keeper(self, request):

        # Retrieve keeper from ID
        keeper = Keeper.get_by_id(request.keeper_id)

        # If not found, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper found with ID '" + str(request.keeper_id) + "'.")

        # Delete species
        keeper.key.delete()

        # Update version
        self.update_version()

        return message_types.VoidMessage()


    ### Visitors ###

    @endpoints.method(
        VisitorRequest,
        VisitorResponse,
        path='visitors',
        http_method='POST',
        name='visitors.create')
    def create_visitor(self, request):

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
        UPDATE_VISITOR_RESOURCE,
        message_types.VoidMessage,
        path='visitors/{visitor_id}',
        http_method='POST',
        name='visitors.update')
    def update_visitor(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.visitor_id) + "'.")

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


    ### Static Methods ###

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

# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
