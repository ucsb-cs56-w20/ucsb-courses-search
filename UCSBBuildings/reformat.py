# Reformat json to array of objects

import json

with open('buildings.json') as json_data:
    d = json.load(json_data)

array_of_buildings = []

for key, value in d.items():
    this_building = {
        "code" : key,
        "name" : value
    }
    array_of_buildings.append(this_building)

with open('buildings2.json', 'w') as outfile:
    json.dump(array_of_buildings, outfile, indent=2)

