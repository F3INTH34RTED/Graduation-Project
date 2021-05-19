from sklearn.feature_extraction.text import TfidfVectorizer
import string
from nltk.corpus import stopwords
from nltk import sent_tokenize, word_tokenize, WordNetLemmatizer
import pandas as pd


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
    # print(tfidf_results.shape)
    # print(len(get_summary(documents, tfidf_results)))
    # print(text_str)
    # print(len(text_str))
    return article_summary


def summary():
    text_file = "articleNASA.txt"
    raw_file = open(text_file, "r", encoding="utf-8")
    file_data = raw_file.readlines()
    sentences = file_data[0].split(".")
    text_str = ''' '''
    for s in file_data:
        text_str += s
    article_summary = tfidf(text_str)
    return article_summary
