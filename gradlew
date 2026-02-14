#!/usr/bin/env sh

# Gradle Wrapper script for UNIX-based systems

# Prevent running in an unsupported terminal
if [ -z "$TERM_PROGRAM" ]; then
  echo "Unsupported terminal. Please run this script in a supported terminal." >&2
  exit 1
fi

# Configuration of script
GRADLE_VERSION=6.8.3
GRADLE_HOME=\"$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin/\"

# Ensure that Gradle is installed
if [ ! -d "$GRADLE_HOME" ]; then
  echo "Gradle ${GRADLE_VERSION} not yet installed. Installing..."
  curl -sSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip
  unzip gradle.zip -d "$HOME/.gradle/wrapper/dists/"
  rm gradle.zip
fi

# Execute Gradle
exec "${GRADLE_HOME}/bin/gradle" "$@"