package com.tzj.garvel.core.builder.api;

import java.util.List;

public interface Compiler {
    /**
     * 1. Create the `target` directory inside the project root.
     * 2. Create a temporary `build` directory inside the `target` directory.
     * 3. Compile the code using the `-d` option to direct the generated classes to the
     * `build` directory. Report any errors to the user.
     * <p>
     * Also, handle the classpath correctly by combining all the dependencies (using their
     * registry locations) and the project `src` root itself, and also any custom paths provided
     * in the `Garvel.gl` file.
     * 4. Generate the appropriate type of JAR (and build script) inside `target`.
     * 5. Delete the `build` directory.
     *
     * @param files
     * @return CompilationResult
     */
    CompilationResult compile(List<String> files);
}
