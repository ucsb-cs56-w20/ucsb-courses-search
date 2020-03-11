#!/usr/bin/env bash

quarter=20202
pageSize=100

rm -rf json_data
mkdir -p json_data

# Note: you may need to adjust the number 26 by trial and error
# until we make this script a little less ad-hoc :-)

for i in {1..26}
do
    curl -X GET "https://api.ucsb.edu/academics/curriculums/v1/classes/search?quarter=${quarter}&pageNumber=${i}&pageSize=${pageSize}&includeClassSections=true" -H "accept: application/json" -H "ucsb-api-version: 1.0" -H "ucsb-api-key: ${UCSB_API_KEY}" > json_data/results_${i}.json

done
