def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
  pipeline {
    agent any
    stages {
      stage('Prepare Environment'){
        steps {
          cleanWs()
          script {
                 // define some variables
                /* githubUrlParts = GITREPO_URL_HTTPS =~ /(?<baseUrl>.*)\/(?<repoOwner>.*)\/(?<repository>.*)/
                 env.GITREPO_BASEURL = githubUrlParts[0][1]
                 env.GITREPO_OWNER = githubUrlParts[0][2]
                 env.GITREPO_NAME = githubUrlParts[0][3].replace('.git','')
                 
                 
                 if (!env.GITREPO_BRANCH) {
                     env.GITREPO_BRANCH = "master"
                 }
                 git.CheckoutRepoHttps(
                    'svc-githubjira',
                     GITREPO_URL_HTTPS,
                     GITREPO_BRANCH,
                     GITREPO_NAME
                   )*/
              echo "BaseURL entered is: $GITREPO_URL_HTTPS"
              
       }
      }
    }
  }
}
}
