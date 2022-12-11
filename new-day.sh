#!/usr/bin/env bash

set -euo pipefail

my_dir=$(cd "$(dirname "${0}")" && pwd)

function find_last_day {
	fd --exact-depth 1 --type d 'day\d\d' "${my_dir}" | sort | tail -1 | sd '^.+/day(\d+)/$' '$1'
}

function find_next_day {
	local last_day
	last_day=$(find_last_day)
	printf "day%02d" $((10#$last_day + 1))
}

function main {
	local next_day
	local dir
	next_day=$(find_next_day)
	dir=${my_dir}/${next_day}

	echo "generating ${dir}"
	cp -rv "${my_dir}/_template" "${dir}"
	echo 'include("'"${next_day}"'")' >>"${my_dir}"/settings.gradle.kts
}

main
