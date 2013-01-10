NEXUS-5045
==========

Example plugin for "Exposing attributes".

Example session, adding "foo=bar" key-values to an item:

```
[cstamas@marvin tmp]$ curl -H "Accept:application/xml" http://localhost:8081/nexus/service/local/repositories/central/attributes/log4j/log4j/1.2.13/log4j-1.2.13.pom<attributes>
  <attributes>
    <attribute>
      <key>storageItem-remoteUrl</key>
      <value>http://repo1.maven.org/maven2/log4j/log4j/1.2.13/log4j-1.2.13.pom</value>
    </attribute>
    <attribute>
      <key>digest.sha1</key>
      <value>e5c244520e897865709c730433f8b0c44ef271f1</value>
    </attribute>
    …
  </attributes>
</attributes>

[cstamas@marvin tmp]$ curl -H "Accept:application/xml" -H "Content-Type:application/xml" -X PUT --data-binary "<attributes><attributes><attribute><key>foo</key><value>bar</value></attribute></attributes></attributes>" http://localhost:8081/nexus/service/local/repositories/central/attributes/log4j/log4j/1.2.13/log4j-1.2.13.pom
<attributes>
  <attributes>
    <attribute>
      <key>storageItem-remoteUrl</key>
      <value>http://repo1.maven.org/maven2/log4j/log4j/1.2.13/log4j-1.2.13.pom</value>
    </attribute>
    …
  </attributes>
</attributes>

[cstamas@marvin tmp]$ curl -H "Accept:application/xml" http://localhost:8081/nexus/service/local/repositories/central/attributes/log4j/log4j/1.2.13/log4j-1.2.13.pom<attributes>
  <attributes>
    <attribute>
      <key>storageItem-remoteUrl</key>
      <value>http://repo1.maven.org/maven2/log4j/log4j/1.2.13/log4j-1.2.13.pom</value>
    </attribute>
    <attribute>
      <key>foo</key>
      <value>bar</value>
    </attribute>
    <attribute>
      <key>digest.sha1</key>
      <value>e5c244520e897865709c730433f8b0c44ef271f1</value>
    </attribute>
    …
  </attributes>
</attributes>

```