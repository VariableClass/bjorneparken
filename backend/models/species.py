from google.appengine.ext import ndb

class Species(ndb.Model):

    # Properties
    common_name = ndb.StringProperty()
    latin = ndb.StringProperty()
    description = ndb.StringProperty()
