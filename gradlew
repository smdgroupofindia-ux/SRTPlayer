#!/usr/bin/env sh

# Gradle wrapper script
# This script allows you to run Gradle tasks without needing to have Gradle installed on your system.

if [ -z "$GRADLE_VERSION" ]; then
  GRADLE_VERSION=7.2
fi

# Check if Gradle is already installed
if [ ! -d "gradle-"${GRADLE_VERSION} ]; then
  echo "Gradle ${GRADLE_VERSION} not found, downloading..."
  wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip
  unzip -q gradle-${GRADLE_VERSION}-bin.zip
  rm gradle-${GRADLE_VERSION}-bin.zip
fi

# Execute the Gradle task(s)
./gradle-${GRADLE_VERSION}/bin/gradle $@