from google.appengine.ext import ndb

class Version(ndb.Model):

    # Properties
    version = ndb.DateTimeProperty()

    # Class Methods
    @classmethod
    def get(cls):
        return cls.query().get()
