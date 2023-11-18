#!/usr/bin/env just --justfile

# renovate: datasource=docker depName=ghcr.io/super-linter/super-linter
super_linter_version := "slim-v5.7.0@sha256:7f6539f601a5915b10a079d2c1d78ed3143ece252afda0411685bc108e160d73" # editorconfig-checker-disable-line

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
