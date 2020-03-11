
import os
import json
import ssl
from pymongo import MongoClient

import requests
import math

def connect():
    client = MongoClient(f"mongodb+srv://{os.environ['DB_USER']}:{os.environ['DB_PASSWORD']}@cluster0-tqzvb.mongodb.net/test?retryWrites=true&w=majority",ssl_cert_reqs=ssl.CERT_NONE)

    db = client.ucsbCourses
    return db

def store_classes_from_text(text,db,pageNumber,numberPages):    
    d = json.loads(text)
    assert pageNumber==d['pageNumber']
    print(f"storing classes from pagenumber={pageNumber} of {numberPages}")
    for i,c in enumerate(d['classes']):
        store_class_in_db(c,db,i,len(d['classes']),pageNumber,numberPages)

def store_class_in_db(c,db,i,numClasses,pageNumber,numberPages):
    print(f"storing {c['courseId']}, quarter {c['quarter']} (course {i} of {numClasses}, page {pageNumber} of {numberPages})")

    # We think this makes the updates idempotent, i.e. no duplicates
    # That only works, though, if you have identified a set of attributes
    # that uniquely identify the document in the collection
    # In our cases that will be the quarter,courseId
    
    filter = {
        "courseId" : c['courseId'],
        "quarter" : c['quarter']
    }
    update = {
        "$set" : c
    }
    result = db.courses.update_one(filter,update,upsert=True)
    print(f"updated {c['courseId']} matched={result.matched_count}, modified={result.modified_count} upsert={result.upserted_id}")
    
pageSize = 100

headers = {
    "accept": "application/json",
    "ucsb-api-version" : "1.0",
    "ucsb-api-key" : os.environ['UCSB_API_KEY'] 
}

def grabCourseData(quarter,pageNumber,numberPages):

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

def grabAndUpdateCourseDataForQuarter(db,quarter="20202"):
    numberPages = getNumberPages(quarter)

    for i in range(1,numberPages+1):
        print(f"getting data for page {i} of {numberPages} for quarter {quarter}")
        text = grabCourseData(quarter,pageNumber=i,numberPages=numberPages)
        store_classes_from_text(text,db,i,numberPages)  

    
if __name__=="__main__":

    print("Connecting...")
    db = connect()
    print("Connected ..")    

    grabAndUpdateCourseDataForQuarter(db,"20202")
    
    for year in range(2018,2016,-1):
        for quarter in range(4,0,-1):
            ay = str(year) + str(quarter)
            print(ay)
            grabAndUpdateCourseDataForQuarter(db,ay)
     


    

    
