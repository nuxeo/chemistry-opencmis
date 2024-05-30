# chemistry-opencmis

This is a fork of [apache/chemistry-opencmis](https://github.com/apache/chemistry-opencmis) which aims to provide a Jakarta compatible version of the library.

Original readme can be found [there](README.txt).

You can download this library from our artifactory.

Direct link is https://packages.nuxeo.com/service/rest/repository/browse/maven-public/org/nuxeo/chemistry/opencmis/.

Or with Maven:

```xml
<project>
  ...
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.nuxeo.chemistry.opencmis</groupId>
        <artifactId>chemistry-opencmis</artifactId>
        <version>VERSION</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  
  ...
  
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
  ...
</project>
```

## Release the projet

Make sure the project builds and its tests pass.

Then create a temporary branch to perform the release:

```bash
git checkout -b tmp-release
```

Then update the project version to final, for instance 2.0.0:

```bash
mvn versions:set -DnewVersion=2.0.0 -DgenerateBackupPoms=false
```

Then commit and tag the release:

```bash
git commit -a -m "Release 2.0.0"
git tag -a -m "Release 2.0.0" chemistry-opencmis-2.0.0
```

Then deploy the maven artefacts:

```bash
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=maven-vendor::default::VENDOR_URL
```

> [!IMPORTANT]
> You should replace the `VENDOR_URL`.
> Your Maven `settings.xml` file should contain appropriate authentication (if any) for the `maven-vendor` repository.

Then push the tag:

```bash
git push --tags
```

Then cleanup your branch and prepare the next development iteration:

```bash
git checkout main
git branch -D tmp-release
mvn versions:set -DnewVersion=2.0.1-SNAPSHOT -DgenerateBackupPoms=false
git commit -a -m "Post release 2.0.0"
git push
```
