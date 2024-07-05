if (dependencies.get("event") != null){
	Object _obj = dependencies.get("event");
	if (_obj instanceof Event) {
		Event _evt = (Event) _obj;
		ItemTooltipEvent _event = (ItemTooltipEvent) _evt;
		_event.getToolTip().add(${input$line}, ${input$tooltips});
	}
}