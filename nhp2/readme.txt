Written Overview for NHP2

A.  Identify a named self-adjusting algorithm (e.g., “Nearest Neighbor algorithm,” 
“Greedy algorithm”) that you used to create your program to deliver the packages.
    The program uses a Greedy algorithm of my own design. It inserts packages into 
    a route, minimizing the distance added to the route.
    
B.  Write an overview of your program, in which you do the following:

    1.  Explain the algorithm’s logic using pseudocode.
        The algorithm takes each package and inserts it into a load's route at a 
        position which minimizes distance added.
        
create loads that can accomodate all parcels
initialize each route start, end = HUB
for each package:
    min_dist = infinite
    for each possible load:
        for index in load's route(exclude first index):
            prev = route[index-1]
            next = route[index]
            distance_added = dist(prev to package) 
                + dist(package to next) 
                - dist(prev to next)
            if distance_added < min_dist:
                chosen_load, route_index = current
    insert package into chosen_load at route_index

    2.  Describe the programming environment you used to create the Python 
    application.
        The program was written in python using Vim. Operating system was Manjaro 
        Linux with Xfce. The development computer has a AMD Ryzen 5 1600X 
        processor, 16gb of RAM, and a Radeon 580X GPU.

    3.  Evaluate the space-time complexity of each major segment of the program, 
    and the entire program, using big-O notation.
        N being the number of packages, the space and time complexity of the 
        components is as follows:
            Package data: 
                Creation of the data structure is O(N) time complexity.
                Space complexity is O(N) Packages are stored in a hash table, the 
                hash table is created with a size equal to N. Time complexity for 
                lookup functions is O(1), the hash table also supports iterating 
                over all packages with a time complexity of O(N)
            Distance data: 
                Creation of the data structure is O(N^2) time complexity.
                Space complexity is O(N^2) as there are N*N elements.
                Time complexity is O(1), distance data is stored in a hash table 
                allowing constant time lookups.
            Routing Algorithm:
                Space complexity is O(N). The router creates routes, the total 
                size of which scale with N. Time complexity is O(N^2). The 
                potential insert locations the router considers for each package
                scales with N.
            Space and time complexity for smaller components can be found in code 
            comments.

    4.  Explain the capability of your solution to scale and adapt to a growing 
    number of packages.
        No component of the program exceeds polynomial space or time complexity, so
        the program should scale well to a growing number of packages. The package 
        and distance tables can be created of any size and maintain constant speed 
        lookups, as long as package data is available when the hash table is 
        created. If packages must be added after the hash table is created, there 
        is the potential for collisions. This could make lookup slower than O(1) 
        Even with heavy collisions, however, lookups are better than O(N).

    5.  Discuss why the software is efficient and easy to maintain.
        The overall space and time complexity are both O(N^2). This allows the 
        program to run efficiently. To facilitate easy maintanence the code for the 
        program is devided into files with logical purposes, meaningful variable 
        names are used, and the code contains comments to explain its function. 

    6.  Discuss the strengths and weaknesses of the self-adjusting data structures 
    (e.g., the hash table).
        The strength of the hash table is that it allows constant speed lookup with 
        the id of a package. It also allows O(1) insertion. 
        However, the hash table only supports lookup by id. If packages must be 
        routinely looked up based on other attributes, the hash table does not suit 
        this purpose well. Also, if more packages must be added after the creation 
        of the table, collisions begin to occur and performance will degrade.

D.  Identify a self-adjusting data structure, such as a hash table, that can be 
used with the algorithm identified in part A to store the package data.
    The program implements a chaining hash table to store package data. When 
    created the hash table is a perfect 1-1 mapping. If packages are then added to 
    the table, collisions are handled with chaining.

    1.  Explain how your data structure accounts for the relationship between the 
    data points you are storing.
        The hash table is created of N size, N being the number of packages. Since 
        the package ids are sequential a simple modulo N hashing function yields a 
        table with no collisions. Packages are stored at that index in a list. If 
        collisions do occur, they are handled by chaining the packages in that 
        list. Look up for the hash table is more efficient then it would be in a 
        list because the hash function returns the index that a specific key is 
        located at. In a list, lookup would require sequentially checking each 
        index for the correct key.

I.  Justify the core algorithm you identified in part A and used in the solution by 
doing the following:

    1.  Describe at least two strengths of the algorithm used in the solution.
        The chosen algorithm is efficient in both space and time complexity. It 
        includes logic to create routes that meet delivery requirements, without 
        needing manual loading of trucks. It can automatically be scaled to 
        accomodate different number of trucks or truck capacity.

    2.  Verify that the algorithm used in the solution meets all requirements in 
    the scenario.
        The pacakges are delivered using a total of 124.5 miles of truck travel.
        All packages are delivered before their deadline.
        All packages are delivered according to their special requirements.
        This can be verified by selecting 'Viewing final parcel status' in the 
        program.

    3.  Identify two other named algorithms, different from the algorithm 
    implemented in the solution, that would meet the requirements in the scenario.
        Other than the algorithm used in the program the problem could be solved 
        using either a Nearest Neighbor algorithm or a Multi-fragment algorithm.

        a.  Describe how each algorithm identified in part I3 is different from the 
        algorithm used in the solution.
            Nearest Neighbor: The nearest neighbor would be simple to implement, 
            and has a good run-time efficiency of O(N^2), but would require some 
            other method of loading the trucks with suitable packages.
            Multi-Fragment: The multi-fragment algorithm runs somewhat slower at 
            O(N^2 logN), but comes to a closer to optimal solution, and could 
            potentially be adapted to automatically do some of the truck loading 
            automatically.

J.  Describe what you would do differently, other than the two algorithms 
identified in I3, if you did this project again.
    Since, the algorithm is mainly concerned with visiting all of the unique 
    addresses of the packages, it would likely be worthwhile to create a data 
    structure that stored packages by address instead. This would allow the 
    algorithm to efficiently look up packages destined for a particular address.
    Although the algorithm used comes to a satisfactory solution, there is room for 
    improvement in coming to a more optimal solution, I believe. Specifically, how 
    the packages are divided into different truck loads has a large impact on the 
    overall milage efficiency. Developing a hueristic to more intelligently sort 
    the packages into loads, could improve the algorithm.
    

K.  Justify the data structure you identified in part D by doing the following:

    1.  Verify that the data structure used in the solution meets all requirements 
    in the scenario.
        The pacakges are delivered using a total of 124.5 miles of truck travel.
        All packages are delivered before their deadline.
        All packages are delivered according to their special requirements.
        The program includes a hash function that provides efficient look up 
        functionality.
        Verification of all requirements can be viewed from the user interface in 
        the program, and all information is accurate.

        a.  Explain how the time needed to complete the look-up function is 
        affected by changes in the number of packages to be delivered.
            The look up time of the hash table reamains O(1) no matter the number 
            of packages, as the hash table creates a perfect 1 to 1 mapping. If 
            extra packages are inserted after the table is created, collisions are 
            resolved with chaining. Only in scenarios where heavy collisions occur 
            is the look up time affected.

        b.  Explain how the data structure space usage is affected by changes in 
        the number of packages to be delivered.
            The hash table's space usage is always O(N), N being the number of 
            packages. The data structure storing distance data scales with O(N^2).

        c.  Describe how changes to the number of trucks or the number of cities 
        would affect the look-up time and the space usage of the data structure.
            The hash table is implemented with no realtion to trucks or cities, 
            changes to trucks or cities would have no effect on look up time or 
            space complexity for the hash table. 

    2.  Identify two other data structures that could meet the same requirements in 
    the scenario.
        Other data structures that would fulfill all of the requirements in the 
        scenario would be a hash table with no collision resolution, or a hash 
        table with linear probing.

        a.  Describe how each data structure identified in part K2 is different 
        from the data structure used in the solution.
            Hash table with no collision resolution: Since the package data is 
            available when the hash table is created a perfect mapping with no 
            collision resolution would be possible. This would have the same O(1) 
            lookup time and O(N) space complexity, but it would not allow for 
            inserting of packages after the table has been created.
            Hash table with linear probing: Instead of resolving collisions with 
            chaining, collisions could be handled with linear probing. This would 
            have the same O(1) lookup time and O(N) space complexity, but 
            collisions would be resolved by mapping to the next sequential index. 
            To accomodate this the table would either need to be created with extra 
            space or be able to resize itself when full.

L.  Acknowledge sources, using in-text citations and references, for content that 
is quoted, paraphrased, or summarized.
    No outside sources were used.
