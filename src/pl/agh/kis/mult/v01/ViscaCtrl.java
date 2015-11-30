package pl.agh.kis.mult.v01;

import java.util.HashMap;

import jssc.SerialPort;
import jssc.SerialPortException;
import pl.agh.kis.mult.v01.command.ChainCommand;
import pl.agh.kis.mult.v01.command.ClearCommand;
import pl.agh.kis.mult.v01.command.DownCommand;
import pl.agh.kis.mult.v01.command.HomeCommand;
import pl.agh.kis.mult.v01.command.LeftCommand;
import pl.agh.kis.mult.v01.command.RightCommand;
import pl.agh.kis.mult.v01.command.SetAddressCommand;
import pl.agh.kis.mult.v01.command.StopCommand;
import pl.agh.kis.mult.v01.command.UpCommand;

public class ViscaCtrl {
	private SerialPort serialPort = null;
	private String definingMacro = null;
	private HashMap<String, ChainCommand> macroMap = new HashMap<String, ChainCommand>();

	public void init() throws SerialPortException {
		serialPort = new SerialPort("com1");

		serialPort.setParams(9600, 8, 1, 0);

	}

	public void closeSerial() throws SerialPortException {
		serialPort.closePort();
	}

	private ChainCommand stringToCommand(String commandString) {
		ChainCommand command = null;
		switch (commandString.trim()) {
		case "clear":
			command = new ClearCommand();
			break;
		case "down":
			command = new DownCommand();
			break;
		case "home":
			command = new HomeCommand();
			break;
		case "left":
			command = new LeftCommand();
			break;
		case "right":
			command = new RightCommand();
			break;
		case "set":
			command = new SetAddressCommand();
			break;
		case "stop":
			command = new StopCommand();
			break;
		case "up":
			command = new UpCommand();
			break;
		}
		return command;
	}

	public String executeCommand(String commandString)
			throws SerialPortException {

		if (commandString.contains("macro-start")) {
			String macroName = commandString.split("\\s+")[1];
			macroMap.put(macroName, null);
			definingMacro = macroName;

		} else if (commandString.contains("macro-stop")) {
			definingMacro = null;
		} else if (commandString.contains("macro-execute")) {
			String macroName = commandString.split("\\s+")[1];
			ChainCommand command = macroMap.get(macroName);

			return executeMacro(command);

		} else if (definingMacro != null) {
			ChainCommand command = stringToCommand(commandString);
			if (command == null) {
				return "fail";
			}
			if (macroMap.get(definingMacro) == null) {
				macroMap.put(definingMacro, command);
			} else {
				macroMap.get(definingMacro).setNext(command);
			}
		} else {

			ChainCommand command = stringToCommand(commandString);

			return executeCommand(command);
		}
		return "ok";
	}

	public String executeCommand(ChainCommand command)
			throws SerialPortException {
		serialPort.writeBytes(command.getCommand());
		return "ok " + command.getCommandName();
	}

	public String executeMacro(ChainCommand command) throws SerialPortException {

		while (command != null) {
			executeCommand(command);
			command = command.getNext();
		}
		return "ok";
	}

}
