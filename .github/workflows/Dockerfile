name: Build and Push Backend Docker Image

on:
  push:
    branches:
      - main
  workflow_dispatch:

jobs:
  build-and-push:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository (this repo)
        uses: actions/checkout@v4

      - name: Log in to Docker Hub
        uses: docker/login-action@v3
        with:
          username: aala09
          password: 22630055Aala

      - name: Build and push backend Docker image
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: aala09/backend:latest