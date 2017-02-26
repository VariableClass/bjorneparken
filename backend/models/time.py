import re

class Time():

    # Static Methods
    @staticmethod
    def validate_times(start_time, end_time):

        # Check start time is in correct format
        if not Time.check_format(start_time):
            return False

        # Check end time is in correct format
        if not Time.check_format(end_time):
            return False

        # Convert time to comparable format
        start_time = Time.get_compare_formatted(start_time)
        end_time = Time.get_compare_formatted(end_time)

        # Check end_time after start_time
        if start_time > end_time:
            return False

        return True

    @staticmethod
    def check_format(time):
        pattern = re.compile("([01][0-9]|2[0-3])\.?[0-5][0-9]")

        # Check time is in correct format
        if not pattern.match(time):
            return False

        return True


    @staticmethod
    def get_compare_formatted(time):
        if len(time) > 4:
            time_array = time.split(".")
            time = time_array[0] + time_array[1]

        return int(time)
