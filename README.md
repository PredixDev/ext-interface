<a href="http://predixdev.github.io/ext-interface" target="_blank">
	<img height="50px" width="100px" src="images/pages.jpg" alt="view github pages"></a>
<br>ext-interface<br>
<a href="http://predixdev.github.io/ext-interface/javadocs/ext-interface/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc">
</a>
<br>ext-mapper<br>
<a href="http://predixdev.github.io/ext-interface/javadocs/ext-mapper/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc">
</a>
<br>ext-model<br>
<a href="http://predixdev.github.io/ext-interface/javadocs/ext-model/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc">
</a>
<br>ext-util<br>
<a href="http://predixdev.github.io/ext-interface/javadocs/ext-util/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc">
</a>
	
# Ext-Interface 

External Interface and External Model projects define Rest APIs for the Reference App.  We use xsds in order to define a Contract first.  In the transport layer we favor JSON over XML but XML is possible should you need it.

We also define a wsdl contract just in case apis have to be ported to other languages or operating systems.  The semantics are Rest however.

We also like the tools available to model API contracts.

##APIs
There are 2 apis that are documented within the repos that use them.  Find the wsdl files in src/main/resources and visulize them in eclipse.

* [Federated Data Handler](https://github.com/PredixDev/fdh-router-service)
* [Run Analytic](https://github.com/PredixDev/rmd-analytics)
 
> Why a WSDL? It's mostly for the pictures. We really want the XSDs.  We use REST, we like REST, we like JSON.  We like the type-safety validation and generation tools of XSD.  We like to support other languages with the same API.  We like polymorphism.  We like enumerations.  We like contracts. We like the embedded docs.  We work with other XSD Industrial Internet based libraries.  


##Events
  * Field Changed Event - As data changes downstream systems should be notified.  The FieldChangedEvent has 
    * Field - the field that changed
    * AssetId - the id of the entity containing the Field or leading to the Field
    * EventTime - the time of the event.  
    * Map - key/value map to pass to downstream systems
    
 
[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/ext-interface/readme?pixel)](https://github.com/PredixDev)
