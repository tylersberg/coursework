from datetime import time, timedelta, datetime, date
from truck import Truck
from parcel import Parcel

class Router:

    def __init__(self, parcels, distances, fleet):
        self.parcels = parcels
        self.grouped = {13, 14, 15, 16, 19, 20}
        self.distances = distances
        self.fleet = fleet
       # Create a number of 'loads' that can accomodate all parcels
        self.loads = []
        for i in range((len(parcels) // Truck.CAPACITY) + (len(parcels) % Truck.CAPACITY > 0)):
            self.loads.append(Load(distances))
        self.create_loads()
        self.schedule_loads()
        self.deliver_loads()

    # Place all parcels into a load.
    # O(N) runtime complexity, O(N) space complexity
    def create_loads(self):
        for parcel in self.parcels:
            # Choose which loads to consider for a parcel based on its attributes.
            # Priority parcels are placed in one of the first n loads where n is the size of the fleet,
            # with special cases for grouped parcels and delayed priority parcels.
            if parcel.available_at > Parcel.START and parcel.priority:
                self.place_parcel(parcel, self.loads[len(self.fleet)-1:len(self.fleet)])
            elif int(parcel.parcel_id) in self.grouped:
                self.place_parcel(parcel, self.loads[0:1])
            elif parcel.priority: 
                self.place_parcel(parcel, self.loads[:len(self.fleet)])
            # Normal parcels are placed in later loads.
            else: 
                self.place_parcel(parcel, self.loads[len(self.fleet):])

    # Places a package into a load and route that will minimize the distance added to the route.
    # The parcel_available_at argument is used if the parcel is delayed, to set the load minimum start time.
    # O(N^2) time complexity
    def place_parcel(self, parcel, possible_loads):
        chosen_load = None
        # Create a new load and place the parcel in it. Used if no compatible load is found.
        def place_in_new(self):
            new = Load(self.distances)
            new.route.insert(1, parcel)
            self.loads.append(new)
            return new
        # If no possible loads create a new load and insert parcel.
        if not possible_loads:
            chosen_load = place_in_new(self)
        else:
            best_load = None
            best_route_index = -1
            min_distance = float("inf")
            # Find insert location
            for load in possible_loads:
                d = self.distances.get_distance
                # Only consider a route if it has space to accomadate a parcel.
                if len(load.route) < Truck.CAPACITY+2:
                    for i, next_parcel in enumerate(load.route[1:], start=1):
                        # A parcel will not be inserted into a route before a parcel with an earlier deadline.
                        if isinstance(next_parcel, Parcel) and next_parcel.deadline < parcel.deadline:
                            continue
                        prev_parcel = load.route[i-1]
                        # A parcel will not be inserted into a route after a parcel with a later deadline.
                        if isinstance(prev_parcel, Parcel) and prev_parcel.deadline > parcel.deadline:
                            break
                        distance = d(prev_parcel, parcel) + d(parcel, next_parcel) - d(prev_parcel, next_parcel)
                        if distance < min_distance:
                            min_distance = distance
                            best_load = load
                            best_route_index = i
            if best_load == None:
                best_load  = place_in_new(self)
            else:
                best_load.route.insert(best_route_index, parcel)
        # Update load data.
        best_load.id_set.add(parcel.parcel_id)
        if best_load.min_start < parcel.available_at:
            best_load.min_start = parcel.available_at
        if parcel.notes == 'Can only be on truck 2':
            best_load.for_truck2 == True

    # Assign loads to trucks and record start and end times.
    # O(N) time complexity, note: for unreasonably large numbers of trucks, would instead be O(T) T being the number of trucks.
    def schedule_loads(self):
        # Handles scheduling a load on a specific truck
        def schedule_load(truck, load):
            truck.loads.append(load)
            load.start_time = max(load.min_start, truck.available_at)
            load.complete_time = (datetime.combine(date.today(), load.start_time) + truck.time_to_complete(load)).time()
            truck.available_at = load.complete_time
        # Returns the truck that will be available soonest
        def next_available_truck():
            next_truck = self.fleet[0]
            for truck in self.fleet[1:]:
                if truck.available_at < next_truck.available_at:
                    next_truck = truck
            return next_truck
        # Determines which truck to assign the loads to
        for load in self.loads:
            if load.for_truck2:
                schedule_load(self.fleet[1], load)
            else: 
                schedule_load(next_available_truck(), load)

    # Record logs of all deliveries. 
    # O(N) time complexity
    def deliver_loads(self):
        for truck in self.fleet:
            for load in truck.loads:
                t = load.start_time
                truck.log.append((t, 'Leaving hub with parcels: ' + str(load.id_set)))
                for parcel in load.route[1:-1]:
                    parcel.status.append((t, 'Loaded onto ' + truck.name))
                for prev_index, destination in enumerate(load.route[1:]):
                    t_to_add = truck.distance_to_time(self.distances.get_distance(load.route[prev_index], destination))
                    t = (datetime.combine(datetime.today(), t) + t_to_add).time()
                    if destination == 'HUB':
                        truck.log.append((t, 'Arrived back at hub'))
                    else:
                        truck.log.append((t, 'Delivered parcel #' + str(destination.parcel_id)))
                        destination.status.append((t, 'Delivered by ' + truck.name))

# Load object that contains a list of parcels to deliver, and other constraints for the load. 
class Load:
    def __init__(self, distances):
        self.distances = distances
        self.for_truck2 = False
        self.min_start = Parcel.START
        # The route contains the list of parcels to deliver, in order, starting and ending at the hub.
        self.route = ['HUB', 'HUB']
        self.id_set = set()

    def total_distance(self):
        total = 0.0
        for i, p in enumerate(self.route[1:]):
            total += self.distances.get_distance(p, self.route[i])
        return round(total, 1)


