package com.tzj.garvel.core.parser;

import com.tzj.garvel.core.parser.json.JsonParserImpl;
import com.tzj.garvel.core.parser.semver.SemverParserImpl;
import com.tzj.garvel.core.parser.api.JsonParser;
import com.tzj.garvel.core.parser.api.ParserService;
import com.tzj.garvel.core.parser.api.SemverParser;

public enum ParserServiceImpl implements ParserService {
    INSTANCE;

    @Override
    public JsonParser getJsonParser(final String filename) {
        return new JsonParserImpl(filename);
    }

    @Override
    public SemverParser getSemVerParser() {
        return new SemverParserImpl();
    }
}