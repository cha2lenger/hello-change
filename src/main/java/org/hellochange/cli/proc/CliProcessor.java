package org.hellochange.cli.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Does command line processing for the list of commands configured.
 * 
 * @author vladimir
 *
 */
public class CliProcessor {
  /** CLI Commands supported by CLI processor. */
  private final Map<String, CliCommand> commandsSupported;
  
  /**
   * Constructor.
   * 
   * @param cliCommands - supported CLI commands.
   */
  public CliProcessor(CliCommand... cliCommands) {
    if((cliCommands == null) || (cliCommands.length == 0)) {
      throw new IllegalArgumentException("Empty array of CLI commands has been passed in.");
    }
    
    final Map<String, CliCommand> commands = new HashMap<>();
    for (CliCommand nextCommand : cliCommands) {
      final String commandName = nextCommand.getName();
      
      if(commands.containsKey(commandName)) {
        throw new IllegalArgumentException(String.format("Duplicate CLI command for the name: %1$s", commandName));
      }
      
      commands.put(commandName, nextCommand);
    }
    
    this.commandsSupported = commands;
  }
  
  /**
   * Runs the CLI processor.
   * 
   * @throws IOException - in case of I/O error.
   */
  public void run() throws IOException {
    final BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
    String line = null;
    System.out.println("ready");
    while((line = inReader.readLine()) != null) {
      final String[] tokens = line.trim().split("\\s+");
      // bypass and continue on empty inputs
      if(tokens.length == 0) {
        continue;
      }
      
      final String commandName = tokens[0];
      final CliCommand command = this.commandsSupported.get(commandName);
      
      if (command == null) {
        System.out.println(String.format(
            "Unsupported command: [%1$s]. Please use one of the supported commands %2$s", commandName, this.commandsSupported.keySet()));
        continue;
      }
      
      final String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
      final boolean status = command.execute(args);
      
      // terminate if command's outcome requires this
      if(! status) {
        break;
      }
    }
  }
}