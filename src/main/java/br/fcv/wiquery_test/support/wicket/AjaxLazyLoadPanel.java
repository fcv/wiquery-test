package br.fcv.wiquery_test.support.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;

/**
 * A panel where you can lazy load another panel. This can be used if you have a panel/component
 * that is pretty heavy in creation and you first want to show the user the page and then replace
 * the panel when it is ready.
 * 
 * @author jcompagner
 * 
 * @since 1.3
 */
public abstract class AjaxLazyLoadPanel extends Panel
{
	private static final String LAZY_LOAD_COMPONENT_ID = "content";

	private static final long serialVersionUID = 1L;

	// state,
	// 0:add loading component
	// 1:loading component added, waiting for ajax replace
	// 2:ajax replacement completed
	private byte state = 0;

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public AjaxLazyLoadPanel(final String id)
	{
		this(id, null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 */
	public AjaxLazyLoadPanel(final String id, final IModel<?> model)
	{
		super(id, model);

		setOutputMarkupId(true);

		add(new AbstractDefaultAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String getChannelName() {			    
			    return id;
			}		

			@Override
			protected void respond(final AjaxRequestTarget target)
			{
				if (state < 2)
				{
					Component component = getLazyLoadComponent(LAZY_LOAD_COMPONENT_ID);
					AjaxLazyLoadPanel.this.replace(component);
					setState((byte)2);
				}
				target.add(AjaxLazyLoadPanel.this);

			}

			@Override
			public void renderHead(final Component component, final IHeaderResponse response)
			{
				super.renderHead(component, response);
				if (state < 2)
				{
					handleCallbackScript(response, getCallbackScript().toString());
				}
			}
		});
	}

	/**
	 * Allows subclasses to change the callback script if needed.
	 * 
	 * @param response
	 * @param callbackScript
	 */
	protected void handleCallbackScript(final IHeaderResponse response, final String callbackScript)
	{
		response.renderOnDomReadyJavaScript(callbackScript);
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		if (state == 0)
		{
			add(getLoadingComponent(LAZY_LOAD_COMPONENT_ID));
			setState((byte)1);
		}
		super.onBeforeRender();
	}

	/**
	 * 
	 * @param state
	 */
	private void setState(final byte state)
	{
		this.state = state;
		getPage().dirty();
	}

	/**
	 * 
	 * @param markupId
	 *            The components markupid.
	 * @return The component that must be lazy created. You may call setRenderBodyOnly(true) on this
	 *         component if you need the body only.
	 */
	public abstract Component getLazyLoadComponent(String markupId);

	/**
	 * @param markupId
	 *            The components markupid.
	 * @return The component to show while the real component is being created.
	 */
	public Component getLoadingComponent(final String markupId)
	{
		IRequestHandler handler = new ResourceReferenceRequestHandler(
			AbstractDefaultAjaxBehavior.INDICATOR);
		return new Label(markupId, "<img alt=\"Loading...\" src=\"" +
			RequestCycle.get().urlFor(handler) + "\"/>").setEscapeModelStrings(false);
	}

}
