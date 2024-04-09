function main() {
  installBrew
  installMaven
  installPostgres
}

function installBrew() {
  which -s brew
  if [[ $? != 0 ]] ; then
    # Install Homebrew
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  else
    echo "Updating Homebrew"
    brew update
  fi
}

function installMaven() {
  which -s mvn
  if [[ $? != 0 ]] ; then
    brew install mvn
    echo "Installed maven"
  fi
  echo "Maven already installed"
}

function installPostgres() {
  which -s postgres
  if [[ $? != 0 ]] ; then
    brew install postgres
    echo "Installed postgres"
  fi
  echo "Postgres already installed"
}

main