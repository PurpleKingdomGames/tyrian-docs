#!/usr/bin/env bash

set -e

sbt buildExamples testAll

cd server-examples
sbt buildAll
cd ..
