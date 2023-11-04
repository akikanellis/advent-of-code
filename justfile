#!/usr/bin/env just --justfile

# renovate: datasource=docker depName=ghcr.io/super-linter/super-linter
super_linter_version := "slim-v5.6.1@sha256:f4258b6658ee4e2b5a7ad91ef8084191163aea8cd92b0efe5cbb6bc4fed4f7e4" # editorconfig-checker-disable-line

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
