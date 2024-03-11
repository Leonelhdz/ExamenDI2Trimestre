package com.example.examendesarrollointerface;

import java.util.ArrayList;

public interface DAO <T>{
    /**
     * Obtiene todos los elementos del tipo T.
     * @return Una lista de todos los elementos del tipo T.
     */
    public ArrayList<T> getAll();

    /**
     * Obtiene un elemento del tipo T mediante su identificador.
     * @param id El identificador del elemento a buscar.
     * @return El elemento del tipo T correspondiente al identificador especificado.
     */
    public T get(Long id);

    /**
     * Guarda un nuevo elemento del tipo T en la base de datos.
     * @param data El elemento del tipo T a guardar.
     * @return El elemento del tipo T guardado.
     */
    public T save(T data);
}
