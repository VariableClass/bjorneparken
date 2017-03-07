import base64
import crud
import endpoints
import re
from google.appengine.ext import ndb

class Image(ndb.Model):
    accepted_mime_types = ["image/jpeg", "image/png", "image/webp", "image/gif", "image/bmp", "image/tiff", "image/ico"]

    name = ndb.IntegerProperty()
    mime_type = ndb.StringProperty()

    # Methods
    def get(self):
        # Retrieve image
        image = crud.retrieve_image_file(self.name)

        # Encode image
        base64_prefix = "data:" + self.mime_type + ";base64,"

        return base64_prefix + base64.b64encode(image)


    def delete(self):
        # Delete image
        crud.delete_file(self.name);


    # Static Methods
    @classmethod
    def is_valid_mime_type(cls, mime_type):
        return mime_type in cls.accepted_mime_types


    @classmethod
    def upload(cls, image_id, image):
        # Knock mime type off start of image base64 and store it
        request_data = image.split(',')
        mime_type = re.split('[:;]+', request_data[0])[1]

        # Validate mime type
        if cls.is_valid_mime_type(mime_type):

            # Write image metadata to
            image = Image(name=image_id, mime_type=mime_type)
            image.put()

            # Decode base64 image
            image_file = request_data[1].decode('base64')

            # Upload image to cloud storage
            crud.upload_image_file(image_file, image_id, mime_type)

            return image

        else:
            raise endpoints.BadRequestException(
                "'" + mime_type + "' is not an accepted type. Please upload an image of one of the following formats:" + ", ".join(accepted_mime_types))
