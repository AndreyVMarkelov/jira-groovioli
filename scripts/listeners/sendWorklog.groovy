if (eventWorklog) {
    def spent = eventWorklog.getTimeSpent()
    def author = eventWorklog.getAuthorObject().getName()
    def url = "http://localhost:3000?spent=${spent}&user=${author}"
    def resp = url.toURL().getText()
    log.info(resp)
}
