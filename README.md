# Excimer

[![Clojars Project](http://clojars.org/excimer/latest-version.svg)](http://clojars.org/excimer)

A Clojure library designed to combine clojure nREPL, and
[btrace](https://kenai.com/projects/btrace) into one convenient package.

This project is inspired by
[jvm-breakglass](https://github.com/matlux/jvm-breakglass). Excimer has almost
all of the functionality of jvm-breakglass. Excimer only has Spring support. The
users will have to define the nREPL server as a Spring bean. After that,
everything works as usual. Excimer also has JMX support. In addition to nREPL,
Excimer incorporates btrace into the REPL. While in a REPL session, one can load
the btrace-agent on the JVM, and then submit btrace instrumentation code to the
JVM.

## Usage
Take a quick look at [btrace-example](https://github.com/marcoy/excimer-example).

More detail instructions coming...

## License

Copyright © 2014-2015 Marco Yuen

Distributed under the GNU General Public License, version 3
