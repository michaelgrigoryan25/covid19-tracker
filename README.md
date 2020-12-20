<h1 align="center">
  Covid-19 Tracker
</h1>

## Getting Started
- Clone the repository
```shell
git clone https://github.com/MichaelGrigoryan25/Covid19-Tracker.git
```
- Open the project folder in Android Studio
- Create a RapidAPI account at https://rapidapi.com/signup then go to https://rapidapi.com/api-sports/api/covid-193 and get your credentials
- After getting your credentials find the find the `COVID.kt` file at `app/src/main/java/com/michaelgrigoryan/covidtracker` and put it your credentials in the Headers as described in the example below
```kotlin
@Headers(
        "x-rapidapi-key: YOUR_KEY",
        "x-rapidapi-host: covid-193.p.rapidapi.com",
        "useQueryString: true"
    )

@Headers(
    "x-rapidapi-key: YOUR_KEY",
    "x-rapidapi-host: covid-193.p.rapidapi.com",
    "useQueryString: true"
)
```
- Now you can click on the green arrow(▶) in the top right corner of your screen ( I assume that you have some kind of AVD or device attached to your computer ) and click it.

That's it! You have successfully configured and installed the Covid-19 Tracker!
If you liked the project, please give a ⭐ to it!
