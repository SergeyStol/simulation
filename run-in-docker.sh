#!/usr/bin/env bash

IMAGE_NAME=sstol-simulation

docker build -t $IMAGE_NAME .
docker run -it $IMAGE_NAME