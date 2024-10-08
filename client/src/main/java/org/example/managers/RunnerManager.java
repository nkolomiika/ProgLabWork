package org.example.managers;

import lombok.SneakyThrows;
import org.example.network.dto.User;
import org.example.network.model.RuntimeMode;
import org.example.runners.Pair;
import org.example.runners.abstracts.Runner;
import org.example.runners.avaliable.AuthRunner;
import org.example.runners.avaliable.MainRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class RunnerManager {

    private final Map<RuntimeMode, Runner> runners;

    {
        this.runners = new HashMap<>();
        this.addAllRunners(
                List.of(
                        new MainRunner(),
                        new AuthRunner()
                )
        );
    }

    private void addRunner(Runner runner) {
        this.runners.put(runner.getRuntimeMode(), runner);
    }

    private void addAllRunners(List<Runner> runners) {
        runners.forEach(this::addRunner);
    }

    @SneakyThrows
    public Pair launchRunner(RuntimeMode runtimeMode, User user) throws NoSuchElementException {
        return this.runners.get(runtimeMode).run(user);
    }

}
