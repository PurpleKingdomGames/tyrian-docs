#!/bin/bash

# This script is used to everything basically builds correctly in a CI environment.

set -e

./mill __.compile
./mill __.fastLinkJS

cd integration-examples
bash ci.sh
cd ..