inputReal = """183, 157
331, 86
347, 286
291, 273
285, 152
63, 100
47, 80
70, 88
333, 86
72, 238
158, 80
256, 140
93, 325
343, 44
89, 248
93, 261
292, 250
240, 243
342, 214
192, 51
71, 92
219, 63
240, 183
293, 55
316, 268
264, 151
68, 98
190, 288
85, 120
261, 59
84, 222
268, 171
205, 134
80, 161
337, 326
125, 176
228, 122
278, 151
129, 287
293, 271
57, 278
104, 171
330, 69
141, 141
112, 127
201, 151
331, 268
95, 68
289, 282
221, 359"""

from collections import defaultdict
import sys
sys.setrecursionlimit(50000)


max = 360
input = inputReal
# inputIntList =  

inputList = input.split('\n')
inputListWithOffset = []
for i in inputList:
	x, y = i.split(', ')
	inputListWithOffset.append([int(x)+max, int(y)+max])


axis_length = int(max * 4)
grid = [None] * axis_length

for x in xrange(0, axis_length):
	grid[x] = [None] * axis_length


grid_length = len(grid)
print grid_length

for i in xrange(0, axis_length):
	for j in xrange(0, axis_length):
		min_distance = max * 100
		for idx, coordinate in enumerate(inputListWithOffset):
			taxi_distance = abs((coordinate[0] - i)) + abs((coordinate[1] - j))

			if taxi_distance == min_distance:
				grid[i][j] = -1
			elif taxi_distance < min_distance:
				min_distance = taxi_distance
				grid[i][j] = idx


candidates = [x for x in xrange(0, 50)]
edge_numbers1 = set(grid[0]).union(set(grid[1439]))
edge_numbers2 = set()

for i in grid:
	edge_numbers2.add(i[0])
	edge_numbers2.add(i[grid_length-1])


all_edge_numbers = edge_numbers1.union(edge_numbers2)

for i in all_edge_numbers:
	if i in candidates:
		candidates.remove(i)

print candidates

area_counter = defaultdict(int)

print 'done first...'
print grid[1][1]
print area_counter

for i in grid:
	for j in i:
		if j in candidates:
			area_counter[j] +=1

print area_counter



# STAR 2 


# Mark areas
grid_star_2 = [None] * max
for x in xrange(0, max):
	grid_star_2[x] = [None] * max


inputListInt = []
for i in inputList:
	x, y = i.split(', ')
	inputListInt.append([int(x), int(y)])

def total_distance(inputListInt, gridLocationI, gridLocationJ):
	total_distance = 0
	for i in inputListInt:
		total_distance += abs((i[0] - gridLocationI)) + abs((i[1] - gridLocationJ))
	return total_distance



for i in xrange(0, max):
	for j in xrange(0, max):
		for idx, coordinate in enumerate(inputListInt):
			total_taxi_distance = total_distance(inputListInt, i, j)
			if total_taxi_distance < 10000:
				# print 'w00t'
				grid_star_2[i][j] = 1

# print grid_star_2[221]



# test_area = [None] * 10
# for i in xrange(0,10):
# 	test_area[i] = [None] * 10

# # print test_area
# test_area[2][2] = 1
# test_area[2][3] = 1
# test_area[2][4] = 1
# test_area[3][2] = 1
# test_area[3][3] = 1
# test_area[3][4] = 1

# test_area[6][6] = 1
# test_area[6][7] = 1

# grid_star_2 = test_area


###### count areas 
grid_star_2_dimension = len(grid_star_2)

total_area_counter = 0
max_area_counter = 0

print grid_star_2[78]
print grid_star_2[79]
print grid_star_2[80]
print grid_star_2[81]



def find_area(x, y):
	global total_area_counter, max_area_counter, grid_star_2
	# print str(x) + " " + str(y)
	
	if grid_star_2[x][y] == None or grid_star_2[x][y] == 0:
		# print "go back: " + str(x) + " " + str(y)
		return

	if grid_star_2[x][y] == 1:
		# print "edit grid star--"
		grid_star_2[x][y] = 0
		# print "grid_star_2 modified: " + str(grid_star_2[x][y])
		total_area_counter += 1

	if x+1 < grid_star_2_dimension:
		if x ==79:
			print "inside x+1: " + str(x) + " " + str(y)
			print "grid star: " + str(grid_star_2[x][y])
		find_area(x+1, y)
	if x-1 >= 0:
		# print "inside x-1: " + str(x) + " " + str(y)
		find_area(x-1, y)
	if y+1 < grid_star_2_dimension:
		# print "inside y+1: " + str(x) + " " + str(y)
		find_area(x, y+1)
		# print "inside y-1: " + str(x) + " " + str(y)
	if y-1 >= 0:
		find_area(x, y-1)
	return

for i in xrange(0, len(grid_star_2)):
	print "row: " + str(i)
	for j in xrange(0, len(grid_star_2[0])):
		find_area(i,j)
		if total_area_counter > max_area_counter:
			max_area_counter = total_area_counter
			total_area_counter = 0

find_area(0,0)
print max_area_counter

print 'done'

