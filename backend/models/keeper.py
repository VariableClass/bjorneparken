from google.appengine.ext import ndb

class Keeper(ndb.Model):

    # Properties
    name = ndb.StringProperty()
    bio = ndb.StringProperty()
