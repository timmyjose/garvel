package com.tzj.garvel.cli;

import com.tzj.garvel.cli.api.core.CLICoreService;
import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.core.CLICoreServiceImpl;
import com.tzj.garvel.cli.parser.CLIParserImpl;
import com.tzj.garvel.common.spi.util.UtilService;
import com.tzj.garvel.common.util.UtilServiceImpl;

/**
 * Interface to the CLI's services.
 */
public enum ModuleLoader {
    INSTANCE;

    /**
     * Get the core service.
     *
     * @return
     */
    public CLICoreService getCLICoreService() {
        return CLICoreServiceImpl.INSTANCE;
    }

    /**
     * Return the CLI Parser.
     *
     * @return
     */
    public CLIParser getParser() {
        return CLIParserImpl.INSTANCE;
    }

    public UtilService getUtils() {
        return UtilServiceImpl.INSTANCE;
    }
}
