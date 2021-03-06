package com.tzj.garvel.core.builder.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * This Visitor recursively traverses the entire directory hierarchy, and
 * return a list of all the files in the entire tree.
 * This is useful since projects artifacts may contain non-source files.
 */
public class AllFilesFileVisitor implements FileVisitor<Path> {
    private final List<File> tree;

    public AllFilesFileVisitor(final List<File> tree) {
        this.tree = tree;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        tree.add(file.toFile());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        if (exc != null) {
            throw exc;
        }

        tree.add(dir.toFile());
        return FileVisitResult.CONTINUE;
    }
}
