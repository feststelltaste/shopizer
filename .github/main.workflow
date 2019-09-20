workflow "New workflow" {
  on = "push"
  resolves = ["GitHub Action for Google Cloud"]
}

action "GitHub Action for Maven" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "package"
}

action "GitHub Action for Google Cloud SDK auth" {
  uses = "actions/gcloud/auth@6a43f01e0e930f639b90eec0670e88ba3ec4aba3"
  needs = ["GitHub Action for Maven"]
  secrets = ["GCLOUD_AUTH"]
}

action "GitHub Action for Google Cloud" {
  uses = "actions/gcloud/cli@6a43f01e0e930f639b90eec0670e88ba3ec4aba3"
  needs = ["GitHub Action for Google Cloud SDK auth"]
  args = "cloud app deploy"
}
