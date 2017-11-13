#!/bin/bash

set -e
docker run -it bpm-stf "sh"
mvn exec:java