# coding=utf-8
# [START imports]
import datetime
import endpoints
import token_handler
from google.appengine.ext import ndb
from models.amenity import Amenity
from models.animal import Animal
from models.area import Area
from models.enclosure import Enclosure
from models.event import Event
from models.feeding import Feeding
from models.i18n import InternationalText
from models.image import Image
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

class VersionResponse(messages.Message):
    version = message_types.DateTimeField(1)
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

class KeeperTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    name = messages.StringField(2)
    bio = messages.MessageField(InternationalMessage, 3, repeated=True)

class KeeperTranslationListResponse(messages.Message):
    keepers = messages.MessageField(KeeperTranslationResponse, 1, repeated=True)
# [END keeper messages]

# [START species messages]
class SpeciesRequest(messages.Message):
    common_name = messages.MessageField(InternationalMessage, 1, repeated=True)
    latin = messages.StringField(2)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    image = messages.StringField(4)

class SpeciesResponse(messages.Message):
    id = messages.IntegerField(1)
    common_name = messages.StringField(2)
    latin = messages.StringField(3)
    description = messages.StringField(4)
    image = messages.StringField(5)

class SpeciesListResponse(messages.Message):
    species = messages.MessageField(SpeciesResponse, 1, repeated=True)

class SpeciesTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    common_name = messages.MessageField(InternationalMessage, 2, repeated=True)
    latin = messages.StringField(3)
    description = messages.MessageField(InternationalMessage, 4, repeated=True)
    image = messages.StringField(5)

class SpeciesTranslationListResponse(messages.Message):
    species = messages.MessageField(SpeciesTranslationResponse, 1, repeated=True)
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

class AnimalTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    name = messages.StringField(2)
    species = messages.MessageField(SpeciesTranslationResponse, 3)
    enclosure_id = messages.IntegerField(4)
    description = messages.MessageField(InternationalMessage, 5, repeated=True)
    is_available = messages.BooleanField(6)

class AnimalTranslationListResponse(messages.Message):
    animals = messages.MessageField(AnimalTranslationResponse, 1, repeated=True)
# [END animal messages]

# [START area messages]
# All Enclosure and Amenity messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance

class EnclosureRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2)
    coordinates = messages.StringField(3, repeated=True)
    image = messages.StringField(4)

class AmenityRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2)
    coordinates = messages.StringField(3, repeated=True)
    description = messages.MessageField(InternationalMessage, 4, repeated=True)
    amenity_type = messages.StringField(5)
    image = messages.StringField(6)

class EnclosureResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    animals = messages.MessageField(AnimalResponse, 5, repeated=True)
    image = messages.StringField(6)

class AmenityResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    description = messages.StringField(5)
    amenity_type = messages.StringField(6)
    image = messages.StringField(7)

class AreaListResponse(messages.Message):
    enclosures = messages.MessageField(EnclosureResponse, 1, repeated=True)
    amenities = messages.MessageField(AmenityResponse, 2, repeated=True)

class EnclosureTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.MessageField(InternationalMessage, 2, repeated=True)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    animals = messages.MessageField(AnimalTranslationResponse, 5, repeated=True)
    image = messages.StringField(6)

class AmenityTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.MessageField(InternationalMessage, 2, repeated=True)
    visitor_destination = messages.StringField(3)
    coordinates = messages.StringField(4, repeated=True)
    description = messages.MessageField(InternationalMessage, 5, repeated=True)
    amenity_type = messages.StringField(6)
    image = messages.StringField(7)

class AreaTranslationListResponse(messages.Message):
    enclosures = messages.MessageField(EnclosureTranslationResponse, 1, repeated=True)
    amenities = messages.MessageField(AmenityTranslationResponse, 2, repeated=True)
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
    image = messages.StringField(7)

class UpdateEventRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)
    image = messages.StringField(6)

class FeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    location_id = messages.IntegerField(3)
    start_time = messages.StringField(4)
    end_time = messages.StringField(5)
    is_active = messages.BooleanField(6)
    keeper_id = messages.IntegerField(7)
    image = messages.StringField(8)

class UpdateFeedingRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    description = messages.MessageField(InternationalMessage, 2, repeated=True)
    start_time = messages.StringField(3)
    end_time = messages.StringField(4)
    is_active = messages.BooleanField(5)
    keeper_id = messages.IntegerField(6)
    image = messages.StringField(7)

class EventResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    description = messages.StringField(3)
    location = messages.MessageField(AmenityResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)
    image = messages.StringField(8)

class FeedingResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.StringField(2)
    description = messages.StringField(3)
    location = messages.MessageField(EnclosureResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)
    keeper = messages.MessageField(KeeperResponse, 8)
    image = messages.StringField(9)

class EventListResponse(messages.Message):
    events = messages.MessageField(EventResponse, 1, repeated=True)
    feedings = messages.MessageField(FeedingResponse, 2, repeated=True)

class EventTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.MessageField(InternationalMessage, 2, repeated=True)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    location = messages.MessageField(AmenityTranslationResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)
    image = messages.StringField(8)

class FeedingTranslationResponse(messages.Message):
    id = messages.IntegerField(1)
    label = messages.MessageField(InternationalMessage, 2, repeated=True)
    description = messages.MessageField(InternationalMessage, 3, repeated=True)
    location = messages.MessageField(EnclosureTranslationResponse, 4)
    start_time = messages.StringField(5)
    end_time = messages.StringField(6)
    is_active = messages.BooleanField(7)
    keeper = messages.MessageField(KeeperTranslationResponse, 8)
    image = messages.StringField(9)

class EventTranslationListResponse(messages.Message):
    events = messages.MessageField(EventTranslationResponse, 1, repeated=True)
    feedings = messages.MessageField(FeedingTranslationResponse, 2, repeated=True)
# [END event messages]

# [START visitor messages]
class VisitorRequest(messages.Message):
    visit_start = message_types.DateTimeField(1)
    visit_end = message_types.DateTimeField(2)

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
AMENITY_LANGUAGE_REQUEST = endpoints.ResourceContainer(
    language_code = messages.StringField(1, required=True),
    amenity_type = messages.StringField(2, required=True))
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

# [START create request resources]
CREATE_ANIMAL_REQUEST = endpoints.ResourceContainer(
    AnimalRequest,
    enclosure_id = messages.IntegerField(1, required = True))
# [END create request resources]

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
ADD_REMOVE_ANIMAL_REQUEST = endpoints.ResourceContainer(
    AnimalReference,
    enclosure_id = messages.IntegerField(1, required=True))
ADD_REMOVE_SPECIES_REQUEST = endpoints.ResourceContainer(
    visitor_id = messages.IntegerField(1, required=True),
    species_id = messages.IntegerField(2, required=True),
    language_code = messages.StringField(3, required=True))
ADD_REMOVE_EVENT_REQUEST = endpoints.ResourceContainer(
    visitor_id = messages.IntegerField(1, required=True),
    event_id = messages.IntegerField(2, required=True),
    location_id = messages.IntegerField(3, required=True),
    language_code = messages.StringField(4, required=True))
SYNC_ITINERARY_REQUEST = endpoints.ResourceContainer(
    itinerary = messages.MessageField(EventListResponse, 1, required=True),
    visitor_id = messages.IntegerField(2, required=True),
    language_code = messages.StringField(3, required=True))
SYNC_STARRED_SPECIES_REQUEST = endpoints.ResourceContainer(
    starred_species = messages.MessageField(SpeciesListResponse, 1, required=True),
    visitor_id = messages.IntegerField(2, required=True),
    language_code = messages.StringField(3, required=True))
# [END update request resources]
# [END request resources]

# [START bjorneparkappen_api]
firebase_issuer = endpoints.Issuer(issuer='https://securetoken.google.com/bjorneparkappen',
                            jwks_uri='https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system.gserviceaccount.com')

bjorneparkappen_api = endpoints.api(name='bjorneparkappen_api', version="v1.0", api_key_required=True, issuers={"firebase":firebase_issuer})

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
        return version.put()

    @staticmethod
    def check_language(language_code):
        # If code is not supported, raise BadRequestException
        if not InternationalText.validate_language_code(language_code):
            raise endpoints.BadRequestException("The provided language_code; "
                    + language_code + ", is not currently supported.")

    @staticmethod
    def validate_user(service):

        try:
            # Retrieve user token
            id_token = service.request_state.headers.get_all('Authorization')[0].split(' ').pop()
            token_handler.validate_token(id_token)

        except(IndexError):
            # Return 401 Unauthorized
            raise endpoints.UnauthorizedException('Please provide user credentials')

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
    def get_species_response(species, language_code):
        # Translate translatable species resources
        species_common_name_translation = InternationalText.get_translation(
                language_code,
                species.common_name)
        species_description_translation = InternationalText.get_translation(
                language_code,
                species.description)

        image = None

        # If species has an image
        if species.image:
            image = species.image.get()

        return SpeciesResponse(
            id=species.key.id(),
            common_name=species_common_name_translation,
            latin=species.latin,
            description=species_description_translation,
            image=image)

    @staticmethod
    def get_species_response_with_translations(species):

        # Translate translatable species resources
        species_common_name_translations = []
        for common_name_translation in species.common_name:
            translation = InternationalMessage(text=common_name_translation.text, language_code=common_name_translation.language_code)
            species_common_name_translations.append(translation)

        species_description_translations = []
        for description_translation in species.description:
            translation = InternationalMessage(text=description_translation.text, language_code=description_translation.language_code)
            species_description_translations.append(translation)

        image = None

        # If species has an image
        if species.image:
            image = species.image.get()

        return SpeciesTranslationResponse(
            id=species.key.id(),
            common_name=species_common_name_translations,
            latin=species.latin,
            description=species_description_translations,
            image=image)

    @staticmethod
    def get_animal_response(animal, language_code):

        # Retrieve species
        species = animal.key.parent().get()

        # Create species response to include in animal response
        species_response = ApiHelper.get_species_response(species, language_code)

        # Translate translatable animal resources
        animal_description_translation = InternationalText.get_translation(
                language_code,
                animal.description)

        return AnimalResponse(
            id=animal.key.id(),
            name=animal.name,
            species=species_response,
            description=animal_description_translation,
            is_available=animal.is_available)

    @staticmethod
    def get_animal_response_with_translations(animal):
        # Retrieve species
        species = animal.key.parent().get()
        species_response = ApiHelper.get_species_response_with_translations(species)

        # Retrieve enclosure
        enclosure = Enclosure.get_for_animal(animal.key.id(), species.key.id())

        # Translate translatable animal resources
        animal_description_translations = []
        for description_translation in animal.description:
            translation = InternationalMessage(text=description_translation.text, language_code=description_translation.language_code)
            animal_description_translations.append(translation)

        return AnimalTranslationResponse(
            id=animal.key.id(),
            name=animal.name,
            species=species_response,
            enclosure_id=enclosure.key.id(),
            description=animal_description_translations,
            is_available=animal.is_available)

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
            animal_response = ApiHelper.get_animal_response(animal, language_code)

            # Add animal to return list
            animals.append(animal_response)

        image = None

        # If area has an image
        if area.image:
            image = area.image.get()

        return EnclosureResponse(
            id=area.key.id(),
            label=area_label_translation,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            animals=animals,
            image=image)

    @staticmethod
    def get_enclosure_response_with_translations(area):

        # Translate translatable area resources
        area_label_translations = []
        for label_translation in area.label:
            translation = InternationalMessage(text=label_translation.text, language_code=label_translation.language_code)
            area_label_translations.append(translation)

        coordinates = []

        # Convert co-ordinate pairs to strings
        for coordinate in area.coordinates:
            coordinates.append(str(coordinate.lon) + ", " + str(coordinate.lat))

        animals = []

        # For each animal/species key set
        for animal_reference in area.animals:

            # Retrieve animal
            animal = Animal.get_by_id(animal_reference.animal_id, parent=ndb.Key(Species, animal_reference.species_id))
            animal_response = ApiHelper.get_animal_response_with_translations(animal)
            animals.append(animal_response)

        image = None

        # If area has an image
        if area.image:
            image = area.image.get()

        return EnclosureTranslationResponse(
            id=area.key.id(),
            label=area_label_translations,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            animals=animals,
            image=image)

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

        image = None

        # If area has an image
        if area.image:
            image = area.image.get()

        # Convert co-ordinate pairs to strings
        for coordinate in area.coordinates:
            coordinates.append(str(coordinate.lon) + ", " + str(coordinate.lat))

        return AmenityResponse(
            id=area.key.id(),
            label=area_label_translation,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            description=amenity_description_translation,
            amenity_type=area.amenity_type,
            image=image)

    @staticmethod
    def get_amenity_response_with_translations(area):

        # Translate translatable area resources
        area_label_translations = []
        for label_translation in area.label:
            translation = InternationalMessage(text=label_translation.text, language_code=label_translation.language_code)
            area_label_translations.append(translation)

        # Translate translatable area resources
        area_description_translations = []
        for description_translation in area.description:
            translation = InternationalMessage(text=description_translation.text, language_code=description_translation.language_code)
            area_description_translations.append(translation)

        coordinates = []

        # Convert co-ordinate pairs to strings
        for coordinate in area.coordinates:
            coordinates.append(str(coordinate.lon) + ", " + str(coordinate.lat))

        image = None

        # If area has an image
        if area.image:
            image = area.image.get()

        return AmenityTranslationResponse(
            id=area.key.id(),
            label=area_label_translations,
            visitor_destination=str(area.visitor_destination.lon) + ", " + str(area.visitor_destination.lat),
            coordinates=coordinates,
            description=area_description_translations,
            amenity_type=area.amenity_type,
            image=image)

    @staticmethod
    def get_keeper_response(keeper, language_code):
        # Translate translatable keeper resources
        keeper_bio_translation = InternationalText.get_translation(
                language_code,
                keeper.bio)

        return KeeperResponse(
            id=keeper.key.id(),
            name=keeper.name,
            bio=keeper_bio_translation)

    @staticmethod
    def get_keeper_response_with_translations(keeper):
        # Translate translatable keeper resources
        keeper_bio_translations = []
        for bio_translation in keeper.bio:
            translation = InternationalMessage(text=bio_translation.text, language_code=bio_translation.language_code)
            keeper_bio_translations.append(translation)

        return KeeperTranslationResponse(
            id=keeper.key.id(),
            name=keeper.name,
            bio=keeper_bio_translations)

    @staticmethod
    def get_event_response(event, language_code):

        # Translate translatable event resources
        event_label_translation = InternationalText.get_translation(
                language_code,
                event.label)

        event_description_translation = InternationalText.get_translation(
                language_code,
                event.description)

        # Retrieve location
        location = ApiHelper.get_amenity_response(event.key.parent().get(), language_code)

        image = None

        # If event has an image
        if event.image:
            image = event.image.get()

        return EventResponse(
            id=event.key.id(),
            label=event_label_translation,
            description=event_description_translation,
            location=location,
            start_time=event.start_time,
            end_time=event.end_time,
            is_active=event.is_active,
            image=image)

    @staticmethod
    def get_event_response_with_translations(event):

        # Translate translatable event resources
        event_label_translations = []
        for label_translation in event.label:
            translation = InternationalMessage(text=label_translation.text, language_code=label_translation.language_code)
            event_label_translations.append(translation)

        event_description_translations = []
        for description_translation in event.description:
            translation = InternationalMessage(text=description_translation.text, language_code=description_translation.language_code)
            event_description_translations.append(translation)

        # Retrieve location
        location = ApiHelper.get_amenity_response_with_translations(event.key.parent().get())

        image = None

        # If event has an image
        if event.image:
            image = event.image.get()

        return EventTranslationResponse(
            id=event.key.id(),
            label=event_label_translations,
            description=event_description_translations,
            location=location,
            start_time=event.start_time,
            end_time=event.end_time,
            is_active=event.is_active,
            image=image)

    @staticmethod
    def get_feeding_response(feeding, language_code):

        # Translate translatable event resources
        feeding_label_translation = InternationalText.get_translation(
                language_code,
                feeding.label)

        feeding_description_translation = InternationalText.get_translation(
                language_code,
                feeding.description)

        # Retrieve location
        location = feeding.key.parent().get()

        # If enclosure inactive update feeding active state
        if not location.is_active():
            feeding.is_active = False
            feeding.put()

        # Get formatted location response
        location_response = ApiHelper.get_enclosure_response(feeding.key.parent().get(), language_code)

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

        image = None

        # If feeding has an image
        if feeding.image:
            image = feeding.image.get()

        return FeedingResponse(
            id=feeding.key.id(),
            label=feeding_label_translation,
            description=feeding_description_translation,
            location=location_response,
            start_time=feeding.start_time,
            end_time=feeding.end_time,
            is_active=feeding.is_active,
            keeper=keeper_response,
            image=image)

    @staticmethod
    def get_feeding_response_with_translations(feeding):

        # Translate translatable event resources
        feeding_label_translations = []
        for label_translation in feeding.label:
            translation = InternationalMessage(text=label_translation.text, language_code=label_translation.language_code)
            feeding_label_translations.append(translation)

        feeding_description_translations = []
        for description_translation in feeding.description:
            translation = InternationalMessage(text=description_translation.text, language_code=description_translation.language_code)
            feeding_description_translations.append(translation)

        # Retrieve location
        location = feeding.key.parent().get()

        # If enclosure inactive update feeding active state
        if not location.is_active():
            feeding.is_active = False
            feeding.put()

        # Get formatted location response
        location_response = ApiHelper.get_enclosure_response_with_translations(feeding.key.parent().get())

        # Retrieve keeper
        keeper = ndb.Key(Keeper, feeding.keeper_id).get()

        keeper_response = ApiHelper.get_keeper_response_with_translations(keeper)

        image = None

        # If event has an image
        if feeding.image:
            image = feeding.image.get()

        return FeedingTranslationResponse(
            id=feeding.key.id(),
            label=feeding_label_translations,
            description=feeding_description_translations,
            location=location_response,
            start_time=feeding.start_time,
            end_time=feeding.end_time,
            is_active=feeding.is_active,
            keeper=keeper_response,
            image=image)
# [END ApiHelper]

# [START Species API]
@bjorneparkappen_api.api_class(path='species')
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

            species_response = ApiHelper.get_species_response(species, request.language_code)

            # Add species to return list
            response.species.append(species_response)

        return response

    @endpoints.method(
        SpeciesRequest,
        SpeciesTranslationListResponse,
        path='create',
        http_method='POST',
        name='species.create')
    def create_species(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Validate all required values have been provided
        if not (request.common_name and request.latin and request.description):
            raise endpoints.BadRequestException("Please provide values for 'common_name', 'latin' and 'description'.")

        # Attempt to retrieve species
        pre_existing = Species.get_by_latin_name(request.latin)

        # If species currently exists, raise BadRequestException
        if pre_existing:
            raise endpoints.BadRequestException("A species with latin name '" + request.latin + "' already exists with ID '" + str(pre_existing.key.id()) + "'")

        # Convert InternationalMessage formats to InternationalText
        common_name = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.common_name)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new species
        species = Species(common_name=common_name,
            latin=request.latin,
            description=description)

        # Write to datastore
        species_key = species.put()

        # If image attached, create one
        if request.image:
            species.image = Image.upload(str(species_key.id()), request.image)
            species.put()

        # Update version
        ApiHelper.update_version()

        return SpeciesApi.list_species_with_translations()

    @endpoints.method(
        UPDATE_SPECIES_REQUEST,
        SpeciesTranslationListResponse,
        path='update',
        http_method='POST',
        name='species.update')
    def update_species(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If value for image provided:
        if request.image:

            # Delete current image
            species.image.delete()

            # Upload new image
            species.image = Image.upload(str(species.key.id()), request.image)

        # Write changes
        species.put()

        # Update version
        ApiHelper.update_version()

        return SpeciesApi.list_species_with_translations()

    @endpoints.method(
        ID_REQUEST,
        SpeciesTranslationListResponse,
        path='delete',
        http_method='DELETE',
        name='species.delete')
    def delete_species(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If species has an image
        if species.image:
            # Delete image
            species.image.delete()

        # Delete species
        species.key.delete()

        # Update version
        ApiHelper.update_version()

        return SpeciesApi.list_species_with_translations()

    @endpoints.method(
        message_types.VoidMessage,
        SpeciesTranslationListResponse,
        path='all_languages',
        http_method='GET',
        name='species.all_languages')
    def list_species_all_languages(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        return SpeciesApi.list_species_with_translations()

    @staticmethod
    def list_species_with_translations():

        # Retrieve all species
        all_species = Species.get_all()

        response = SpeciesTranslationListResponse()

        # Build up response of all species
        for species in all_species:

            species_response = ApiHelper.get_species_response_with_translations(species)

            # Add species to return list
            response.species.append(species_response)

        return response
# [END Species API]

# [START Animals API]
@bjorneparkappen_api.api_class(path='animals')
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

            animal_response = ApiHelper.get_animal_response(animal, request.language_code)

            # Add animal to return list
            response.animals.append(animal_response)

        return response

    @endpoints.method(
        CREATE_ANIMAL_REQUEST,
        AnimalTranslationListResponse,
        path='create',
        http_method='POST',
        name='animals.create')
    def create_animal(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Validate all required values have been provided
        if not (request.name and request.species_id and request.description and request.enclosure_id and request.is_available is not None):
            raise endpoints.BadRequestException("Please provide values for 'name', 'species_id', 'description' and 'is_available'.")

        # Retrieve species from provided ID
        species = ndb.Key(Species, request.species_id).get()

        # If species not found, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("Species of ID '" + str(request.species_id) + "' not found")

        # Retrieve enclosure from provided ID
        enclosure = ndb.Key(Enclosure, request.enclosure_id).get()

        # If enclosure not found, raise BadRequestException
        if not enclosure:
            raise endpoints.BadRequestException("Enclosure of ID '" + str(request.species_id) + "' not found")

        # Convert InternationalMessage formats to InternationalText
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new animal
        animal = Animal(parent=species.key,
            name=request.name,
            description=description,
            is_available=request.is_available).put()

        # Add animal to enclosure and write changes
        enclosure.animals.append(Animal.AnimalLookup(animal_id=animal.id(), species_id=request.species_id))
        enclosure.put()

        # Update version
        ApiHelper.update_version()

        return AnimalsApi.list_animals_with_translations()

    @endpoints.method(
        UPDATE_ANIMAL_REQUEST,
        AnimalTranslationListResponse,
        path='update',
        http_method='POST',
        name='animals.update')
    def update_animal(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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
                feeding.is_active = enclosure.is_active()
                feeding.put()

        # Update version
        ApiHelper.update_version()

        return AnimalsApi.list_animals_with_translations()

    @endpoints.method(
        ANIMAL_ID_REQUEST,
        AnimalTranslationListResponse,
        path='delete',
        http_method='DELETE',
        name='animals.delete')
    def delete_animal(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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
        for animal_reference in enclosure.animals:
            if animal_reference.animal_id == request.animal_id and animal_reference.species_id == request.species_id:
                enclosure.animals.remove(animal_reference)
                enclosure.put()

        # Retrieve all feedings which include the enclosure
        feedings = Feeding.get_all_for_enclosure(enclosure)

        # Set feeding to active state of enclosure
        for feeding in feedings:
            feeding.is_active = enclosure.is_active()
            feeding.put()

        # Delete animal
        animal.key.delete()

        # Update version
        ApiHelper.update_version()

        return AnimalsApi.list_animals_with_translations()

    @endpoints.method(
        message_types.VoidMessage,
        AnimalTranslationListResponse,
        path='all_languages',
        http_method='GET',
        name='animals.all_languages')
    def list_animals_all_languages(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        return AnimalsApi.list_animals_with_translations()

    @staticmethod
    def list_animals_with_translations():

        # Retrieve all animals
        animals = Animal.get_all()

        response = AnimalTranslationListResponse()

        # Build up response of all animals
        for animal in animals:

            animal_response = ApiHelper.get_animal_response_with_translations(animal)

            # Add animal to return list
            response.animals.append(animal_response)

        return response
# [END Animals API]

# [START Areas API]
@bjorneparkappen_api.api_class(path='areas')
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

        # Build up response of all areas
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
        AMENITY_LANGUAGE_REQUEST,
        AreaListResponse,
        path='amenities',
        http_method='GET',
        name='areas.amenities.type')
    def list_amenities_of_type(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Validate AmenityType is existing AmenityType
        if not Amenity.AmenityType.validate(request.amenity_type):

            # Generate a string representation of the Amenity.AmenityType enum
            amenity_types = [amenity_type.name for amenity_type in Amenity.AmenityType]
            amenity_types.sort()
            string_amenity_types = ', '.join(amenity_types)

            # Raise BadRequestException
            raise endpoints.BadRequestException("No AmenityType found by the name '" + request.amenity_type +
                                                "'. Amenities may be any of the following: " + string_amenity_types)

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaListResponse()

        # Build up response of all enclosures
        for area in areas:

            # If area is an Amenity of specified type
            if type(area) is Amenity and area.amenity_type == request.amenity_type:

                # Retrieve an amenity response
                area_response = ApiHelper.get_amenity_response(area, request.language_code)

                # Add area to return list
                response.amenities.append(area_response)

        return response

    @endpoints.method(
        EnclosureRequest,
        AreaTranslationListResponse,
        path='enclosures/create',
        http_method='POST',
        name='areas.enclosures.create')
    def create_enclosure(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # TODO Check for intersection of any existing areas

        # Validate all required values have been provided
        if not (request.label and request.visitor_destination and request.coordinates):
            raise endpoints.BadRequestException("Please provide values for 'label', 'visitor_destination' and 'coordinates'.")

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
            try:
                coordinate_array = coordinate_string.split(", ")
                coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))
            except:
                raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        # Create new enclosure
        enclosure = Enclosure(label=label,
                visitor_destination=visitor_destination,
                coordinates=coordinates)

        # Write to datastore
        enclosure_key = enclosure.put()

        # If image attached, create one
        if request.image:
            enclosure.image = Image.upload(str(enclosure_key.id()), request.image)
            enclosure.put()

        # Update version
        ApiHelper.update_version()

        return AreasApi.list_areas_with_translations()

    @endpoints.method(
        AmenityRequest,
        AreaTranslationListResponse,
        path='amenities/create',
        http_method='POST',
        name='areas.amenities.create')
    def create_amenity(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # TODO Check for intersection of any existing areas

        # Validate all required values have been provided
        if not (request.label and request.visitor_destination and request.coordinates and request.description and request.amenity_type):
            raise endpoints.BadRequestException("Please provide values for 'label', 'visitor_destination', 'coordinates', 'description' and 'amenity_type'.")


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
            try:
                coordinate_array = coordinate_string.split(", ")
                coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))
            except:
                raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

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
        amenity = Amenity(label=label,
                visitor_destination=visitor_destination,
                coordinates=coordinates,
                description=description,
                amenity_type=amenity_type.name)

        # Write to datastore
        amenity_key = amenity.put()

        # If image attached, create one
        if request.image:
            amenity.image = Image.upload(str(amenity_key.id()), request.image)
            amenity.put()

        # Update version
        ApiHelper.update_version()

        return AreasApi.list_areas_with_translations()

    @endpoints.method(
        UPDATE_ENCLOSURE_REQUEST,
        AreaTranslationListResponse,
        path='enclosures/update',
        http_method='POST',
        name='areas.enclosures.update')
    def update_enclosure(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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
                try:
                    coordinate_array = coordinate_string.split(", ")
                    coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))
                except:
                    raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        # If value for image provided:
        if request.image:
            # Delete current image
            enclosure.image.delete()

            # Upload new image
            enclosure.image = Image.upload(str(enclosure.key.id()), request.image)

        # Write changes
        enclosure.put()

        # Update version
        ApiHelper.update_version()

        return AreasApi.list_areas_with_translations()

    @endpoints.method(
        ADD_REMOVE_ANIMAL_REQUEST,
        message_types.VoidMessage,
        path='enclosures/animals/add',
        http_method='POST',
        name='areas.enclosures.animals.add')
    def add_animal_to_enclosure(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Attempt to retrieve enclosure
        enclosure = Enclosure.get_by_id(request.enclosure_id)

        # If enclosure does not exist, raise BadRequestException
        if not enclosure:
            raise endpoints.BadRequestException("No enclosure found with ID '" +
                                                str(request.enclosure_id) + "'.")

        # Check animal does not already exist in list
        for animal_reference in enclosure.animals:
            if animal_reference.animal_id == request.animal_id and animal_reference.species_id == request.species_id:
                raise endpoints.BadRequestException("Animal already in enclosure.")

        # Retrieve animal
        animal = Animal.get_by_id(request.animal_id, parent=ndb.Key(Species, request.species_id))

        if not animal:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No animal found with ID '" +
                                                str(animal_reference.animal_id) +
                                                "' and species ID '" +
                                                str(animal_reference.species_id) +
                                                "'.")

        # Check whether animal is in another enclosure
        current_enclosure = Enclosure.get_for_animal(request.animal_id, request.species_id)

        # If it is, remove the animal from that enclosure and write changes
        if current_enclosure:
            for animal_reference in current_enclosure.animals:
                if animal_reference.animal_id == request.animal_id and animal_reference.species_id == request.species_id:
                    current_enclosure.animals.remove(animal_reference)
                    current_enclosure.put()

        # Add animal to this enclosure
        enclosure.animals.append(Animal.AnimalLookup(animal_id=request.animal_id,
                                            species_id=request.species_id))

        # Write changes
        enclosure.put()

        # Update version
        ApiHelper.update_version()

        return message_types.VoidMessage()

    @endpoints.method(
        UPDATE_AMENITY_REQUEST,
        AreaTranslationListResponse,
        path='amenities/update',
        http_method='POST',
        name='areas.amenities.update')
    def update_amenity(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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
                try:
                    coordinate_array = coordinate_string.split(", ")
                    coordinates.append(ndb.GeoPt(coordinate_array[0], coordinate_array[1]))
                except:
                    raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

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

            amenity.amenity_type = request.amenity_type

        # If value for image provided:
        if request.image:
            # Delete current image
            amenity.image.delete()

            # Upload new image
            amenity.image = Image.upload(str(amenity.key.id()), request.image)

        # Write changes
        amenity.put()

        # Update version
        ApiHelper.update_version()

        return AreasApi.list_areas_with_translations()

    @endpoints.method(
        ID_REQUEST,
        AreaTranslationListResponse,
        path='delete',
        http_method='DELETE',
        name='areas.delete')
    def delete_area(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If area has an image
        if area.image:
            # Delete image
            area.image.delete()

        # Delete area
        area.key.delete()

        # Update version
        ApiHelper.update_version()

        return AreasApi.list_areas_with_translations()

    @endpoints.method(
        message_types.VoidMessage,
        AreaTranslationListResponse,
        path='all_languages',
        http_method='GET',
        name='areas.all_languages')
    def list_areas_all_languages(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        return AreasApi.list_areas_with_translations()

    @staticmethod
    def list_areas_with_translations():

        # Retrieve all areas
        areas = Area.get_all()

        response = AreaTranslationListResponse()

        # Build up response of all areas
        for area in areas:

            # If area is an Enclosure
            if type(area) is Enclosure:

                # Retrieve an Enclosure response
                area_response = ApiHelper.get_enclosure_response_with_translations(area)

                # Add area to return list
                response.enclosures.append(area_response)

            # Else if area is an Amenity
            elif type(area) is Amenity:

                # Retrieve an Amenity response
                area_response = ApiHelper.get_amenity_response_with_translations(area)

                # Add area to return list
                response.amenities.append(area_response)

            else:
                raise endpoints.UnexpectedException("Area found which is neither Enclosure nor Amenity, consult developer.")

        return response

# [END Areas API]

# [START Events API]
@bjorneparkappen_api.api_class(path='events')
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
        EventTranslationListResponse,
        path='create',
        http_method='POST',
        name='events.create')
    def create_event(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Validate all required values have been provided
        if not (request.label and request.description and request.location_id and request.start_time and request.end_time and request.is_active is not None):
            raise endpoints.BadRequestException("Please provide values for 'label', 'description', 'location_id', 'start_time', 'end_time' and 'is_active'.")

        # Retrieve location from provided ID
        location = Area.get_by_id(request.location_id)

        # If location not found, raise exception
        if not location or location._class_name() is Enclosure._class_name():
            raise endpoints.BadRequestException("Amenity with ID '" + str(request.location_id) + "' not found")

        # Validate times
        if not Time.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # Check location is free
        if not location.is_available(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Location with ID '" + str(request.location_id) + "' is holding another event during the selected times.")

        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

        # Create new event
        event = Event(parent=location.key,
            label=label,
            description=description,
            start_time=request.start_time,
            end_time=request.end_time,
            is_active=request.is_active)

        # Write to datastore
        event_key = event.put()

        # If image attached, create one
        if request.image:
            event.image = Image.upload(str(event_key.parent().id()) + "-" + str(event_key.id()), request.image)
            event.put()

        # Update version
        ApiHelper.update_version()

        return EventsApi.list_all_events_with_translations()

    @endpoints.method(
        UPDATE_EVENT_REQUEST,
        EventTranslationListResponse,
        path='update',
        http_method='POST',
        name='events.update')
    def update_event(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If values for both start_time and end_time provided
        if request.start_time and request.end_time:
            # Validate times
            if not Time.validate_times(request.start_time, request.end_time):
                raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

            event.start_time = request.start_time
            event.end_time = request.end_time

        # Else, if value for start_time only provided
        elif request.start_time:
            # Validate times
            if not Time.validate_times(request.start_time, event.end_time):
                raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

            event.start_time = request.start_time

        # Else, if value for end_time only provided
        elif request.end_time:
            # Validate times
            if not Time.validate_times(event.start_time, request.end_time):
                raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

            event.end_time = request.end_time

        # If value for image provided:
        if request.image:

            # Delete current image
            event.image.delete()

            # Upload new image
            event.image = Image.upload(str(event.key.parent().id()) + "-" + str(event.key.id()), request.image)

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

        return EventsApi.list_all_events_with_translations()

    @endpoints.method(
        FeedingRequest,
        EventTranslationListResponse,
        path='feedings/create',
        http_method='POST',
        name='events.feedings.create')
    def create_feeding(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Validate all required values have been provided
        if not (request.label and request.description and request.location_id and request.start_time and request.end_time and request.is_active is not None):
            raise endpoints.BadRequestException("Please provide values for 'label', 'description', 'location_id', 'start_time', 'end_time' and 'is_active'.")

        # Retrieve location from provided ID
        location = Area.get_by_id(request.location_id)

        # Validate times
        if not Time.validate_times(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Time must be in the format 'HH.MM' and end time must not exceed start time.")

        # If location not found, raise exception
        if not location or location._class_name() is Amenity._class_name():
            raise endpoints.BadRequestException("Enclosure of ID '" + str(request.location_id) + "' not found")

        # Check location is free
        if not location.is_available(request.start_time, request.end_time):
            raise endpoints.BadRequestException("Location with ID '" + str(request.location_id) + "' is holding another event during the selected times.")

        # Convert InternationalMessage formats to InternationalText
        label = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.label)
        description = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.description)

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
        feeding_key = feeding.put()

        # If image attached, create one
        if request.image:
            feeding.image = Image.upload(str(feeding_key.parent().id()) + "-" + str(feeding_key.id()), request.image)
            feeding.put()

        # Update version
        ApiHelper.update_version()

        return EventsApi.list_all_events_with_translations()

    @endpoints.method(
        UPDATE_FEEDING_REQUEST,
        EventTranslationListResponse,
        path='feedings/update',
        http_method='POST',
        name='events.feedings.update')
    def update_feeding(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If value for start_time provided
        if request.start_time:
            feeding.start_time = request.start_time

        # If value for end_time provided
        if request.end_time:
            feeding.end_time = request.end_time

        # If value for image provided:
        if request.image:
            # Delete current image
            feeding.image.delete()

            # Upload new image
            feeding.image = Image.upload(str(feeding.key.parent().id()) + "-" + str(feeding.key.id()), request.image)

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

            # If keeper busy, raise BadRequestException
            if not keeper.is_available(feeding.start_time, feeding.end_time):
                raise endpoints.BadRequestException("Keeper '" + str(request.keeper_id) + "' busy during requested times.")

            # Update keeper_id value
            feeding.keeper_id = keeper.id

        # Write changes
        feeding.put()

        # Update version
        ApiHelper.update_version()

        return EventsApi.list_all_events_with_translations()

    @endpoints.method(
        EVENT_ID_REQUEST,
        EventTranslationListResponse,
        path='delete',
        http_method='DELETE',
        name='events.delete')
    def delete_event(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        # If event has an image
        if event.image:
            # Delete image
            event.image.delete()

        # Delete event
        event.key.delete()

        # Update version
        ApiHelper.update_version()

        return EventsApi.list_all_events_with_translations()

    @endpoints.method(
        message_types.VoidMessage,
        EventTranslationListResponse,
        path='all_languages',
        http_method='GET',
        name='events.all_languages')
    def list_all_events_all_languages(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        return EventsApi.list_all_events_with_translations()

    @staticmethod
    def list_all_events_with_translations():

        # Retrieve all events
        events = Event.get_all()

        response = EventTranslationListResponse()

        # Build up response of all events
        for event in events:

            # If event is an Event
            if type(event) is Event:

                # Retrieve an Event response
                event_response = ApiHelper.get_event_response_with_translations(event)

                # Add event to return list
                response.events.append(event_response)

            # Else if event is a Feeding
            elif type(event) is Feeding:

                # Retrieve a Feeding response
                feeding_response = ApiHelper.get_feeding_response_with_translations(event)

                # Add feeding to return list
                response.feedings.append(feeding_response)

        return response

# [END Events API]

# [START Keepers API]
@bjorneparkappen_api.api_class(path='keepers')
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

            keeper_response = ApiHelper.get_keeper_response(keeper, request.language_code)

            # Add keeper to return list
            response.keepers.append(keeper_response)

        return response

    @endpoints.method(
        KeeperRequest,
        KeeperTranslationListResponse,
        path='create',
        http_method='POST',
        name='keepers.create')
    def create_keeper(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Validate all required values have been provided
        if not request.name and request.bio:
            raise endpoints.BadRequestException("Please provide values for 'name' and 'bio'.")


        # Convert InternationalMessage formats to InternationalText
        bio = ApiHelper.convert_i18n_messages_to_i18n_texts(international_messages=request.bio)

        # Create new keeper
        Keeper(name=request.name,
            bio=bio).put()

        # Update version
        ApiHelper.update_version()

        return KeepersApi.list_keepers_with_translations()

    @endpoints.method(
        UPDATE_KEEPER_REQUEST,
        KeeperTranslationListResponse,
        path='update',
        http_method='POST',
        name='keepers.update')
    def update_keeper(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

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

        return KeepersApi.list_keepers_with_translations()

    @endpoints.method(
        ID_REQUEST,
        KeeperTranslationListResponse,
        path='delete',
        http_method='DELETE',
        name='keepers.delete')
    def delete_keeper(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        # Retrieve keeper from ID
        keeper = Keeper.get_by_id(request.id)

        # If not found, raise BadRequestException
        if not keeper:
            raise endpoints.BadRequestException("No keeper found with ID '" + str(request.id) + "'.")

        # Delete species
        keeper.key.delete()

        # Update version
        ApiHelper.update_version()

        return KeepersApi.list_keepers_with_translations()

    @endpoints.method(
        message_types.VoidMessage,
        KeeperTranslationListResponse,
        path='all_languages',
        http_method='GET',
        name='keepers.all_languages')
    def list_keepers_all_languages(self, request):

        # Check authorisation
        ApiHelper.validate_user(self)

        return KeepersApi.list_keepers_with_translations()

    @staticmethod
    def list_keepers_with_translations():

        # Retrieve all keepers
        keepers = Keeper.get_all()

        response = KeeperTranslationListResponse()

        # Build up response of all keepers
        for keeper in keepers:

            keeper_response = ApiHelper.get_keeper_response_with_translations(keeper)

            # Add keeper to return list
            response.keepers.append(keeper_response)

        return response
# [END Keepers API]

# [START Versions API]
@bjorneparkappen_api.api_class()
class VersionApi(remote.Service):

    @endpoints.method(
        message_types.VoidMessage,
        VersionResponse,
        path='version',
        http_method='GET',
        name='version.get')
    def get_version(self, request):

        # Retrieve current version
        version = Version.get()

        # If no version exists
        if version is None:
            # Create one and retrieve it
            version_key = ApiHelper.update_version()
            version = version_key.get()

        return VersionResponse(version=version.version)
# [END Versions API]

# [START Visitors API]
@bjorneparkappen_api.api_class(path='visitors')
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
            raise endpoints.BadRequestException("Please provide values for 'visit_start' and 'visit_end'.")

        # Validate duration of visit is correct and less than 1 week
        if (request.visit_end - request.visit_start).total_seconds() < 0 or (request.visit_end - request.visit_start).total_seconds() > 604800:
            raise endpoints.BadRequestException("End date must be equal to or greater than start date. Duration may not exceed 14 days.")

        # Build up default itinerary and starred_species list
        itinerary = []
        starred_species = []
        events = Event.get_all()

        for event in events:

            if type(event) is Feeding:

                if event.is_active:

                    # Retrieve event location
                    location = event.key.parent().get()

                    # Retrieve species at location
                    species_list = location.get_species()

                    # Add each species id to starred species
                    for species in species_list:
                        if species.key.id() not in starred_species:
                            starred_species.append(species.key.id())

                    # Add feeding to default itinerary
                    itinerary.append(Event.EventLookup(event_id=event.key.id(), location_id=location.key.id()))

        # Create new visitor
        visitor_key = Visitor(starred_species=starred_species,
            itinerary=itinerary,
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
        name='visitors.itinerary.get')
    def get_itinerary(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.id) + "'.")

        response = EventListResponse()

        # For all events in the visitor's itinerary
        for event_reference in visitor.itinerary:

            # Attempt to retrieve the event
            event = Event.get_by_id(event_reference.event_id, parent=ndb.Key(Area, event_reference.location_id))

            # If event successfully retrieved
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

    @endpoints.method(
        ADD_REMOVE_EVENT_REQUEST,
        EventListResponse,
        path='itinerary/add',
        http_method='POST',
        name='visitors.itinerary.add')
    def add_event_to_itinerary(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.visitor_id) + "'.")

        # Attempt to retrieve event
        event = Event.get_by_id(request.event_id, parent=ndb.Key(Area, request.location_id))

        # If event does not exist, raise BadRequestException
        if not event:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No event found with ID '" +
                                                str(request.event_id) +
                                                "' and location ID '" +
                                                str(request.location_id) +
                                                "'.")

        response = EventListResponse()

        # Check event does not already exist in list
        for event_inst in visitor.itinerary:

            # Check event is not already starred
            if event_inst.event_id == event.key.id() and event_inst.location_id == event.key.parent().id():
                raise endpoints.BadRequestException("Event already starred.")

            # If not, retrieve the event from the itinerary to compare with the newly starred event
            compare_event = Event.get_by_id(event_inst.event_id, parent=ndb.Key(Area, event_inst.location_id))

            # If newly starred event end time after start_time
            if Time.validate_times(compare_event.start_time, event.end_time):
                # Check newly starred event start time not before end time
                if Time.validate_times(event.start_time, compare_event.end_time):
                    raise endpoints.BadRequestException("Event conflicts with another at the same time.")

            # If newly starred event start time before end_time
            if Time.validate_times(event.start_time, compare_event.end_time):
                # Check end time not after newly starred event end time
                if Time.validate_times(compare_event.start_time, event.end_time):
                    raise endpoints.BadRequestException("Event conflicts with another at the same time.")

        # Add event to visitor's itinerary
        visitor.itinerary.append(Event.EventLookup(event_id=request.event_id, location_id=request.location_id))

        # Write changes
        visitor.put()

        # Build up response
        for event_inst in visitor.itinerary:

            # Attempt to retrieve the event
            event = Event.get_by_id(event_inst.event_id, parent=ndb.Key(Area, event_inst.location_id))

            # If event successfully retrieved
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

    @endpoints.method(
        ADD_REMOVE_EVENT_REQUEST,
        EventListResponse,
        path='itinerary/remove',
        http_method='POST',
        name='visitors.itinerary.remove')
    def remove_event_from_itinerary(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.visitor_id) + "'.")

        event_to_remove = None

        response = EventListResponse()

        # Attempt to retrieve event
        for event_inst in visitor.itinerary:
            if event_inst.event_id == request.event_id and event_inst.location_id == request.location_id:
                event_to_remove = event_inst

            else:
                # Attempt to retrieve the event
                event = Event.get_by_id(event_inst.event_id, parent=ndb.Key(Area, event_inst.location_id))

                # If event successfully retrieved
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

        if event_to_remove is None:
            raise endpoints.BadRequestException("Event not found in itinerary.")

        # Remove event from visitor's itinerary
        visitor.itinerary.remove(event_to_remove)

        # Write changes
        visitor.put()

        return response

    @endpoints.method(
        SYNC_ITINERARY_REQUEST,
        EventListResponse,
        path='itinerary/sync',
        http_method='POST',
        name='visitors.itinerary.sync')
    def sync_itinerary(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.id) + "'.")

        response = EventListResponse()

        # Create a new itinerary to write against the user
        itinerary_to_write = []

        # Validate received itinerary events
        for received_event in request.itinerary.events:
            # Attempt to retrieve the event
            event = Event.get_by_id(received_event.id, parent=ndb.Key(Area, received_event.location.id))

            # If event is found, add to new itinerary and response
            if event:
                itinerary_to_write.append(Event.EventLookup(event_id=event.key.id(), location_id=event.key.parent().id()))

                # Retrieve an Event response
                event_response = ApiHelper.get_event_response(event, request.language_code)

                # Add event to response
                response.events.append(event_response)

        # Validate received itinerary feedings
        for received_feeding in request.itinerary.feedings:
            # Attempt to retrieve feeding
            feeding = Feeding.get_by_id(received_feeding.id, parent=ndb.Key(Area, received_feeding.location.id))

            # If feeding is found, add to new itinerary and response
            if feeding:
                itinerary_to_write.append(Event.EventLookup(event_id=feeding.key.id(), location_id=feeding.key.parent().id()))

                # Retrieve a Feeding response
                feeding_response = ApiHelper.get_feeding_response(feeding, request.language_code)

                # Add feeding to response
                response.feedings.append(feeding_response)

        # Overwrite the visitor's itinerary with the newly created itinerary
        visitor.itinerary = itinerary_to_write
        visitor.put()

        return response

    @endpoints.method(
        ID_LANGUAGE_REQUEST,
        SpeciesListResponse,
        path='starred_species',
        http_method='GET',
        name='visitors.starred_species.get')
    def get_starred_species(self, request):
        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.id) + "'.")

        response = SpeciesListResponse()

        # For all species in the visitor's starred species list
        for species_id in visitor.starred_species:

            # Attempt to retrieve the species
            species = Species.get_by_id(species_id)

            # If species successfully retrieved
            if species:

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
        ADD_REMOVE_SPECIES_REQUEST,
        SpeciesListResponse,
        path='starred_species/add',
        http_method='POST',
        name='visitors.starred_species.add')
    def add_starred_species(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.visitor_id) + "'.")

        # Attempt to retrieve species
        species = Species.get_by_id(request.species_id)

        # If species does not exist, raise BadRequestException
        if not species:
            raise endpoints.BadRequestException("No species found with ID '" +
                                                str(request.species_id) + "'.")

        response = SpeciesListResponse()

        # Check species does not already exist in list
        for species_inst in visitor.starred_species:
            if species_inst == request.species_id:
                raise endpoints.BadRequestException("Species already starred.")

        # Add species to starred_species list
        visitor.starred_species.append(request.species_id)

        # Write changes
        visitor.put()

        # Build up response
        for species_inst in visitor.starred_species:
            # Attempt to retrieve the species
            species = Species.get_by_id(species_inst)

            # If species successfully retrieved
            if species:

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
        ADD_REMOVE_SPECIES_REQUEST,
        SpeciesListResponse,
        path='starred_species/remove',
        http_method='POST',
        name='visitors.starred_species.remove')
    def remove_starred_species(self, request):

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.visitor_id) + "'.")

        species_to_remove = None

        response = SpeciesListResponse()

        # Attempt to retrieve species
        for species_inst in visitor.starred_species:
            if species_inst == request.species_id:
                species_to_remove = species_inst

            else:
                # Attempt to retrieve the species
                species = Species.get_by_id(species_inst)

                # If species successfully retrieved
                if species:

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

        if species_to_remove is None:
            raise endpoints.BadRequestException("Species not found in starred species list.")

        # Remove species starred_species list
        visitor.starred_species.remove(species_to_remove)

        # Retrieve all feedings of that species
        feedings = Feeding.get_all_for_species(species_to_remove)

        # For each feeding for that species
        for feeding in feedings:

            # Check visitor itinerary for the feeding
            for event_ref in visitor.itinerary:

                # If they have starred the feeding
                if event_ref.event_id == feeding.key.id() and event_ref.location_id == feeding.key.parent().id():

                    # Remove it
                    visitor.itinerary.remove(event_ref)
                    break

        # Write changes
        visitor.put()

        return response

    @endpoints.method(
        SYNC_STARRED_SPECIES_REQUEST,
        SpeciesListResponse,
        path='starred_species/sync',
        http_method='POST',
        name='visitors.starred_species.sync')
    def sync_starred_species(self, request):

        # Validate language code
        ApiHelper.check_language(language_code=request.language_code)

        # Attempt to retrieve visitor
        visitor = Visitor.get_by_id(request.visitor_id)

        # If visitor does not exist, raise BadRequestException
        if not visitor:
            raise endpoints.BadRequestException("No visitor found with ID '" +
                                                str(request.id) + "'.")

        response = SpeciesListResponse()

        # Create a new starred species list to write against the user
        starred_species_to_write = []

        # Validate received species
        for received_species in request.starred_species.species:
            # Attempt to retrieve the species
            species = Species.get_by_id(received_species.id)

            # If species is found, add to new starred species list and response
            if species is not None:
                starred_species_to_write.append(species.key.id())

                # Translate translatable species resources
                species_common_name_translation = InternationalText.get_translation(
                        request.language_code,
                        species.common_name)
                species_description_translation = InternationalText.get_translation(
                        request.language_code,
                        species.description)

                image = None

                # If species has an image
                if not species.image is None:
                    image = species.image.get()

                # Add species to return list
                response.species.append(SpeciesResponse(
                    id=species.key.id(),
                    common_name=species_common_name_translation,
                    latin=species.latin,
                    description=species_description_translation,
                    image=image))

        # Overwrite the visitor's starred species list with the newly created list
        visitor.starred_species = starred_species_to_write
        visitor.put()

        return response

# [END Visitors API]
# [END bjorneparkappen_api]

# [START api_server]
api = endpoints.api_server([bjorneparkappen_api])
# [END api_server]
