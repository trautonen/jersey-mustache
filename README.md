jersey-mustache
===============

View processor implementation for [Jersey](http://jersey.java.net/) to render
[{{ mustache }}](http://mustache.github.io/) templates.


#### Maven artifacts

Release versions of jersey-mustache are available at the [Central repository](http://search.maven.org/). Snapshots are
available at the [Sonatype OSS repository](https://oss.sonatype.org/).

```xml
<dependency>
    <groupId>org.eluder.jersey</groupId>
    <artifactId>jersey-mustache</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```


#### Dependencies

The only dependencies for jersey-mustache are [Jersey](http://jersey.java.net/) and
[Mustache.java](https://github.com/spullara/mustache.java).


#### Continuous integration

Travis CI builds jersey-mustache with Oracle JDK 7. All successfully built snapshots are deployed to Sonatype OSS
repository.

[![Build Status](https://travis-ci.org/trautonen/jersey-mustache.png)](https://travis-ci.org/trautonen/jersey-mustache)


#### License

The project jersey-mustache is licensed under the MIT license.
