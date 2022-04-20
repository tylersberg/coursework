import csv
from parcel import Parcel
from parcel import ParcelTable

# Read distance data.
def load_distances(path):
    with open(path) as data:
        distance_data = csv.reader(data, delimiter=',')
        next(distance_data)
        address_indexes = {}
        distance_matrix = []
        current_index = 0
        for line in distance_data:
            address_indexes[line[0]] = current_index
            distance_matrix.append(line[1:])
            current_index += 1
        return DistanceData(address_indexes, distance_matrix)

# Read parcel data from csv file, and return a hash table of parcels.
def load_parcels(path):
    with open(path) as data:
        parcel_data = csv.reader(data, delimiter=',')
        next(parcel_data)
        parcel_list = []
        for parcel in parcel_data:
            parcel_id = parcel[0]
            address = parcel[1]
            city = parcel[2]
            state = parcel[3]
            zipcode = parcel[4]
            deadline = parcel[5]
            mass = parcel[6]
            notes = parcel[7]
            parcel_list.append(Parcel(parcel_id, address, city, state, zipcode, deadline, mass, notes))
    parcel_table = ParcelTable(len(parcel_list))
    for parcel in parcel_list:
        parcel_table.insert(parcel)
    return parcel_table

# Handles storing and looking up distance data.
class DistanceData:
    def __init__(self, address_indexes, distance_matrix):
        self.address_indexes = address_indexes
        self.distance_matrix = distance_matrix

    # Return distance between two parcels or a parcel and the hub.
    def get_distance(self, start, destination):
        index1 = 0 if start == 'HUB' else self.get_index(start)
        index2 = 0 if destination == 'HUB' else self.get_index(destination)
        if self.distance_matrix[index2][index1] != '':
            return float(self.distance_matrix[index2][index1])
        else:
            return float(self.distance_matrix[index1][index2])

    # Return the distance_matrix index for a parcel.
    def get_index(self, parcel):
        # Format parcel data to match key in address_indexes
        key = ' ' + parcel.address + '\n(' + parcel.zipcode + ')'
        return self.address_indexes[key]
