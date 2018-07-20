import com.atlassian.jira.issue.link.IssueLink

def linkType = ["Blocks"]
for (IssueLink link in issueLinkManager.getInwardLinks(issue.id)) {
    if (linkType.contains(link.issueLinkType.name) && !link.sourceObject.resolutionId) {
        return false
    }
}
return true
