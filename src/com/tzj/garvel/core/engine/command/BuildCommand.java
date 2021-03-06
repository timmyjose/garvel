package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.BuildJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BuildCommand extends Command {
    public BuildCommand() {
        super(new InstallCommand());
    }

    /**
     * `build` has a dependency on `install`.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        try {
            prerequisiteCommand.run(new InstallCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (install) for build command failed, %s\n", e.getErrorString()));
        }
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final Job<BuildCommandResult> job = new BuildJob();
        final Future<BuildCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        BuildCommandResult cmdRes = null;
        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal exception");
        } catch (ExecutionException e) {
            if (e.getCause() != null && e.getCause() instanceof JobException) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorString());
            }
        }

        return cmdRes;
    }
}
