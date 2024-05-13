package net.giuse.mainmodule.files;

import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;

import java.io.File;

@Getter
public class SQLFile extends AbstractConfig {
    @FileAnnotation(name = "sql.db", path = "plugins/LifeServer/sql.db")
    private File sqlLite;

}
