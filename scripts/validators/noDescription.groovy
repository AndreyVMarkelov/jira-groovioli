// Check if issue description is set
if (!issue.description) {
    throw new com.opensymphony.workflow.InvalidInputException("Description should be set!")
}