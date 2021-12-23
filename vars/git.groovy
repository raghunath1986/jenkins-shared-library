def CheckoutRepoHttps(String credentialsId,
    String repoUrl,
    String branchName,
    String outputPath,
    String commitMessagePattern = '',
    String commitId = '') {
        withCredentials([usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'username',
            passwordVariable: 'password'
        )]) {
            repoUrlAuth = BuildUrlWithCredentials(repoUrl, "$username", "$password")

            sh (
                label: "clone $repoUrl to $outputPath",
                script: """
                    rm -rf $outputPath
                    mkdir -p $outputPath && cd $outputPath
                    git init
                    git remote add origin $repoUrlAuth
                    git fetch origin
                    pattern=$commitMessagePattern
                    echo "message pattern \$pattern"
                    pattern=`echo \$pattern | sed 's:\\\\\\\\:\\\\\\\\\\\\\\\\:g'`
                    echo "message pattern \$pattern"
                    if [[ -n "\$pattern" ]]; then
                        echo "entered pattern empty block"
                        commit=`git rev-list --grep=\$pattern origin/$branchName`
                        if [[ -z \$commit ]]; then
                            echo "Unable to find commit with message matching pattern!"
                            exit 1
                        fi
                    elif [[ -n "$commitId" ]]; then
                        echo "entered commitId else block"
                        commit=$commitId
                    fi

                    git checkout $branchName
                    echo "commitid is \$commit"
                    if [[ -n \$commit ]]; then
                        echo "entered if commitid block"
                        commit=`echo \$commit | cut -d' ' -f1`
                        git reset --hard \$commit
                    fi
                """
            )
        }
}

def BuildUrlWithCredentials(String url, String username, String password) {
    return url.replace("https://", "https://${username}:${password}@")
}
