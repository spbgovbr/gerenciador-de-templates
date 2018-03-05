package br.gov.ans.exceptions;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.jboss.logging.Logger;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
		
	private ExceptionHandler wrapped;
	
	private static final Logger log = Logger.getLogger(CustomExceptionHandler.class);

	public CustomExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void handle() throws FacesException {
		Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			Throwable throwable = context.getException();
			
			FacesContext fc = FacesContext.getCurrentInstance();

			try {
				log.error(throwable, throwable);
				
				Flash flash = fc.getExternalContext().getFlash();

				flash.put("errorDetails", throwable.getMessage());
				
				NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();

				navigationHandler.handleNavigation(fc, null,"/erros/error?faces-redirect=true");

				fc.renderResponse();
				
			} finally {
				iterator.remove();
			}
		}

		// Let the parent handle the rest
		getWrapped().handle();
	}

}
