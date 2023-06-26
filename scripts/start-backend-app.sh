#!/bin/bash

cd ../

echo "Starting backend part"
gradle :decision-engine:clean :decision-engine:build
java -jar ./decision-engine/build/libs/decision-engine-1.0.0.jar