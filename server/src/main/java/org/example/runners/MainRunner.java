package org.example.runners;

import lombok.SneakyThrows;
import org.example.Server;
import org.example.commands.avaliable.auth.LoginCommand;
import org.example.commands.avaliable.auth.RegisterCommand;
import org.example.commands.avaliable.runtime.*;
import org.example.managers.collection.CollectionManager;
import org.example.managers.command.CommandManager;
import org.example.managers.threads.FutureManager;
import org.example.network.ServerTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Role;
import org.example.network.model.RuntimeMode;
import org.example.network.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainRunner {

    private static final Logger logger;
    private static Role role;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private static final ExecutorService fixedThreadPool;


    static {
        logger = LoggerFactory.getLogger(Server.class);
        role = Role.NON_AUTH;
        fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ;
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
                            new InfoCommand(collectionManager),
                            new LoginCommand(),
                            new RegisterCommand()
                    ));
        }};
        this.commandManager.addCommand(new HelpCommand(commandManager));
    }

    public void run() throws Exception {
        ServerTCP.sendResponse(
                new Response(
                        Status.OK,
                        commandManager.getAllCommandsName(role).toString())
        );

        while (true) {
            try {
                // FutureManager.checkAllFutures();
                Request req = ServerTCP.receiveRequest();

                logger.info(String.format("Address: %s; %s",
                        ServerTCP.getSocketChannel().getLocalAddress(),
                        req.toString()
                ));

                Future<Response> future = fixedThreadPool.submit(() -> commandManager.executeCommand(req));
                Response response = future.get();
                // Response response = commandManager.executeCommand(req);
                ServerTCP.sendResponse(response);

                if (response.getStatus().equals(Status.LOGIN_SUCCESS)) {
                    collectionManager.loadCollection(req.getUser());
                    role = Role.AUTH;
                    break;
                }
                if (response.getStatus().equals(Status.EXIT)) {
                    collectionManager.clearCollectionByExit();
                    role = Role.NON_AUTH;
                    break;
                }
            } catch (RuntimeException exception) {
                logger.error(exception.getMessage());
                ServerTCP.sendResponse(
                        new Response(Status.ERROR, exception.getMessage())
                );
            }
        }
    }
}
