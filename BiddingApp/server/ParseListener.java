package server;


public interface ParseListener{

    public abstract void parsingFinished(  ClientHandle  clientHandle, 
                                           ClientRequest clientRequest );

    public abstract void parsingFailed( ClientHandle clientHandle, String strError );
    
}