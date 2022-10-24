//@file:Repository("https://repo.maven.apache.org/maven2/")
//@file:DependsOn("org.apache.commons:commons-text:1.6")

//export DANGER_GITHUB_API_TOKEN="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"

import systems.danger.kotlin.*
import systems.danger.kotlin.models.danger.Utils
import systems.danger.kotlin.models.github.GitHubCommit
import systems.danger.kotlin.models.github.GitHubPR

//PARAMETERS
val SOGLIA_MAX_COMMIT: Int = 20
val SOGLIA_MAX_CHANGES: Int = 400
val RIFERIMENTO_ATTIVITA: String = "Ref #"

danger(args) {
    val allSourceFiles = git.modifiedFiles + git.createdFiles
    //val changelogChanged = allSourceFiles.contains("CHANGELOG.md")
    //val sourceChanges = allSourceFiles.firstOrNull { it.contains("src") }

    //Le regole all'interno di "onGitHub" vengono eseguite solo sulle PR di github
    onGitHub {

        DangerRules().bigPr(pullRequest)

        DangerRules().tooMuchCommit(pullRequest)

        DangerRules().noAsegnee(pullRequest)

        DangerRules().noDescription(pullRequest)

        DangerRules().checkSpecificActivity(github.commits)

        DangerRules().nullPointerSafeCheck(git.modifiedFiles, github.commits, utils, git.changedLines)

        DangerRules().hardcodedStringCheck(git.modifiedFiles, github.commits, utils, git.changedLines)

    }
    
}


class DangerRules{

    // REGOLA 1: PR molto grande
    fun bigPr(pullRequest: GitHubPR){
        if ((pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0) > SOGLIA_MAX_CHANGES) {
            warn("PR molto grande, consiglio di dividerla in più per meno impattanti per il codice")
        }
    }

    // REGOLA 2: PR ha più commit della soglia massima
    fun tooMuchCommit(pullRequest: GitHubPR){
        if (pullRequest.commitCount ?: 0 > SOGLIA_MAX_COMMIT) {
            warn("Nella PR sono presenti più commit della soglia massima:\nCommit Presenti > ${pullRequest.commitCount}\nSoglia Massima -> $SOGLIA_MAX_COMMIT")
            //warn((pullRequest).toString() ?: "")
        }
    }

    // REGOLA 3: Asegnatario non inserito
    fun noAsegnee(pullRequest: GitHubPR){
        if(pullRequest.assignee == null){
            warn("Non hai indicato l'assegnatario alla PR")
        }
    }

    // REGOLA 4: Descrizione non inserito
    fun noDescription(pullRequest: GitHubPR){
        if(pullRequest.body.isNullOrEmpty()){
            warn("Non hai indicato la descrizione alla PR")
        }
    }

    // REGOLA 5: Riferimento all'attività non presente
    fun checkSpecificActivity(commits: List<GitHubCommit>) {
        for (commit in commits) {
            if (commit.commit.message.contains(RIFERIMENTO_ATTIVITA)) {
                warn("Nel commit ${commit.sha} non è inserito il riferimento all'attività ($RIFERIMENTO_ATTIVITA)")
            }
        }
    }

    // REGOLA 6: Null pointer Exception safe check
    fun nullPointerSafeCheck(files: List<String>, commits: List<GitHubCommit>, utils: Utils, changedLines: PullRequestChangedLines){
        //Versione Locale
        for(file in files){
            if(utils.readFile(file).contains("!!"))
                warn("Nel file $file in uno o più punti c'è un rischio di null pointer exception dovuto all'operatore '!!', rimuoverlo.")
        }
        //Versione PR con diff
        for(commit in commits){
            changedLines.diff?.let{
                if(it.contains("!!")) {
                    warn("Nella PR in uno o più punti c'è un rischio di null pointer exception dovuto all'operatore '!!', rimuoverlo.")
                    return
                }
            }
        }
    }

    // REGOLA 7: Stringhe schiantate check
    fun hardcodedStringCheck(files: List<String>, commits: List<GitHubCommit>, utils: Utils, changedLines: PullRequestChangedLines){
        //Versione Locale
        for(file in files){
            if(utils.readFile(file).contains("\""))
                warn("Nel file $file è presente una o più stringhe schiantate, utilizza il file xml strings per inserire nuove stringhe")
        }
        //Versione PR con diff
        for(commit in commits){
            changedLines.diff?.let{
                if(it.contains("\"")) {
                    warn("Nella PR è presente una o più stringhe schiantate, utilizza il file xml strings per inserire nuove stringhe")
                    return
                }
            }
        }
    }

}
