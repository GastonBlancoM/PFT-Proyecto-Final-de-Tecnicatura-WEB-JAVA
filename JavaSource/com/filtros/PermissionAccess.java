package com.filtros;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.enums.TipoUsuario;

import lombok.Getter;
import lombok.Setter;

/**
 * La clase PermissionAccess representa los permisos de acceso para diferentes tipos de usuarios en una aplicación.
 */
@Getter
@Setter
public class PermissionAccess {
	private String path; // La ruta a la que se aplica el permiso
    private HashMap<String, List<TipoUsuario>> map; // Mapa que almacena los permisos para cada tipo de usuario

    /**
     * Constructor de la clase PermissionAccess.
     *
     * @param map  el mapa que almacena los permisos para cada tipo de usuario
     * @param path la ruta a la que se aplica el permiso
     */
    public PermissionAccess(HashMap<String, List<TipoUsuario>> map, String path) {
        this.map = map;
        this.path = path;
        map.put(path, new ArrayList<TipoUsuario>());
    }

    /**
     * Agrega un tipo de usuario específico al permiso para la ruta actual.
     *
     * @param tipoUsuario el tipo de usuario que se agrega al permiso
     * @return el objeto PermissionAccess actualizado
     */
    public PermissionAccess add(TipoUsuario tipoUsuario) {
        getList(path).add(tipoUsuario);
        return this;
    }

    /**
     * Agrega el tipo de usuario Estudiante al permiso para la ruta actual.
     *
     * @return el objeto PermissionAccess actualizado
     */
    public PermissionAccess addEstudiante() {
        return add(TipoUsuario.ESTUDIANTE);
    }

    /**
     * Agrega el tipo de usuario Tutor al permiso para la ruta actual.
     *
     * @return el objeto PermissionAccess actualizado
     */
    public PermissionAccess addTutor() {
        return add(TipoUsuario.TUTOR);
    }

    /**
     * Agrega el tipo de usuario Analista al permiso para la ruta actual.
     *
     * @return el objeto PermissionAccess actualizado
     */
    public PermissionAccess addAnalista() {
        return add(TipoUsuario.ANALISTA);
    }

    /**
     * Agrega todos los tipos de usuarios al permiso para la ruta actual.
     */
    public void addAll() {
        add(TipoUsuario.ANALISTA);
        add(TipoUsuario.ESTUDIANTE);
        add(TipoUsuario.TUTOR);
    }

    /**
     * Obtiene la lista de tipos de usuarios asociada a una ruta específica.
     *
     * @param path la ruta para la que se obtiene la lista de tipos de usuarios
     * @return la lista de tipos de usuarios asociada a la ruta especificada
     */
    private List<TipoUsuario> getList(String path) {
        List<TipoUsuario> list = map.get(path);
        return list;
    }
}
