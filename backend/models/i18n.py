from google.appengine.ext import ndb

class InternationalText(ndb.Model):

    # Static Methods
    @staticmethod
    def get_translation(language_code, translations):

        if language_code == 'nn' or language_code == 'nb' or language_code == 'no':
            for translation in translations:
                if translation.language_code == 'no':
                    return translation.text

        for translation in translations:
            if translation.language_code == language_code:
                return translation.text

        # If the desired language code is not supported, return English
        for translation in translations:
            if translation.language_code == 'en':
                return translation.text

    @classmethod
    def validate_language_code(cls, language_code):
        if language_code in cls.SUPPORTED_LANGUAGES:
            return True

        return False

    # Constants
    SUPPORTED_LANGUAGES = ['en', 'no', 'nb', 'nn']

    # Properties
    text = ndb.StringProperty()
    language_code = ndb.StringProperty()
