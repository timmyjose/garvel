package com.tzj.garvel.common.spi.core.command.result;

import java.nio.file.Path;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class InitCommandResult extends CommandResult {
    private Path srcPath;
    private Path testsPath;
    private Path configPath;

    public InitCommandResult(final Path srcPath, final Path testsPath, final Path configPath) {
        this.srcPath = srcPath;
        this.testsPath = testsPath;
        this.configPath = configPath;
    }

    public Path getSrcPath() {
        return srcPath;
    }

    public Path getTestsPath() {
        return testsPath;
    }

    public Path getConfigPath() {
        return configPath;
    }
}
