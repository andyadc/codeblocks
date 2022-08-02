### v2.7.3

⭐ New Features

+ Auto-configure Spring Data's new ReactiveElasticsearchClient in place of the old one #31755
+ Add auto-configuration for JdkClientHttpConnector #31709

🐞 Bug fixes:

+ Publishing a docker image to a private registry fails without authentication #31825
+ In a non-reactive application, health indicators in a parent context are not found #31819

📔 Documentation

+ Clarify how docker image publishing registry is determined #31826
+ Fix typo in "HTTP and WebSocket" section of GraphQL documentation #31518

🔨 Dependency Upgrades

* Upgrade to AppEngine SDK 1.9.98 #31790

+ Upgrade to Byte Buddy 1.12.12 #31735

❤️ Contributors

We'd like to thank all the contributors who worked on this release!

+ @andyadc
