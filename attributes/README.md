<!--

    Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
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