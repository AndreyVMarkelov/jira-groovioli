def result = true
def subTasks = issue.getSubTaskObjects()

subTasks.each {
    if (it.issueTypeObject.name == "Sub-task" && ! it.resolutionObject) {
        result = false
    }
}
return result
