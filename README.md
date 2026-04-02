# Setup

```bash
mkdir ~/Projects
cd ~/Projects
git clone git@github.com:ws-9/e-commerce-team-26.git

curl https://mise.run/bash | sh
source ~/.bashrc

cd ~/Projects/e-commerce-team-26/
mise trust ~/Projects/e-commerce-team-26/
mise install
```

# Working with the project

Make sure to be in the project directory (./e-commerce-team-26).

```bash
# Run this to initialize frontend autocompletion
just init

# Running the project
just up

# Cleaning the project
just clean
```
