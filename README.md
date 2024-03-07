Too optimistic locking - BoilingFrogs 2024 example app
------------------------------------------------------

### Branches in this repo
* `master` - simple "typical" Spring Boot REST
* `pessimistic` - how would it be with pessimistic locking
* `http_versioning` - how can we leverage Etag/If-Match/If-None-Match headers

### References
* [RFC 7232 Hypertext Transfer Protocol (HTTP/1.1): Conditional Requests](https://datatracker.ietf.org/doc/html/rfc7232)
* Mozilla
  * [428 Precondition required](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/428)
  * [412 Precondition failed](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/412)