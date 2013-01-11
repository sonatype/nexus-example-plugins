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
NEXUS-5046
==========

Select then act upon. Pluggable "selections" and then pluggable "actors" invoked against selection.

Exposed over REST API as:
```
http://localhost:8081/nexus/service/local/select/<repo_id>/<selector_id>/<actor_id>?<terms>
```

The terms is a series of query parameters, and should be prefixed with `t_`.

Example No1:
----
```
http://localhost:8081/nexus/service/local/select/central/gav/delete?t_g=log4j&t_a=log4j&t_v=1.2.13
```

Would run against repository with ID "central", perform a GAV selection and would delete the matched selection. GAV selection would happen against groupId="log4j", artifactId="log4j", version="1.2.13". Note: this operation would NOT maintain Maven metadata!

Example No2:
----
```
http://localhost:8081/nexus/service/local/select/releases/gav/tag?t_g=com.mycorp&t_a=myartifact&t_c=bundle&t_tagAttributeKey=foo&t_tagAttributeValue=bar
```

Would run against repository with ID "releases", perform a GAV selection with groupId="com.mycorp", artifactId="myartifact" and classifier="bundle" and would "tag" them (add key-value to attributes) with "foo=bar" attributes.

Selectors
----

Implemented selectors:

* GAVSelector - receives terms `g`, `a`, `v`, `bv` (base version, matters in case of snapshots as `v` would be timestamped snapshot, while this one would be "X-SNAPSHOT"), `e` (extension) and `c` (classifier). The terms values represents plain Java Regexp values, and this selector works only against Maven repositories. Creates a selection that will contain Maven artifacts matching the given terms.
* TargetSelector - receives terms `targetId`. Works against any kind of repository. Creates a selection that contains all items matching given target.
* TagSelector - receives terms `attributeKey` and (optionally) `attributeValue`. Works against any repository. Creates a selection that contains all items that has attribute with `attributeKey`, and if value given, it's value matched with given value.


Actors
----
Implemented actors:

* DeleteActor -- simply performs "deleteItem" calls against selected items. No terms.
* TagActor -- receives `tagAttributeKey` and `tagAttributeValue` (both terms are mandatory) and "tags" the items in selection.

Response
---

A rather simplistic one for now (for example No1 actually performing deletions):

```
<runReport>
  <repositoryId>central</repositoryId>
  <selectorId>gav</selectorId>
  <selectedCount>2</selectedCount>
  <actorId>delete</actorId>
  <actedCount>2</actedCount>
  <success>true</success>
</runReport>
```

Todo
---

This is just an example. More of extra work would be needed to properly finish this example (like UI for this, other selection imple etc). While this might work in production too, I believe it is not needed to mention it is not supported. Also, due to current naive implementation of "selections" (in-memory), doing this on LARGE repository instances with large selections (or not narrowing the selection to contain few hits) would lead to OOMs.