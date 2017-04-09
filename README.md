# phobos

Generate a photoblog from Flickr.

## Setup

Copy `secrets.fish.sample` to `secrets.fish` and add the Flickr API key.

## Usage

    ./phobos path/to/mustache/template > path/to/output.html

A sample template is included in `template.sample`.

## Dependencies

- curl
- jq
- mustache
- fish
