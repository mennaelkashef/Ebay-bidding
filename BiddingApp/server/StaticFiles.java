package server;

import java.util.Hashtable;
import java.util.Vector;

public class StaticFiles{
    
        protected  Hashtable  _hbtlFiles;
        
        public StaticFiles( Vector<String> vDirs ){
            
        }
        
        public StringBuffer getTextFile(  String strFileName ){
            return (StringBuffer) _hbtlFiles.get( strFileName );
        }

        public  byte[] getBinaryFile( String strFileName ){
            StringBuffer buffer = (StringBuffer) _hbtlFiles.get( strFileName );
            return String.valueOf(buffer).getBytes();
        }
}