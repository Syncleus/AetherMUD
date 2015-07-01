#!/bin/bash

for file in $(ls *.json)
do
	echo "sed 's/^.*spawnIntervalTick.*$/      \"spawnIntervalTicks\": 600,/g' ${file} | tee ${file}"
done
