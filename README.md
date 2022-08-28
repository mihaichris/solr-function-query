# solr-function-query


## Description

Two custom Solr functions that can extract values from an array/object based on an argument given.

## Installation

First, you must package the project into a jar.

```sh
mvn package
```

And then add the jar file to the location where Solr is installed, into `dist/` directory.

## Basic Usage

```
{
    "id":"1",
    "dictField":"{\"key_1\":\"val_1\", \"key_2\": \"val_2\"}",
    "listField":"['val_1', 'val_2']"
}
```
Having `valdict(dictField,"key_2")` and `vallist(listField,0)` in the `fl` (Field List) parameter would generate a response like:
```
{
  "response":{
    "docs":[
      {
        "id":"1",
        "valdict(dictField,"key_2")":"val_2",
        "vallist(listField,0)": "val_1"
      }
    ]
  }
} 
```



