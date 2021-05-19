def google_query(site):  # parse google text
    site = site.replace('google ', '')
    site = site.replace('google', '')
    site = site.replace('browse ', '')
    site = site.replace('for ', '')
    site = site.replace('search ', '')
    site = site.replace('on ', '')
    site = site.replace('find ', '')
    query = ""
    if not site:  # if the string is empty
        query = "There was nothing to search for on google."
    else:
        query = site

    return query
