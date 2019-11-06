# ucsb-courses-search

A project to:
* first build a clone of the page <https://my.sa.ucsb.edu/public/curriculum/coursesearch.aspx>
* then build many additional features using the same data.

# You need an API Key to run this app

If you are a student in CS56, you can get an API key from your mentor.

Mentors, can get API keys by visiting <https://developer.ucsb.edu/>.

Once you have the API key, you need to do this:

* Copy `localhost.json.SAMPLE` to `localhost.json`.
* Edit `localhost.json` and put in the correct value of the key.
* Each time you fire up a terminal window to run the app, you need to do
  ```
  source env.sh
  ```

  That will define `SPRING_APPLICATION_JSON` to have the extra values defined in `localhost.json`

* Then run `mvn spring-boot:run`
   

# Using Maven

| Type this | to get this result |
|-----------|------------|
| `mvn package` | to make a jar file|
| `mvn spring-boot:run` | to run the web app|
