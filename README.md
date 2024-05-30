# chemistry-opencmis

This is a fork of apache/chemistry-opencmis which aims to provide a Jakarta compatible version of the library.

Original readme can be found [there](README.txt).

You can download this library from our artifactory.

Direct link is https://packages.nuxeo.com/service/rest/repository/browse/maven-public/org/apache/chemistry/opencmis/.

Or with Maven:

```xml
<dependency>
  <groupId>org.apache.chemistry.opencmis</groupId>
  <artifactId>chemistry-opencmis-commons-api</artifactId>
  <version>VERSION</version>
</dependency>

<repositories>
  <repository>
    <id>nuxeo-public</id>
    <url>https://packages.nuxeo.com/repository/maven-public/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <updatePolicy>always</updatePolicy>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

## Deploy the artefacts to another location

To deploy artefacts to our repository you can use the option `altDeploymentRepository` to specify the target repository:

```
mvn clean source:jar javadoc:jar deploy -DaltDeploymentRepository=maven-vendor::default::VENDOR_URL
```

Your Maven `settings.xml` file should contain appropriate authentication (if any) for the `maven-vendor` repository. 

