import com.opensymphony.workflow.InvalidInputException

if (issue.resolutionObject && issue.resolutionObject.name == "Done" && ! issue.fixVersions) {
    throw new InvalidInputException("fixVersions", "Please specify Fix Version/s")
}