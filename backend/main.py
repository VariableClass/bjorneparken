# [START imports]
from models.animal import Animal
import endpoints
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]


# [START messages]
class AnimalMessage(messages.Message):
    name = messages.StringField(1)
    species = messages.StringField(2)
    description = messages.StringField(3)


class ListResponse(messages.Message):
    list_content = messages.MessageField(AnimalMessage, 1, repeated=True)

# [END messages]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

    @endpoints.method(
        message_types.VoidMessage,
        ListResponse,
        path='animals',
        http_method='GET',
        name='animals.list')
    def list_animals(self, response):
        animals = Animal.get_all_animals()

        animal_messages = []

        for animal in animals:
            animal_messages.append(AnimalMessage(name=animal.name, species=animal.species.common_name, description=animal.description))

        return ListResponse(list_content=animal_messages)
# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
