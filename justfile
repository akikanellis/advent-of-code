#!/usr/bin/env just --justfile

# renovate: datasource=docker depName=github/super-linter
super_linter_version := "slim-v5.0.0@sha256:25586f44c22b01c465fe00f939fd4d9c5222c71f647dee5f9d07413690067d3e" # editorconfig-checker-disable-line

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
  github/super-linter:{{super_linter_version}}

# Run the tests
test:
  ./gradlew test

# Clean generated files
clean:
  ./gradlew clean
