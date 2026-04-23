#!/bin/sh

OS="$(uname -s 2>/dev/null)"

case "$OS" in
  Linux*|Darwin*)
    echo "Detected OS: $OS"
    mvn checkstyle:check pmd:check
    ;;
  CYGWIN*|MINGW*|MSYS*)
    echo "Detected OS: Windows (Unix shell)"
    mvn checkstyle:check pmd:check
    ;;
  *)
    echo "Unknown OS: $OS"
    echo "Attempting to run mvn anyway..."
    mvn checkstyle:check pmd:check
    ;;
esac