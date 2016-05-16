package com.gluonapplication;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/*
 * if operation selected but no number entered before, nothing happen
 * no two operations come after each other
 * the result is displayed after(operand +operation +operand)
 * add boolean to check if there is operation pending
 * 
 * */
public class BasicView extends View {
	TextArea display;
	boolean operationSelected = false;
	boolean opJustFin=false;
	String operation = "";
	String operand = "";
	String secOperand = "";
	int disCount=1;
	
	//Operation
	Button mult;
	Button plus;
	Button div;
	Button mod;
	Button minus;
	Button pow;
	
	public BasicView(String name) {
		super(name);
	   	
		display = new TextArea();
		setCenter(display);
		display.setDisable(true);
		display.setWrapText(true);
		display.setStyle("-fx-font: 45 arial; -fx-base: #b6e7c9;");
		
		
		//event to decrease font size
		
		display.textProperty().addListener(e->{	
			String txt =display.getText();
			double ratio=txt.length() / (10*disCount);
			if(txt.matches(""))
			{
				display.setStyle("-fx-font: 45 arial; -fx-base: #b6e7c9;");
				disCount=1;
			}
			else if(ratio>0)
			{	
				disCount+=3;
				ratio+=0.7;
				double nw=display.getFont().getSize()/ratio;
				display.setStyle("-fx-font: "+nw+" arial; -fx-base: #b6e7c9;");
			}
		});
		
		//Buttons
		Button dot = new Button(".");
		setBind(dot);
		dot.setStyle("-fx-font: 40 arial; -fx-base: #b6e7c9;-fx-background-color: #0277BD;");
		
		dot.setOnAction(e -> {
			String dis = display.getText();

			if (!dis.contains(".") && !dis.matches("")) {
				display.setText(dis + ".");
			}
		});

		//
		
		Button equal = new Button("=");
		setBind(equal);
		equal.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9; -fx-background-color:#00B8D4");
		equal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!operation.matches("")) {
					secOperand=display.getText();
					String result = "";
					if (operand.contains(".") || secOperand.contains("."))
						result = "" + BasicView.<Double> calculate(Double.parseDouble(operand),
								Double.parseDouble(secOperand), operation);
					else
						result = "" + BasicView.<Integer> calculate(Integer.parseInt(operand),
								Integer.parseInt(secOperand), operation);
					
					display.setText(result);
					operation = "";
					opJustFin=true;
					resetOperations();
				}
			}
		});

		//
		
		Button del = new Button("AC");
		setBind(del);
		del.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		del.setOnAction(e->{
			
			display.clear();
			operand="";
			secOperand="";
			operationSelected=false;
			opJustFin=false;
			operation="";
			resetOperations();

		});
		

		 mult = new Button("X");
		setBind(mult);
		mult.setStyle("-fx-font: 28 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		mult.setOnAction(operationHandler);
		
		 div = new Button("÷");
		setBind(div);
		div.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		div.setOnAction(operationHandler);
		
		
		 plus = new Button("+");
		setBind(plus);
		plus.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		plus.setOnAction(operationHandler);
		
		
		 minus = new Button("-");
		setBind(minus);
		minus.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		minus.setOnAction(operationHandler);
		
		 pow = new Button("^");
		setBind(pow);
		pow.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		pow.setOnAction(operationHandler);
		
		Button sqrt = new Button("√");
		sqrt.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		setBind(sqrt);
		sqrt.setOnAction(e->{
			if(!display.getText().matches("")&& operation.matches(""))
			{
				String res=""+Math.sqrt(Double.parseDouble(display.getText()));
				display.setText(res);
				opJustFin=true;
			}
		});
		
		 mod = new Button("%");
		setBind(mod);
		mod.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		mod.setOnAction(operationHandler);
		
		HBox h1 = new HBox();
		HBox h2 = new HBox();
		HBox h3 = new HBox();
		HBox h4 = new HBox();
		Button[] buttons = new Button[10];
		for (int i = 9; i >= 0; i--) {
			buttons[i] = new Button("" + i);
			setBind(buttons[i]);
			buttons[i].setOnAction(numberHandler);
			buttons[i].setStyle("-fx-font: 28 arial; -fx-base: #b6e7c9;-fx-background-color: #0277BD;");

		}
		h1.getChildren().addAll(buttons[7], buttons[8], buttons[9], div, del);
		h2.getChildren().addAll(buttons[4], buttons[5], buttons[6], mult, pow);
		h3.getChildren().addAll(buttons[1], buttons[2], buttons[3], plus, sqrt);
		h4.getChildren().addAll(dot, buttons[0], equal, minus, mod);
		VBox v1 = new VBox();
		v1.getChildren().addAll(h1, h2, h3, h4);

		setBottom(v1);

	}

	// number before operation -> concatenate
	// choosing operation -> save operation, save operand, set operationSelected
	// true
	// number after operation -> replace with the new number, set operation
	// selected to false
	// equal pressed -> do operation with the two operands
	EventHandler<ActionEvent> numberHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			String num = ((Button) event.getSource()).getText();

			if (!operationSelected && !opJustFin)
				//Concatenate
				display.setText(display.getText() + num);

			else{
				resetDisTextSize();
				display.setText(num);
				operationSelected = false;
				opJustFin=false;
				
			}
		}
	};
	EventHandler<ActionEvent> operationHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if(!display.getText().matches(""))
			if (!operationSelected && operation.matches("")) {
				Button bt = ((Button) event.getSource());
				operation=bt.getText();
				operationSelected = true;
				operand=display.getText();
				resetOperations();
				bt.setStyle("-fx-font: 28 arial; -fx-base: #b6e7c9;-fx-background-color:#006064;");
				
			}
			else if(!operationSelected && !operation.matches(""))
			{
				secOperand=display.getText();
				String result="";
				//calculate
				if (operand.contains(".") || secOperand.contains("."))
					result = "" + BasicView.<Double> calculate(Double.parseDouble(operand),
							Double.parseDouble(secOperand), operation);
				else
					result = "" + BasicView.<Integer> calculate(Integer.parseInt(operand),
							Integer.parseInt(secOperand), operation);
				display.setText(result);
				operand=result;
				Button bt = ((Button) event.getSource());
				operation=bt.getText();
				
				operationSelected=true;
				opJustFin=true;
				resetOperations();
				bt.setStyle("-fx-font: 28 arial; -fx-base: #b6e7c9;-fx-background-color:#006064;");
			}else if(operationSelected && !operation.matches(""))
			{
				Button bt = ((Button) event.getSource());
				operation=bt.getText();
				resetOperations();
				bt.setStyle("-fx-font: 28 arial; -fx-base: #b6e7c9;-fx-background-color:#006064;");

			}
		}
	};

	private static <T extends Number> Number calculate(T o1, T o2, String operation) {
		boolean d=false;
		if(o1.doubleValue()-o1.intValue()>0 || o2.doubleValue()-o2.intValue()>0)
			d=true;
		if(d){
		switch (operation) {
		case "+":
			return o1.doubleValue() + o2.doubleValue();
		case "-":
			return o1.doubleValue() - o2.doubleValue();
		case "X":
			return o1.doubleValue() * o2.doubleValue();
		case "÷":
			if(o2.doubleValue()!=0)
			return o1.doubleValue() / o2.doubleValue();
			else 
				{
				MobileApplication.getInstance().showMessage("Error!");
				return 0;
				
				}
		case "%":
			return o1.doubleValue() % o2.doubleValue();
		case "^":
			return Math.pow(o1.doubleValue(), o2.doubleValue());
			
		default:
			return 0;
		}}
		else
		{
			switch (operation) {
			case "+":
				return o1.intValue() + o2.intValue();
			case "-":
				return o1.intValue() - o2.intValue();
			case "X":
				return o1.intValue() * o2.intValue();
			case "÷":
				if(o2.doubleValue()!=0)
				return o1.doubleValue() / o2.doubleValue();
				else return 0;
			case "%":
				return o1.intValue() % o2.intValue();
			case "^":
				return Math.pow(o1.intValue(), o2.intValue());
				
			default:
				return 0;
			}
		}
	}
	private void resetOperations()
	{
		 mult.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");;
		 plus.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");;
		 div.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");;
		 mod.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");
		 minus.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");;
		 pow.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;-fx-background-color:#00B0FF");;
	}
	
	private void resetDisTextSize()
	{
		display.setStyle("-fx-font: 45 arial; -fx-base: #b6e7c9;");
		disCount=1;
	}
	private void setBind(Button b) {
		b.prefWidthProperty().bind(this.widthProperty().divide(4));
		b.prefHeightProperty().bind(this.widthProperty().divide(3.5));
	}
	  @Override
	    protected void updateAppBar(AppBar appBar) {
	       appBar.setStyle(" -fx-background-color: #039BE5");
	       appBar.setTitleText("Calculator");
	       
	  
	    }
}
