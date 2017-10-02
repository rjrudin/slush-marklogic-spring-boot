Readme info taken from [Migrating a Roxy project to ml gradle](https://github.com/marklogic-community/ml-gradle/wiki/Migrating-a-Roxy-project-to-ml-gradle) page.

# Loading data

ml-gradle doesn't have a pre-defined task for loading data from a specific directory like Roxy does - e.g. the "data" directory and "ml local deploy content". Instead, you can configure as many data-loading tasks as you want using [MLCP (Content Pump)](https://developer.marklogic.com/products/mlcp). Please see [this guide](https://github.com/marklogic-community/ml-gradle/wiki/Content-Pump-and-Gradle) for more information. 

The reason why ml-gradle doesn't have a pre-defined task for loading data is so that ml-gradle doesn't depend on a specific version of MLCP. Instead, you can use whatever version of MLCP you need via Gradle's normal dependency management. 


Tip: Check the deployContent task in build.gradle file.
