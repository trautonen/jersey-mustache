jersey-mustache
===============

[![Build Status](https://travis-ci.org/trautonen/jersey-mustache.png)](https://travis-ci.org/trautonen/jersey-mustache)
[![Coverage Status](https://coveralls.io/repos/trautonen/jersey-mustache/badge.png?branch=master)](https://coveralls.io/r/trautonen/jersey-mustache?branch=master)

View processor implementation for [Jersey](http://jersey.java.net/) to render
[{{ mustache }}](http://mustache.github.io/) templates.


#### Maven artifacts

Release versions of jersey-mustache are available at the [Central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.eluder.jersey%22%20AND%20a%3A%22jersey-mustache%22).
Snapshots are available at the [Sonatype OSS repository](https://oss.sonatype.org/index.html#nexus-search;gav~org.eluder.jersey~jersey-mustache~~~).

```xml
<dependency>
    <groupId>org.eluder.jersey</groupId>
    <artifactId>jersey-mustache</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```


#### Configuration

If Jersey is configured to use classpath scanning for root resources and providers, only adding the
jersey-mustache.jar to classpath is enough for enabling the provider. Mustache view processor can be also configured
with [init parameters](src/main/java/org/eluder/jersey/mustache/MustacheViewProcessor.java#L70-80) in servlet
container. When package scanning is used, `com.sun.jersey.config.property.packages` parameter must include
`org.eluder.jersey.mustache` value.

* [`mustache.file.root`](src/main/java/org/eluder/jersey/mustache/MustacheViewProcessor.java#L71)

  Defines the root folder in file system for mustache templates. If file or resource root is not defined, templates
  are loaded from the classpath root.

* [`mustache.resource.root`](src/main/java/org/eluder/jersey/mustache/MustacheViewProcessor.java#L74)

  Defines the root resource folder in classpath for mustache templates. If file or resource root is not defined,
  templates are loaded from the classpath root.

* [`mustache.factory.class`](src/main/java/org/eluder/jersey/mustache/MustacheViewProcessor.java#L77)

  Fully qualified name of a custom mustache factory implementation class. If file or resource root is defined, tries
  to initialize the factory with the corresponding constructor with `String` argument for resource root or `File`
  argument for file root, otherwise default constructor is used.

* [`mustache.template.expiry`](src/main/java/org/eluder/jersey/mustache/MustacheViewProcessor.java#L80)

  Shorthand for defining reloading mustache factory with expiry time in milliseconds. This parameter will always
  ignore `mustache.factory.class` parameter. Template root parameters are honored.


#### Examples

Examples using embedded Grizzly servlet container are available in the test sources.

* [Simple servlet example using init parameters](src/test/java/org/eluder/jersey/mustache/examples/simple)
* [Guice Jersey integration with custom Mustache view processor](src/test/java/org/eluder/jersey/mustache/examples/guice)


#### Dependencies

The only dependencies for jersey-mustache are [Jersey](http://jersey.java.net/) and
[Mustache.java](https://github.com/spullara/mustache.java).


#### Continuous integration

Travis CI builds jersey-mustache with Oracle JDK 7. All successfully built snapshots are deployed to Sonatype OSS
repository. Unit test code coverage is reported on every successfull build to Coveralls web service.


#### License

The project jersey-mustache is licensed under the MIT license.
