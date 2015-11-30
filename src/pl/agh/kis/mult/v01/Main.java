package pl.agh.kis.mult.v01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jssc.SerialPortException;
import pl.agh.kis.mult.v01.command.ChainCommand;
import pl.agh.kis.mult.v01.command.DownCommand;
import pl.agh.kis.mult.v01.command.LeftCommand;
import pl.agh.kis.mult.v01.command.RightCommand;
import pl.agh.kis.mult.v01.command.UpCommand;

/**
 * Created by Krzysiek on 2015-11-27.
 */
public class Main {
	public static void main(String[] args) {

		// ChainCommand up = new UpCommand();
		// ChainCommand down = new DownCommand();
		// ChainCommand left = new LeftCommand();
		// ChainCommand right = new RightCommand();
		// up.setNext(down);
		// down.setNext(left);
		// left.setNext(right);

		ViscaCtrl viscaCtrl = new ViscaCtrl();

		// viscaCtrl.init();

		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		try {
			line = buffer.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (line.equals("close") == false) {
			try {
				System.out.println(viscaCtrl.executeCommand(line));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				line = buffer.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			viscaCtrl.closeSerial();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
