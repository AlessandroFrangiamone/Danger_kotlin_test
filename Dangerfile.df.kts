//@file:Repository("https://repo.maven.apache.org/maven2/")
//@file:DependsOn("org.apache.commons:commons-text:1.6")

import systems.danger.kotlin.*

danger(args) {
    // Empty dangerFile
    //val allSourceFiles = git.modifiedFiles + git.createdFiles
    //val changelogChanged = allSourceFiles.contains("CHANGELOG.md")
    //val sourceChanges = allSourceFiles.firstOrNull { it.contains("src") }

    onGitHub {
        //val isTrivial = pullRequest.title.contains("#trivial")

        // Big PR Check
        if ((pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0) > 800) {
            warn("Big PR, try to keep changes smaller if you can")
        }

        // Small PR Check
        if ((pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0) < 20) {
            warn("Small PR, a pr is really necessary? why not to direct commit such a small changes?")
        }
        
    }
    
}
