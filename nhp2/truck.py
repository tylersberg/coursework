import router
from datetime import time, timedelta

# Truck object which can accept and process Load objects the router creates.
class Truck:

    CAPACITY = 16
    # Speed is expressed as per second for easy use with timedelta.
    SPEED = 18.0 / 60 / 60

    def __init__(self, name):
        self.name = name
        self.loads = []
        self.available_at = time(hour=8)
        # Truck log is kept in a list of tuples, the first value of the tuple is a time,
        # and the second a string describing an event that occured at that time.
        # Logs should be recorded in order, with the earliest log entries recorded first.
        self.log = [(self.available_at, 'Ready for loading')]

    # Return a timedelta representing the amount of time the truck will require to complete a given load.
    def time_to_complete(self, load):
         return timedelta(seconds=load.total_distance() / self.SPEED)

    # Return a timedelta representing the amount of time it will take the truck to cover a given distance. 
    def distance_to_time(self, distance):
        return timedelta(seconds=distance / self.SPEED)

    # Return distance truck has traveled at specific time.
    def distance_at_time(self, t):
        total_distance = 0
        for load in self.loads:
            if load.complete_time <= t:
                total_distance += load.total_distance()
            elif load.start_time < t:
                run_time = (datetime.combine(date.today(), t) - load.start_time).seconds()
                total_distance += run_time * self.SPEED 
        return total_distance

    # Print the log of the truck.
    def print_log(self, t):
        print('Activity log for ' + self.name + ':')
        for log in self.log:
            if log[0] > t:
                return
            else:
                print(log[0].isoformat('minutes') + ' : ' +log[1])


