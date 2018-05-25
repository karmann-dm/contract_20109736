package com.karmanno.verificator.io;

import java.io.File;

public interface FileLoader {
    Object load(File file) throws Exception;
    void save(File file, Object object) throws Exception;
}
