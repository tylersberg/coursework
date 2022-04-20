from datetime import time
# Holds data representing a single parcel.
class Parcel:
    START = time(hour=8) # Default time that parcels are available by
    END = time(hour=17) # Deafault time that parcels should be delivered by

    def __init__(self, parcel_id, address, city, state, zipcode, deadline, mass, notes):
        self.parcel_id = parcel_id
        self.address = address
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.mass = mass
        self.notes = notes
        self.priority = False 
        # Convert deadline into time.
        if deadline == 'EOD':
            self.deadline = Parcel.END
        else:
            if deadline[1] == ':':
                self.deadline = time.fromisoformat('0'+deadline[:4])
            else: 
                self.deadline = time.fromisoformat(deadline[:5])
        if self.deadline != Parcel.END: self.priority = True
         # Parcel status is kept as a list of tuples, the first value of the tuple being 
        # the time that status was set. So that the parcel's status can be retrieved at any time.
        self.status = []
        # Record initial status.
        if notes == 'Delayed on flight---will not arrive to depot until 9:05 am':
            t = time(hour=9, minute=5)
            self.available_at = t
            self.status.append((Parcel.START, 'Delayed until 9:05 am'))
            self.status.append((t, 'At the hub'))
        elif self.notes == 'Wrong address listed':
            t = time(hour=10, minute=20)
            self.available_at = t
            self.status.append((Parcel.START, 'Wrong address listed'))
            self.status.append((t, 'At the hub'))
        else:
            self.available_at = Parcel.START
            self.status.append((Parcel.START, 'At the hub'))
       
    def __str__(self):
        parcel_info = "Parcel #{parcel_id}\nAddress: {address}, {city}, {state} {zipcode}\nMass: {mass}\nDeadline: {deadline}\nNotes: {notes}\n"
        parcel_info = parcel_info.format(
                parcel_id = self.parcel_id,
                address = self.address,
                city = self.city,
                state = self.state,
                zipcode = self.zipcode,
                mass = self.mass,
                deadliine = self.deadline.isoformat('minutes'),
                notes = self.notes)
        parcel_status = "Parcel log:"
        for line in self.status:
            parcel_status += "\n" + line[0].isoformat('minutes') + ' : ' + line[1]
        return (parcel_info + parcel_status)

    # Returns the status of the parcel at a given time.
    def get_status(self, t):
        self.status
        for i in range(len(self.status)):
            # If the time of the next entry is later than the provided time or if the current entry is the last.
            if i == len(self.status)-1 or self.status[i+1][0] > t: 
                # Return the current status
                return self.status[i]


# Hash table that stores parcel objects, using the parcel_id as the key.
# Collisions are resolved with chaining.
# Note: Table does not enforce unique id's, take care when inserting items.
class ParcelTable:
    def __init__(self, size):
        self.table_size = size
        self.table = [None] * size
        self.parcel_count = 0

    # Make the hash table iterable.
    def __iter__(self):
        self.table_index = 0
        self.list_index = 0
        return self

    def __next__(self):
        if self.table_index >= len(self.table):
            raise StopIteration
        else:
            current_data = self.table[self.table_index][self.list_index]
        if self.list_index + 1 < len(self.table[self.table_index]):
            self.list_index += 1
        else:
            self.list_index = 0
            self.table_index += 1
        return current_data

    # Make compatible with python len() function
    def __len__(self):
        return self.parcel_count

    # Return the index that a key hashes to. Used in insert() and get().
    def hash_key(self, key):
        return int(key) % self.table_size

    # Insert a parcel into the hash table at index provided by hash_key().
    def insert(self, parcel):
        index = self.hash_key(parcel.parcel_id)
        if self.table[index] is None:
            self.table[index] = [parcel]
        else:
            self.table[index].append(parcel)
        self.parcel_count += 1

    # Retrieve a parcel from the hash table.
    def get(self, parcel_id):
        for parcel in self.table[self.hash_key(parcel_id)]:
            if int(parcel.parcel_id) == parcel_id:
                return parcel
        return None        
