#!/usr/bin/env bash

set -euo pipefail

my_dir=$(cd "$(dirname "${0}")" && pwd)

function find_last_day {
	fd --exact-depth 1 --type d 'day\d\d' "${my_dir}" | sort | tail -1 | sd '^.+/day(\d+)/$' '$1'
}

function find_next_day {
	local last_day
	last_day=$(find_last_day)
	printf "day%02d" $((last_day + 1))
}

function main {
	local next_day
	local dir
	next_day=$(find_next_day)
	dir=${my_dir}/${next_day}

	echo "generating ${dir}"
	mkdir -p "${dir}"
	curl \
		-s \
		-d type=java-application \
		-d dsl=kotlin \
		-d archive=tgz \
		-d gradleVersion=7.6 \
		-d projectName="${next_day}" \
		-d packageName=dev.fsouza.aoc."${next_day}" \
		https://gradle-initializr.cleverapps.io/starter | tar -C "${dir}" -xzf -
	echo "success, created ${dir}"

	cat >>"${dir}"/app/build.gradle.kts <<EOF

tasks.named<JavaExec>("run") {
    standardInput = System.$(in)
}
EOF
	rm -rf "${dir}"/app/src/test
}

main
