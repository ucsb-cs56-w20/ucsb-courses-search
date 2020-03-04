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

# Some values for `application.properties`

| Value | Sample Value | Explanation |
|-------|-------------|---------------|
| `app.start_quarter` | `S20` | The first quarter shown in quarter selection menus.   |
| `app.end_quarter` | `F17` | The last quarter shown in quarter selection menus  |

# Note concerning `app.start_quarter` and `app.end_quarter`

The order given is the order in which the quarters appear in the menus.
If they are given in reverse order, the orders will be reversed.

For example if start is S20 and end is F17, the quarters will appear
in reverse chronological order. If start is F17 and end is S20, they will
appear in chronological order.

The oldest quarter for which data is available is `F83` 

