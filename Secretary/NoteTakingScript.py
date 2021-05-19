import importlib
import urllib
from sklearn.feature_extraction.text import TfidfVectorizer
import string
from nltk.corpus import stopwords
from nltk import sent_tokenize, word_tokenize, WordNetLemmatizer
import pandas as pd
import pyrebase
from warnings import simplefilter
import inputs
#import the module here, so that it can be reloaded.

simplefilter(action='ignore', category=FutureWarning)

config = {
    "apiKey": "AIzaSyB70srkaYwGkf8cI23JcnuhVG2KVhPGk4s",
    "authDomain": "virtual-secretary-fec6f.firebaseapp.com",
    "databaseURL": "https://virtual-secretary-fec6f.firebaseio.com",
    "projectId": "virtual-secretary-fec6f",
    "storageBucket": "virtual-secretary-fec6f.appspot.com",
    "messagingSenderId": "118347623643",
    "appId": "1:118347623643:web:f74b35f786bd3ebc30b0fe",
    "measurementId": "G-NPW2T0EWJ1",
    "serviceAccount": "/Users/faizarahman/Desktop/virtual-secretary.json"
}

firebase = pyrebase.initialize_app(config)
storage = firebase.storage()
storage2 = firebase.storage()
storage3 = firebase.storage()

url = storage.child("Audio Name").get_url(None)
print(url)
text_file = urllib.request.urlopen(url).read()
print(type(text_file))

name_list = storage.list_files()
# folder_name = "Transcripts/ "
for file in name_list:
    # print(file)
    try:
        unicode_text = text_file.decode("utf-8")
        transcript_name = file.name.replace(" ", "")
        # print(transcript_name)
        # print(unicode_text)
        if transcript_name == unicode_text:
            # driver = webdriver.Safari()
            # location = driver.get("https://console.firebase.google.com/u/1/project/virtual-secretary-fec6f/storage/virtual-secretary-fec6f.appspot.com/files")
            # location.refresh()
            text_file1 = storage.child(unicode_text).get_url(None)
            importlib.reload(inputs)
            # txt =text_file1.each()
            print(text_file1)
            text_file2 = urllib.request.urlopen(text_file1).read()
            print(type(text_file2))
            unicode_text1 = text_file2.decode("utf-8")
            # open and read the file with the name unicode text
            print(unicode_text1)

        # z=storage.child(file.name).get_url('/')
        # print(z.name)
    except:
        print('Download Failed')


# print(storage.child(name_list.name).get_url(None))
# print(name_list)
# for file in name_list:
#    if text_file == file:


def lemmatizing(tokenized_text):
    wn = WordNetLemmatizer()
    text = [wn.lemmatize(word) for word in tokenized_text]
    return text


def preprocess(text):
    stop_words = set(stopwords.words("english"))

    text = text.lower()
    tokens = word_tokenize(text)
    tokens = [word for word in tokens if word not in stop_words]
    tokens = [i for i in tokens if i not in string.punctuation]
    lemmatize = lemmatizing(tokens)
    return lemmatize


def get_average(values):
    greater_than_zero_count = total = 0
    for value in values:
        if value != 0:
            greater_than_zero_count += 1
            total += value
    return total / greater_than_zero_count


def get_threshold(tfidf_results):
    i = total = 0
    while i < (tfidf_results.shape[0]):
        total += get_average(tfidf_results[i, :].toarray()[0])
        i += 1
    return total / tfidf_results.shape[0]


def get_summary(documents, tfidf_results):
    summary = ""
    i = 0
    while i < (tfidf_results.shape[0]):
        if (get_average(tfidf_results[i, :].toarray()[0])) >= get_threshold(tfidf_results) * 0.95:
            summary += "\n" + "\n" + '\u2022' + documents[i]
        i += 1
    return summary


def tfidf(text_str):
    # preprocess(text_str)
    documents = sent_tokenize(text_str)
    v = TfidfVectorizer(preprocess(text_str))
    tfidf_results = v.fit_transform(documents)
    df1 = pd.DataFrame(tfidf_results.toarray(), columns=v.get_feature_names())
    df2 = tfidf_results.astype(str)
    # print(df1)
    print(get_summary(documents, tfidf_results))
    article_summary = get_summary(documents, tfidf_results)
    return article_summary


def summary():
    output = unicode_text1
    text_str = ''' '''
    for s in output:
        text_str += s
    article_summary = tfidf(text_str)
    return article_summary


summary = summary()

file1 = open("MyFile.txt", "w+")
file1.write(summary)
file1.read()
file1.close()
with open('MyFile.txt', 'r') as f2:
    data = f2.read()

fixresult = "/Users/faizarahman/Desktop/MyFile.txt"
cloudfilename = "Summarized Text"
storage.child(cloudfilename).put(fixresult)
print(storage.child(cloudfilename).get_url(None))
