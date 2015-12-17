package it.tellnet.gradle

import com.vaadin.sass.internal.handler.SCSSErrorHandler
import org.w3c.css.sac.CSSException
import org.w3c.css.sac.CSSParseException

/**
 * @author Radu Andries
 */
class SilentErrorHandler extends SCSSErrorHandler {
    @Override
    void warning(CSSParseException exception) throws CSSException {
        //VAI TRA
    }

    @Override
    void error(CSSParseException exception) throws CSSException {
        //VAI TRA
    }

    @Override
    void fatalError(CSSParseException exception) throws CSSException {
        //VAI TRA
    }

    @Override
    void traverseError(Exception e) {
        // VAI TRA
    }

    @Override
    void traverseError(String message) {
        // VAI TRA
    }

    @Override
    boolean isErrorsDetected() {
        return false
    }
}
