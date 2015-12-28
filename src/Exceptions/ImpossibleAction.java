package Exceptions;

import Game.Action;

public class ImpossibleAction extends Exception {

	Action action;
	public ImpossibleAction(Action action) {
		super(action.getText());
		this.action=action;
	}
	
	private static final long serialVersionUID = 1L;
}
