package com.mmasata.eagenerator;

/**
 * Interface that every generator must extend.
 * It defines the method that the generator framework will call via reflection to start generator profile logic.
 */
public interface GeneratorHandler {

    void run();
}
