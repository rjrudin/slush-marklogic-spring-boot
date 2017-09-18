See [the MarkLogic docs](http://docs.marklogic.com/REST/POST/manage/v2/privileges) for what a privilege JSON/XML file can contain.

See [more ml-config samples](https://github.com/marklogic-community/ml-gradle/tree/master/examples/sample-project/src/main/ml-config) from ml-gradle github project.

Note on how to avoid issues when deploying security assets [as noted here](https://github.com/marklogic-community/ml-app-deployer/issues/114). 

Create the privilege without any roles:

`{
  "privilege-name": "mltap-runner",
  "action": "http://github.com/jmakeig/mltap/runner",
  "kind": "execute",
  "role": [ ]
}
`

Add the privilege to the privilege array of the newly created role:

`
{
  "role-name": "mltap-tester",
  "description": "",
  "privilege": [
    {
      "action": "http://marklogic.com/xdmp/privileges/xdbc-eval",
      "kind": "execute",
      "privilege-name": "xdbc:eval"
    },
    {
      "action": "http://github.com/jmakeig/mltap/runner",
      "kind": "execute",
      "privilege-name": "mltap-runner"
    }
  ]
}
`
