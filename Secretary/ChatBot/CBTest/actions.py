from typing import Dict, Text, Any, List

from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher

from datetime import date  # to get current date and time
import time

from summarizer import summary
from browseGoogle import google_query

import requests


class ActionContent(Action):

    def name(self) -> Text:
        return "action_content"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        # query = content_query(tracker.latest_message.get('text'))
        dispatcher.utter_message(text="/c")

        return []


class ActionFile(Action):

    def name(self) -> Text:
        return "action_file"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        # query = file_query(tracker.latest_message.get('text'))
        dispatcher.utter_message(text="/c")

        return []


class ActionFolder(Action):

    def name(self) -> Text:
        return "action_folder"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        # query = folder_query(tracker.latest_message.get('text'))
        dispatcher.utter_message(text="/c")

        return []


class ActionSummarize(Action):

    def name(self) -> Text:
        return "action_summarize"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        summarized = summary()
        dispatcher.utter_message(text="/r")

        return []


class ActionDateTime(Action):

    def name(self) -> Text:
        return "action_date_time"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        today = date.today()  # get today's date
        date_format = today.strftime("%B %d, %Y")  # set the date format

        gulf_standard_time = time.localtime()  # get current time
        time_format = time.strftime("%I: %M: %S %p", gulf_standard_time)  # set time format from 24-hr format to 12 hr

        # store everything in one string variable
        current_date_time = "Today's date is " + date_format + " and the time is " + time_format
        dispatcher.utter_message(text=current_date_time)  # print the current date time string

        return []


class ActionWeather(Action):
    def name(self) -> Text:
        return "action_weather"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        complete_api_link = "http://api.openweathermap.org/data/2.5/weather?q=Dubai&appid=2635dde77d99e63c01e82829cb754c9f"
        api_link = requests.get(complete_api_link)
        api_data = api_link.json()
        temp_city = ((api_data['main']['temp']) - 273.15)
        weather_desc = api_data['weather'][0]['description']
        humidity = api_data['main']['humidity']
        wind_speed = api_data['wind']['speed']
        temp_String = "Current temperature is: {:.2f} degress celcius".format(temp_city)
        weather_String = "Current weather desc: {}".format(weather_desc)
        final_weather_String = temp_String + "\n" + weather_String
        dispatcher.utter_message(text=final_weather_String)

        return []


class ActionGoogle(Action):

    def name(self) -> Text:
        return "action_google"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        query = google_query(tracker.latest_message.get('text'))
        dispatcher.utter_message(text=query + "/g")

        return []

# REFERENCES
# 1. Get current date: https://www.programiz.com/python-programming/datetime/current-datetime
# 2. Get user input to actions: https://forum.rasa.com/t/how-to-input-free-text-with-custom-actions-slots/9304
# 3. Searching google: https://www.edureka.co/community/57755/google-search-using-python
# 4. Chat bot Tutorial: https://www.youtube.com/watch?v=RpWeNzfSUHw&list=PLqnslRFeH2UrFW4AUgn-eY37qOAWQpJyg
# 5. Get weather: https://www.youtube.com/watch?v=w-V1pMrGAjc
# 6. Integrating AS and Rasa Chat bot: https://medium.com/@dishant_gandhi/rasa-chatbot-android-covid-app-tutorial-part-1-1010f667c06c
# Use command to run on android studio: rasa run -m models --enable-api --endpoints endpoints.yml
# rasa run actions
# rasa run -m models --enable-api --endpoints endpoints.yml
# open ngrok
# type ngrok http 5005
