#!/bin/bash

# This script is used to build the site in a sensible fashion.
# Purpledoc will do everything, but with no regard for what
# your system can actually manage!

set -e

if [ -z "$PURPLEDOC_JAR" ]; then
  echo "PURPLEDOC_JAR must be set" >&2
  exit 1
fi

./mill --no-server __.compile
./mill --no-server __.reformat
./mill --no-server __.compile
./mill --no-server -j1 __.fullLinkJS

java -jar $PURPLEDOC_JAR -i .