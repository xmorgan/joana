package edu.kit.joana.ui.ifc.wala.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import edu.kit.joana.ui.ifc.wala.console.console.IFCConsole;
import edu.kit.joana.ui.ifc.wala.console.io.IFCConsoleOutput;

/**
 * Runs the program on the command line
 */
@Parameters(commandDescription = "Basic quantitative information flow analysis")
public class Main {

	@Parameters(commandDescription = "Run IFC console commands directly")
	private static class ConsoleCommand {

		@Parameter(description = "Commands to execute")
		private List<String> commands = new ArrayList<>();

		@Parameter(names = "-i", description = "Run interactive console afterwards")
		private boolean interactive;

		private IFCConsole console;

		void process(Main main) {
			console = new IFCConsole(new BufferedReader(new InputStreamReader(System.in)), new IFCConsoleOutput() {

				@Override
				public void log(String logMessage) {
					if (main.verbose) {
						System.out.println(logMessage);
					}
				}

				@Override
				public void logln(String logMessage) {
					if (main.verbose) {
						System.out.println(logMessage);
					}
				}

				@Override
				public void info(String infoMessage) {
					System.out.println(infoMessage);
				}

				@Override
				public void error(String errorMessage) {
					if (main.verbose) {
						System.err.println(errorMessage);
					}
				}

				@Override
				public Answer question(String questionMessage) {
					System.out.println(questionMessage);
					try {
						return new BufferedReader(new InputStreamReader(System.in)).readLine().contains("y")
								? Answer.YES
								: Answer.NO;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public PrintStream getPrintStream() {
					return System.out;
				}

			});
			if (main.classPath.length() > 0) {
				console.setClasspath(main.classPath);
			}
			for (String command : commands) {
				if (!console.processCommand(command)) {
					System.exit(1);
				}
			}
			if (interactive) {
				try {
					console.interactive();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}

	@Parameter(names = { "-cp",
			"-classpath" }, description = "Classpath that contains the code that is going to be analysed, should also contain "
					+ "the cli JAR itself", required = false)
	private String classPath = "";

	@Parameter(names = "-v", description = "Verbose output?")
	private boolean verbose = false;
	
	public static void main(String[] args) {
		Main main = new Main();
		ConsoleCommand console = new ConsoleCommand();
		JCommander com = JCommander.newBuilder().addObject(main).programName("cli").addCommand("console", console)
				.build();
		com.parse(args);
		if (com.getParsedCommand() == null) {
			com.usage();
			System.exit(0);
		}
		switch (com.getParsedCommand()) {
		case "console":
			console.process(main);
			break;
		default:
			com.usage();
			System.exit(1);
		}
	}

}
