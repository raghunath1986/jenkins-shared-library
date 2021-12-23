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
                    pattern=`echo \$pattern | sed 's:\\\\\\\\:\\\\\\\\\\\\\\\\:g'`

                    if [[ -n "\$pattern" ]]; then
                        commit=`git rev-list --grep=\$pattern origin/$branchName`
                        if [[ -z \$commit ]]; then
                            echo "Unable to find commit with message matching pattern!"
                            exit 1
                        fi
                    elif [[ -n "$commitId" ]]; then
                        commit=$commitId
                    fi

                    git checkout $branchName

                    if [[ -n \$commit ]]; then
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
