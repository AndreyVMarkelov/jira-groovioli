package ru.andreymarkelov.atlas.plugins.jira.groovioli.util;

public class ScriptDslException extends RuntimeException {
    public ScriptDslException() {
    }

    public ScriptDslException(String message) {
        super(message);
    }

    public ScriptDslException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptDslException(Throwable cause) {
        super(cause);
    }

    public ScriptDslException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
