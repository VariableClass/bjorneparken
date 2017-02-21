# [START imports]
import endpoints
from protorpc import message_types
from protorpc import messages
from protorpc import remote
# [END imports]


# [START messages]
class AnimalRequest(messages.Message):
    content = messages.StringField(1)


class AnimalResponse(messages.Message):
    content = messages.StringField(1)


ANIMALS_RESOURCE = endpoints.ResourceContainer(
    AnimalRequest)
# [END messages]


# [START api]
@endpoints.api(name='bjorneparkappen', version='v1', api_key_required=True)
class BjorneparkappenApi(remote.Service):

    @endpoints.method(
        ANIMALS_RESOURCE,
        AnimalResponse,
        path='animals',
        http_method='GET',
        name='animals.list')
    def list_animals(self, request):
        return AnimalResponse(content="Success!")
# [END api]

# [START api_server]
api = endpoints.api_server([BjorneparkappenApi])
# [END api_server]
