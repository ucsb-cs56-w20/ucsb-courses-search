
import os
import json
import ssl
from pymongo import MongoClient

import requests
import math

pageSize = 100

    
headers = {
    "accept": "application/json",
    "ucsb-api-version" : "1.0",
    "ucsb-api-key" : os.environ['UCSB_API_KEY'] 
}

def getCourseData(quarter="20202",pageNumber=1):

    url = f"https://api.ucsb.edu/academics/curriculums/v1/classes/search?quarter={quarter}&pageNumber={pageNumber}&pageSize={pageSize}&includeClassSections=true" 
      
    # > json_data/results_${i}.json

    r = requests.get(url, headers=headers)
    if r.status_code != 200:
        raise Exception("Bad status "+r.status_code)
    return r.text
    

def getNumberPages(quarter="20202"):
    url = f"https://api.ucsb.edu/academics/curriculums/v1/classes/search?quarter={quarter}&pageNumber=1&pageSize={pageSize}&includeClassSections=true" 

    # > json_data/results_${i}.json

    r = requests.get(url, headers=headers)
    if r.status_code != 200:
        raise Exception("Bad status "+r.status_code)
    return math.ceil(r.json()['total']/pageSize)

def getCourseDataForQuarter(quarter="20202"):
    numberPages = getNumberPages(quarter)

    for i in range(1,numberPages+1):
        print(f"getting data for page {i} of {numberPages} for quarter {quarter}")
        text = getCourseData(quarter,pageNumber=i)
        out_filename = f"json_data/results_{quarter}_{i}.json"
        with open(out_filename, 'w') as out_file:
            out_file.write(text)
        print(f"data for page {i} for quarter {quarter} written to {out_filename}")
    
if __name__=="__main__":
    getCourseDataForQuarter("20201")
