#!/bin/bash
set -e

# Build the notebooks first
echo "Building notebooks..."
clojure -M build.clj

# Navigate to docs directory
cd docs

# Add all files and commit
git add -A
git commit -m "Deploy $(date +%Y-%m-%d_%H:%M:%S)" || echo "Nothing to commit"

# Push to main branch
git push origin main

echo "Deployed to GitHub Pages!"
