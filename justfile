#!/usr/bin/env just --justfile

# renovate: datasource=docker depName=ghcr.io/super-linter/super-linter
super_linter_version := "slim-v5.5.0@sha256:cc518362f52b981ceccf43badd51e7a96d4e71aef57040b12e1a132d87e07a5e" # editorconfig-checker-disable-line

@_default:
  just --list

# Install tools and dependencies
install: install-tools install-dependencies

# Install tools
install-tools:
  asdf install

# Install dependencies
install-dependencies:
  ./gradlew dependencies

# Lint project
lint *extra_args:
  docker run \
  --env RUN_LOCAL=true \
  --env DEFAULT_BRANCH=main \
  --env IGNORE_GENERATED_FILES=true \
  --env IGNORE_GITIGNORED_FILES=true \
  --env YAML_ERROR_ON_WARNING=true \
  --env FILTER_REGEX_EXCLUDE="(.*gradlew.*|.*day-.+-input-.+\.txt)" \
  --volume {{justfile_directory()}}:/tmp/lint \
  {{extra_args}} \
  ghcr.io/super-linter/super-linter:{{super_linter_version}}

# Run the tests
test:
  ./gradlew test

# Clean generated files
clean:
  ./gradlew clean
