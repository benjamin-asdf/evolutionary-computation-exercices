#!/bin/bash
set -e

# Build the notebooks first
echo "Building notebooks..."
clojure -M build.clj

echo "deploy to github"

# Add all files and commit
git add -- docs/*
git commit -m "Deploy $(date +%Y-%m-%d_%H:%M:%S)" || echo "Nothing to commit"

# Push to main branch
git push origin main

echo "Deployed to GitHub Pages!"
