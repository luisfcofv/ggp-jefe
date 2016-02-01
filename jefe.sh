#!/bin/bash
# This script is a slightly nicer way to run the JefeGamer app than via Gradle.
# Sample usage: ./jefe.sh 9147

./gradlew playerRunner -Pport=$1 -Pgamer=JefeGamer
