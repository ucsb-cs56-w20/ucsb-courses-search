
import os
import json
import ssl
from pymongo import MongoClient

def connect():
    client = MongoClient(f"mongodb+srv://{os.environ['DB_USER']}:{os.environ['DB_PASSWORD']}@cluster0-tqzvb.mongodb.net/test?retryWrites=true&w=majority",ssl_cert_reqs=ssl.CERT_NONE)

    db = client.ucsbCourses
    return db

def main():
    print("Connecting...")
    db = connect()
    print("Connected ..")    

    for i in range(1,27):
        store_classes_from_file("json_data/results_20201_" + str(i) + ".json", db)

def store_classes_from_file(filename,db):    
    with open(filename) as json_data:
       d = json.load(json_data)

    for c in d['classes']:
        store_class_in_db(c,db)

def store_class_in_db(c,db):
    print("storing... ",c['courseId'])

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
    print(f"result= {result}, updated {c['courseId']}")
    
if __name__=="__main__":
    main()
