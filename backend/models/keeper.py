from google.appengine.ext import ndb
from i18n import InternationalText

class Keeper(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    bio = ndb.LocalStructuredProperty(InternationalText, repeated=True)

    # Class Methods
    @classmethod
    def get_all(cls):
        return cls.query().fetch()
