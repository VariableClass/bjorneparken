# [START imports]
import endpoints
from google.appengine.ext import ndb
from models.amenity import Amenity
from models.animal import Animal
from models.area import Area
from models.enclosure import Enclosure
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


class AnimalReference(messages.Message):
    animal_id = messages.IntegerField(1, required=True)
    species_id = messages.IntegerField(2, required=True)

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

# All Enclosure and Amenity messages appear to duplicate unnecessarily.
# This has been done because ProtoRPC messages do not support inheritance
class EnclosureRequest(messages.Message):
    label = messages.MessageField(InternationalMessage, 1, repeated=True)
    visitor_destination = messages.StringField(2, required=True)
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
# [END messages]

# [START resources]
LANGUAGE_RESOURCE = endpoints.ResourceContainer(
    message_types.VoidMessage,
    language_code=messages.StringField(1, required=True))

UPDATE_SPECIES_RESOURCE = endpoints.ResourceContainer(
    SpeciesRequest,
    species_id=messages.IntegerField(1, required=True))
SPECIES_RESOURCE = endpoints.ResourceContainer(
    species_id=messages.IntegerField(1, required=True))

ANIMAL_RESOURCE = endpoints.ResourceContainer(
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
# [END resources]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

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

        return message_types.VoidMessage()


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
        ANIMAL_RESOURCE,
        message_types.VoidMessage,
        path='animals',
        http_method='DELETE',
        name='animals.delete')
    def delete_animal(self, request):

        # Retrieve animal
        animal = Animal.get_by_id(request.animal_id, parent=ndb.Key(Species, request.species_id))


        if not animal:
            # If not found, raise BadRequestException
            raise endpoints.BadRequestException("No animal found with ID '" +
                                                str(request.animal_id) +
                                                "' and species ID '" +
                                                str(request.species_id) +
                                                "'.")

        # Delete animal
        animal.key.delete()

        return message_types.VoidMessage()


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
                raise endpoints.UnexpectedException("Area found which is neither Enclosure or Amenity, consult developer.")

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

            # Update common name values
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

            try:
                # Convert visitor destination from string to GeoPt
                visitor_destination_array = request.visitor_destination.split(", ")

                # Update visitor destination value
                amenity.visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

            except:
                raise endpoints.BadRequestException("Co-ordinates must be in the form 'X-value, Y-value'")

        # If value for visitor destination provided
        if request.visitor_destination:

            # Convert visitor destination from string to GeoPt
            visitor_destination_array = request.visitor_destination.split(", ")

            # Update visitor destination value
            amenity.visitor_destination = ndb.GeoPt(visitor_destination_array[0], visitor_destination_array[1])

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


# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
