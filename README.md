# Ext-Interface 

External Interface and External Model projects define Rest APIs for the Reference App.  We use xsds in order to define a Contract first.  In the transport layer we favor JSON over XML but XML is possible should you need it.

We also define a wsdl contract just in case apis have to be ported to other languages or operating systems.  The semantics are Rest however.

We also like the tools available to model API contracts.

There are 2 apis that are documented within the repos that use them.

* [Federated Data Handler](https://github.com/PredixDev/fdh-router-service)
* [Run Analytic](https://github.com/PredixDev/rmd-analytics)
