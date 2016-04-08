# persistent-actor-messages
Akka actors which use persistent messages stored in elasticsearch to communicate

# test
## embedded
for embedded testing, use -Xmx2g
sbt -J-Xmx2g test

## remote
for remote testing start an elasticsearch instance with default ports

