package org.example.runners;

import lombok.SneakyThrows;
import org.example.Server;
import org.example.commands.avaliable.*;
import org.example.managers.collection.CollectionManager;
import org.example.managers.command.CommandManager;
import org.example.model.data.IdCounter;
import org.example.network.ServerTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;
import org.example.runner.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainRunner extends Runner {

    private static final Logger logger;

    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    static {
        logger = LoggerFactory.getLogger(Server.class);
    }

    {
        this.collectionManager = new CollectionManager();
        this.commandManager = new CommandManager() {{
            this.addAllCommands(
                    List.of(
                            new AddCommand(collectionManager),
                            new AddIfMinCommand(collectionManager),
                            new ExitCommand(),
                            new ClearCommand(collectionManager),
                            new ShowCommand(collectionManager),
                            new MeowCommand(),
                            new FilterByDifficultyCommand(collectionManager),
                            new RemoveFirstCommand(collectionManager),
                            new RemoveByIdCommand(collectionManager),
                            new ExecuteScriptCommand(),
                            new SumOfMinimalPointCommand(collectionManager),
                            new RemoveGreaterCommand(collectionManager),
                            new UpdateByIdCommand(collectionManager),
                            new InfoCommand(collectionManager)
                    ));
        }};
        this.commandManager.addCommand(new HelpCommand(commandManager));
    }

    @Override
    @SneakyThrows
    public void run() {
        collectionManager.loadCollection();

        ServerTCP.acceptConnection();
        ServerTCP.sendResponse(
                new Response(
                        Status.OK,
                        commandManager.getAllCommandsName().toString())
        );
        ServerTCP.sendResponse(
                new Response(
                        Status.OK,
                        IdCounter.getNextId().toString()
                )
        );

        while (true) {
            try {
                Request req = ServerTCP.receiveRequest();

                logger.info(String.format("Address: %s; %s",
                        ServerTCP.getSocketChannel().getLocalAddress(),
                        req.toString()
                ));

                Response response = this.commandManager.executeCommand(req);
                ServerTCP.sendResponse(response);
            } catch (RuntimeException exception) {
                logger.error(exception.getMessage());
                ServerTCP.sendResponse(
                        new Response(Status.ERROR, exception.getMessage())
                );
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                break;
            }
        }

        collectionManager.saveCollection();
    }
}
