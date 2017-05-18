import os
import cloudstorage as gcs
from google.appengine.api import app_identity
from google.appengine.api import images
from google.appengine.ext import blobstore


# Upload image to cloud storage bucket
def upload_image_file(image, name, mime_type):

    # Define retry parameters
    write_retry_params = gcs.RetryParams(backoff_factor=1.1)

    # Retrieve default bucket
    bucket_name = retrieve_default_bucket()

    # Define filename
    filename = '/' + bucket_name + '/' + str(name)

    # Create new cloud storage file to write to
    gcs_file = gcs.open(filename,
                        'w',
                        content_type=mime_type,
                        retry_params=write_retry_params,
                        options={'x-goog-acl': 'public-read', 'cache-control': 'private, max-age=0, no-cache'})

    # Write the image file to cloud storage
    gcs_file.write(image)

    # Close the cloud storage file
    gcs_file.close()

    return bucket_name


# Retrieve image from cloud storage bucket
def retrieve_image_file(filename):

    # Retrieve default bucket
    bucket_name = retrieve_default_bucket()

    # Open image file from cloud storage to read from
    gcs_file = gcs.open('/' + bucket_name + '/' + str(filename))

    # Read image file from cloud storage
    image = gcs_file.read()

    # Close the cloud storage file
    gcs_file.close()

    return image


# Delete image from cloud storage bucket
def delete_file(filename):

    # Try to delete the file, ignore if file is not found
    try:

        # Retrieve default bucket
        bucket_name = retrieve_default_bucket()

        gcs.delete('/' + bucket_name + '/' + str(filename))

    except gcs.NotFoundError:
        print (filename + 'not found')


# Retrieves a public URL for an image
def get_serving_url(filename):

    # Retrieve default bucket
    bucket_name = retrieve_default_bucket()

    # Create a blobstore style filename
    blobstore_filename = '/gs/' + bucket_name + '/' + str(filename)

    try:

        # Create a blob key from which to build a serving URL
        blob_key = blobstore.create_gs_key(blobstore_filename)

        # Return a public serving URL
        serving_url = images.get_serving_url(blob_key=blob_key)

        return serving_url

    except images.ObjectNotFoundError:

        return ''


# Retrieve default bucket
def retrieve_default_bucket():

    # Retrieve default bucket
    return os.environ.get('BUCKET_NAME', app_identity.get_default_gcs_bucket_name())
