package fab.files;

import java.io.File;
import java.util.List;

public class Filewalker {

    public void walk(String path, List<String> lista) {
    	
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk(f.getAbsolutePath(), lista);
            }
            else {
            	lista.add("" + f.getAbsoluteFile());
            }
        }
    }

}