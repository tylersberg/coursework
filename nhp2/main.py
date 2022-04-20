# Tyler Berg ID#001362374
# NHP2 WGUPS Routing Program

# readme.txt contains the required written component.
# Screenshots are in the root folder.

# Overview of components:
    # main.py - Provides user interface and passes the correct data to the router.
    # parcel.py - Defines parcel objects, and a hash table for storing parcel data enabling efficient lookup.
    # truck.py - Defines truck objects which are assigned a list of 'loads' to deliver.
    # data.py - Handles the parsing of data files and creation of data structures.
    # router.py - Takes in parcel, distance, fleet data to place all parcels into 
    # loads with an assigned route and assign those loads to trucks. 

import data
from datetime import time
from router import Router
from truck import Truck

PARCEL_DATA = 'parcelData.csv'
DISTANCE_DATA = 'distanceData.csv'
TRUCK_COUNT = 2
# Load parcel data into a hash table.
# Space complexity for parcel hash table is O(N), N being the number of parcels.
# Lookup time is O(1)
all_parcels = data.load_parcels(PARCEL_DATA)
# Load point to point distance data.
# Space complexity for distance data is O(N^2), N being the number of parcels.
# Note: Distance data scales with the number of unique locations, which has an upper bound of N
# Lookup time is O(1)
distance_data = data.load_distances(DISTANCE_DATA)
# Create a list of trucks with a length equal to truck_count, named Truck 1 ... Truck n
fleet = []
for i in range(TRUCK_COUNT):
    fleet.append(Truck('Truck ' + str(i + 1)))
# Create router.
# Runtime complexity of the router is O(N^2)
router = Router(all_parcels, distance_data, fleet)

# Main menu for the program
def menu():
    MENU = """Select an option:
    1) View parcel information by ID
    2) View status of all parcels at a specific time
    3) View truck status at a specific time
    4) View final truck status
    5) View final parcel status
    0) Exit"""

    TIMESPEC = 'minutes' # Default timespec for output.
    def time_prompt():
        print('Enter time as hh:mm (24 hour time, include leading 0)')
        t = time.fromisoformat(input('Enter time:'))
        return t

    def parcel_summary(t):
        for parcel in all_parcels:
            status_time, status = parcel.get_status(t)
            print('Parcel #{id}: Deadline = {deadline}, {status} as of {time} Notes: {notes}'.format(
                    id=parcel.parcel_id, 
                    deadline=parcel.deadline.isoformat(TIMESPEC), 
                    status=status, 
                    time=status_time.isoformat(TIMESPEC), 
                    notes=parcel.notes))

    def continue_prompt():
        input('Press Enter to continue')

    exit = False;
    while not exit:
        print(MENU)
        selection = input()
        if selection == '1':
            print(all_parcels.get(int(input('Enter parcel ID:'))))
            continue_prompt()
        elif selection == '2':
            parcel_summary(time_prompt())
            continue_prompt()
        elif selection == '3':
            t = time_prompt()
            for truck in fleet:
                truck.print_log(t)
            continue_prompt()
        elif selection == '4':
            total_distance = 0.0
            for truck in fleet:
                truck.print_log(time.max)
                truck_distance = truck.distance_at_time(time.max)
                total_distance += truck_distance
                print('Total distance for {name} {dist:0.1f}'.format(name=truck.name, dist=truck_distance))
            print('Total distance for all trucks: {total}'.format(total=total_distance))
            continue_prompt()
        elif selection == '5':
            parcel_summary(time.max)
            continue_prompt()
        elif selection == '0':
            exit = True
        else:
            print('Not a valid option')
menu()

